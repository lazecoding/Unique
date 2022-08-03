package lazecoding.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.api.OpenApi;
import lazecoding.model.UniqueRecord;
import lazecoding.mvc.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    private static RestTemplate restTemplate;

    static {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30 * 1000);
        httpRequestFactory.setConnectTimeout(30 * 1000);
        httpRequestFactory.setReadTimeout(30 * 1000);
        restTemplate = new RestTemplate(httpRequestFactory);
    }

    /**
     * 获取客户端配置的 namespace-region 下所有 tags
     */
    public static List<String> getTags() {
        // /api/tag/get/{namespace}/{region}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/get/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace() + "/" + OpenApi.UNIQUE_CLIENT_CONFIG.getRegion();
        List<String> list = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                list = MAPPER.convertValue(resultBean.getValue(), new TypeReference<List<String>>() {
                });
            } else {
                throw new RuntimeException("getTags ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[" + requestUrl + "] ERROR.", e);
            throw new RestClientException("requestUrl:[" + requestUrl + "] ERROR.");
        }
        return list;
    }

    /**
     * 判断客户端配置的 namespace-region 下是否存在某 tag
     */
    public static boolean existTag(String tag) {
        // /api/tag/exist/{namespace}/{region}/{tag}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/exist/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace()
                + "/" + OpenApi.UNIQUE_CLIENT_CONFIG.getRegion() + "/" + tag;
        boolean isExist = false;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                isExist = MAPPER.convertValue(resultBean.getValue(), new TypeReference<Boolean>() {
                });
            } else {
                throw new RuntimeException("existTag ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[" + requestUrl + "] ERROR.", e);
            throw new RestClientException("requestUrl:[" + requestUrl + "] ERROR.");
        }
        return isExist;
    }

    /**
     * 在客户端配置的 namespace-region 下新增 tag
     */
    public static UniqueRecord addTag(String tag, long maxId, int step, String description) {
        //  /api/tag/add/{namespace}/{region}/{tag}/{maxId}/{step}/{description}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/add/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace()
                + "/" + OpenApi.UNIQUE_CLIENT_CONFIG.getRegion() + "/" + tag + "/" + maxId + "/" + step + "/" + description;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                uniqueRecord = MAPPER.convertValue(resultBean.getValue(), new TypeReference<UniqueRecord>() {
                });
            } else {
                throw new RuntimeException("addTag ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[" + requestUrl + "] ERROR.", e);
            throw new RestClientException("requestUrl:[" + requestUrl + "] ERROR.");
        }
        return uniqueRecord;
    }

    /**
     * 在客户端配置的 namespace-region 下删除 tag
     */
    public static boolean removeTag(String tag) {
        //  /api/tag/remove/{namespace}/{region}/{tag}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/remove/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace()
                + "/" + OpenApi.UNIQUE_CLIENT_CONFIG.getRegion() + "/" + tag;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            return resultBean.isSuccess();
        } catch (RestClientException e) {
            logger.error("requestUrl:[" + requestUrl + "] ERROR.", e);
            throw new RestClientException("requestUrl:[" + requestUrl + "] ERROR.");
        }
    }

    /**
     * 申请号段
     */
    public static UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        // /api/segment/apply/{namespace}/{region}/{tag}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/segment/apply/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace()
                + "/" + OpenApi.UNIQUE_CLIENT_CONFIG.getRegion() + "/" + tag;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                uniqueRecord = MAPPER.convertValue(resultBean.getValue(), new TypeReference<UniqueRecord>() {
                });
            } else {
                throw new RuntimeException("updateMaxIdAndGetUniqueRecord ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[" + requestUrl + "] ERROR.", e);
            throw new RestClientException("requestUrl:[" + requestUrl + "] ERROR.");
        }
        return uniqueRecord;
    }

    /**
     * 申请号段（自定义步长）
     */
    public static UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(String tag, int step) {
        // /api/segment/apply/{namespace}/{region}/{tag}/{step}
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/segment/apply/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace()
                + "/" + OpenApi.UNIQUE_CLIENT_CONFIG.getRegion() + "/" + tag + "/" + step;
        UniqueRecord uniqueRecord = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                uniqueRecord = MAPPER.convertValue(resultBean.getValue(), new TypeReference<UniqueRecord>() {
                });
            } else {
                throw new RuntimeException("updateMaxIdByCustomStepAndGetLeafAlloc ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[" + requestUrl + "] ERROR.", e);
            throw new RestClientException("requestUrl:[" + requestUrl + "] ERROR.");
        }
        return uniqueRecord;
    }

}
