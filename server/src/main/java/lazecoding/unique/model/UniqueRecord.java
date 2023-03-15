package lazecoding.unique.model;

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
     * 区域
     */
    private String region;

    /**
     * namespaceId
     */
    private String namespaceId;

    /**
     * 更新时间
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
        return "UniqueRecord{" +
                "uid=" + uid +
                ", tag='" + tag + '\'' +
                ", maxId=" + maxId +
                ", step=" + step +
                ", description='" + description + '\'' +
                ", region='" + region + '\'' +
                ", namespaceId='" + namespaceId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
