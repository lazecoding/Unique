package lazecoding.unique.controller;

import lazecoding.api.OpenApi;
import lazecoding.exception.AuthorizationException;
import lazecoding.exception.NilParamException;
import lazecoding.model.NameSpace;
import lazecoding.mvc.ResultBean;
import lazecoding.unique.config.ServerConfig;
import lazecoding.unique.service.NameSpaceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * SegmentManagerController
 *
 * @author lazecoding
 */
@RestController
public class NameSpaceController {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private NameSpaceManager nameSpaceManager;

    /**
     * 获取 namespace
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/namespace/find/{authorization}/{namespace}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean find(@PathVariable("authorization") String authorization, @PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(authorization)) {
            throw new NilParamException("未正确输入 authorization:" + authorization);
        }
        if (!serverConfig.getAuthorization().equals(authorization)) {
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("未正确输入 namespace:" + namespace);
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(true);
        resultBean.setValue(nameSpaceManager.findById(namespace));
        return resultBean;
    }

    /**
     * 删除 namespace
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/namespace/remove/{authorization}/{namespace}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean remove(@PathVariable("authorization") String authorization, @PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(authorization)) {
            throw new NilParamException("未正确输入 authorization:" + authorization);
        }
        if (!serverConfig.getAuthorization().equals(authorization)) {
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("未正确输入 namespace:" + namespace);
        }
        boolean isSuccess = false;
        String message = "";
        try {
            nameSpaceManager.remove(namespace);
            isSuccess = true;
            message = "删除成功";
        } catch (Exception e) {
            isSuccess = false;
            message = "删除失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 申请 namespace
     *
     * @param description 描述
     */
    @RequestMapping(value = "/api/namespace/apply/{authorization}/{description}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean apply(@PathVariable("authorization") String authorization, @PathVariable("description") String description) {
        if (!StringUtils.hasText(authorization)) {
            throw new NilParamException("未正确输入 authorization:" + authorization);
        }
        if (!serverConfig.getAuthorization().equals(authorization)) {
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(description)) {
            throw new NilParamException("未正确输入 description:" + description);
        }
        boolean isSuccess = false;
        NameSpace nameSpace = null;
        String message = "";
        try {
            nameSpace = nameSpaceManager.apply(description);
            isSuccess = true;
            message = "申请 namespace 成功";
        } catch (Exception e) {
            isSuccess = false;
            message = "申请 namespace 失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(nameSpace);
        resultBean.setMessage(message);
        return resultBean;
    }

}
