package lazecoding.unique.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @className: Segment
 * @description:
 * @datetime: 2020/10/12   22:50
 * @author: lazecoding
 */
public class Segment {
    /**
     * 用来原子消费分布式 Id
     */
    private AtomicLong value = new AtomicLong(0);

    /**
     * 这个 Segment 中最大值
     */
    private volatile long max;

    /**
     * 该号段真实步长，即这个号段取了多少个数值
     */
    private volatile int step;

    /**
     * 管理该 Segment 的 SegmentBuffer
     */
    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public AtomicLong getValue() {
        return value;
    }

    public void setValue(AtomicLong value) {
        this.value = value;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public SegmentBuffer getBuffer() {
        return buffer;
    }

    public long getIdle() {
        return this.getMax() - getValue().get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Segment(");
        sb.append("value:");
        sb.append(value);
        sb.append(",max:");
        sb.append(max);
        sb.append(",step:");
        sb.append(step);
        sb.append(")");
        return sb.toString();
    }
}
