package lazecoding.unique.model;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @className: SegmentBuffer
 * @description:
 * @datetime: 2020/10/12   22:57
 * @author: lazecoding
 */
public class SegmentBuffer {

    /**
     * 唯一标识
     */
    private String tag;

    /**
     * 双 buffer
     */
    private Segment[] segments;

    /**
     * 当前的使用的 Segment 的 Index
     */
    private volatile int currentPos;

    /**
     * 下一个 Segment 是否处于可切换状态
     */
    private volatile boolean nextReady;

    /**
     * 是否初始化完成
     */
    private volatile boolean initSuccess;

    /**
     * 线程是否在运行中
     */
    private final AtomicBoolean threadRunning;

    /**
     * 读写锁
     */
    private final ReadWriteLock lock;

    /**
     * 动态步长
     */
    private volatile int step;

    /**
     * 最小步长，即数据库中录入的步长
     */
    private volatile int minStep;

    private volatile long updateTimestamp;

    public SegmentBuffer() {
        segments = new Segment[]{new Segment(this), new Segment(this)};
        currentPos = 0;
        nextReady = false;
        initSuccess = false;
        threadRunning = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Segment[] getSegments() {
        return segments;
    }

    public Segment getCurrent() {
        return segments[currentPos];
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    /**
     * 切换 Segment
     */
    public void switchPos() {
        currentPos = nextPos();
    }

    public boolean isinitSuccess() {
        return initSuccess;
    }

    public void setinitSuccess(boolean initSuccess) {
        this.initSuccess = initSuccess;
    }

    public boolean isNextReady() {
        return nextReady;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public AtomicBoolean getThreadRunning() {
        return threadRunning;
    }

    /**
     * 获取读锁
     *
     * @return
     */
    public Lock getReadLock() {
        return lock.readLock();
    }

    /**
     * 获取写锁
     *
     * @return
     */
    public Lock getWriteLock() {
        return lock.writeLock();
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getMinStep() {
        return minStep;
    }

    public void setMinStep(int minStep) {
        this.minStep = minStep;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        return "SegmentBuffer{" +
                "tag='" + tag + '\'' +
                ", segments=" + Arrays.toString(segments) +
                ", currentPos=" + currentPos +
                ", nextReady=" + nextReady +
                ", initSuccess=" + initSuccess +
                ", threadRunning=" + threadRunning +
                ", lock=" + lock +
                ", step=" + step +
                ", minStep=" + minStep +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}
