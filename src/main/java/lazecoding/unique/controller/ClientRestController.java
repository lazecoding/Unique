package lazecoding.unique.controller;

import lazecoding.api.OpenApi;
import lazecoding.exception.InitException;
import lazecoding.exception.NilParamException;
import lazecoding.mvc.ResultBean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * ClientRestController
 * <p>
 * 服务端通过 client 处理逻辑，提供 REST 接口；
 * 服务端管理所有 tags，第三方客户端只可管理自己的 tags。
 *
 * @author lazecoding
 */
@RestController
public class ClientRestController {

    /**
     * 获取 Id
     */
    @RequestMapping(value = "/api/get/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getUniqueId(@PathVariable("tag") String tag) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入 tag:" + tag);
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(true);
        resultBean.setValue(OpenApi.getUniqueId(tag));
        return resultBean;
    }

    /**
     * 批量获取 Id
     */
    @RequestMapping(value = "/api/batch/{tag}/{size}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean batch(@PathVariable("tag") String tag, @PathVariable("size") Integer size) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (size == null) {
            throw new NilParamException("size 不得为空");
        }
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(true);
        resultBean.setValue(OpenApi.batch(tag, size));
        return resultBean;
    }

}
