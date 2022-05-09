package lazecoding.unique.controller;

import lazecoding.exception.IllegalParamException;
import lazecoding.exception.NilParamException;
import lazecoding.model.UniqueRecord;
import lazecoding.mvc.ResultBean;
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
    @RequestMapping(value = "/api/segment/apply/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("tag") String tag) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        UniqueRecord uniqueRecord = null;
        boolean isSuccess = false;
        String message = "";
        try {
            uniqueRecord = segmentManager.updateMaxIdAndGetUniqueRecord(tag);
            isSuccess = true;
            message = "获取成功";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/segment/apply/{tag}] 获取失败", e);
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
    @RequestMapping(value = "/api/segment/apply/{tag}/{step}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("tag") String tag, @PathVariable("step") Integer step) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (step == null) {
            throw new NilParamException("step 不得为空");
        }
        if (step < 1) {
            throw new IllegalParamException("未正确输入 step:" + tag);
        }
        UniqueRecord uniqueRecord = null;
        boolean isSuccess = false;
        String message = "";
        try {
            uniqueRecord = segmentManager.updateMaxIdByCustomStepAndGetLeafAlloc(tag, step);
            isSuccess = true;
            message = "获取成功";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/segment/apply/{tag}/{step}] 获取失败", e);
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(uniqueRecord);
        resultBean.setMessage(message);
        return resultBean;
    }

}
