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

    /**
     * 类型：0 客户端；1 服务端（全局）
     */
    private int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NameSpace{" +
                "namespaceId='" + namespaceId + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}
