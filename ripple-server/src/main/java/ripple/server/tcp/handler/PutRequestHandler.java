package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.PutRequest;
import ripple.common.tcp.message.PutResponse;
import ripple.server.core.Node;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class PutRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PutRequestHandler.class);
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public PutRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        PutRequest putRequest = (PutRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[PutRequestHandler] [{}:{}<-->{}:{}] Receive PUT request. UUID = {}, application name = {}, key = {}, value = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), putRequest.getUuid(), putRequest.getApplicationName()
                , putRequest.getKey(), putRequest.getValue());


        boolean result = this.getNode().put(putRequest.getApplicationName(), putRequest.getKey(), putRequest.getValue());

        PutResponse putResponse = new PutResponse();
        putResponse.setUuid(putRequest.getUuid());
        putResponse.setSuccess(result);

        LOGGER.info("[PutRequestHandler] [{}:{}<-->{}:{}] Send PUT response. Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), putResponse.isSuccess());
        return putResponse;
    }
}