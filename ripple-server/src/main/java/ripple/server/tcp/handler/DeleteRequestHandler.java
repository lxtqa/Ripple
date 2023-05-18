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
import ripple.common.tcp.message.DeleteRequest;
import ripple.common.tcp.message.DeleteResponse;
import ripple.server.core.Node;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class DeleteRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRequestHandler.class);
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public DeleteRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        DeleteRequest deleteRequest = (DeleteRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[DeleteRequestHandler] [{}:{}<-->{}:{}] Receive DELETE request. UUID = {}, application name = {}, key = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), deleteRequest.getUuid(), deleteRequest.getApplicationName(), deleteRequest.getKey());

        boolean result = this.getNode().delete(deleteRequest.getApplicationName(), deleteRequest.getKey());

        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setUuid(deleteRequest.getUuid());
        deleteResponse.setSuccess(result);

        LOGGER.info("[DeleteRequestHandler] [{}:{}<-->{}:{}] Send DELETE response. Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), deleteResponse.isSuccess());
        return deleteResponse;
    }
}