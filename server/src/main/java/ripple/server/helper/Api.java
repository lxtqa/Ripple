package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.Endpoint;
import ripple.common.Parameter;
import ripple.common.entity.Message;
import ripple.common.entity.MessageType;
import ripple.common.entity.UpdateMessage;
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
            headers.put(Parameter.UUID, message.getUuid().toString());
            headers.put(Parameter.TYPE, message.getType());
            headers.put(Parameter.APPLICATION_NAME, message.getApplicationName());
            headers.put(Parameter.KEY, message.getKey());
            if (message instanceof UpdateMessage) {
                headers.put(Parameter.VALUE, ((UpdateMessage) message).getValue());
            }
            headers.put(Parameter.LAST_UPDATE, String.valueOf(message.getLastUpdate().getTime()));
            headers.put(Parameter.LAST_UPDATE_SERVER_ID, String.valueOf(message.getLastUpdateServerId()));
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
            headers.put(Parameter.UUID, messageUuid.toString());
            headers.put(Parameter.SOURCE_ID, String.valueOf(sourceId));
            headers.put(Parameter.NODE_ID, String.valueOf(nodeId));
            String url = "http://" + address + ":" + port + Endpoint.API_ACK;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            // Suppress exception
            // exception.printStackTrace();
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
            // exception.printStackTrace();
            return false;
        }
    }
}
