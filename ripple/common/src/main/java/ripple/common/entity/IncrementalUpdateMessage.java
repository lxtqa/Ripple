package ripple.common.entity;

import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateMessage {
    // TODO: Fix this
    private UUID baseMessageUuid;
    private String atomicOperation;
    private String value;
}
