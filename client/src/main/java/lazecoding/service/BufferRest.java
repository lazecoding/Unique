package lazecoding.service;

import lazecoding.api.OpenApi;
import lazecoding.model.UniqueRecord;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * BufferRest 服务端请求
 *
 * @author lazecoding
 */
public class BufferRest {

    static RestTemplate restTemplate = new RestTemplate();

    public static List<String> getAllTags() {
        String requestUrl = OpenApi.UNIQUE_CLIENT_CONFIG.getUrl() + "/api/all/tags";
        List<String> list = restTemplate.getForObject(requestUrl, List.class);
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
