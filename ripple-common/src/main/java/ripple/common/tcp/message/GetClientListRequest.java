package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class GetClientListRequest extends Message {
    private String clientListSignature;

    public String getClientListSignature() {
        return clientListSignature;
    }

    public void setClientListSignature(String clientListSignature) {
        this.clientListSignature = clientListSignature;
    }

    public GetClientListRequest() {
        this.setType(MessageType.GET_CLIENT_LIST_REQUEST);
    }
}
