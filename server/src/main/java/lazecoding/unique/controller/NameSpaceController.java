package lazecoding.unique.controller;


import lazecoding.unique.config.ServerConfig;
import lazecoding.unique.exception.AuthorizationException;
import lazecoding.unique.exception.NilParamException;
import lazecoding.unique.exception.RestrictedOperationException;
import lazecoding.unique.model.NameSpace;
import lazecoding.unique.mvc.ResultBean;
import lazecoding.unique.service.NameSpaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * NameSpaceController
 *
 * @author lazecoding
 */
@RestController
public class NameSpaceController {

    private static final Logger logger = LoggerFactory.getLogger(NameSpaceController.class);

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
            throw new NilParamException("authorization 不得为空");
        }
        if (!serverConfig.getAuthorization().equals(authorization)) {
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
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
            throw new NilParamException("authorization 不得为空");
        }
        if (!serverConfig.getAuthorization().equals(authorization)) {
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        boolean isSuccess = false;
        String message = "";
        try {
            nameSpaceManager.remove(namespace);
            isSuccess = true;
            message = "删除成功";
        } catch (RestrictedOperationException e) {
            isSuccess = false;
            message = "该 namespace 下存在 tag，禁止删除";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/namespace/remove/{authorization}/{namespace}] 删除失败", e);
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
            throw new NilParamException("authorization 不得为空");
        }
        if (!serverConfig.getAuthorization().equals(authorization)) {
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(description)) {
            throw new NilParamException("namespace 不得为空");
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
            logger.error("接口:[/api/namespace/apply/{authorization}/{description}] 申请 namespace 失败", e);
            message = "申请 namespace 失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(nameSpace);
        resultBean.setMessage(message);
        return resultBean;
    }

}
