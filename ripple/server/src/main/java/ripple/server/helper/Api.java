package ripple.server.helper;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.UpdateMessage;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.message.SyncRequest;
import ripple.server.tcp.message.AckRequest;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class Api {
    private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);

    private Api() {

    }

    public static void heartbeat(Channel channel) {
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
        heartbeatRequest.setUuid(UUID.randomUUID());
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send HEARTBEAT request. UUID = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), heartbeatRequest.getUuid());
        channel.writeAndFlush(heartbeatRequest);
    }

    public static void sync(Channel channel, AbstractMessage message) {
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

    public static void ack(Channel channel, UUID messageUuid, int sourceId, int nodeId) {
        AckRequest ackRequest = new AckRequest();
        ackRequest.setUuid(UUID.randomUUID());
        ackRequest.setMessageUuid(messageUuid);
        ackRequest.setSourceId(sourceId);
        ackRequest.setNodeId(nodeId);
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        LOGGER.info("[Api] [{}:{}<-->{}:{}] Send ACK request. UUID = {}, Message UUID = {}" +
                        ", Source ID = {}, Node ID = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), ackRequest.getUuid(), ackRequest.getMessageUuid()
                , ackRequest.getSourceId(), ackRequest.getNodeId());
        channel.writeAndFlush(ackRequest);
    }
}
