package lazecoding.unique.model;

/**
 * @className: UniqueRecord
 * @description:
 * @datetime: 2020/10/12   22:32
 * @author: lazecoding
 */
public class UniqueRecord {
    private String tag;
    private long maxId;
    private int step;
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
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
