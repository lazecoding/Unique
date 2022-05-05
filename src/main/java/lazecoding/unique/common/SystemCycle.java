package lazecoding.unique.common;

import lazecoding.unique.config.ServerConfig;
import lazecoding.unique.service.NameSpaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 系统生命周期行为
 *
 * @author lazecoding
 */
@Component("systemCycle")
public class SystemCycle {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 系统初始化 （服务启动前）
     */
    public void init() {
        logger.info("SystemInit Start");
        // 1.检验 authorization 是否配置
        ServerConfig serverConfig = BeanUtil.getBean("serverConfig", ServerConfig.class);
        if (ObjectUtils.isEmpty(serverConfig) || !StringUtils.hasText(serverConfig.getAuthorization())) {
            logger.error("ServerConfig:[{}]", serverConfig);
            // 初始化失败，关闭容器
            this.exit();
            return;
        }
        logger.info("ServerConfig:[{}]", serverConfig);
        // 2. 初始化系统 namespace
        NameSpaceManager nameSpaceManager = BeanUtil.getBean("nameSpaceManager", NameSpaceManager.class);
        nameSpaceManager.init();
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
