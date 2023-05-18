// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client.core.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ripple.client.RippleClient;
import ripple.client.core.tcp.handler.DeleteResponseHandler;
import ripple.client.core.tcp.handler.DispatchRequestHandler;
import ripple.client.core.tcp.handler.GetClientListResponseHandler;
import ripple.client.core.tcp.handler.GetResponseHandler;
import ripple.client.core.tcp.handler.IncrementalUpdateResponseHandler;
import ripple.client.core.tcp.handler.PutResponseHandler;
import ripple.client.core.tcp.handler.SubscribeResponseHandler;
import ripple.client.core.tcp.handler.SyncRequestHandler;
import ripple.client.core.tcp.handler.SyncResponseHandler;
import ripple.client.core.tcp.handler.SystemInfoResponseHandler;
import ripple.client.core.tcp.handler.UnsubscribeResponseHandler;
import ripple.common.tcp.MessageDecoder;
import ripple.common.tcp.MessageEncoder;
import ripple.common.tcp.MessageHandler;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.decoder.DeleteRequestDecoder;
import ripple.common.tcp.decoder.DeleteResponseDecoder;
import ripple.common.tcp.decoder.DispatchRequestDecoder;
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
import ripple.common.tcp.decoder.SystemInfoRequestDecoder;
import ripple.common.tcp.decoder.SystemInfoResponseDecoder;
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
import ripple.common.tcp.encoder.SystemInfoRequestEncoder;
import ripple.common.tcp.encoder.SystemInfoResponseEncoder;
import ripple.common.tcp.encoder.UnsubscribeRequestEncoder;
import ripple.common.tcp.encoder.UnsubscribeResponseEncoder;
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
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
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
        messageHandler.registerHandler(MessageType.SYNC_REQUEST, new SyncRequestHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.SYNC_RESPONSE, new SyncResponseEncoder());
        messageDecoder.registerDecoder(MessageType.SYNC_RESPONSE, new SyncResponseDecoder());
        messageHandler.registerHandler(MessageType.SYNC_RESPONSE, new SyncResponseHandler(this.getRippleClient()));

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

        messageEncoder.registerEncoder(MessageType.INCREMENTAL_UPDATE_REQUEST, new IncrementalUpdateRequestEncoder());
        messageDecoder.registerDecoder(MessageType.INCREMENTAL_UPDATE_REQUEST, new IncrementalUpdateRequestDecoder());

        messageEncoder.registerEncoder(MessageType.INCREMENTAL_UPDATE_RESPONSE, new IncrementalUpdateResponseEncoder());
        messageDecoder.registerDecoder(MessageType.INCREMENTAL_UPDATE_RESPONSE, new IncrementalUpdateResponseDecoder());
        messageHandler.registerHandler(MessageType.INCREMENTAL_UPDATE_RESPONSE, new IncrementalUpdateResponseHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.SUBSCRIBE_REQUEST, new SubscribeRequestEncoder());
        messageDecoder.registerDecoder(MessageType.SUBSCRIBE_REQUEST, new SubscribeRequestDecoder());

        messageEncoder.registerEncoder(MessageType.SUBSCRIBE_RESPONSE, new SubscribeResponseEncoder());
        messageDecoder.registerDecoder(MessageType.SUBSCRIBE_RESPONSE, new SubscribeResponseDecoder());
        messageHandler.registerHandler(MessageType.SUBSCRIBE_RESPONSE, new SubscribeResponseHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.UNSUBSCRIBE_REQUEST, new UnsubscribeRequestEncoder());
        messageDecoder.registerDecoder(MessageType.UNSUBSCRIBE_REQUEST, new UnsubscribeRequestDecoder());

        messageEncoder.registerEncoder(MessageType.UNSUBSCRIBE_RESPONSE, new UnsubscribeResponseEncoder());
        messageDecoder.registerDecoder(MessageType.UNSUBSCRIBE_RESPONSE, new UnsubscribeResponseDecoder());
        messageHandler.registerHandler(MessageType.UNSUBSCRIBE_RESPONSE, new UnsubscribeResponseHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.DISPATCH_REQUEST, new DispatchRequestEncoder());
        messageDecoder.registerDecoder(MessageType.DISPATCH_REQUEST, new DispatchRequestDecoder());
        messageHandler.registerHandler(MessageType.DISPATCH_REQUEST, new DispatchRequestHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.DISPATCH_RESPONSE, new DispatchResponseEncoder());
        messageDecoder.registerDecoder(MessageType.DISPATCH_RESPONSE, new DispatchRequestDecoder());

        messageEncoder.registerEncoder(MessageType.GET_CLIENT_LIST_REQUEST, new GetClientListRequestEncoder());
        messageDecoder.registerDecoder(MessageType.GET_CLIENT_LIST_REQUEST, new GetClientListResponseDecoder());

        messageEncoder.registerEncoder(MessageType.GET_CLIENT_LIST_RESPONSE, new GetClientListResponseEncoder());
        messageDecoder.registerDecoder(MessageType.GET_CLIENT_LIST_RESPONSE, new GetClientListResponseDecoder());
        messageHandler.registerHandler(MessageType.GET_CLIENT_LIST_RESPONSE, new GetClientListResponseHandler(this.getRippleClient()));

        messageEncoder.registerEncoder(MessageType.SYSTEM_INFO_REQUEST, new SystemInfoRequestEncoder());
        messageDecoder.registerDecoder(MessageType.SYSTEM_INFO_REQUEST, new SystemInfoRequestDecoder());

        messageEncoder.registerEncoder(MessageType.SYSTEM_INFO_RESPONSE, new SystemInfoResponseEncoder());
        messageDecoder.registerDecoder(MessageType.SYSTEM_INFO_RESPONSE, new SystemInfoResponseDecoder());
        messageHandler.registerHandler(MessageType.SYSTEM_INFO_RESPONSE, new SystemInfoResponseHandler(this.getRippleClient()));
    }
}
