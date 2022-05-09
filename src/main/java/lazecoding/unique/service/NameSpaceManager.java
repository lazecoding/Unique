package lazecoding.unique.service;

import lazecoding.model.NameSpace;
import lazecoding.unique.mapper.NameSpaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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
     * System NameSpace:可以获取所有 namespace 下的 tags
     */
    public static final String SYSTEM_NAMESPACE_ID = "b9fefb0d-6ff4-47c3-a5bc-f5f9c172fe59";

    @Autowired
    private NameSpaceMapper nameSpaceMapper;

    /**
     * 初始化,type = 1,用于服务端，可以获取所以 namespace 的 tags
     */
    public void init() {
        NameSpace nameSpace = nameSpaceMapper.findById(SYSTEM_NAMESPACE_ID);
        if (ObjectUtils.isEmpty(nameSpace)) {
            // type == 1 表示系统默认
            nameSpaceMapper.add(SYSTEM_NAMESPACE_ID, "系统默认", 1);
        }
    }

    /**
     * 申请 namespace
     *
     * @param description 描述
     */
    public NameSpace apply(String description) {
        String namespaceId = UUID.randomUUID().toString();
        // 默认 type = 0
        this.add(namespaceId, description, 0);
        NameSpace nameSpace = this.findById(namespaceId);
        return nameSpace;
    }

    /**
     * 新增 namespace
     *
     * @param namespaceId namespaceId
     * @param description 描述
     * @param type        类型
     */
    private void add(String namespaceId, String description, int type) {
        nameSpaceMapper.add(namespaceId, description, type);
        logger.info("新增 namespace：[{}],description:[{}],type:[{}]", namespaceId, description, type);
    }

    /**
     * 删除（但是只能删除 type = 0）
     *
     * @param namespaceId namespaceId
     */
    public void remove(String namespaceId) {
        nameSpaceMapper.remove(namespaceId);
        logger.info("删除 namespace：[{}]", namespaceId);
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
