package ripple.common.tcp;

import java.util.UUID;

public class AckRequest {
    private UUID messageUuid;
    private int sourceId;
    private int nodeId;
}
