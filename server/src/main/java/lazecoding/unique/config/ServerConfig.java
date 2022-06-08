package lazecoding.unique.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 服务端配置
 *
 * @author lazecoding
 */
@Configuration("serverConfig")
@ConfigurationProperties(prefix = "project.server-config")
public class ServerConfig {
    /**
     * 授权码
     */
    private String authorization = "";

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "authorization='" + authorization + '\'' +
                '}';
    }
}

