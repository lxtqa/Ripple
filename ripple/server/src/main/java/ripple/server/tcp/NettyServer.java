package ripple.server.tcp;

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
import ripple.common.tcp.message.HeartbeatRequest;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhen Tang
 */
public class NettyServer {
    private boolean running;
    private int port;
    private List<Channel> connectedNodes;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;

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

    private ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    private void setServerBootstrap(ServerBootstrap serverBootstrap) {
        this.serverBootstrap = serverBootstrap;
    }

    public NettyServer(int port) {
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
            this.setServerBootstrap(new ServerBootstrap());
            this.getServerBootstrap()
                    .group(this.getBossGroup(), this.getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerChannelInitializer(this));
            ChannelFuture future = this.getServerBootstrap().bind(this.getPort()).sync();
            if (this.getPort() == 0) {
                this.setPort(((NioServerSocketChannel) future.channel()).localAddress().getPort());
            }
            this.setRunning(true);
            executor.execute(new MessageSender(this));
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

    private static final class MessageSender implements Runnable {
        private NettyServer nettyServer;

        public MessageSender(NettyServer nettyServer) {
            this.nettyServer = nettyServer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    for (Channel channel : nettyServer.getConnectedNodes()) {
                        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
                        heartbeatRequest.setUuid(UUID.randomUUID());
                        InetSocketAddress address = ((NioSocketChannel) channel).remoteAddress();
                        System.out.println("[" + address.getHostString() + ":" + address.getPort() + "] "
                                + "Send heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
                        channel.writeAndFlush(heartbeatRequest);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();
}
