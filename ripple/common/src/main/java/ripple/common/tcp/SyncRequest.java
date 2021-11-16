package ripple.common.tcp;

import java.util.Date;
import java.util.UUID;

public class SyncRequest extends Request {
    private UUID uuid;
    private String type;
    private String applicationName;
    private String key;
    private Date lastUpdate;
    private int lastUpdateServerId;
}
