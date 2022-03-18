package lazecoding.unique.service;

import lazecoding.unique.exception.InitException;

/**
 * @className: UniqueRecordService
 * @description:
 * @datetime: 2020/10/12   21:50
 * @author: lazecoding
 */
public interface UniqueRecordService {
    boolean init();

    long getUniqueId(String tag) throws InitException;

}
