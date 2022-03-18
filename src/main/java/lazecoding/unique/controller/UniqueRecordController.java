package lazecoding.unique.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import lazecoding.unique.common.StringUtil;
import lazecoding.unique.exception.InitException;
import lazecoding.unique.exception.NilTagException;
import lazecoding.unique.service.UniqueRecordService;

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
    private UniqueRecordService uniqueRecordService;

    @RequestMapping(value = "/api/get/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public long getUniqueId(@PathVariable("tag") String tag) throws InitException {
        if (StringUtil.isEmpty(tag)) {
            throw new NilTagException("未正确输入tag" + tag);
        }
        return uniqueRecordService.getUniqueId(tag);
    }
}
