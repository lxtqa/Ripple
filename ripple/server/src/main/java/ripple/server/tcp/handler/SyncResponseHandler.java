package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncResponse;
import ripple.server.core.Node;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class SyncResponseHandler implements Handler {
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public SyncResponseHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        SyncResponse syncResponse = (SyncResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Receive sync response. uuid = "
                + syncResponse.getUuid().toString()
                + ", success = " + syncResponse.isSuccess());
        return null;
    }
}
