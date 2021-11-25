package ripple.server.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.MessageHandler;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class ServerMessageHandler extends MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);

    private NettyServer nettyServer;

    private NettyServer getNettyServer() {
        return nettyServer;
    }

    private void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public ServerMessageHandler(NettyServer nettyServer) {
        this.setNettyServer(nettyServer);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ServerMessageHandler] [{}:{}<-->{}:{}] Connected."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort());
        this.getNettyServer().getConnectedNodes().add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ServerMessageHandler] [{}:{}<-->{}:{}] Disconnected."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort());
        this.getNettyServer().getConnectedNodes().remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        LOGGER.info("[ServerMessageHandler] [{}:{}<-->{}:{}] Exception caught: {}."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort()
                , cause.getLocalizedMessage());
    }
}
