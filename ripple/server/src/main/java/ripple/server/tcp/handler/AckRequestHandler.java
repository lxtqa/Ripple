package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.server.core.Node;
import ripple.server.tcp.message.AckRequest;
import ripple.server.tcp.message.AckResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class AckRequestHandler implements Handler {
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public AckRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        AckRequest ackRequest = (AckRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Receive ack request. uuid = "
                + ackRequest.getUuid().toString()
                + ", message uuid = " + ackRequest.getMessageUuid().toString()
                + ", source id = " + ackRequest.getSourceId()
                + ", node id  = " + ackRequest.getNodeId());

        AckResponse ackResponse = new AckResponse();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Send ack response.");
        ackResponse.setUuid(ackRequest.getUuid());
        ackResponse.setSuccess(true);
        return ackResponse;
    }
}
