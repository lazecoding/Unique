package lazecoding.demo.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.api.OpenApi;
import lazecoding.exception.InitException;
import lazecoding.exception.NilParamException;
import lazecoding.exception.NilTagException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能表现
 *
 * @author lazecoding
 */
@RestController
public class PerformanceMain {

    /**
     * 性能表现 get
     *
     * @param tag     请求的 tag
     * @param threads 线程数量
     * @param times   每个线程执行次数
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/performance/get/{tag}/{threads}/{times}", method = RequestMethod.GET)
    @ResponseBody
    public String performanceGet(@PathVariable("tag") String tag
            , @PathVariable("threads") Integer threads
            , @PathVariable("times") Integer times
    ) {
        if (!StringUtils.hasText(tag)) {
            throw new NilTagException("tag 不得为空");
        }
        if (threads == null || threads < 1) {
            throw new NilParamException("线程数量不得为空");
        }
        if (times == null || times < 1) {
            throw new NilParamException("每个线程执行次数不得为空");
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CountDownLatch latch = new CountDownLatch(threads);

        // 获取开始的 Id
        Long startId = null;
        try {
            startId = OpenApi.getUniqueId(tag);
        } catch (InitException e) {
            throw new RuntimeException(e);
        }
        // 总数
        int total = threads * times;
        // 成功数量
        AtomicInteger successCount = new AtomicInteger(0);
        // 失败数量
        AtomicInteger failCount = new AtomicInteger(0);
        // 去重校验
        Set<Object> idSet = new ConcurrentSkipListSet<>();
        long startTime = System.currentTimeMillis();
        while (threads > 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int count = times;
                    while (count > 0) {
                        try {
                            OpenApi.getUniqueId(tag);
                            Object id = successCount.incrementAndGet();
                            idSet.add(id);
                        } catch (Exception e) {
                            failCount.incrementAndGet();
                            // System.out.println("error");
                        }
                        count--;
                    }
                    latch.countDown();
                }
            });
            threads--;
        }

        String responseContent = "压测结果:";
        try {
            latch.await();
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            // 期望值
            Long startExceptId = startId + (long) total + 1;
            Long lastExceptId = startId + successCount.get() + 1;
            Object trueId = OpenApi.getUniqueId(tag);
            responseContent += "<br> 处理结束 costTime:[" + costTime + "ms]";
            int idSetSize = idSet.size();
            idSet.clear();
            responseContent += "<br> tag:[" + tag + "] total:[" + total + "]  失败个数:[" + (failCount.get() > 0 ? getRedSpan(failCount) : failCount) + "] 成功个数:[" + successCount + "]  去重校验后集合元素个数:[" + idSetSize + "]";
            responseContent += "<br> 全成功期望值:[" + startExceptId + "]" + " 执行完毕期望值:[" + lastExceptId + "]" + " 真实值:[" + trueId + "]";
            responseContent += "<br> 执行完毕期望值 是否等于 真实值:" + (lastExceptId.equals(trueId) ? getBlueSpan(true) : getRedSpan(false));
            responseContent += "<br> 成功个数 是否等于 去重校验后的集合元素个数:" + (successCount.get() == idSetSize ? getBlueSpan(true) : getRedSpan(false));
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InitException e) {
            throw new RuntimeException(e);
        }

        return responseContent;
    }

    /**
     * 性能表现 batch
     *
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/performance/batch/{tags}/{size}/{threads}/{times}", method = RequestMethod.GET)
    @ResponseBody
    public String performanceBatch(@PathVariable("tags") String tags
            , @PathVariable("size") Integer size
            , @PathVariable("threads") Integer threads
            , @PathVariable("times") Integer times
    ) {
        if (!StringUtils.hasText(tags)) {
            throw new NilTagException("tags 不得为空");
        }
        if (size == null || size < 1) {
            throw new NilParamException("批量大小不得为空");
        }
        if (threads == null || threads < 1) {
            throw new NilParamException("线程数量不得为空");
        }
        if (times == null || times < 1) {
            throw new NilParamException("每个线程执行次数不得为空");
        }

        String[] tagArray = tags.split(",");
        int len = tagArray.length;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CountDownLatch latch = new CountDownLatch(threads);
        long startTime = System.currentTimeMillis();
        int totol = threads * times * size;
        while (threads > 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int count = times;
                    while (count > 0) {
                        try {
                            int index = count % len;
                            OpenApi.batch(tagArray[index], size);
                        } catch (Exception e) {
                            // System.out.println("error");
                        }
                        count--;
                    }
                    latch.countDown();
                }
            });
            threads--;
        }
        String responseContent = "压测结果:";
        responseContent += "<br> tags:" + Arrays.toString(tagArray) + " totol:" + totol;
        try {
            latch.await();
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            responseContent += "<br> 处理结束 costTime:" + costTime + "ms";
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return responseContent;
    }

    /**
     * 红色文本
     */
    private String getRedSpan(Object obj) {
        return "<span style=\"color: red;\">" + obj + "</span>";
    }

    /**
     * 蓝色文本
     */
    private String getBlueSpan(Object obj) {
        return "<span style=\"color: blue;\">" + obj + "</span>";
    }
}