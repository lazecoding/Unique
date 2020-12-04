package personal.unique.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.unique.exception.InitException;
import personal.unique.exception.NilTagException;
import personal.unique.mapper.UniqueRecordMapper;
import personal.unique.model.Segment;
import personal.unique.model.SegmentBuffer;
import personal.unique.model.UniqueRecord;
import personal.unique.service.UniqueRecordService;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @className: UniqueRecordServiceImpl
 * @package: personal.unique.service.impl
 * @description:
 * @datetime: 2020/10/12   21:50
 * @author: lazecoding
 */
@Service("UniqueRecordServiceImpl")
public class UniqueRecordServiceImpl implements UniqueRecordService {
    private Logger logger = LoggerFactory.getLogger(UniqueRecordServiceImpl.class);

    /**
     * 最大步长不超过100,0000
     */
    private static final int MAX_STEP = 1000000;
    /**
     * 一个Segment维持时间为15分钟
     */
    private static final long SEGMENT_DURATION = 15 * 60 * 1000L;
    /**
     * 线程池
     */
    private ExecutorService service = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new UpdateThreadFactory());
    /**
     * 标识初始化是否成功
     */
    private volatile boolean initSuccess = false;
    /**
     * IdCache
     */
    private Map<String, SegmentBuffer> IdCache = new ConcurrentHashMap<String, SegmentBuffer>();

    @Autowired
    private UniqueRecordMapper uniqueRecordMapper;

    public static class UpdateThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Segment-Update-" + nextThreadNum());
        }
    }

    @Override
    public boolean init() {
        boolean beSuccess = updateTagsFromDb();
        if (beSuccess) {
            initSuccess = true;
        }
        // 定时线程：更新IdCache
        updateCacheFromDbAtEveryMinute();
        return initSuccess;
    }

    /**
     * Update Tags From Db
     *
     * @return
     */
    private boolean updateTagsFromDb() {
        logger.info("Update Tags From Db Start");
        // 标识更新IdCache是否成功
        boolean beSuccess = false;
        try {
            // 获取数据库中全部 bus_tag
            List<String> dbTags = uniqueRecordMapper.getAllTags();
            if (dbTags == null || dbTags.isEmpty()) {
                beSuccess = true;
                return beSuccess;
            }
            // IdCache 中的全部 bus_tag
            List<String> cacheTags = new ArrayList<String>(IdCache.keySet());
            Set<String> insertTagsSet = new HashSet<>(dbTags);
            Set<String> removeTagsSet = new HashSet<>(cacheTags);
            for (int i = 0; i < cacheTags.size(); i++) {
                String tmp = cacheTags.get(i);
                if (insertTagsSet.contains(tmp)) {
                    // 数据库新增的 bus_tag
                    insertTagsSet.remove(tmp);
                }
            }
            for (String tag : insertTagsSet) {
                SegmentBuffer buffer = new SegmentBuffer();
                buffer.setTag(tag);
                Segment segment = buffer.getCurrent();
                segment.setValue(new AtomicLong(0));
                segment.setMax(0);
                segment.setStep(0);
                IdCache.put(tag, buffer);
                logger.info("Add Tags From Db To IdCache, SegmentBuffer {}", tag, buffer);
            }
            for (int i = 0; i < dbTags.size(); i++) {
                String tmp = dbTags.get(i);
                if (removeTagsSet.contains(tmp)) {
                    // 数据库中已经不存在的 bus_tag
                    removeTagsSet.remove(tmp);
                }
            }
            for (String tag : removeTagsSet) {
                IdCache.remove(tag);
                logger.info("Remove Tags In IdCache", tag);
            }
            beSuccess = true;
            logger.info("Update Tags From Db Ready");
            return beSuccess;
        } catch (Exception e) {
            logger.warn("Update Tags From Db Exception", e);
            beSuccess = false;
            return beSuccess;
        }
    }

    /**
     * 定时线程：Update Cache From Db
     */
    private void updateCacheFromDbAtEveryMinute() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("Update-IdCache-Thread");
                t.setDaemon(true);
                return t;
            }
        });
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                updateTagsFromDb();
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    @Override
    public long getUniqueId(String tag) throws InitException {
        if (!initSuccess) {
            throw new InitException("IdCache 未初始化");
        }
        if (IdCache.containsKey(tag)) {
            SegmentBuffer buffer = IdCache.get(tag);
            // buffer 未更新先更新buffer
            if (!buffer.isinitSuccess()) {
                synchronized (buffer) {
                    // 双重校验防止被其他线程更新了
                    if (!buffer.isinitSuccess()) {
                        try {
                            updateSegmentFromDb(tag, buffer.getCurrent());
                            logger.info("Init Tag {} Buffer {} From Db", tag, buffer.getCurrent());
                            // buffer 初始化成功
                            buffer.setinitSuccess(true);
                        } catch (Exception e) {
                            logger.warn("Init Tag {} Buffer {} From Db Exception", tag, buffer.getCurrent(), e);
                        }
                    }
                }
            }
            return getIdFromSegmentBuffer(IdCache.get(tag));
        } else {
            throw new NilTagException("IdCache中不存在tag:" + tag);
        }
    }

    /**
     * Update Buffer Segment From Db
     *
     * @param tag
     * @param segment
     */
    public void updateSegmentFromDb(String tag, Segment segment) {
        SegmentBuffer buffer = segment.getBuffer();
        UniqueRecord uniqueRecord = null;
        if (!buffer.isinitSuccess()) {
            //未初始化
            // bus_tag 更新并获取max_id
            uniqueRecord = updateMaxIdAndGetUniqueRecord(tag);
            buffer.setStep(uniqueRecord.getStep());
            buffer.setMinStep(uniqueRecord.getStep());
        } else if (buffer.getUpdateTimestamp() == 0) {
            uniqueRecord = updateMaxIdAndGetUniqueRecord(tag);
            buffer.setUpdateTimestamp(System.currentTimeMillis());
            buffer.setStep(uniqueRecord.getStep());
            buffer.setMinStep(uniqueRecord.getStep());
        } else {
            // 当剩余ID不足会预更新另一个 Buffer Segment
            long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
            int nextStep = buffer.getStep();
            //当更新时间小于15分钟，扩大步长，更新时间大于30分钟，缩小步长。 为了防止某时间段业务量飙升
            if (duration < SEGMENT_DURATION) {
                if (nextStep * 2 > MAX_STEP) {
                    //Do Nothing
                } else {
                    nextStep = nextStep * 2;
                }
            } else if (duration < SEGMENT_DURATION * 2) {
                //Do Nothing with nextStep
            } else {
                nextStep = nextStep / 2 >= buffer.getMinStep() ? nextStep / 2 : nextStep;
            }
            logger.info("tag[{}], step[{}], duration[{}mins], nextStep[{}]", tag, buffer.getStep(), String.format("%.2f", ((double) duration / (1000 * 60))), nextStep);
            UniqueRecord temp = new UniqueRecord();
            temp.setTag(tag);
            temp.setStep(nextStep);
            uniqueRecord = updateMaxIdByCustomStepAndGetLeafAlloc(temp);
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

    @Transactional
    public UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        uniqueRecordMapper.updateMaxId(tag);
        return uniqueRecordMapper.getUniqueRecord(tag);
    }

    @Transactional
    public UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(UniqueRecord uniqueRecord) {
        String tag = uniqueRecord.getTag();
        int step = uniqueRecord.getStep();
        uniqueRecordMapper.updateMaxIdByCustomStep(tag, step);
        return uniqueRecordMapper.getUniqueRecord(tag);

    }

    public long getIdFromSegmentBuffer(final SegmentBuffer buffer) throws InitException {
        while (true) {
            //加读锁
            buffer.getReadLock().lock();
            try {
                final Segment segment = buffer.getCurrent();
                // buffer.getThreadRunning() 标识整站初始化 防止同时出现多个线程初始化
                if (!buffer.isNextReady() && (segment.getIdle() < segment.getStep() / 2) && buffer.getThreadRunning().compareAndSet(false, true)) {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            Segment next = buffer.getSegments()[buffer.nextPos()];
                            boolean updateOk = false;
                            try {
                                updateSegmentFromDb(buffer.getTag(), next);
                                updateOk = true;
                                logger.info("Update Buffer Segment {} From Db {}", buffer.getTag(), next);
                            } catch (Exception e) {
                                logger.warn(buffer.getTag() + " Update Buffer Segment From Db Exception", e);
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
                    logger.error("Both Two Buffer Segments In {} Are Not Ready!", buffer);
                    throw new InitException("SegmentBuffer中的两个Segment均未从DB中装载");
                }
            } finally {
                buffer.getWriteLock().unlock();
            }
        }
    }
}
