package lazecoding.model;

/**
 * UniqueRecord
 *
 * @author lazecoding
 */
public class UniqueRecord {
    /**
     * 唯一标识
     */
    private String tag;

    /**
     * 最大值
     */
    private long maxId;

    /**
     * 步长
     */
    private int step;

    /**
     * 描述
     */
    private String description;

    /**
     * namespaceId
     */
    private String namespaceId;

    /**
     * 更新事件
     */
    private String updateTime;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UniqueRecord{" +
                "tag='" + tag + '\'' +
                ", maxId=" + maxId +
                ", step=" + step +
                ", description='" + description + '\'' +
                ", namespaceId='" + namespaceId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
