package ripple.server.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.MessageHandler;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class ServerMessageHandler extends MessageHandler {
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
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Connected.");
        this.getNettyServer().getConnectedNodes().add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Disconnected.");
        this.getNettyServer().getConnectedNodes().remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        InetSocketAddress localAddress = ((NioSocketChannel) ctx.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) ctx.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Exception caught: " + cause.getLocalizedMessage());
    }
}
