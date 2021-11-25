package ripple.server.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageDecoder;
import ripple.common.tcp.MessageEncoder;
import ripple.common.tcp.MessageHandler;
import ripple.server.core.Node;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
    private Node node;

    private boolean running;
    private int port;
    private Channel serverChannel;
    private List<Channel> connectedNodes;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private MessageEncoder messageEncoder;
    private MessageDecoder messageDecoder;
    private MessageHandler messageHandler;

    public Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private Channel getServerChannel() {
        return serverChannel;
    }

    private void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public List<Channel> getConnectedNodes() {
        return connectedNodes;
    }

    private void setConnectedNodes(List<Channel> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    private EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    private void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    private EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    private void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public MessageEncoder getMessageEncoder() {
        return messageEncoder;
    }

    private void setMessageEncoder(MessageEncoder messageEncoder) {
        this.messageEncoder = messageEncoder;
    }

    public MessageDecoder getMessageDecoder() {
        return messageDecoder;
    }

    private void setMessageDecoder(MessageDecoder messageDecoder) {
        this.messageDecoder = messageDecoder;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    private void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public NettyServer(Node node, int port) {
        this.setNode(node);
        this.setPort(port);
    }

    public synchronized boolean start() {
        try {
            if (this.isRunning()) {
                return true;
            }

            this.setConnectedNodes(new ArrayList<>());
            this.setBossGroup(new NioEventLoopGroup());
            this.setWorkerGroup(new NioEventLoopGroup());


            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.getBossGroup(), this.getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerChannelInitializer(this));
            ChannelFuture future = serverBootstrap.bind(this.getPort()).sync();
            if (this.getPort() == 0) {
                this.setPort(((NioServerSocketChannel) future.channel()).localAddress().getPort());
            }
            this.setServerChannel(future.channel());
            this.setRunning(true);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public synchronized boolean stop() {
        if (!this.isRunning()) {
            return true;
        }
        try {
            this.getBossGroup().shutdownGracefully();
            this.getWorkerGroup().shutdownGracefully();
            this.setRunning(false);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public synchronized Channel connect(String address, int port) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.getWorkerGroup())
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ServerChannelInitializer(this));

            ChannelFuture future = bootstrap.connect(address, port).sync();
            InetSocketAddress remoteAddress = ((NioSocketChannel) future.channel()).remoteAddress();
            LOGGER.info("[NettyServer] [Server-{}] connect(): Connected to {}:{}."
                    , this.getNode().getId(), remoteAddress.getHostString(), remoteAddress.getPort());
            return future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Channel findChannel(String address, int port) {
        for (Channel channel : this.getConnectedNodes()) {
            InetSocketAddress remoteAddress = ((NioSocketChannel) channel).remoteAddress();
            if (remoteAddress.getHostString().equals(address) && remoteAddress.getPort() == port) {
                return channel;
            }
        }
        LOGGER.info("[NettyServer] [Server-{}] findChannel(): Cannot find the channel for {}:{}."
                , this.getNode().getId(), address, port);
        return null;
    }

    public boolean sendMessage(String address, int port, Message message) {
        Channel channel = this.findChannel(address, port);
        if (channel == null) {
            LOGGER.info("[NettyServer] [Server-{}] sendMessage(): Channel is null.", this.getNode().getId());
            return false;
        }
        channel.writeAndFlush(message);
        return true;
    }
}
