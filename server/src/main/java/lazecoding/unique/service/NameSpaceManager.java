package lazecoding.unique.service;


import lazecoding.unique.exception.RestrictedOperationException;
import lazecoding.unique.mapper.NameSpaceMapper;
import lazecoding.unique.mapper.UniqueRecordMapper;
import lazecoding.unique.model.NameSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * NameSpaceManager
 *
 * @author lazecoding
 */
@Component("nameSpaceManager")
public class NameSpaceManager {

    private static final Logger logger = LoggerFactory.getLogger(NameSpaceManager.class);

    @Autowired
    private NameSpaceMapper nameSpaceMapper;

    @Autowired
    private UniqueRecordMapper uniqueRecordMapper;


    /**
     * 申请 namespace
     *
     * @param description 描述
     */
    public NameSpace apply(String description) {
        String namespaceId = UUID.randomUUID().toString();
        this.add(namespaceId, description);
        // 组织 nameSpace
        NameSpace nameSpace = new NameSpace();
        nameSpace.setNamespaceId(namespaceId);
        nameSpace.setDescription(description);
        return nameSpace;
    }

    /**
     * 新增 namespace
     *
     * @param namespaceId namespaceId
     * @param description 描述
     */
    private void add(String namespaceId, String description) {
        nameSpaceMapper.add(namespaceId, description);
        logger.info("新增 namespace:[{}],description:[{}]", namespaceId, description);
    }

    /**
     * 删除
     *
     * @param namespaceId namespaceId
     */
    public void remove(String namespaceId) {
        // namespace 下是否存在 tag
        List<String> tags = uniqueRecordMapper.getTags(namespaceId);
        if (CollectionUtils.isEmpty(tags)) {
            throw new RestrictedOperationException("该 namespace 下存在 tag，禁止删除");
        }
        nameSpaceMapper.remove(namespaceId);
        logger.info("删除 namespace:[{}]", namespaceId);
    }

    /**
     * 根据 namespaceId 获取 NameSpace
     *
     * @param namespaceId namespaceId
     * @return NameSpace
     */
    public NameSpace findById(String namespaceId) {
        NameSpace nameSpace = nameSpaceMapper.findById(namespaceId);
        return nameSpace;
    }

}
