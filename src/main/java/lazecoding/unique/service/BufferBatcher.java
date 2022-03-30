package lazecoding.unique.service;

import lazecoding.unique.exception.BatchException;
import lazecoding.unique.exception.InitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 批量操作类
 *
 * @author liux
 */
@Component("bufferBatcher")
public class BufferBatcher {

    private final Logger logger = LoggerFactory.getLogger(BufferBatcher.class);

    @Autowired
    private BufferHolder bufferHolder;

    /**
     * 批量获取最大值
     */
    private static final int MAX_SIZE = 1000;

    /**
     * 批量获取分布式 Id
     *
     * @param size 获取个数
     */
    public List<Long> batch(String tag, int size) throws InitException {
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
            batchList.add(bufferHolder.getUniqueId(tag));
            targetNum--;
        }
        if (size != batchList.size()) {
            logger.error("批量获取失败 tag:[{}]", tag);
            throw new BatchException("批量获取失败 tag:" + tag);
        }
        return batchList;
    }
}
