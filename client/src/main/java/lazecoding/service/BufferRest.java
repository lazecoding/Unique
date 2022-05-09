package lazecoding.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.api.OpenApi;
import lazecoding.model.UniqueRecord;
import lazecoding.mvc.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * BufferRest 服务端请求
 *
 * @author lazecoding
 */
public class BufferRest {

    /**
     * jackson 序列化
     */
    private static ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(BufferRest.class);

    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * 获取客户端配置的 namespace 下所有 tags
     */
    public static List<String> getTags() {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/get/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace();
        List<String> list = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                list = MAPPER.convertValue(resultBean.getValue(), new TypeReference<List<String>>() {
                });
            } else {
                throw new RuntimeException("updateMaxIdAndGetUniqueRecord ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.", requestUrl);
        }
        return list;
    }

    /**
     * 在客户端配置的 namespace 下新增 tag
     */
    public static UniqueRecord addTag(String tag, long maxId, int step, String description) {
        //  /api/tag/add/{namespace}/{tag}/{maxId}/{step}/{description}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/add/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace()
                + "/" + tag + "/" + maxId + "/" + step + "/" + description;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                uniqueRecord = MAPPER.convertValue(resultBean.getValue(), new TypeReference<UniqueRecord>() {});
            } else {
                throw new RuntimeException("addTag ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.", requestUrl);
        }
        return uniqueRecord;
    }

    /**
     * 在客户端配置的 namespace 下删除 tag
     */
    public static boolean removeTag(String tag) {
        //  /api/tag/remove/{namespace}/{tag}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/remove/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace() + "/" + tag ;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            return resultBean.isSuccess();
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.", requestUrl);
        }
        return false;
    }

    /**
     * 申请号段
     */
    public static UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/segment/apply/" + tag;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                uniqueRecord = MAPPER.convertValue(resultBean.getValue(), new TypeReference<UniqueRecord>() {});
            } else {
                throw new RuntimeException("updateMaxIdAndGetUniqueRecord ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.", requestUrl);
            throw new RuntimeException(e.getCause().toString());
        }
        return uniqueRecord;
    }

    /**
     * 申请号段（自定义步长）
     */
    public static UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(String tag, int step) {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/segment/apply/" + tag + "/" + step;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                uniqueRecord = MAPPER.convertValue(resultBean.getValue(), new TypeReference<UniqueRecord>() {});
            } else {
                throw new RuntimeException("updateMaxIdByCustomStepAndGetLeafAlloc ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.", requestUrl);
            throw new RuntimeException(e.getCause().toString());
        }
        return uniqueRecord;
    }

}
