package lazecoding.unique.listener;

import lazecoding.unique.service.SystemCycle;
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
    @Autowired
    private SystemCycle systemCycle;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        systemCycle.init();
    }
}
