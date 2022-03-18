package lazecoding.unique.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import lazecoding.unique.init.CacheInit;

/**
 * @author: lazecoding
 * @date: 2020/12/4 20:47
 * @description: 系统生命周期行为
 */
@Component("SystemCycle")
public class SystemCycle {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CacheInit cacheInit;

    /**
     * 系统初始化 （服务启动前）
     */
    public void init() {
        logger.info("SystemInit Start");
        boolean flag = false;
        // 初始化 Tags
        flag = cacheInit.initTags();
        if (!flag) {
            // 初始化失败，关闭容器
            this.exit();
            return;
        }
        logger.info("SystemInit Ready");
    }

    /**
     * 容器关闭
     */
    public void exit() {
        logger.info("SpringApplication Exit Start");
        SpringApplication.exit(applicationContext);
        logger.info("SpringApplication Exit Starting");
    }
}
