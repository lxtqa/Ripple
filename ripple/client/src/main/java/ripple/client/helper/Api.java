package ripple.client.helper;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.UpdateMessage;
import ripple.common.tcp.message.DeleteRequest;
import ripple.common.tcp.message.GetClientListRequest;
import ripple.common.tcp.message.GetRequest;
import ripple.common.tcp.message.IncrementalUpdateRequest;
import ripple.common.tcp.message.PutRequest;
import ripple.common.tcp.message.SubscribeRequest;
import ripple.common.tcp.message.SyncRequest;
import ripple.common.tcp.message.UnsubscribeRequest;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public final class Api {
    private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);

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

    public static void incrementalUpdateAsync(Channel channel, String applicationName, String key
            , UUID baseMessageUuid, String atomicOperation, String value) {
        IncrementalUpdateRequest incrementalUpdateRequest = new IncrementalUpdateRequest();
        incrementalUpdateRequest.setUuid(UUID.randomUUID());
        incrementalUpdateRequest.setApplicationName(applicationName);
        incrementalUpdateRequest.setKey(key);
        incrementalUpdateRequest.setBaseMessageUuid(baseMessageUuid);
        incrementalUpdateRequest.setAtomicOperation(atomicOperation);
        incrementalUpdateRequest.setValue(value);
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send INCREMENTAL_UPDATE request. UUID = {}, Application Name = {}" +
                        ", Key = {}, Base Message UUID = {}, Atomic Operation = {}, Value = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), incrementalUpdateRequest.getUuid(), incrementalUpdateRequest.getApplicationName()
                , incrementalUpdateRequest.getKey(), incrementalUpdateRequest.getBaseMessageUuid()
                , incrementalUpdateRequest.getAtomicOperation(), incrementalUpdateRequest.getValue());
        channel.writeAndFlush(incrementalUpdateRequest);
    }

    public static void subscribeAsync(Channel channel, String applicationName, String key, String callbackAddress, int callbackPort) {
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setUuid(UUID.randomUUID());
        subscribeRequest.setApplicationName(applicationName);
        subscribeRequest.setKey(key);
        subscribeRequest.setCallbackAddress(callbackAddress);
        subscribeRequest.setCallbackPort(callbackPort);
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send SUBSCRIBE request. UUID = {}, Application Name = {}, Key = {}, Callback Address = {}, Callback Port = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), subscribeRequest.getUuid(), subscribeRequest.getApplicationName()
                , subscribeRequest.getKey(), subscribeRequest.getCallbackAddress(), subscribeRequest.getCallbackPort());
        channel.writeAndFlush(subscribeRequest);
    }

    public static void unsubscribeAsync(Channel channel, String applicationName, String key, String callbackAddress, int callbackPort) {
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        unsubscribeRequest.setUuid(UUID.randomUUID());
        unsubscribeRequest.setApplicationName(applicationName);
        unsubscribeRequest.setKey(key);
        unsubscribeRequest.setCallbackAddress(callbackAddress);
        unsubscribeRequest.setCallbackPort(callbackPort);
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send UNSUBSCRIBE request. UUID = {}, Application Name = {}, Key = {}, Callback Address = {}, Callback Port = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), unsubscribeRequest.getUuid(), unsubscribeRequest.getApplicationName()
                , unsubscribeRequest.getKey(), unsubscribeRequest.getCallbackAddress(), unsubscribeRequest.getCallbackPort());
        channel.writeAndFlush(unsubscribeRequest);
    }

    public static void getClientListAsync(Channel channel, String clientListSignature) {
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        GetClientListRequest getClientListRequest = new GetClientListRequest();
        getClientListRequest.setUuid(UUID.randomUUID());
        getClientListRequest.setClientListSignature(clientListSignature);
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send GET_CLIENT_LIST request. UUID = {}, Client List Signature = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), getClientListRequest.getUuid(), getClientListRequest.getClientListSignature());
        channel.writeAndFlush(getClientListRequest);
    }

    public static void syncAsync(Channel channel, AbstractMessage message) {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUuid(UUID.randomUUID());
        syncRequest.setMessageUuid(message.getUuid());
        syncRequest.setOperationType(message.getType());
        syncRequest.setApplicationName(message.getApplicationName());
        syncRequest.setKey(message.getKey());
        if (message instanceof UpdateMessage) {
            syncRequest.setValue(((UpdateMessage) message).getValue());
        } else if (message instanceof IncrementalUpdateMessage) {
            syncRequest.setBaseMessageUuid(((IncrementalUpdateMessage) message).getBaseMessageUuid());
            syncRequest.setAtomicOperation(((IncrementalUpdateMessage) message).getAtomicOperation());
            syncRequest.setValue(((IncrementalUpdateMessage) message).getValue());
        }
        syncRequest.setLastUpdate(message.getLastUpdate());
        syncRequest.setLastUpdateServerId(message.getLastUpdateServerId());
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send SYNC request. UUID = {}, Message UUID = {}" +
                        ", Operation Type = {}, Application Name = {}, Key = {}, Base Message UUID = {}" +
                        ", Atomic Operation = {}, Value = {}, Last Update = {}" +
                        ", Last Update Server Id = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), syncRequest.getUuid(), syncRequest.getMessageUuid()
                , syncRequest.getOperationType(), syncRequest.getApplicationName(), syncRequest.getKey()
                , syncRequest.getBaseMessageUuid(), syncRequest.getAtomicOperation()
                , syncRequest.getValue(), SimpleDateFormat.getDateTimeInstance().format(syncRequest.getLastUpdate())
                , syncRequest.getLastUpdateServerId());
        channel.writeAndFlush(syncRequest);
    }
}
