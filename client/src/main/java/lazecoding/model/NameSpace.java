package lazecoding.model;

/**
 * NameSpace
 *
 * 作用于：获取 tags、申请 tags、删除 tags、修改 tags。
 *
 * @author lazecoding
 */
public class NameSpace {

    /**
     * namespaceId
     */
    private String namespaceId;

    /**
     * 描述
     */
    private String description;

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
