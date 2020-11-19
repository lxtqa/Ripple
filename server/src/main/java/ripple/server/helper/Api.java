package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.server.ClientMetadata;
import ripple.server.Endpoint;
import ripple.server.NodeMetadata;
import ripple.server.entity.Item;

import java.util.HashMap;
import java.util.Map;

public final class Api {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static boolean notifyClient(ClientMetadata metadata, Item item) {
        try {
            Map<String, String> headers = new HashMap<>(4);
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
