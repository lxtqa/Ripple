// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.hlc;

/**
 * @author Zhen Tang
 */
public class HybridLogicalClock {
    private String processName;
    private long wallTime;
    private long logicalClock;

    public HybridLogicalClock(String processName, long wallTime, long logicalClock) {
        this.setProcessName(processName);
        this.setWallTime(wallTime);
        this.setLogicalClock(logicalClock);
    }

    public HybridLogicalClock(String processName) {
        this(processName, 0, 0);
    }

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
