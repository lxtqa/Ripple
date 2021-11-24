package ripple.server.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ripple.common.tcp.MessageDecoder;
import ripple.common.tcp.MessageEncoder;
import ripple.common.tcp.MessageHandler;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.decoder.HeartbeatRequestDecoder;
import ripple.common.tcp.decoder.HeartbeatResponseDecoder;
import ripple.common.tcp.encoder.HeartbeatRequestEncoder;
import ripple.common.tcp.encoder.HeartbeatResponseEncoder;
import ripple.common.tcp.handler.HeartbeatRequestHandler;
import ripple.server.tcp.decoder.AckRequestDecoder;
import ripple.server.tcp.decoder.AckResponseDecoder;
import ripple.server.tcp.encoder.AckRequestEncoder;
import ripple.server.tcp.encoder.AckResponseEncoder;
import ripple.server.tcp.handler.AckRequestHandler;
import ripple.server.tcp.handler.AckResponseHandler;
import ripple.server.tcp.handler.HeartbeatResponseHandler;

/**
 * @author Zhen Tang
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private NettyServer nettyServer;

    private NettyServer getNettyServer() {
        return nettyServer;
    }

    private void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public ServerChannelInitializer(NettyServer nettyServer) {
        this.setNettyServer(nettyServer);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));

        MessageEncoder messageEncoder = new MessageEncoder();
        MessageDecoder messageDecoder = new MessageDecoder();
        MessageHandler messageHandler = new ServerMessageHandler(this.getNettyServer());

        pipeline.addLast(messageEncoder);
        pipeline.addLast(messageDecoder);
        pipeline.addLast(messageHandler);

        messageEncoder.registerEncoder(MessageType.HEARTBEAT_REQUEST, new HeartbeatRequestEncoder());
        messageDecoder.registerDecoder(MessageType.HEARTBEAT_REQUEST, new HeartbeatRequestDecoder());
        messageHandler.registerHandler(MessageType.HEARTBEAT_REQUEST, new HeartbeatRequestHandler());

        messageEncoder.registerEncoder(MessageType.HEARTBEAT_RESPONSE, new HeartbeatResponseEncoder());
        messageDecoder.registerDecoder(MessageType.HEARTBEAT_RESPONSE, new HeartbeatResponseDecoder());
        messageHandler.registerHandler(MessageType.HEARTBEAT_RESPONSE, new HeartbeatResponseHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.ACK_REQUEST, new AckRequestEncoder());
        messageDecoder.registerDecoder(MessageType.ACK_REQUEST, new AckRequestDecoder());
        messageHandler.registerHandler(MessageType.ACK_REQUEST, new AckRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.ACK_RESPONSE, new AckResponseEncoder());
        messageDecoder.registerDecoder(MessageType.ACK_RESPONSE, new AckResponseDecoder());
        messageHandler.registerHandler(MessageType.ACK_RESPONSE, new AckResponseHandler(this.getNettyServer().getNode()));
    }
}
