package ripple.server.core;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Atomic operation on the key-value store
 *
 * @author lostcharlie
 */
public class Operation {
    private UUID uuid;
    private OperationType operationType;
    private String targetValue;
    private Date timestamp;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Operation() {
        this.setUuid(UUID.randomUUID());
    }

}