package ripple.common.tcp.message;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class GetResponseItem {
    private UUID messageUuid;
    private String operationType;
    private String applicationName;
    private String key;
    private UUID baseMessageUuid;
    private String atomicOperation;
    private String value;
    private Date lastUpdate;
    private int lastUpdateServerId;

    public UUID getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UUID getBaseMessageUuid() {
        return baseMessageUuid;
    }

    public void setBaseMessageUuid(UUID baseMessageUuid) {
        this.baseMessageUuid = baseMessageUuid;
    }

    public String getAtomicOperation() {
        return atomicOperation;
    }

    public void setAtomicOperation(String atomicOperation) {
        this.atomicOperation = atomicOperation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUpdateServerId() {
        return lastUpdateServerId;
    }

    public void setLastUpdateServerId(int lastUpdateServerId) {
        this.lastUpdateServerId = lastUpdateServerId;
    }

}
