package lazecoding.unique.controller;

import lazecoding.exception.NilParamException;
import lazecoding.model.UniqueRecord;
import lazecoding.unique.service.SegmentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SegmentManagerController
 *
 * @author lazecoding
 */
@RestController
public class SegmentManagerController {

    @Autowired
    private SegmentManager segmentManager;

    @RequestMapping(value = "/api/tags/{namespace}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getTags(@PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("未正确输入 namespace:" + namespace);
        }
        return segmentManager.getTags(namespace);
    }

    @RequestMapping(value = "/api/apply/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public UniqueRecord apply(@PathVariable("tag") String tag) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入tag" + tag);
        }
        return segmentManager.updateMaxIdAndGetUniqueRecord(tag);
    }

    @RequestMapping(value = "/api/apply/{tag}/{step}", method = RequestMethod.GET)
    @ResponseBody
    public UniqueRecord apply(@PathVariable("tag") String tag, @PathVariable("step") Integer step) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入tag" + tag);
        }
        if (step == null || step < 1) {
            throw new NilParamException("未正确输入tag" + tag);
        }
        UniqueRecord uniqueRecord = new UniqueRecord();
        uniqueRecord.setTag(tag);
        uniqueRecord.setStep(step);
        return segmentManager.updateMaxIdByCustomStepAndGetLeafAlloc(uniqueRecord);
    }

}
