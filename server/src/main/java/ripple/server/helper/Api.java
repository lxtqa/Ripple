package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.server.core.ClientMetadata;
import ripple.server.core.Endpoint;
import ripple.server.core.Item;
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

    public static boolean notifyClient(ClientMetadata metadata, Item item) {
        try {
            Map<String, String> headers = new HashMap<>(4);
            headers.put("x-ripple-application-name", item.getApplicationName());
            headers.put("x-ripple-key", item.getKey());
            headers.put("x-ripple-value", item.getValue());
            headers.put("x-ripple-last-update", String.valueOf(item.getLastUpdate().getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(item.getLastUpdateServerId()));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.CLIENT_NOTIFY;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean syncToServer(NodeMetadata metadata, Item item) {
        try {
            Map<String, String> headers = new HashMap<>(4);
            headers.put("x-ripple-application-name", item.getApplicationName());
            headers.put("x-ripple-key", item.getKey());
            headers.put("x-ripple-value", item.getValue());
            headers.put("x-ripple-last-update", String.valueOf(item.getLastUpdate().getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(item.getLastUpdateServerId()));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.SERVER_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
