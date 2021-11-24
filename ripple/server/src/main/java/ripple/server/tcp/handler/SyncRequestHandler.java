package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncRequest;
import ripple.common.tcp.message.SyncResponse;
import ripple.server.core.Node;

import java.net.InetSocketAddress;
import java.text.DateFormat;

/**
 * @author Zhen Tang
 */
public class SyncRequestHandler implements Handler {
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public SyncRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        SyncRequest syncRequest = (SyncRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Receive sync request. uuid = "
                + syncRequest.getUuid().toString()
                + ", message uuid = " + syncRequest.getMessageUuid().toString()
                + ", operation type = " + syncRequest.getOperationType()
                + ", application name = " + syncRequest.getApplicationName()
                + ", key = " + syncRequest.getKey()
                + ", value = " + syncRequest.getValue()
                + ", last update = " + DateFormat.getDateTimeInstance().format(syncRequest.getLastUpdate())
                + ", last update server id  = " + syncRequest.getLastUpdateServerId());

        SyncResponse syncResponse = new SyncResponse();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Send sync response.");
        syncResponse.setUuid(syncRequest.getUuid());
        syncResponse.setSuccess(true);
        return syncResponse;
    }
}