package lazecoding.unique.service;

import lazecoding.exception.RestrictedOperationException;
import lazecoding.model.NameSpace;
import lazecoding.unique.mapper.NameSpaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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

    /**
     * Server NameSpace
     */
    public static final String SYSTEM_NAMESPACE_ID = "b9fefb0d-6ff4-47c3-a5bc-f5f9c172fe59";

    @Autowired
    private NameSpaceMapper nameSpaceMapper;

    @Autowired
    private TagManager tagManager;

    /**
     * 初始化
     */
    public void init() {
        NameSpace nameSpace = nameSpaceMapper.findById(SYSTEM_NAMESPACE_ID);
        if (ObjectUtils.isEmpty(nameSpace)) {
            nameSpaceMapper.add(SYSTEM_NAMESPACE_ID, "Server NameSpace");
        }
    }

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
        List<String> tags = tagManager.getTags(namespaceId);
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
