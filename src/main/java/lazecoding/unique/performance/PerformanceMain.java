package lazecoding.unique.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.api.OpenApi;
import lazecoding.exception.NilParamException;
import lazecoding.exception.NilTagException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 性能表现
 *
 * @author lazecoding
 */
@RestController
public class PerformanceMain {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 性能表现 get
     *
     * http://localhost:8090/performance/get/unique-record-segment-test/40/200000
     *
     * @param tags    请求的 tag，英文逗号分隔。
     * @param threads 线程数量
     * @return
     * @throws JsonProcessingException
     * @[param times   每个线程执行次数
     */
    @RequestMapping(value = "/performance/get/{tags}/{threads}/{times}", method = RequestMethod.GET)
    @ResponseBody
    public String performanceGet(@PathVariable("tags") String tags
            , @PathVariable("threads") Integer threads
            , @PathVariable("times") Integer times
    ) {
        if (StringUtils.isEmpty(tags)) {
            throw new NilTagException("tags 不得为空");
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
        int totol = threads * times;
        while (threads > 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    int count = times;
                    while (count > 0) {
                        try {
                            int index = count % len;
                            OpenApi.getUniqueId(tagArray[index]);
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
     * 性能表现 batch
     *
     * http://localhost:8090/performance/batch/unique-record-segment-test/10/40/200000
     *
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/performance/batch/{tags}/{size}/{threads}/{times}", method = RequestMethod.GET)
    @ResponseBody
    public String performanceBatch(@PathVariable("tags") String tags
            , @PathVariable("size") Integer size
            , @PathVariable("threads") Integer threads
            , @PathVariable("times") Integer times
    ) {
        if (StringUtils.isEmpty(tags)) {
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

}