package lazecoding.unique.controller;

import lazecoding.api.OpenApi;
import lazecoding.exception.InitException;
import lazecoding.exception.NilParamException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClientRestController
 *
 * 服务端通过 client 处理逻辑，提供 REST 接口；
 * 服务端管理所有 tags，第三方客户端只可管理自己的 tags。
 *
 * @author lazecoding
 */
@RestController
public class ClientRestController {

    @RequestMapping(value = "/api/get/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public long getUniqueId(@PathVariable("tag") String tag) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("未正确输入 tag:" + tag);
        }
        return OpenApi.getUniqueId(tag);
    }

    @RequestMapping(value = "/api/batch/{tag}/{size}", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> batch(@PathVariable("tag") String tag, @PathVariable("size") Integer size) throws InitException {
        if (!StringUtils.hasText(tag)) {
            throw new NilParamException("tag 不得为空");
        }
        if (size == null) {
            throw new NilParamException("size 不得为空");
        }
        return OpenApi.batch(tag, size);
    }

}
