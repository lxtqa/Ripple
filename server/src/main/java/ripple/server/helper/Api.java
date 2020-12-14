package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.Endpoint;
import ripple.common.Message;
import ripple.common.MessageType;
import ripple.common.Parameter;
import ripple.common.UpdateMessage;
import ripple.common.helper.Http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public final class Api {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Api() {

    }

    public static boolean sync(String address, int port, Message message) {
        try {
            Map<String, String> headers = new HashMap<>(message.getType().equals(MessageType.UPDATE) ? 7 : 6);
            headers.put("x-ripple-uuid", message.getUuid().toString());
            headers.put(Parameter.TYPE, message.getType());
            headers.put(Parameter.APPLICATION_NAME, message.getApplicationName());
            headers.put(Parameter.KEY, message.getKey());
            if (message instanceof UpdateMessage) {
                headers.put(Parameter.VALUE, ((UpdateMessage) message).getValue());
            }
            headers.put("x-ripple-last-update", String.valueOf(message.getLastUpdate().getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(message.getLastUpdateServerId()));
            String url = "http://" + address + ":" + port + Endpoint.API_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean ack(String address, int port, UUID messageUuid, int sourceId, int nodeId) {
        try {
            Map<String, String> headers = new HashMap<>(3);
            headers.put("x-ripple-uuid", messageUuid.toString());
            headers.put("x-ripple-source-id", String.valueOf(sourceId));
            headers.put("x-ripple-node-id", String.valueOf(nodeId));
            String url = "http://" + address + ":" + port + Endpoint.API_ACK;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            // Suppress exception
            return false;
        }
    }

    public static boolean heartbeat(String address, int port) {
        try {
            Map<String, String> headers = new HashMap<>(0);
            String url = "http://" + address + ":" + port + Endpoint.API_HEARTBEAT;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            // Suppress exception
            return false;
        }
    }
}
