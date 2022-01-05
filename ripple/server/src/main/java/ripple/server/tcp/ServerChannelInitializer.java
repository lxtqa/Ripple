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
import ripple.common.tcp.decoder.DeleteRequestDecoder;
import ripple.common.tcp.decoder.DeleteResponseDecoder;
import ripple.common.tcp.decoder.DispatchRequestDecoder;
import ripple.common.tcp.decoder.DispatchResponseDecoder;
import ripple.common.tcp.decoder.GetClientListRequestDecoder;
import ripple.common.tcp.decoder.GetClientListResponseDecoder;
import ripple.common.tcp.decoder.GetRequestDecoder;
import ripple.common.tcp.decoder.GetResponseDecoder;
import ripple.common.tcp.decoder.HeartbeatRequestDecoder;
import ripple.common.tcp.decoder.HeartbeatResponseDecoder;
import ripple.common.tcp.decoder.IncrementalUpdateRequestDecoder;
import ripple.common.tcp.decoder.IncrementalUpdateResponseDecoder;
import ripple.common.tcp.decoder.PutRequestDecoder;
import ripple.common.tcp.decoder.PutResponseDecoder;
import ripple.common.tcp.decoder.SubscribeRequestDecoder;
import ripple.common.tcp.decoder.SubscribeResponseDecoder;
import ripple.common.tcp.decoder.SyncRequestDecoder;
import ripple.common.tcp.decoder.SyncResponseDecoder;
import ripple.common.tcp.decoder.UnsubscribeRequestDecoder;
import ripple.common.tcp.decoder.UnsubscribeResponseDecoder;
import ripple.common.tcp.encoder.DeleteRequestEncoder;
import ripple.common.tcp.encoder.DeleteResponseEncoder;
import ripple.common.tcp.encoder.DispatchRequestEncoder;
import ripple.common.tcp.encoder.DispatchResponseEncoder;
import ripple.common.tcp.encoder.GetClientListRequestEncoder;
import ripple.common.tcp.encoder.GetClientListResponseEncoder;
import ripple.common.tcp.encoder.GetRequestEncoder;
import ripple.common.tcp.encoder.GetResponseEncoder;
import ripple.common.tcp.encoder.HeartbeatRequestEncoder;
import ripple.common.tcp.encoder.HeartbeatResponseEncoder;
import ripple.common.tcp.encoder.IncrementalUpdateRequestEncoder;
import ripple.common.tcp.encoder.IncrementalUpdateResponseEncoder;
import ripple.common.tcp.encoder.PutRequestEncoder;
import ripple.common.tcp.encoder.PutResponseEncoder;
import ripple.common.tcp.encoder.SubscribeRequestEncoder;
import ripple.common.tcp.encoder.SubscribeResponseEncoder;
import ripple.common.tcp.encoder.SyncRequestEncoder;
import ripple.common.tcp.encoder.SyncResponseEncoder;
import ripple.common.tcp.encoder.UnsubscribeRequestEncoder;
import ripple.common.tcp.encoder.UnsubscribeResponseEncoder;
import ripple.common.tcp.handler.HeartbeatRequestHandler;
import ripple.server.tcp.decoder.AckRequestDecoder;
import ripple.server.tcp.decoder.AckResponseDecoder;
import ripple.server.tcp.encoder.AckRequestEncoder;
import ripple.server.tcp.encoder.AckResponseEncoder;
import ripple.server.tcp.handler.AckRequestHandler;
import ripple.server.tcp.handler.AckResponseHandler;
import ripple.server.tcp.handler.DeleteRequestHandler;
import ripple.server.tcp.handler.DispatchResponseHandler;
import ripple.server.tcp.handler.GetClientListRequestHandler;
import ripple.server.tcp.handler.GetRequestHandler;
import ripple.server.tcp.handler.HeartbeatResponseHandler;
import ripple.server.tcp.handler.IncrementalUpdateRequestHandler;
import ripple.server.tcp.handler.PutRequestHandler;
import ripple.server.tcp.handler.SubscribeRequestHandler;
import ripple.server.tcp.handler.SyncRequestHandler;
import ripple.server.tcp.handler.SyncResponseHandler;
import ripple.server.tcp.handler.UnsubscribeRequestHandler;

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
        pipeline.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
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

        messageEncoder.registerEncoder(MessageType.SYNC_REQUEST, new SyncRequestEncoder());
        messageDecoder.registerDecoder(MessageType.SYNC_REQUEST, new SyncRequestDecoder());
        messageHandler.registerHandler(MessageType.SYNC_REQUEST, new SyncRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.SYNC_RESPONSE, new SyncResponseEncoder());
        messageDecoder.registerDecoder(MessageType.SYNC_RESPONSE, new SyncResponseDecoder());
        messageHandler.registerHandler(MessageType.SYNC_RESPONSE, new SyncResponseHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.GET_REQUEST, new GetRequestEncoder());
        messageDecoder.registerDecoder(MessageType.GET_REQUEST, new GetRequestDecoder());
        messageHandler.registerHandler(MessageType.GET_REQUEST, new GetRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.GET_RESPONSE, new GetResponseEncoder());
        messageDecoder.registerDecoder(MessageType.GET_RESPONSE, new GetResponseDecoder());

        messageEncoder.registerEncoder(MessageType.PUT_REQUEST, new PutRequestEncoder());
        messageDecoder.registerDecoder(MessageType.PUT_REQUEST, new PutRequestDecoder());
        messageHandler.registerHandler(MessageType.PUT_REQUEST, new PutRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.PUT_RESPONSE, new PutResponseEncoder());
        messageDecoder.registerDecoder(MessageType.PUT_RESPONSE, new PutResponseDecoder());

        messageEncoder.registerEncoder(MessageType.DELETE_REQUEST, new DeleteRequestEncoder());
        messageDecoder.registerDecoder(MessageType.DELETE_REQUEST, new DeleteRequestDecoder());
        messageHandler.registerHandler(MessageType.DELETE_REQUEST, new DeleteRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.DELETE_RESPONSE, new DeleteResponseEncoder());
        messageDecoder.registerDecoder(MessageType.DELETE_RESPONSE, new DeleteResponseDecoder());

        messageEncoder.registerEncoder(MessageType.INCREMENTAL_UPDATE_REQUEST, new IncrementalUpdateRequestEncoder());
        messageDecoder.registerDecoder(MessageType.INCREMENTAL_UPDATE_REQUEST, new IncrementalUpdateRequestDecoder());
        messageHandler.registerHandler(MessageType.INCREMENTAL_UPDATE_REQUEST, new IncrementalUpdateRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.INCREMENTAL_UPDATE_RESPONSE, new IncrementalUpdateResponseEncoder());
        messageDecoder.registerDecoder(MessageType.INCREMENTAL_UPDATE_RESPONSE, new IncrementalUpdateResponseDecoder());

        messageEncoder.registerEncoder(MessageType.SUBSCRIBE_REQUEST, new SubscribeRequestEncoder());
        messageDecoder.registerDecoder(MessageType.SUBSCRIBE_REQUEST, new SubscribeRequestDecoder());
        messageHandler.registerHandler(MessageType.SUBSCRIBE_REQUEST, new SubscribeRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.SUBSCRIBE_RESPONSE, new SubscribeResponseEncoder());
        messageDecoder.registerDecoder(MessageType.SUBSCRIBE_RESPONSE, new SubscribeResponseDecoder());

        messageEncoder.registerEncoder(MessageType.UNSUBSCRIBE_REQUEST, new UnsubscribeRequestEncoder());
        messageDecoder.registerDecoder(MessageType.UNSUBSCRIBE_REQUEST, new UnsubscribeRequestDecoder());
        messageHandler.registerHandler(MessageType.UNSUBSCRIBE_REQUEST, new UnsubscribeRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.UNSUBSCRIBE_RESPONSE, new UnsubscribeResponseEncoder());
        messageDecoder.registerDecoder(MessageType.UNSUBSCRIBE_RESPONSE, new UnsubscribeResponseDecoder());

        messageEncoder.registerEncoder(MessageType.DISPATCH_REQUEST, new DispatchRequestEncoder());
        messageDecoder.registerDecoder(MessageType.DISPATCH_REQUEST, new DispatchRequestDecoder());

        messageEncoder.registerEncoder(MessageType.DISPATCH_RESPONSE, new DispatchResponseEncoder());
        messageDecoder.registerDecoder(MessageType.DISPATCH_RESPONSE, new DispatchResponseDecoder());
        messageHandler.registerHandler(MessageType.DISPATCH_RESPONSE, new DispatchResponseHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.GET_CLIENT_LIST_REQUEST, new GetClientListRequestEncoder());
        messageDecoder.registerDecoder(MessageType.GET_CLIENT_LIST_REQUEST, new GetClientListRequestDecoder());
        messageHandler.registerHandler(MessageType.GET_CLIENT_LIST_REQUEST, new GetClientListRequestHandler(this.getNettyServer().getNode()));

        messageEncoder.registerEncoder(MessageType.GET_CLIENT_LIST_RESPONSE, new GetClientListResponseEncoder());
        messageDecoder.registerDecoder(MessageType.GET_CLIENT_LIST_RESPONSE, new GetClientListResponseDecoder());
    }
}
