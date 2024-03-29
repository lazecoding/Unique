package lazecoding.api;

import lazecoding.config.ClientConstant;
import lazecoding.config.UniqueClientConfig;
import lazecoding.exception.IllegalParamException;
import lazecoding.exception.InitException;
import lazecoding.exception.NilParamException;
import lazecoding.model.UniqueRecord;
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

    /**
     * 获取 Id
     */
    public static long getUniqueId(String tag) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        return BufferHolder.getUniqueId(tag);
    }

    /**
     * 批量获取 Id
     */
    public static List<Long> batch(String tag, Integer size) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (size == null) {
            throw new NilParamException("size 不得为空");
        }
        return BufferBatcher.batch(tag, size);
    }

    // TODO namespace-region 下 CURD tag

    /**
     * 获取客户端配置的 namespace-region 下所有 tags
     */
    public static List<String> getTags() {
        return BufferHolder.getTags();
    }


    /**
     * 判断客户端配置的 namespace-region 下是否存在某 tag
     */
    public static boolean existTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        return BufferHolder.existTag(tag);
    }


    /**
     * 在客户端配置的 namespace-region 下新增 tag
     */
    public static UniqueRecord addTag(String tag, long maxId, int step, String description) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (maxId < 1) {
            throw new IllegalParamException("初始化的 maxId 值不可以小于 1");
        }
        if (step < ClientConstant.MIN_STEP) {
            throw new IllegalParamException("start 不可以小于 " + ClientConstant.MIN_STEP);
        }
        if (step > ClientConstant.MAX_STEP) {
            throw new IllegalParamException("start 不可以大于 " + ClientConstant.MAX_STEP);
        }
        if (!StringUtils.hasText(description)) {
            throw new NilParamException("description 不得为空");
        }
        return BufferHolder.addTag(tag, maxId, step, description);
    }

    /**
     * 在客户端配置的 namespace-region 下删除 tag
     */
    public static boolean removeTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        return BufferHolder.removeTag(tag);
    }

}
