package personal.unique.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import personal.unique.controller.UniqueRecordController;
import personal.unique.exception.InitException;
import personal.unique.service.UniqueRecordService;

/**
 * @className: OnStart
 * @package: personal.unique.init
 * @description: 系统启动进行IdCache预热
 * @datetime: 2020/10/12   22:28
 * @author: lazecoding
 */
@Component("OnStart")
public class OnStart implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(OnStart.class);

    @Autowired
    private UniqueRecordService uniqueRecordService;

    @Override
    public void run(String... args) throws InitException {
        logger.info("OnStart To Init Cache Begin");
        boolean flag = uniqueRecordService.init();
        if (flag) {
            logger.info("OnStart To Init Cache Success");
        } else {
            logger.warn("OnStart To Init Cache Fail");
        }
    }
}
