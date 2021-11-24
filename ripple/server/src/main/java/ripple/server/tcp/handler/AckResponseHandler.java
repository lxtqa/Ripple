package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.server.core.Node;
import ripple.server.tcp.message.AckResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class AckResponseHandler implements Handler {
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public AckResponseHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        AckResponse ackResponse = (AckResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Receive ack response. uuid = "
                + ackResponse.getUuid().toString()
                + ", success = " + ackResponse.isSuccess());
        return null;
    }
}
