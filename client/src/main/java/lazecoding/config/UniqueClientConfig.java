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

    /**
     * 区域
     */
    private String region = "";

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", namespace='" + namespace + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
