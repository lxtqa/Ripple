// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.server.core.Node;
import ripple.server.tcp.message.AckRequest;
import ripple.server.tcp.message.AckResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class AckRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AckRequestHandler.class);
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public AckRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        AckRequest ackRequest = (AckRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[AckRequestHandler] [{}:{}<-->{}:{}] Receive ACK of {} (source server {}) from server {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), ackRequest.getMessageUuid(), ackRequest.getSourceId(), ackRequest.getNodeId());

        this.getNode().getTracker().recordAck(ackRequest.getMessageUuid()
                , ackRequest.getSourceId(), ackRequest.getNodeId());

        AckResponse ackResponse = new AckResponse();
        ackResponse.setUuid(ackRequest.getUuid());
        ackResponse.setSuccess(true);
        LOGGER.info("[AckRequestHandler] [{}:{}<-->{}:{}] Send ACK response. Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), ackResponse.isSuccess());
        return ackResponse;
    }
}
