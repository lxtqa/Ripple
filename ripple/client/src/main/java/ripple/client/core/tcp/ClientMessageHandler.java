package ripple.client.core.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.tcp.MessageHandler;

import java.net.InetSocketAddress;

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
        this.getRippleClient().setChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ClientMessageHandler] [{}:{}<-->{}:{}] Disconnected."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort());
        this.getRippleClient().setChannel(null);
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