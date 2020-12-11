package ripple.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class Item {
    private String applicationName;
    private String key;
    private List<Message> messages;

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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Item() {
        this.setMessages(new ArrayList<>());
    }
}