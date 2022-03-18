package lazecoding.unique.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lazecoding.unique.service.UniqueRecordService;

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
     * 初始化 Tags
     *
     * @return
     */
    public boolean initTags() {
        logger.info("Init Tags Strat");
        boolean flag = uniqueRecordService.init();
        if (flag) {
            logger.info("Init Tags Ready");
        } else {
            logger.warn("Init Tags Fail");
        }
        return flag;
    }
}
