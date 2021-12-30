package ripple.client.core.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.entity.NodeMetadata;
import ripple.common.tcp.MessageHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class ClientMessageHandler extends MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);
    private RippleClient rippleClient;

    private RippleClient getRippleClient() {
        return rippleClient;
    }

    private void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public ClientMessageHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ClientMessageHandler] [{}:{}<-->{}:{}] Connected."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort());
        for (NodeMetadata nodeMetadata : this.getRippleClient().getMappingCache().values()) {
            if (nodeMetadata.getAddress().equals(remoteAddress.getHostString())
                    && nodeMetadata.getPort() == remoteAddress.getPort()) {
                this.getRippleClient().getConnections().put(nodeMetadata, ctx.channel());
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ClientMessageHandler] [{}:{}<-->{}:{}] Disconnected."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort());
        List<NodeMetadata> toRemove = new ArrayList<>();
        for (NodeMetadata nodeMetadata : this.getRippleClient().getConnections().keySet()) {
            if (this.getRippleClient().getConnections().get(nodeMetadata) == ctx.channel()) {
                toRemove.add(nodeMetadata);
            }
        }
        for (NodeMetadata nodeMetadata : toRemove) {
            this.getRippleClient().getConnections().remove(nodeMetadata);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ClientMessageHandler] [{}:{}<-->{}:{}] Exception caught: {}."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort()
                , cause.getLocalizedMessage());
    }
}