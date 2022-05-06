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
     * 根据 namespace 获取 tags
     *
     * @return
     */
    public static List<String> getTags() {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tag/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace();
        List<String> list = null;
        ResultBean resultBean;
        try {
            resultBean = restTemplate.getForObject(requestUrl, ResultBean.class);
            Assert.notNull(resultBean, "resultBean is null");
            if (resultBean.isSuccess()) {
                list = MAPPER.convertValue(resultBean.getValue(), new TypeReference<List<String>>() {});
            } else {
                throw new RuntimeException("updateMaxIdAndGetUniqueRecord ERROR:" + resultBean.getMessage());
            }
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.", requestUrl);
        }
        return list;
    }

    /**
     * 申请号段
     */
    public static UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/apply/" + tag;
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
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/apply/" + tag + "/" + step;
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
