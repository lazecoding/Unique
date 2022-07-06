package lazecoding.unique.listener;

import lazecoding.unique.controller.SegmentManagerController;
import lazecoding.unique.service.SystemCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * AppStartupRunner 系统启动后执行
 *
 * @author lazecoding
 */
@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    private SystemCycle systemCycle;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            systemCycle.init();
        } catch (Exception e) {
            logger.error("ApplicationRunner Exception", e);
        }
    }
}
