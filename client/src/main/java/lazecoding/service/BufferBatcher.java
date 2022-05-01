package lazecoding.service;

import lazecoding.exception.BatchException;
import lazecoding.exception.InitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 批量操作类
 *
 * @author lazecoding
 */
public class BufferBatcher {

    private static final Logger logger = LoggerFactory.getLogger(BufferBatcher.class);

    /**
     * 批量获取最大值
     */
    private static final int MAX_SIZE = 1000;

    /**
     * 批量获取分布式 Id
     *
     * @param size 获取个数
     */
    public static List<Long> batch(String tag, int size) throws InitException {
        List<Long> batchList = new LinkedList<>();
        if (size <= 0) {
            return batchList;
        }
        if (size > MAX_SIZE) {
            logger.error("批量获取最大 size:[{}]", MAX_SIZE);
            throw new BatchException("批量获取最大 size:" + MAX_SIZE);
        }
        int targetNum = size;
        while (targetNum > 0) {
            batchList.add(BufferHolder.getUniqueId(tag));
            targetNum--;
        }
        if (size != batchList.size()) {
            logger.error("批量获取失败 tag:[{}]", tag);
            throw new BatchException("批量获取失败 tag:" + tag);
        }
        return batchList;
    }
}
