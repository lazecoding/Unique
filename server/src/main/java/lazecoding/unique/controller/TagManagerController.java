package lazecoding.unique.controller;

import lazecoding.api.OpenApi;
import lazecoding.exception.IllegalParamException;
import lazecoding.exception.NilNameSpaceException;
import lazecoding.exception.NilParamException;
import lazecoding.model.UniqueRecord;
import lazecoding.mvc.ResultBean;
import lazecoding.unique.service.TagManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TagManagerController
 *
 * @author lazecoding
 */
@RestController
public class TagManagerController {

    private static final Logger logger = LoggerFactory.getLogger(TagManagerController.class);

    @Autowired
    private TagManager tagManager;

    /**
     * 获取 namespace 下 tags
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/tag/get/{namespace}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getTags(@PathVariable("namespace") String namespace) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        List<String> tags = null;
        boolean isSuccess = false;
        String message = "";
        try {
            tags = tagManager.getTags(namespace);
            isSuccess = true;
            message = "获取成功";
        } catch (NilNameSpaceException e) {
            isSuccess = false;
            message = "该 namespace 不存在";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/tag/get/{namespace}] 获取失败", e);
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(tags);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 判断 namespace 下是否存在某 tag
     *
     * @param namespace namespaceId
     * @param tag       tag
     * @return
     */
    @RequestMapping(value = "/api/tag/exist/{namespace}/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean existTag(@PathVariable("namespace") String namespace, @PathVariable("tag") String tag) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        boolean isExist = false;
        boolean isSuccess = false;
        String message = "";
        try {
            isExist = tagManager.existTag(namespace, tag);
            isSuccess = true;
            message = "获取成功";
        } catch (NilNameSpaceException e) {
            isExist = false;
            isSuccess = false;
            message = "该 namespace 不存在";
        } catch (Exception e) {
            isExist = false;
            isSuccess = false;
            logger.error("接口:[/api/tag/exist/{namespace}/{tag}] 获取失败", e);
            message = "系统异常，获取失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(isExist);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 在 namespace 下新增 tag
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/tag/add/{namespace}/{tag}/{maxId}/{step}/{description}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean add(@PathVariable("namespace") String namespace, @PathVariable("tag") String tag
            , @PathVariable("maxId") Long maxId, @PathVariable("step") Integer step, @PathVariable("description") String description) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (maxId == null) {
            throw new NilParamException("maxId 不得为空");
        }
        if (step == null) {
            throw new NilParamException("step 不得为空");
        }
        if (!StringUtils.hasText(description)) {
            throw new NilParamException("description 不得为空");
        }

        UniqueRecord uniqueRecord = null;
        boolean isSuccess = false;
        String message = "";
        try {
            uniqueRecord = tagManager.add(namespace, tag, maxId, step, description);
            isSuccess = true;
            message = "新增成功";
        } catch (NilNameSpaceException e) {
            isSuccess = false;
            message = "该 namespace 不存在，新增 tag 失败";
        } catch (IllegalParamException e) {
            isSuccess = false;
            message = "参数不合法:[" + e.getMessage() + "]，新增失败";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/tag/add/{namespace}/{tag}/{maxId}/{step}/{description}] 新增失败", e);
            message = "系统异常，新增失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setValue(uniqueRecord);
        resultBean.setMessage(message);
        return resultBean;
    }

    /**
     * 在 namespace 下删除 tag
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/tag/remove/{namespace}/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean remove(@PathVariable("namespace") String namespace, @PathVariable("tag") String tag) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }

        boolean isSuccess = false;
        String message = "";
        try {
            tagManager.remove(namespace, tag);
            isSuccess = true;
            message = "删除成功";
        } catch (NilNameSpaceException e) {
            isSuccess = false;
            message = "该 namespace 不存在，删除 tag 失败";
        } catch (Exception e) {
            isSuccess = false;
            logger.error("接口:[/api/tag/remove/{namespace}/{tag}] 删除失败", e);
            message = "系统异常，删除失败";
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(isSuccess);
        resultBean.setMessage(message);
        return resultBean;
    }

    // /api/tag/modify/{namespace}/{tag}/{start}/{step}/{description}
    // /api/tag/modify-start/{namespace}/{tag}/{start}
    // /api/tag/modify-step/{namespace}/{tag}/{step}
    // /api/tag/modify-description/{namespace}/{tag}/{description}

    /**
     * 在 namespace 下删除 tag(待删除代码)
     *
     * @param namespace namespaceId
     * @return
     */
    @RequestMapping(value = "/api/tag/test/{namespace}/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathVariable("namespace") String namespace, @PathVariable("tag") String tag) {
        if (!StringUtils.hasText(namespace)) {
            throw new NilParamException("namespace 不得为空");
        }
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        UniqueRecord uniqueRecord = OpenApi.addTag(tag, 1, 22222, "23721632");
        System.out.println(uniqueRecord.toString());
        System.out.println( "存在：" + OpenApi.existTag(tag));
        System.out.println( "删除：" + OpenApi.removeTag(tag));
        System.out.println( "存在：" + OpenApi.existTag(tag));
        System.out.println( "删除：" + OpenApi.removeTag(tag));
        return "resultBean";
    }

}
