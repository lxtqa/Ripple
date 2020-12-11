package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.API_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean syncWithServer(NodeMetadata metadata, Message message) {
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
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.API_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
