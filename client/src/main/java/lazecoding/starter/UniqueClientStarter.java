package lazecoding.starter;

import lazecoding.config.UniqueClientConfig;
import lazecoding.api.OpenApi;
import lazecoding.service.BufferHolder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * UniqueClientStarter
 *
 * @author lazecoding
 */
@Configuration
@EnableConfigurationProperties(UniqueClientConfig.class)
public class UniqueClientStarter {

    @Resource
    private UniqueClientConfig uniqueClientConfig;

    @Bean
    public OpenApi openApi(UniqueClientConfig uniqueClientConfig) {
        OpenApi openApi = new OpenApi();
        openApi.init(uniqueClientConfig);
        BufferHolder.init();
        return openApi;
    }

}
