package ripple.engine.entity;

import java.util.Date;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class TestDetail {
    private UUID messageUuid;
    private Date startDate;
    private Date endDate;
    private long runTime;

    public UUID getMessageUuid() {
        return messageUuid;
    }

    private void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public TestDetail(UUID messageUuid) {
        this.setMessageUuid(messageUuid);
    }
}
