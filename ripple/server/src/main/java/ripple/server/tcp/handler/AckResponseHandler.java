package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.server.core.Node;
import ripple.server.tcp.message.AckResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class AckResponseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AckResponseHandler.class);

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
        LOGGER.info("[AckResponseHandler] [{}:{}<-->{}:{}] Receive ACK response. UUID = {}, Success = {}"
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), ackResponse.getUuid(), ackResponse.isSuccess());
        return null;
    }
}
