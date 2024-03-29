package lazecoding.service;

import lazecoding.api.OpenApi;
import lazecoding.config.ClientConstant;
import lazecoding.exception.InitException;
import lazecoding.exception.NilParamException;
import lazecoding.exception.NilTagException;
import lazecoding.model.Segment;
import lazecoding.model.SegmentBuffer;
import lazecoding.model.UniqueRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SegmentBuffer 持有者
 *
 * @author lazecoding
 * @apiNote init() 初始化; getUniqueId() 获取分布式 Id。
 */
public class BufferHolder {

    private static final Logger logger = LoggerFactory.getLogger(BufferHolder.class);

    /**
     * 核心数
     */
    private static final int CORE_NUM = Math.max(4, Runtime.getRuntime().availableProcessors());

    /**
     * 线程池
     */
    private static final ExecutorService service = new ThreadPoolExecutor(CORE_NUM, CORE_NUM * 2, 60L, TimeUnit.SECONDS, new SynchronousQueue<>()
            , new UpdateThreadFactory());

    /**
     * 线程工厂
     */
    private static class UpdateThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Segment-Update-" + nextThreadNum());
        }
    }

    /**
     * 标识初始化是否成功
     */
    private static volatile boolean initSuccess = false;

    /**
     * IdCache
     */
    private static final Map<String, SegmentBuffer> IdCache = new ConcurrentHashMap<>();

    /**
     * 获取 IdCache
     *
     * @return IdCache
     */
    public static Map<String, SegmentBuffer> getIdCache() {
        return IdCache;
    }

    /**
     * 初始化
     */
    public static boolean init() {
        // 检验 UniqueClientConfig
        Assert.notNull(OpenApi.UNIQUE_CLIENT_CONFIG, "UniqueClientConfig is null");
        if (!StringUtils.hasText(OpenApi.UNIQUE_CLIENT_CONFIG.getUrl())) {
            throw new NilParamException("unique.client.url is null");
        }

        if (!StringUtils.hasText(OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace())) {
            throw new NilParamException("unique.client.namespace is null");
        }

        if (!StringUtils.hasText(OpenApi.UNIQUE_CLIENT_CONFIG.getRegion())) {
            throw new NilParamException("unique.client.region is null");
        }

        logger.info("unique.client.config:[{}] init success", OpenApi.UNIQUE_CLIENT_CONFIG.toString());

        // Sync Tags IN Db/Cache
        boolean beSuccess = syncTagsFromDb();
        if (beSuccess) {
            initSuccess = true;
        }
        // 定时线程：Sync Cache
        sycnCacheAtCycle();
        return initSuccess;
    }

    /**
     * 获取客户端配置的 namespace-region 下所有 tags
     */
    public static List<String> getTags() {
        return BufferRest.getTags();
    }

    /**
     * 判断客户端配置的 namespace-region 下是否存在某 tag
     */
    public static boolean existTag(String tag) {
        return BufferRest.existTag(tag);
    }

    /**
     * 在客户端配置的 namespace-region 下新增 tag
     */
    public static UniqueRecord addTag(String tag, long maxId, int step, String description) {
        return BufferRest.addTag(tag, maxId, step, description);
    }

    /**
     * 在客户端配置的 namespace-region 下删除 tag
     */
    public static boolean removeTag(String tag) {
        return BufferRest.removeTag(tag);
    }

    /**
     * Sync Tags IN Db/Cache
     */
    private static boolean syncTagsFromDb() {
        logger.debug("Sync Tags IN Db/Cache Start");
        // 标识更新IdCache是否成功
        boolean isSuccess = false;
        try {
            // 根据 namespace 获取 tags
            List<String> dbTags = BufferHolder.getTags();
            if (CollectionUtils.isEmpty(dbTags)) {
                logger.debug("Sync Tags IN Db/Cache Ready");
                initSuccess = true;
                isSuccess = true;
                return isSuccess;
            }
            // IdCache 中的全部 bus_tag
            List<String> cacheTags = new ArrayList<String>(IdCache.keySet());
            Set<String> insertTagsSet = new HashSet<>(dbTags);
            Set<String> removeTagsSet = new HashSet<>(cacheTags);
            for (int i = 0; i < cacheTags.size(); i++) {
                String tmp = cacheTags.get(i);
                // 移除内存中已经有的，得到数据库新增的 bus_tag
                insertTagsSet.remove(tmp);
            }
            for (String tag : insertTagsSet) {
                SegmentBuffer buffer = new SegmentBuffer();
                buffer.setTag(tag);
                Segment segment = buffer.getCurrent();
                segment.setValue(new AtomicLong(0));
                segment.setMax(0);
                segment.setStep(0);
                IdCache.put(tag, buffer);
                logger.debug("Add Tags From Db To IdCache, SegmentBuffer:[{}]", tag, buffer);
            }
            for (int i = 0; i < dbTags.size(); i++) {
                String tmp = dbTags.get(i);
                // 内存中 bus_tag，再移除数据库中有的，剩下的就是内存中有，但是数据库中没有的
                // 也就是需要移除的 bus_tag
                removeTagsSet.remove(tmp);
            }
            for (String tag : removeTagsSet) {
                IdCache.remove(tag);
                logger.debug("Remove Tags:[{}] In IdCache", tag);
            }
            isSuccess = true;
            initSuccess = true;
            logger.debug("Sync Tags IN Db/Cache Ready");
            return isSuccess;
        } catch (Exception e) {
            logger.error("Sync Tags IN Db/Cache Exception", e);
            isSuccess = false;
            return isSuccess;
        }
    }

    /**
     * 定时线程：Sync Tags IN Db/Cache
     */
    private static void sycnCacheAtCycle() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Sync-Cache-Task");
                t.setDaemon(true);
                return t;
            }
        });
        service.scheduleWithFixedDelay(BufferHolder::syncTagsFromDb, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 获取分布式 Id
     */
    public static long getUniqueId(String tag) throws InitException {
        if (!initSuccess) {
            throw new InitException("IdCache 未初始化");
        }
        if (IdCache.containsKey(tag)) {
            SegmentBuffer buffer = IdCache.get(tag);
            // buffer 未更新先更新buffer
            if (!buffer.isInitSuccess()) {
                synchronized (buffer) {
                    // 双重校验防止被其他线程更新了
                    if (!buffer.isInitSuccess()) {
                        try {
                            applySegmentFromDb(tag, buffer.getCurrent());
                            logger.debug("Init Tag:[{}] Buffer:[{}] From Db ", tag, buffer.getCurrent());
                            // buffer 初始化成功
                            buffer.setInitSuccess(true);
                        } catch (Exception e) {
                            logger.error("Init Tag:[" + tag + "] Buffer:[" + buffer.getCurrent() + "] From Db Exception", e);
                        }
                    }
                }
            }
            return getIdFromSegmentBuffer(IdCache.get(tag));
        } else {
            throw new NilTagException("IdCache 中不存在 tag:" + tag);
        }
    }

    /**
     * Apply Buffer Segment From Db
     */
    private static void applySegmentFromDb(String tag, Segment segment) {
        SegmentBuffer buffer = segment.getBuffer();
        UniqueRecord uniqueRecord;
        if (!buffer.isInitSuccess()) {
            //未初始化
            // apply record
            uniqueRecord = BufferRest.updateMaxIdAndGetUniqueRecord(tag);
            buffer.setUpdateTimestamp(System.currentTimeMillis());
            buffer.setStep(uniqueRecord.getStep());
            buffer.setMinStep(uniqueRecord.getStep());
        } else {
            // 当剩余ID不足会预更新另一个 Buffer Segment
            long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
            int nextStep = buffer.getStep();
            //当更新时间小于15分钟，扩大步长，更新时间大于30分钟，缩小步长。 为了防止某时间段业务量飙升
            if (duration < ClientConstant.SEGMENT_DURATION) {
                if (nextStep * 2 > ClientConstant.MAX_STEP) {
                    nextStep = ClientConstant.MAX_STEP;
                } else {
                    nextStep = nextStep * 2;
                }
            } else if (duration < ClientConstant.SEGMENT_DURATION * 2) {
                //Do Nothing with nextStep
            } else {
                nextStep = nextStep / 2 >= buffer.getMinStep() ? nextStep / 2 : nextStep;
            }
            logger.debug("transform for tag[{}],step[{}],duration[{}mins],nextStep[{}]", tag, buffer.getStep(), String.format("%.2f", ((double) duration / (1000 * 60))), nextStep);
            // apply record
            uniqueRecord = BufferRest.updateMaxIdByCustomStepAndGetLeafAlloc(tag, nextStep);
            buffer.setUpdateTimestamp(System.currentTimeMillis());
            buffer.setStep(nextStep);
            buffer.setMinStep(uniqueRecord.getStep());
        }
        // must set value before set max
        long value = uniqueRecord.getMaxId() - buffer.getStep();
        segment.getValue().set(value);
        segment.setMax(uniqueRecord.getMaxId());
        segment.setStep(buffer.getStep());
    }

    /**
     * 获取分布式 Id 核心代码，保证线程安全。
     *
     * @param buffer
     * @return
     * @throws InitException
     */
    private static long getIdFromSegmentBuffer(final SegmentBuffer buffer) throws InitException {
        // 循环，意在写锁内切换完 Segment，重新再读锁中获取 Id
        while (true) {
            //加读锁
            buffer.getReadLock().lock();
            try {
                final Segment segment = buffer.getCurrent();
                // buffer.getThreadRunning() 标识正在初始化 防止同时出现多个线程初始化
                if (!buffer.isNextReady() && (segment.getIdle() < segment.getStep() * 0.9)
                        && buffer.getThreadRunning().compareAndSet(false, true)) {
                    service.execute(() -> {
                        Segment next = buffer.getSegments()[buffer.nextPos()];
                        boolean updateOk = false;
                        try {
                            applySegmentFromDb(buffer.getTag(), next);
                            updateOk = true;
                            logger.debug("Tag:[{}] Update Buffer Segment From Db [{}]", buffer.getTag(), next);
                        } catch (Exception e) {
                            logger.error("Tag:[" + buffer.getTag() + "] Update Buffer Segment From Db Exception", e);
                        } finally {
                            if (updateOk) {
                                // 加写锁
                                buffer.getWriteLock().lock();
                                buffer.setNextReady(true);
                                buffer.getThreadRunning().set(false);
                                buffer.getWriteLock().unlock();
                            } else {
                                buffer.getThreadRunning().set(false);
                            }
                        }
                    });
                }
                long value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    return value;
                }
            } finally {
                buffer.getReadLock().unlock();
            }
            // 要切换 Segment 加写锁
            buffer.getWriteLock().lock();
            try {
                // 重新校验，防止已经被其他线程切换了
                final Segment segment = buffer.getCurrent();
                long value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    return value;
                }
                if (buffer.isNextReady()) {
                    buffer.switchPos();
                    buffer.setNextReady(false);
                } else {
                    logger.error("Tag:[{}] Both Two Buffer Segments In [{}] Are Not Ready!", buffer.getTag(), buffer);
                    throw new InitException("SegmentBuffer 中的两个 Segment 均未从 DB 中装载");
                }
            } finally {
                buffer.getWriteLock().unlock();
            }
        }
    }
}