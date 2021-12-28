package ripple.common.entity;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateMessage extends AbstractMessage {
    private UUID baseMessageUuid;
    private String atomicOperation;
    private String value;

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

    public IncrementalUpdateMessage() {

    }

    public IncrementalUpdateMessage(String applicationName, String key, UUID baseMessageUuid
            , String atomicOperation, String value, Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), applicationName, key, baseMessageUuid
                , atomicOperation, value, lastUpdate, lastUpdateServerId);
    }

    public IncrementalUpdateMessage(UUID uuid, String applicationName, String key, UUID baseMessageUuid
            , String atomicOperation, String value, Date lastUpdate, int lastUpdateServerId) {
        super(uuid, Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE, applicationName, key, lastUpdate, lastUpdateServerId);
        this.setBaseMessageUuid(baseMessageUuid);
        this.setAtomicOperation(atomicOperation);
        this.setValue(value);
    }
}
