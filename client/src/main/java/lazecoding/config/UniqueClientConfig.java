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

    /**
     * 服务端地址
     */
    private String url = "";

    /**
     * namespace
     */
    private String namespace = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return "UniqueClientConfig{" +
                "url='" + url + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
