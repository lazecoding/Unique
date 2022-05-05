package lazecoding.service;

import lazecoding.api.OpenApi;
import lazecoding.model.UniqueRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * BufferRest 服务端请求
 *
 * @author lazecoding
 */
public class BufferRest {

    private static final Logger logger = LoggerFactory.getLogger(BufferRest.class);


    static RestTemplate restTemplate = new RestTemplate();

    /**
     * 根据 namespace 获取 tags
     *
     * @return
     */
    public static List<String> getTags() {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/tags/" + OpenApi.UNIQUE_CLIENT_CONFIG.getNamespace();
        List<String> list = null;
        try {
            list = restTemplate.getForObject(requestUrl, List.class);
        } catch (RestClientException e) {
            logger.error("requestUrl:[{}] ERROR.",requestUrl);
        }
        return list;
    }

    /**
     * 申请号段
     */
    public static UniqueRecord updateMaxIdAndGetUniqueRecord(String tag) {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/apply/" + tag;
        UniqueRecord uniqueRecord = restTemplate.getForObject(requestUrl, UniqueRecord.class);
        return uniqueRecord;
    }

    /**
     * 申请号段（自定义步长）
     */
    public static UniqueRecord updateMaxIdByCustomStepAndGetLeafAlloc(UniqueRecord uniqueRecord) {
        String tag = uniqueRecord.getTag();
        int step = uniqueRecord.getStep();
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/apply/" + tag + "/" + step;
        uniqueRecord = restTemplate.getForObject(requestUrl, UniqueRecord.class);
        return uniqueRecord;
    }

}
