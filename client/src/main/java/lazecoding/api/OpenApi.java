package lazecoding.api;

import lazecoding.config.UniqueClientConfig;
import lazecoding.exception.InitException;
import lazecoding.exception.NilParamException;
import lazecoding.service.BufferBatcher;
import lazecoding.service.BufferHolder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * OpenApi：对客户端开放的接口
 *
 * @author lazecoding
 */
public class OpenApi {

    public static UniqueClientConfig UNIQUE_CLIENT_CONFIG;

    public OpenApi() {
    }

    public void init(UniqueClientConfig uniqueClientConfig) {
        OpenApi.UNIQUE_CLIENT_CONFIG = uniqueClientConfig;
    }

    public static long getUniqueId(String tag) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入 tag:" + tag);
        }
        return BufferHolder.getUniqueId(tag);
    }

    public static List<Long> batch(String tag, Integer size) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (size == null) {
            throw new NilParamException("size 不得为空");
        }
        return BufferBatcher.batch(tag, size);
    }
}
