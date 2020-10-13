package personal.unique.service;

import personal.unique.exception.InitException;

/**
 * @className: UniqueRecordService
 * @package: personal.unique.service
 * @description:
 * @datetime: 2020/10/12   21:50
 * @author: lazecoding
 */
public interface UniqueRecordService {
    boolean init();

    long getUniqueId(String tag) throws InitException;

}
