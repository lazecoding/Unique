package lazecoding.unique.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lazecoding.unique.model.SegmentBuffer;
import lazecoding.unique.model.SegmentBufferView;
import lazecoding.unique.service.BufferHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Monitor
 *
 * @author lazecoding
 */
@RestController
public class MonitorController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private BufferHolder bufferHolder;

    /**
     * 获取内存中的 SegmentBuffer 运行状况
     *
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/monitor/segment-buffer", method = RequestMethod.GET)
    @ResponseBody
    public String segmentBuffer() throws JsonProcessingException {
        Map<String, SegmentBuffer> cache = bufferHolder.getIdCache();
        Map<String, SegmentBufferView> data = new HashMap<>();
        for (Map.Entry<String, SegmentBuffer> entry : cache.entrySet()) {
            SegmentBufferView segmentBufferView = new SegmentBufferView();
            SegmentBuffer buffer = entry.getValue();
            segmentBufferView.setInitSuccess(buffer.isInitSuccess());
            segmentBufferView.setTag(buffer.getTag());
            segmentBufferView.setPos(buffer.getCurrentPos());
            segmentBufferView.setNextReady(buffer.isNextReady());
            segmentBufferView.setMax0(buffer.getSegments()[0].getMax());
            segmentBufferView.setValue0(buffer.getSegments()[0].getValue().get());
            segmentBufferView.setStep0(buffer.getSegments()[0].getStep());
            segmentBufferView.setMax1(buffer.getSegments()[1].getMax());
            segmentBufferView.setValue1(buffer.getSegments()[1].getValue().get());
            segmentBufferView.setStep1(buffer.getSegments()[1].getStep());
            data.put(entry.getKey(), segmentBufferView);
        }
        String responseContent = MAPPER.writeValueAsString(data);
        return responseContent;
    }
}
