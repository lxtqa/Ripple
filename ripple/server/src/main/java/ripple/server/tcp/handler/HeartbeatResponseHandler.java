package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatResponse;
import ripple.server.core.Node;
import ripple.server.core.NodeMetadata;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatResponseHandler.class);

    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public HeartbeatResponseHandler(Node node) {
        this.setNode(node);
    }

    private NodeMetadata findNodeMetadata(String address, int port) {
        for (NodeMetadata nodeMetadata : this.getNode().getHealthManager().getAliveMap().keySet()) {
            if (nodeMetadata.getAddress().equals(address) && nodeMetadata.getPort() == port) {
                return nodeMetadata;
            }
        }
        return null;
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        LOGGER.info("[HeartbeatResponseHandler] [{}:{}<-->{}:{}] Receive HEARTBEAT response. UUID = {}, Success = {}"
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), heartbeatResponse.getUuid(), heartbeatResponse.isSuccess());
        NodeMetadata nodeMetadata = this.findNodeMetadata(remoteAddress.getHostString(), remoteAddress.getPort());
        if (nodeMetadata == null) {
            LOGGER.info("[HeartbeatResponseHandler] [{}:{}<-->{}:{}] Node metadata is null."
                    , localAddress.getHostString(), localAddress.getPort()
                    , remoteAddress.getHostString(), remoteAddress.getPort());
        } else {
            LOGGER.info("[HeartbeatResponseHandler] [{}:{}<-->{}:{}] Set liveness of node {}:{} to {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), nodeMetadata.getAddress(), nodeMetadata.getPort(), heartbeatResponse.isSuccess());
            this.getNode().getHealthManager().getAliveMap().put(nodeMetadata, heartbeatResponse.isSuccess());
        }
        return null;
    }
}
