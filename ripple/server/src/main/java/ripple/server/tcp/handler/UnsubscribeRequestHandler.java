package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.UnsubscribeRequest;
import ripple.common.tcp.message.UnsubscribeResponse;
import ripple.server.core.Node;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class UnsubscribeRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeRequestHandler.class);
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public UnsubscribeRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        UnsubscribeRequest unsubscribeRequest = (UnsubscribeRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[UnsubscribeRequestHandler] [{}:{}<-->{}:{}] Receive UNSUBSCRIBE request. UUID = {}" +
                        ", application name = {}, key = {}, callback address = {}, callback port = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), unsubscribeRequest.getUuid(), unsubscribeRequest.getApplicationName()
                , unsubscribeRequest.getKey(), unsubscribeRequest.getCallbackAddress(), unsubscribeRequest.getCallbackPort());

        this.getNode().unsubscribe(unsubscribeRequest.getCallbackAddress(), unsubscribeRequest.getCallbackPort()
                , unsubscribeRequest.getApplicationName(), unsubscribeRequest.getKey());

        UnsubscribeResponse unsubscribeResponse = new UnsubscribeResponse();
        unsubscribeResponse.setUuid(unsubscribeRequest.getUuid());
        unsubscribeResponse.setSuccess(true);

        LOGGER.info("[UnsubscribeRequestHandler] [{}:{}<-->{}:{}] Send UNSUBSCRIBE response. Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), unsubscribeResponse.isSuccess());
        return unsubscribeResponse;
    }
}