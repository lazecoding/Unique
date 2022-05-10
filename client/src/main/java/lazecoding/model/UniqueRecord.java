package lazecoding.model;

import java.util.Date;

/**
 * UniqueRecord
 *
 * @author lazecoding
 */
public class UniqueRecord {

    /**
     * 主键
     */
    private int uid;

    /**
     * 业务标识
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
    private Date updateTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "{" +
                "uid=" + uid +
                ", tag='" + tag + '\'' +
                ", maxId=" + maxId +
                ", step=" + step +
                ", description='" + description + '\'' +
                ", namespaceId='" + namespaceId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
