package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class GetResponse extends Message {
    private String applicationName;
    private String key;
    private List<GetResponseItem> items;

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

    public List<GetResponseItem> getItems() {
        return items;
    }

    public void setItems(List<GetResponseItem> items) {
        this.items = items;
    }

    public GetResponse() {
        this.setType(MessageType.GET_RESPONSE);
        this.setItems(new ArrayList<>());
    }
}