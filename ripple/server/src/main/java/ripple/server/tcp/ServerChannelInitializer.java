package ripple.server.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ripple.common.tcp.HeartbeatRequestProcessor;
import ripple.common.tcp.HeartbeatResponseProcessor;
import ripple.common.tcp.MessageDecoder;
import ripple.common.tcp.MessageEncoder;
import ripple.common.tcp.MessageHandler;
import ripple.common.tcp.MessageType;

/**
 * @author Zhen Tang
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        MessageEncoder messageEncoder = new MessageEncoder();
        MessageDecoder messageDecoder = new MessageDecoder();
        MessageHandler messageHandler = new MessageHandler();
        pipeline.addLast(messageEncoder);
        pipeline.addLast(messageDecoder);
        pipeline.addLast(messageHandler);

        HeartbeatRequestProcessor heartbeatRequestProcessor = new HeartbeatRequestProcessor();
        messageEncoder.registerEncoder(MessageType.HEARTBEAT_REQUEST, heartbeatRequestProcessor);
        messageDecoder.registerDecoder(MessageType.HEARTBEAT_REQUEST, heartbeatRequestProcessor);
        messageHandler.registerHandler(MessageType.HEARTBEAT_REQUEST, heartbeatRequestProcessor);
        
        HeartbeatResponseProcessor heartbeatResponseProcessor = new HeartbeatResponseProcessor();
        messageEncoder.registerEncoder(MessageType.HEARTBEAT_RESPONSE, heartbeatResponseProcessor);
        messageDecoder.registerDecoder(MessageType.HEARTBEAT_RESPONSE, heartbeatResponseProcessor);
        messageHandler.registerHandler(MessageType.HEARTBEAT_RESPONSE, heartbeatResponseProcessor);
    }
}
