package ripple.server.core.hlc;

public class HybridLogicalClock {
    private String processName;
    private long wallTime;
    private long logicalClock;

    public String getProcessName() {
        return processName;
    }

    private void setProcessName(String processName) {
        this.processName = processName;
    }

    public long getWallTime() {
        return wallTime;
    }

    private void setWallTime(long wallTime) {
        this.wallTime = wallTime;
    }

    public long getLogicalClock() {
        return logicalClock;
    }

    private void setLogicalClock(long logicalClock) {
        this.logicalClock = logicalClock;
    }

    public HybridLogicalClock(String processName, long wallTime, long logicalClock) {
        this.setProcessName(processName);
        this.setWallTime(wallTime);
        this.setLogicalClock(logicalClock);
    }

    public HybridLogicalClock(String processName) {
        this(processName, 0, 0);
    }

    public void set(long wallTime, long logicalClock) {
        this.setWallTime(wallTime);
        this.setLogicalClock(logicalClock);
    }

    public boolean smallerThan(HybridLogicalClock another) {
        if (this.getWallTime() < another.getWallTime()) {
            return true;
        }
        if ((this.getWallTime() == another.getWallTime())
                && (this.getLogicalClock() < another.getLogicalClock())) {
            return true;
        }
        return false;
    }
}
