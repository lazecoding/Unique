package lazecoding.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * UniqueClientConfig：客户端配置
 *
 * @author lazecoding
 */
@Component
@ConfigurationProperties("unique.client")
public class UniqueClientConfig {

    private String url = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UniqueClientConfig{" +
                "url='" + url + '\'' +
                '}';
    }
}
