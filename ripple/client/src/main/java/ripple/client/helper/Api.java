package ripple.client.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Endpoint;
import ripple.common.Parameter;
import ripple.common.helper.Http;
import ripple.common.tcp.message.DeleteRequest;
import ripple.common.tcp.message.GetRequest;
import ripple.common.tcp.message.PutRequest;

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

    public static void putAsync(Channel channel, String applicationName, String key, String value) {
        PutRequest putRequest = new PutRequest();
        putRequest.setUuid(UUID.randomUUID());
        putRequest.setApplicationName(applicationName);
        putRequest.setKey(key);
        putRequest.setValue(value);
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send PUT request. UUID = {}, Application Name = {}, Key = {}, Value = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), putRequest.getUuid(), putRequest.getApplicationName()
                , putRequest.getKey(), putRequest.getValue());
        channel.writeAndFlush(putRequest);
    }

    public static void deleteAsync(Channel channel, String applicationName, String key) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setUuid(UUID.randomUUID());
        deleteRequest.setApplicationName(applicationName);
        deleteRequest.setKey(key);
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send DELETE request. UUID = {}, Application Name = {}, Key = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), deleteRequest.getUuid(), deleteRequest.getApplicationName(), deleteRequest.getKey());
        channel.writeAndFlush(deleteRequest);
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
