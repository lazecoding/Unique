package lazecoding.unique.controller;

import lazecoding.unique.common.StringUtil;
import lazecoding.unique.exception.InitException;
import lazecoding.unique.exception.NilParamException;
import lazecoding.unique.service.BufferHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @className: UniqueRecordController
 * @description:
 * @datetime: 2020/10/12   21:19
 * @author: lazecoding
 */
@RestController
public class UniqueRecordController {

    private Logger logger = LoggerFactory.getLogger(UniqueRecordController.class);

    @Autowired
    private BufferHolder bufferHolder;

    @RequestMapping(value = "/api/get/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public long getUniqueId(@PathVariable("tag") String tag) throws InitException {
        if (StringUtil.isEmpty(tag)) {
            throw new NilParamException("未正确输入tag" + tag);
        }
        return bufferHolder.getUniqueId(tag);
    }
}
