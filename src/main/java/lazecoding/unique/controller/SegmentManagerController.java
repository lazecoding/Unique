package lazecoding.unique.controller;

import lazecoding.exception.NilNameSpaceException;
import lazecoding.exception.NilParamException;
import lazecoding.model.UniqueRecord;
import lazecoding.mvc.ResultBean;
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

    /**
     * 根据 namespace 获取 tags
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/tag/{namespace}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTag(@PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("未正确输入 namespace:" + namespace);
        }
        List<String> tags = null;
        boolean isSuccess = false;
        String message = "";
        try {
            tags = segmentManager.getTags(namespace);
            isSuccess = true;
            message = "获取成功";
        } catch (NilNameSpaceException e) {
            isSuccess = false;
            message = "该 namespace 不存在";
        } catch (Exception e) {
            isSuccess = false;
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(tags);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 申请号段
     */
    @RequestMapping(value = "/api/apply/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("tag") String tag) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入 tag:" + tag);
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
    @RequestMapping(value = "/api/apply/{tag}/{step}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("tag") String tag, @PathVariable("step") Integer step) {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入 tag:" + tag);
        }
        if (step == null || step < 1) {
            throw new NilParamException("未正确输入 step:" + tag);
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
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(uniqueRecord);
        resultBean.setMessage(message);
        return resultBean;
    }

}
