package lazecoding.unique.controller;

import lazecoding.unique.exception.IllegalParamException;
import lazecoding.unique.exception.NilParamException;
import lazecoding.unique.model.UniqueRecord;
import lazecoding.unique.mvc.ResultBean;
import lazecoding.unique.service.SegmentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * SegmentManagerController
 *
 * @author lazecoding
 */
@RestController
public class SegmentManagerController {

    private static final Logger logger = LoggerFactory.getLogger(SegmentManagerController.class);

    @Autowired
    private SegmentManager segmentManager;

    /**
     * 申请号段
     */
    @RequestMapping(value = "/api/segment/apply/{namespace}/{region}/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("namespace") String namespace, @PathVariable("region") String region, @PathVariable("tag") String tag) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        if (!StringUtils.hasText(region)) {
            throw new NilParamException("region 不得为空");
        }
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        UniqueRecord uniqueRecord = null;
        boolean isSuccess = false;
        String message = "";
        try {
            uniqueRecord = segmentManager.updateMaxIdAndGetUniqueRecord(namespace, region, tag);
            isSuccess = true;
            message = "获取成功";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/segment/apply/{namespace}/{region}/{tag}] 获取失败", e);
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(uniqueRecord);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 申请号段（自定义）
     */
    @RequestMapping(value = "/api/segment/apply/{namespace}/{region}/{tag}/{step}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("namespace") String namespace, @PathVariable("region") String region, @PathVariable("tag") String tag, @PathVariable("step") Integer step) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        if (!StringUtils.hasText(region)) {
            throw new NilParamException("region 不得为空");
        }
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (step == null) {
            throw new NilParamException("step 不得为空");
        }
        if (step < 20000) {
            throw new IllegalParamException("start 不可以小于 20,000");
        }
        if (step > 1000000) {
            throw new IllegalParamException("start 不可以大于 1000,000");
        }
        UniqueRecord uniqueRecord = null;
        boolean isSuccess = false;
        String message = "";
        try {
            uniqueRecord = segmentManager.updateMaxIdByCustomStepAndGetLeafAlloc(namespace, region, tag, step);
            isSuccess = true;
            message = "获取成功";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/segment/apply/{namespace}/{region}/{tag}/{step}] 获取失败", e);
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(uniqueRecord);
        resultBean.setMessage(message);
        return resultBean;
    }

}
