package ripple.common.tcp.message;

import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class UnsubscribeRequest extends Message {
    private String applicationName;
    private String key;
    private String callbackAddress;
    private int callbackPort;

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

    public String getCallbackAddress() {
        return callbackAddress;
    }

    public void setCallbackAddress(String callbackAddress) {
        this.callbackAddress = callbackAddress;
    }

    public int getCallbackPort() {
        return callbackPort;
    }

    public void setCallbackPort(int callbackPort) {
        this.callbackPort = callbackPort;
    }

    public UnsubscribeRequest() {
        this.setType(MessageType.UNSUBSCRIBE_REQUEST);
    }
}