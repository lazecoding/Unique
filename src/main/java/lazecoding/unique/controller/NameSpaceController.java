package lazecoding.unique.controller;

import lazecoding.exception.AuthorizationException;
import lazecoding.exception.NilParamException;
import lazecoding.model.NameSpace;
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
    public NameSpace find(@PathVariable("authorization") String authorization, @PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(authorization)) {
            throw new NilParamException("未正确输入 authorization:" + authorization);
        }
        if (!serverConfig.getAuthorization().equals(authorization)){
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("未正确输入 namespace:" + namespace);
        }
        return nameSpaceManager.findById(namespace);
    }

    /**
     * 删除 namespace
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/namespace/remove/{authorization}/{namespace}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean remove(@PathVariable("authorization") String authorization, @PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(authorization)) {
            throw new NilParamException("未正确输入 authorization:" + authorization);
        }
        if (!serverConfig.getAuthorization().equals(authorization)){
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("未正确输入 namespace:" + namespace);
        }
        try {
            nameSpaceManager.remove(namespace);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 申请 namespace
     *
     * @param description 描述
     */
    @RequestMapping(value = "/api/namespace/apply/{authorization}/{description}", method = RequestMethod.GET)
    @ResponseBody
    public NameSpace apply(@PathVariable("authorization") String authorization, @PathVariable("description") String description) {
        if (!StringUtils.hasText(authorization)) {
            throw new NilParamException("未正确输入 authorization:" + authorization);
        }
        if (!serverConfig.getAuthorization().equals(authorization)){
            throw new AuthorizationException("鉴权失败");
        }
        if (!StringUtils.hasText(description)) {
            throw new NilParamException("未正确输入 description:" + description);
        }
        return nameSpaceManager.apply(description);
    }

}
