package lazecoding.unique.model;

/**
 * @author lazecoding
 */
public class SegmentBufferView {
    private String tag;
    private long value0;
    private int step0;
    private long max0;

    private long value1;
    private int step1;
    private long max1;
    private int pos;
    private boolean nextReady;
    private boolean initSuccess;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getValue1() {
        return value1;
    }

    public void setValue1(long value1) {
        this.value1 = value1;
    }

    public int getStep1() {
        return step1;
    }

    public void setStep1(int step1) {
        this.step1 = step1;
    }

    public long getMax1() {
        return max1;
    }

    public void setMax1(long max1) {
        this.max1 = max1;
    }

    public long getValue0() {
        return value0;
    }

    public void setValue0(long value0) {
        this.value0 = value0;
    }

    public int getStep0() {
        return step0;
    }

    public void setStep0(int step0) {
        this.step0 = step0;
    }

    public long getMax0() {
        return max0;
    }

    public void setMax0(long max0) {
        this.max0 = max0;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isNextReady() {
        return nextReady;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }

    public void setInitSuccess(boolean initSuccess) {
        this.initSuccess = initSuccess;
    }

    @Override
    public String toString() {
        return "SegmentBufferView{" +
                "tag='" + tag + '\'' +
                ", value0=" + value0 +
                ", step0=" + step0 +
                ", max0=" + max0 +
                ", value1=" + value1 +
                ", step1=" + step1 +
                ", max1=" + max1 +
                ", pos=" + pos +
                ", nextReady=" + nextReady +
                ", initSuccess=" + initSuccess +
                '}';
    }
}
