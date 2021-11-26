package ripple.client.core.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ripple.client.RippleClient;
import ripple.client.core.tcp.handler.DeleteResponseHandler;
import ripple.client.core.tcp.handler.GetResponseHandler;
import ripple.client.core.tcp.handler.PutResponseHandler;
import ripple.common.tcp.MessageDecoder;
import ripple.common.tcp.MessageEncoder;
import ripple.common.tcp.MessageHandler;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.decoder.DeleteRequestDecoder;
import ripple.common.tcp.decoder.DeleteResponseDecoder;
import ripple.common.tcp.decoder.GetRequestDecoder;
import ripple.common.tcp.decoder.GetResponseDecoder;
import ripple.common.tcp.decoder.HeartbeatRequestDecoder;
import ripple.common.tcp.decoder.HeartbeatResponseDecoder;
import ripple.common.tcp.decoder.PutRequestDecoder;
import ripple.common.tcp.decoder.PutResponseDecoder;
import ripple.common.tcp.decoder.SyncRequestDecoder;
import ripple.common.tcp.decoder.SyncResponseDecoder;
import ripple.common.tcp.encoder.DeleteRequestEncoder;
import ripple.common.tcp.encoder.DeleteResponseEncoder;
import ripple.common.tcp.encoder.GetRequestEncoder;
import ripple.common.tcp.encoder.GetResponseEncoder;
import ripple.common.tcp.encoder.HeartbeatRequestEncoder;
import ripple.common.tcp.encoder.HeartbeatResponseEncoder;
import ripple.common.tcp.encoder.PutRequestEncoder;
import ripple.common.tcp.encoder.PutResponseEncoder;
import ripple.common.tcp.encoder.SyncRequestEncoder;
import ripple.common.tcp.encoder.SyncResponseEncoder;
import ripple.common.tcp.handler.HeartbeatRequestHandler;

/**
 * @author Zhen Tang
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private RippleClient rippleClient;

    private RippleClient getRippleClient() {
        return rippleClient;
    }

    private void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public ClientChannelInitializer(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        MessageEncoder messageEncoder = new MessageEncoder();
        MessageDecoder messageDecoder = new MessageDecoder();
        MessageHandler messageHandler = new ClientMessageHandler(this.getRippleClient());
        pipeline.addLast(messageEncoder);
        pipeline.addLast(messageDecoder);
        pipeline.addLast(messageHandler);

        messageEncoder.registerEncoder(MessageType.HEARTBEAT_REQUEST, new HeartbeatRequestEncoder());
        messageDecoder.registerDecoder(MessageType.HEARTBEAT_REQUEST, new HeartbeatRequestDecoder());
        messageHandler.registerHandler(MessageType.HEARTBEAT_REQUEST, new HeartbeatRequestHandler());

        messageEncoder.registerEncoder(MessageType.HEARTBEAT_RESPONSE, new HeartbeatResponseEncoder());
        messageDecoder.registerDecoder(MessageType.HEARTBEAT_RESPONSE, new HeartbeatResponseDecoder());

        messageEncoder.registerEncoder(MessageType.SYNC_REQUEST, new SyncRequestEncoder());
        messageDecoder.registerDecoder(MessageType.SYNC_REQUEST, new SyncRequestDecoder());

        messageEncoder.registerEncoder(MessageType.SYNC_RESPONSE, new SyncResponseEncoder());
        messageDecoder.registerDecoder(MessageType.SYNC_RESPONSE, new SyncResponseDecoder());

        messageEncoder.registerEncoder(MessageType.GET_REQUEST, new GetRequestEncoder());
        messageDecoder.registerDecoder(MessageType.GET_REQUEST, new GetRequestDecoder());

        messageEncoder.registerEncoder(MessageType.GET_RESPONSE, new GetResponseEncoder());
        messageDecoder.registerDecoder(MessageType.GET_RESPONSE, new GetResponseDecoder());
        messageHandler.registerHandler(MessageType.GET_RESPONSE, new GetResponseHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.PUT_REQUEST, new PutRequestEncoder());
        messageDecoder.registerDecoder(MessageType.PUT_REQUEST, new PutRequestDecoder());

        messageEncoder.registerEncoder(MessageType.PUT_RESPONSE, new PutResponseEncoder());
        messageDecoder.registerDecoder(MessageType.PUT_RESPONSE, new PutResponseDecoder());
        messageHandler.registerHandler(MessageType.PUT_RESPONSE, new PutResponseHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.DELETE_REQUEST, new DeleteRequestEncoder());
        messageDecoder.registerDecoder(MessageType.DELETE_REQUEST, new DeleteRequestDecoder());

        messageEncoder.registerEncoder(MessageType.DELETE_RESPONSE, new DeleteResponseEncoder());
        messageDecoder.registerDecoder(MessageType.DELETE_RESPONSE, new DeleteResponseDecoder());
        messageHandler.registerHandler(MessageType.DELETE_RESPONSE, new DeleteResponseHandler(this.getRippleClient()));
    }
}
