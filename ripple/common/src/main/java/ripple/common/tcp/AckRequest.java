package ripple.common.tcp;

import java.util.UUID;

public class AckRequest extends Request {
    private UUID messageUuid;
    private int sourceId;
    private int nodeId;
}
