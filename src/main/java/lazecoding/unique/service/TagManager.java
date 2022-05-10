package lazecoding.unique.service;

import lazecoding.exception.IllegalParamException;
import lazecoding.exception.NilNameSpaceException;
import lazecoding.model.NameSpace;
import lazecoding.model.UniqueRecord;
import lazecoding.unique.mapper.UniqueRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * TagManager
 *
 * @author lazecoding
 */
@Component("tagManager")
public class TagManager {

    private static final Logger logger = LoggerFactory.getLogger(TagManager.class);

    @Autowired
    private UniqueRecordMapper uniqueRecordMapper;

    @Autowired
    private NameSpaceManager nameSpaceManager;

    /**
     * 获取 namespace 下 tags
     */
    public List<String> getTags(String namespaceId) {
        // 1. 获取 NameSpace
        NameSpace nameSpace = nameSpaceManager.findById(namespaceId);
        if (ObjectUtils.isEmpty(nameSpace)) {
            throw new NilNameSpaceException("该 namespace 不存在");
        }

        // 2. 根据 namespace 获取
        return uniqueRecordMapper.getTags(namespaceId);
    }

    /**
     * 判断 namespace 下是否存在某 tag
     */
    public boolean existTag(String namespaceId, String tag) {
        // 1. 获取 NameSpace
        NameSpace nameSpace = nameSpaceManager.findById(namespaceId);
        if (ObjectUtils.isEmpty(nameSpace)) {
            throw new NilNameSpaceException("该 namespace 不存在");
        }

        // 2. 根据 namespace 和 tag 获取，如果获取到了值，说明存在。
        String res = uniqueRecordMapper.existTag(namespaceId, tag);
        return StringUtils.hasText(res);
    }

    /**
     * 在 namespace 下新增 tag
     */
    public UniqueRecord add(String namespaceId, String tag, long maxId, int step, String description) {
        // 1. 获取 NameSpace
        NameSpace nameSpace = nameSpaceManager.findById(namespaceId);
        if (ObjectUtils.isEmpty(nameSpace)) {
            throw new NilNameSpaceException("该 namespace 不存在");
        }
        if (maxId < 1) {
            throw new IllegalParamException("初始化的 maxId 值不可以小于 1");
        }
        if (step < 20000) {
            throw new IllegalParamException("start 不可以小于 20,000");
        }
        if (step > 1000000) {
            throw new IllegalParamException("start 不可以大于 1000,000");
        }
        // 2. add
        uniqueRecordMapper.add(namespaceId, tag, maxId, step, description);
        // 3. 组织 uniqueRecord
        UniqueRecord uniqueRecord = new UniqueRecord();
        uniqueRecord.setTag(tag);
        uniqueRecord.setMaxId(maxId);
        uniqueRecord.setStep(step);
        uniqueRecord.setDescription(description);
        uniqueRecord.setNamespaceId(namespaceId);
        logger.info("namespace:[{}] 下新增 tag:[{}],maxId:[{}],step:[{}],description:[{}]", namespaceId, tag, maxId, step, description);
        return uniqueRecord;
    }

    /**
     * 在 namespace 下删除 tag
     */
    public void remove(String namespaceId, String tag) {
        // 1. 获取 NameSpace
        NameSpace nameSpace = nameSpaceManager.findById(namespaceId);
        if (ObjectUtils.isEmpty(nameSpace)) {
            throw new NilNameSpaceException("该 namespace 不存在");
        }

        // 2. remove
        uniqueRecordMapper.remove(namespaceId, tag);
        logger.info("Namespace:[{}] 下删除 tag:[{}]", namespaceId, tag);
    }

}