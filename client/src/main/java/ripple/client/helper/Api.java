package ripple.client.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.Endpoint;
import ripple.common.Item;
import ripple.common.Parameter;
import ripple.common.helper.Http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhen Tang
 */
public final class Api {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Api() {

    }

    public static Item get(String address, int port, String applicationName, String key) {
        try {
            Map<String, String> headers = new HashMap<>(2);
            headers.put(Parameter.APPLICATION_NAME, applicationName);
            headers.put("x-ripple-key", key);
            String url = "http://" + address + ":" + port + Endpoint.API_GET;
            String returnValue = Http.get(url, headers);
            return MAPPER.readValue(returnValue, Item.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static boolean put(String address, int port, String applicationName, String key, String value) {
        try {
            Map<String, String> headers = new HashMap<>(3);
            headers.put(Parameter.APPLICATION_NAME, applicationName);
            headers.put("x-ripple-key", key);
            headers.put("x-ripple-value", value);
            String url = "http://" + address + ":" + port + Endpoint.API_PUT;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean delete(String address, int port, String applicationName, String key) {
        try {
            Map<String, String> headers = new HashMap<>(2);
            headers.put(Parameter.APPLICATION_NAME, applicationName);
            headers.put("x-ripple-key", key);
            String url = "http://" + address + ":" + port + Endpoint.API_DELETE;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean subscribe(String serverAddress, int serverPort
            , String callbackAddress, int callbackPort, String applicationName, String key) {
        try {
            Map<String, String> headers = new HashMap<>(4);
            headers.put(Parameter.APPLICATION_NAME, applicationName);
            headers.put("x-ripple-callback-address", callbackAddress);
            headers.put("x-ripple-callback-port", String.valueOf(callbackPort));
            headers.put("x-ripple-key", key);
            String url = "http://" + serverAddress + ":" + serverPort + Endpoint.API_SUBSCRIBE;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean unsubscribe(String serverAddress, int serverPort
            , String callbackAddress, int callbackPort, String applicationName, String key) {
        try {
            Map<String, String> headers = new HashMap<>(4);
            headers.put(Parameter.APPLICATION_NAME, applicationName);
            headers.put("x-ripple-callback-address", callbackAddress);
            headers.put("x-ripple-callback-port", String.valueOf(callbackPort));
            headers.put("x-ripple-key", key);
            String url = "http://" + serverAddress + ":" + serverPort + Endpoint.API_UNSUBSCRIBE;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
