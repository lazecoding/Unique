package personal.unique.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import personal.unique.service.UniqueRecordService;

/**
 * @author: lazecoding
 * @date: 2020/12/4 20:54
 * @description: BufferCache 初始化类
 */
@Component("CacheInit")
public class CacheInit {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UniqueRecordService uniqueRecordService;

    /**
     * 初始化 BufferCache
     * @return
     */
    public boolean initCache() {
        logger.info("OnStart To Init Cache Begin");
        boolean flag = uniqueRecordService.init();
        if (flag) {
            logger.info("OnStart To Init Cache Success");
        } else {
            logger.warn("OnStart To Init Cache Fail");
        }
        return flag;
    }
}
