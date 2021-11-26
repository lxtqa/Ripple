package ripple.client.core.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.entity.Constants;
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.Item;
import ripple.common.entity.UpdateMessage;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.GetResponse;
import ripple.common.tcp.message.GetResponseItem;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

/**
 * @author Zhen Tang
 */
public class GetResponseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetResponseHandler.class);
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public GetResponseHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        GetResponse getResponse = (GetResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        LOGGER.info("[GetResponseHandler] [{}:{}<-->{}:{}] Receive GET response. UUID = {}, Application Name = {}, Key = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), getResponse.getUuid(), getResponse.getApplicationName(), getResponse.getKey());

        Item item = this.getRippleClient().getStorage().getItemService().getItem(getResponse.getApplicationName(), getResponse.getKey());
        if (item == null) {
            this.getRippleClient().getStorage().getItemService().newItem(getResponse.getApplicationName(), getResponse.getKey());
        }

        for (GetResponseItem getResponseItem : getResponse.getItems()) {
            if (getResponseItem.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
                LOGGER.info("[GetResponseHandler] [{}:{}<-->{}:{}] ---> History: Message UUID = {}" +
                                ", Operation Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}" +
                                ", Last Update Server Id = {}."
                        , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                        , remoteAddress.getPort(), getResponseItem.getMessageUuid()
                        , getResponseItem.getOperationType(), getResponseItem.getApplicationName(), getResponseItem.getKey()
                        , getResponseItem.getValue(), SimpleDateFormat.getDateTimeInstance().format(getResponseItem.getLastUpdate())
                        , getResponseItem.getLastUpdateServerId());
                UpdateMessage updateMessage = new UpdateMessage(getResponseItem.getMessageUuid()
                        , getResponseItem.getApplicationName(), getResponseItem.getKey()
                        , getResponseItem.getValue(), getResponseItem.getLastUpdate()
                        , getResponseItem.getLastUpdateServerId());
                if (!this.getRippleClient().getStorage().getMessageService().exist(getResponseItem.getMessageUuid())) {
                    this.getRippleClient().getStorage().getMessageService().newMessage(updateMessage);
                }
            } else if (getResponseItem.getOperationType().equals(Constants.MESSAGE_TYPE_DELETE)) {
                LOGGER.info("[GetResponseHandler] [{}:{}<-->{}:{}] ---> History: Message UUID = {}" +
                                ", Operation Type = {}, Application Name = {}, Key = {}, Last Update = {}" +
                                ", Last Update Server Id = {}."
                        , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                        , remoteAddress.getPort(), getResponseItem.getMessageUuid()
                        , getResponseItem.getOperationType(), getResponseItem.getApplicationName(), getResponseItem.getKey()
                        , SimpleDateFormat.getDateTimeInstance().format(getResponseItem.getLastUpdate()), getResponseItem.getLastUpdateServerId());
                DeleteMessage deleteMessage = new DeleteMessage(getResponseItem.getMessageUuid()
                        , getResponseItem.getApplicationName(), getResponseItem.getKey()
                        , getResponseItem.getLastUpdate(), getResponseItem.getLastUpdateServerId());
                if (!this.getRippleClient().getStorage().getMessageService().exist(getResponseItem.getMessageUuid())) {
                    this.getRippleClient().getStorage().getMessageService().newMessage(deleteMessage);
                }
            }
        }
        return null;
    }
}
