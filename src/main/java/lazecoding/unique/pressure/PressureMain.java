package lazecoding.unique.pressure;

import lazecoding.unique.pressure.mapper.PressureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 压力数据入口
 *
 * @author lazecoding
 */
@RestController
public class PressureMain {

    private Logger logger = LoggerFactory.getLogger(PressureMain.class);

    @Autowired
    private PressureMapper pressureMapper;

    /**
     * http://localhost:8090/pressure/uuid
     *
     * @return
     */
    @RequestMapping(value = "/pressure/uuid", method = RequestMethod.GET)
    @ResponseBody
    public String pressureUuid() {
        String result = "\nstart";
        long countNum = 0;
        long time = System.currentTimeMillis();
        long startTime = time;
        while (countNum < 1000000) {
            if (countNum % 1000 == 0) {
                long currentTime = System.currentTimeMillis();
                long cost = currentTime - time;
                time = currentTime;
                String log = "current-time:" + currentTime + " current-num:" + countNum + " per 1000 cost-time:" + cost;
                logger.info("/pressure/uuid >> " + log);
                result += "\n" + log;
            }
            countNum++;
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            long longId = countNum;
            long softId = countNum;
            pressureMapper.pressureUuid(uuid, "张三" + countNum, softId);
        }
        result += "\natomicInteger:" + countNum + " whole-cost:" + (time - startTime);
        result += "\nend";
        logger.info("/pressure/uuid >> " + result);
        return result;
    }

    /**
     * http://localhost:8090/pressure/long
     *
     * @return
     */
    @RequestMapping(value = "/pressure/long", method = RequestMethod.GET)
    @ResponseBody
    public String pressureLong() {
        String result = "\nstart";
        long countNum = 0;
        long time = System.currentTimeMillis();
        long startTime = time;
        while (countNum < 1000000) {
            if (countNum % 1000 == 0) {
                long currentTime = System.currentTimeMillis();
                long cost = currentTime - time;
                time = currentTime;
                String log = "current-time:" + currentTime + " current-num:" + countNum + " per 1000 cost-time:" + cost;
                logger.info("/pressure/long >> " + log);
                result += "\n" + log;
            }
            countNum++;
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            long longId = countNum;
            long softId = countNum;
            pressureMapper.pressureLong(longId, "张三" + countNum, softId);
        }
        result += "\natomicInteger:" + countNum + " whole-cost:" + (time - startTime);
        result += "\nend";
        logger.info("/pressure/long >> " + result);
        return result;
    }

    /**
     * http://localhost:8090/pressure/m-uuid
     *
     * @return
     */
    @RequestMapping(value = "/pressure/m-uuid", method = RequestMethod.GET)
    @ResponseBody
    public String pressureMUuid() {
        int totolNum = 100000;
        int coreNum = 8;
        int size = totolNum / coreNum;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreNum, coreNum, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setName("executor-async-uuid");
                    thread.setDaemon(true);
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy());

        int start = 0;
        int end = 0;
        AtomicInteger atomicInteger = new AtomicInteger();
        int threadNum = 1;
        while (threadNum <= coreNum) {
            start = end + 1;
            if (threadNum == coreNum) {
                end = totolNum;
            } else {
                end = start + size;
            }
            threadNum++;

            int finalEnd = end;
            int finalStart = start;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    long startTime = time;
                    Thread.currentThread().setName("executor-async-uuid start:" + finalStart + " end:" + finalEnd);
                    logger.info(Thread.currentThread().getName() + " start-time:" + startTime);
                    for (int i = finalStart; i <= finalEnd; i++) {
                        long longId = atomicInteger.incrementAndGet();
                        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                        pressureMapper.pressureUuid(uuid, "张三" + longId, longId);
                    }
                    long endTime = System.currentTimeMillis();
                    long costTime = endTime - startTime;
                    logger.info(Thread.currentThread().getName() + " end-time:" + endTime + "  cost-time:" + costTime + " atomicInteger:" + atomicInteger.get());

                }
            });

        }
        return "/pressure/m-uuid";
    }

    /**
     * http://localhost:8090/pressure/m-long
     *
     * @return
     */
    @RequestMapping(value = "/pressure/m-long", method = RequestMethod.GET)
    @ResponseBody
    public String pressureMLong() {
        int totolNum = 100000;
        int coreNum = 8;
        int size = totolNum / coreNum;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreNum, coreNum, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setName("executor-async-uuid");
                    thread.setDaemon(true);
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy());

        int start = 0;
        int end = 0;
        AtomicInteger atomicInteger = new AtomicInteger();
        int threadNum = 1;
        while (threadNum <= coreNum) {
            start = end + 1;
            if (threadNum == coreNum) {
                end = totolNum;
            } else {
                end = start + size;
            }
            threadNum++;

            int finalEnd = end;
            int finalStart = start;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    long startTime = time;
                    Thread.currentThread().setName("executor-async-long start:" + finalStart + " end:" + finalEnd);
                    logger.info(Thread.currentThread().getName() + " start-time:" + startTime);
                    for (int i = finalStart; i <= finalEnd; i++) {
                        long longId = atomicInteger.incrementAndGet();
                        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                        pressureMapper.pressureLong(longId, "张三" + longId, longId);
                    }
                    long endTime = System.currentTimeMillis();
                    long costTime = endTime - startTime;
                    logger.info(Thread.currentThread().getName() + " end-time:" + endTime + "  cost-time:" + costTime + " atomicInteger:" + atomicInteger.get());
                }
            });

        }
        return "/pressure/m-long";
    }

    public static void main(String[] args) {
        /*
        10W:
        long  1647666810511 - 1647666381589
        uuid  1647666354175 - 1647665922048
        longCost:428922  uuidCost:432127  相差:3205
         */
        long longCost = 1647666810511L - 1647666381589L;
        long uuidCost = 1647666354175L - 1647665922048L;

        System.out.println("longCost:" + longCost + "  uuidCost:" + uuidCost + "  相差:" + (uuidCost - longCost));
    }

}
