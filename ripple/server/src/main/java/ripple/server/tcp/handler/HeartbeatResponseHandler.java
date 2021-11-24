package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
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
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Receive heartbeat response. uuid = "
                + heartbeatResponse.getUuid().toString()
                + ", success = " + heartbeatResponse.isSuccess());
        NodeMetadata nodeMetadata = this.findNodeMetadata(remoteAddress.getHostString(), remoteAddress.getPort());
        if (nodeMetadata == null) {
            System.out.println("node metadata is null");
        } else {
            System.out.println("Set state of " + nodeMetadata.getAddress() + ":" + nodeMetadata.getPort() + " to " + heartbeatResponse.isSuccess());
            this.getNode().getHealthManager().getAliveMap().put(nodeMetadata, heartbeatResponse.isSuccess());
        }
        return null;
    }
}
