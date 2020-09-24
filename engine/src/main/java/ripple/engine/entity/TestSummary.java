package ripple.engine.entity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuxiao.tz
 */
public class TestSummary {
    private String nodeType;
    private int nodeCount;
    private int tps;
    private int duration;
    private long minTime;
    private long maxTime;
    private ConcurrentHashMap<UUID, TestDetail> details;

    public String getNodeType() {
        return nodeType;
    }

    private void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    private void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getTps() {
        return tps;
    }

    private void setTps(int tps) {
        this.tps = tps;
    }

    public int getDuration() {
        return duration;
    }

    private void setDuration(int duration) {
        this.duration = duration;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public ConcurrentHashMap<UUID, TestDetail> getDetails() {
        return details;
    }

    public void setDetails(ConcurrentHashMap<UUID, TestDetail> details) {
        this.details = details;
    }

    public TestSummary(String nodeType, int nodeCount, int tps, int duration) {
        this.setNodeType(nodeType);
        this.setNodeCount(nodeCount);
        this.setTps(tps);
        this.setDuration(duration);
        this.setDetails(new ConcurrentHashMap<>());
    }
}
