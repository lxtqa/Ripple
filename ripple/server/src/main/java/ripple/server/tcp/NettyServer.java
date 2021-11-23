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
import ripple.server.helper.NettyApi;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhen Tang
 */
public class NettyServer {
    private boolean running;
    private int port;
    private Channel serverChannel;
    private List<Channel> connectedNodes;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

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

    public synchronized Channel connect(String address, int port) {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.getWorkerGroup())
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ServerChannelInitializer(this));

            ChannelFuture future = bootstrap.connect(address, port).sync();
            InetSocketAddress removeAddress = ((NioSocketChannel) future.channel()).remoteAddress();
            System.out.println("Connected to " + removeAddress.getHostString() + ":" + removeAddress.getPort());
            return future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
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
                    System.out.println(nettyServer.getConnectedNodes().size());
                    for (Channel channel : nettyServer.getConnectedNodes()) {
                        NettyApi.heartbeat(channel);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();
}
