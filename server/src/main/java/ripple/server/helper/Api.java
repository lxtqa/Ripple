package ripple.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.Endpoint;
import ripple.common.Item;
import ripple.common.helper.Http;
import ripple.server.core.ClientMetadata;
import ripple.server.core.NodeMetadata;
import ripple.common.NotifyType;
import ripple.server.core.SyncType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhen Tang
 */
public final class Api {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Api() {

    }

    public static boolean notifyUpdateToClient(ClientMetadata metadata, Item item) {
        try {
            Map<String, String> headers = new HashMap<>(6);
            headers.put("x-ripple-type", NotifyType.UPDATE);
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

    public static boolean notifyDeleteToClient(ClientMetadata metadata, String applicationName, String key) {
        try {
            Map<String, String> headers = new HashMap<>(5);
            headers.put("x-ripple-type", NotifyType.DELETE);
            headers.put("x-ripple-application-name", applicationName);
            headers.put("x-ripple-key", key);
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.CLIENT_NOTIFY;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean syncUpdateToServer(NodeMetadata metadata, String applicationName, String key, String value, Date lastUpdate, int lastUpdateServerId) {
        try {
            Map<String, String> headers = new HashMap<>(6);
            headers.put("x-ripple-type", SyncType.UPDATE);
            headers.put("x-ripple-application-name", applicationName);
            headers.put("x-ripple-key", key);
            headers.put("x-ripple-value", value);
            headers.put("x-ripple-last-update", String.valueOf(lastUpdate.getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(lastUpdateServerId));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.SERVER_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean syncDeleteToServer(NodeMetadata metadata, String applicationName, String key, Date lastUpdate, int lastUpdateServerId) {
        try {
            Map<String, String> headers = new HashMap<>(5);
            headers.put("x-ripple-type", SyncType.DELETE);
            headers.put("x-ripple-application-name", applicationName);
            headers.put("x-ripple-key", key);
            headers.put("x-ripple-last-update", String.valueOf(lastUpdate.getTime()));
            headers.put("x-ripple-last-update-server-id", String.valueOf(lastUpdateServerId));
            String url = "http://" + metadata.getAddress() + ":" + metadata.getPort() + Endpoint.SERVER_SYNC;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
