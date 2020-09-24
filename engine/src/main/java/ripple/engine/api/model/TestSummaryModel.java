package ripple.engine.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuxiao.tz
 */
public class TestSummaryModel {
    private String nodeType;
    private int nodeCount;
    private int tps;
    private int duration;
    private long minTime;
    private long maxTime;
    private List<TestDetailModel> details;

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getTps() {
        return tps;
    }

    public void setTps(int tps) {
        this.tps = tps;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public List<TestDetailModel> getDetails() {
        return details;
    }

    public void setDetails(List<TestDetailModel> details) {
        this.details = details;
    }

    public TestSummaryModel() {
        this.setDetails(new ArrayList<>());
    }
}
