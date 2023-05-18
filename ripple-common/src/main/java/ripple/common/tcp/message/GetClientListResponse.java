package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class GetClientListResponse extends Message {
    private String clientListSignature;
    private List<GetClientListResponseItem> items;

    public String getClientListSignature() {
        return clientListSignature;
    }

    public void setClientListSignature(String clientListSignature) {
        this.clientListSignature = clientListSignature;
    }

    public List<GetClientListResponseItem> getItems() {
        return items;
    }

    public void setItems(List<GetClientListResponseItem> items) {
        this.items = items;
    }

    public GetClientListResponse() {
        this.setType(MessageType.GET_CLIENT_LIST_RESPONSE);
        this.setItems(new ArrayList<>());
    }
}
