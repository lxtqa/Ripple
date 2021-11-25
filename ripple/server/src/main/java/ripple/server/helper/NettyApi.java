package ripple.server.helper;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.entity.Message;
import ripple.common.entity.UpdateMessage;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.message.SyncRequest;

import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class NettyApi {
    public static void heartbeat(Channel channel) {
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
        heartbeatRequest.setUuid(UUID.randomUUID());
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Send heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        channel.writeAndFlush(heartbeatRequest);
    }

    public static void sync(Channel channel, Message message) {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUuid(UUID.randomUUID());
        syncRequest.setMessageUuid(message.getUuid());
        syncRequest.setOperationType(message.getType());
        syncRequest.setApplicationName(message.getApplicationName());
        syncRequest.setKey(message.getKey());
        if (message instanceof UpdateMessage) {
            syncRequest.setValue(((UpdateMessage) message).getValue());
        }
        syncRequest.setLastUpdate(message.getLastUpdate());
        syncRequest.setLastUpdateServerId(message.getLastUpdateServerId());
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Send sync request. uuid = " + syncRequest.getUuid().toString()
                + ", message uuid = " + syncRequest.getMessageUuid().toString()
                + ", operation type = " + syncRequest.getOperationType()
                + ", application name = " + syncRequest.getApplicationName()
                + ", key = " + syncRequest.getKey()
                + ", value = " + syncRequest.getValue()
                + ", last update = " + DateFormat.getDateTimeInstance().format(syncRequest.getLastUpdate())
                + ", last update server id  = " + syncRequest.getLastUpdateServerId());
        channel.writeAndFlush(syncRequest);
    }
}
