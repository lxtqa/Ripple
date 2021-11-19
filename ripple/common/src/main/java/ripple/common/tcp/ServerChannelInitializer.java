package ripple.common.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

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

        HeartbeatMessageProcessor heartbeatMessageProcessor = new HeartbeatMessageProcessor();
        messageEncoder.registerEncoder(MessageType.HEARTBEAT, heartbeatMessageProcessor);
        messageDecoder.registerDecoder(MessageType.HEARTBEAT, heartbeatMessageProcessor);
        messageHandler.registerHandler(MessageType.HEARTBEAT, heartbeatMessageProcessor);
    }
}
