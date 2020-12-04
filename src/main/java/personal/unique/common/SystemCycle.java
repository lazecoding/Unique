package personal.unique.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import personal.unique.init.CacheInit;

/**
 * @author: lazecoding
 * @date: 2020/12/4 20:47
 * @description: 系统生命周期行为
 */
@Component("SystemCycle")
public class SystemCycle {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CacheInit cacheInit;

    /**
     *  系统初始化 （服务启动前）
     */
    public void init() {
        logger.debug("SystemInit Start");
        // 初始化 Tags
        cacheInit.initTags();
        logger.debug("SystemInit Ready");
    }
}
