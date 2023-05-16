package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class SystemInfoRequest extends Message {
    public SystemInfoRequest() {
        this.setType(MessageType.SYSTEM_INFO_REQUEST);
    }
}
