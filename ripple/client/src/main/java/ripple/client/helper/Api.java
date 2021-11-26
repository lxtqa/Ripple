package ripple.client.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.message.DeleteRequest;
import ripple.common.tcp.message.GetRequest;
import ripple.common.tcp.message.PutRequest;
import ripple.common.tcp.message.SubscribeRequest;
import ripple.common.tcp.message.UnsubscribeRequest;

import java.net.InetSocketAddress;
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

    public static void subscribeAsync(Channel channel, String applicationName, String key) {
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setUuid(UUID.randomUUID());
        subscribeRequest.setApplicationName(applicationName);
        subscribeRequest.setKey(key);
        subscribeRequest.setCallbackAddress(localAddress.getHostString());
        subscribeRequest.setCallbackPort(localAddress.getPort());
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send SUBSCRIBE request. UUID = {}, Application Name = {}, Key = {}, Callback Address = {}, Callback Port = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), subscribeRequest.getUuid(), subscribeRequest.getApplicationName()
                , subscribeRequest.getKey(), subscribeRequest.getCallbackAddress(), subscribeRequest.getCallbackPort());
        channel.writeAndFlush(subscribeRequest);
    }

    public static void unsubscribeAsync(Channel channel, String applicationName, String key) {
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        unsubscribeRequest.setUuid(UUID.randomUUID());
        unsubscribeRequest.setApplicationName(applicationName);
        unsubscribeRequest.setKey(key);
        unsubscribeRequest.setCallbackAddress(localAddress.getHostString());
        unsubscribeRequest.setCallbackPort(localAddress.getPort());
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send UNSUBSCRIBE request. UUID = {}, Application Name = {}, Key = {}, Callback Address = {}, Callback Port = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), unsubscribeRequest.getUuid(), unsubscribeRequest.getApplicationName()
                , unsubscribeRequest.getKey(), unsubscribeRequest.getCallbackAddress(), unsubscribeRequest.getCallbackPort());
        channel.writeAndFlush(unsubscribeRequest);
    }
}
