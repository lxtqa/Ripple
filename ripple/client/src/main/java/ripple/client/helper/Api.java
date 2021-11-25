package ripple.client.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Endpoint;
import ripple.common.Parameter;
import ripple.common.helper.Http;
import ripple.common.tcp.message.GetRequest;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public final class Api {
    private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Api() {

    }

    public static void getAsync(Channel channel, String applicationName, String key) {
        GetRequest getRequest = new GetRequest();
        getRequest.setUuid(UUID.randomUUID());
        getRequest.setApplicationName(applicationName);
        getRequest.setKey(key);
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send GET request. UUID = {}, Application Name = {}, Key = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), getRequest.getUuid(), getRequest.getApplicationName(), getRequest.getKey());
        channel.writeAndFlush(getRequest);
    }

    public static boolean put(String address, int port, String applicationName, String key, String value) {
        try {
            Map<String, String> headers = new HashMap<>(3);
            headers.put(Parameter.APPLICATION_NAME, applicationName);
            headers.put(Parameter.KEY, key);
            headers.put(Parameter.VALUE, value);
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
            headers.put(Parameter.KEY, key);
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
            headers.put(Parameter.KEY, key);
            headers.put(Parameter.CALLBACK_ADDRESS, callbackAddress);
            headers.put(Parameter.CALLBACK_PORT, String.valueOf(callbackPort));
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
            headers.put(Parameter.KEY, key);
            headers.put(Parameter.CALLBACK_ADDRESS, callbackAddress);
            headers.put(Parameter.CALLBACK_PORT, String.valueOf(callbackPort));
            String url = "http://" + serverAddress + ":" + serverPort + Endpoint.API_UNSUBSCRIBE;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
