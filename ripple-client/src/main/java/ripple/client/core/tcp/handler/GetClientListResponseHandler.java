// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client.core.tcp.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.helper.Api;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.ClientMetadata;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetClientListResponse;
import ripple.common.tcp.message.GetClientListResponseItem;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Zhen Tang
 */
public class GetClientListResponseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetClientListResponseHandler.class);
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public GetClientListResponseHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        GetClientListResponse getClientListResponse = (GetClientListResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        LOGGER.info("[GetClientListResponseHandler] [{}:{}<-->{}:{}] Receive GET_CLIENT_LIST response. UUID = {}, Client List Signature = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), getClientListResponse.getUuid(), getClientListResponse.getClientListSignature());

        List<ClientMetadata> clientList = new ArrayList<>();

        for (GetClientListResponseItem getClientListResponseItem : getClientListResponse.getItems()) {
            LOGGER.info("[GetClientListResponseHandler] [{}:{}<-->{}:{}] ---> Client Metadata: Address = {}, Port = {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), getClientListResponseItem.getAddress(), getClientListResponseItem.getPort());
            clientList.add(new ClientMetadata(getClientListResponseItem.getAddress(), getClientListResponseItem.getPort()));
        }

        this.getRippleClient().getClientListCache().put(getClientListResponse.getClientListSignature(), clientList);

        ExecutorService pool = Executors.newFixedThreadPool(clientList.size());

        Queue<AbstractMessage> pendingMessages = this.getRippleClient().getPendingMessages().get(getClientListResponse.getClientListSignature());
        if (pendingMessages != null) {
            while (!pendingMessages.isEmpty()) {
                AbstractMessage toSend = pendingMessages.poll();
                for (ClientMetadata clientMetadata : clientList) {
                    Callable<Void> task = new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            Channel channel = getRippleClient().findOrConnectToClient(clientMetadata);
                            Api.syncAsync(channel, toSend);
                            return null;
                        }
                    };
                    pool.submit(task);
                }
            }
        }
        pool.shutdown();
        return null;
    }
}