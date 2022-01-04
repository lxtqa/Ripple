package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.ClientMetadata;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetClientListRequest;
import ripple.common.tcp.message.GetClientListResponse;
import ripple.common.tcp.message.GetClientListResponseItem;
import ripple.server.core.Node;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class GetClientListRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetClientListRequestHandler.class);
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public GetClientListRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        GetClientListRequest getClientListRequest = (GetClientListRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[GetClientListRequestHandler] [{}:{}<-->{}:{}] Receive GET_CLIENT_LIST request. UUID = {}, client list signature = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), getClientListRequest.getUuid(), getClientListRequest.getClientListSignature());

        List<ClientMetadata> clientList = this.getNode().getClientListCache()
                .get(getClientListRequest.getClientListSignature());
        List<GetClientListResponseItem> items = new ArrayList<>();
        for (ClientMetadata clientMetadata : clientList) {
            GetClientListResponseItem getClientListResponseItem = new GetClientListResponseItem();
            getClientListResponseItem.setAddress(clientMetadata.getAddress());
            getClientListResponseItem.setPort(clientMetadata.getPort());
            items.add(getClientListResponseItem);
        }

        GetClientListResponse getClientListResponse = new GetClientListResponse();
        getClientListResponse.setUuid(getClientListRequest.getUuid());
        getClientListResponse.setClientListSignature(getClientListRequest.getClientListSignature());
        getClientListResponse.setItems(items);

        LOGGER.info("[GetClientListRequestHandler] [{}:{}<-->{}:{}] Send GET_CLIENT_LIST response."
                , localAddress.getHostString(), localAddress.getPort()
                , remoteAddress.getHostString(), remoteAddress.getPort());
        return getClientListResponse;
    }
}