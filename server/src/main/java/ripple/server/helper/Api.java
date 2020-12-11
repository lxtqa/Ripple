package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.DeleteMessage;
import ripple.common.Endpoint;
import ripple.common.Message;
import ripple.common.MessageType;
import ripple.common.UpdateMessage;
import ripple.common.helper.Http;
import ripple.server.core.ClientMetadata;
import ripple.server.core.NodeMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhen Tang
 */
public final class Api {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Api() {

    }

    public static boolean notifyClient(ClientMetadata metadata, Message message) {
        try {
            Map<String, String> headers = new HashMap<>(message.getType().equals(MessageType.UPDATE) ? 7 : 6);
            headers.put("x-ripple-uuid", message.getUuid().toString());
            headers.put("x-ripple-type", message.getType());
            headers.put("x-ripple-application-name", message.getApplicationName());
            headers.put("x-ripple-key", message.getKey());
            if (message instanceof UpdateMessage) {
                headers.put("x-ripple-value", ((UpdateMessage) message).getValue());
            }
            headers.put("x-ripple-last-update", String.valueOf(message.getLastUpdate().getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(message.getLastUpdateServerId()));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.CLIENT_NOTIFY;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean syncUpdateToServer(NodeMetadata metadata, UpdateMessage updateMessage) {
        try {
            Map<String, String> headers = new HashMap<>(7);
            headers.put("x-ripple-uuid", updateMessage.getUuid().toString());
            headers.put("x-ripple-type", updateMessage.getType());
            headers.put("x-ripple-application-name", updateMessage.getApplicationName());
            headers.put("x-ripple-key", updateMessage.getKey());
            headers.put("x-ripple-value", updateMessage.getValue());
            headers.put("x-ripple-last-update", String.valueOf(updateMessage.getLastUpdate().getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(updateMessage.getLastUpdateServerId()));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.SERVER_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean syncDeleteToServer(NodeMetadata metadata, DeleteMessage deleteMessage) {
        try {
            Map<String, String> headers = new HashMap<>(6);
            headers.put("x-ripple-uuid", deleteMessage.getUuid().toString());
            headers.put("x-ripple-type", deleteMessage.getType());
            headers.put("x-ripple-application-name", deleteMessage.getApplicationName());
            headers.put("x-ripple-key", deleteMessage.getKey());
            headers.put("x-ripple-last-update", String.valueOf(deleteMessage.getLastUpdate().getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(deleteMessage.getLastUpdateServerId()));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.SERVER_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
