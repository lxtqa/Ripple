package ripple.common.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.Constants;
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

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        GetResponse getResponse = (GetResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        LOGGER.info("[GetResponseHandler] [{}:{}<-->{}:{}] Receive GET response. UUID = {}, Application Name = {}, Key = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), getResponse.getUuid(), getResponse.getApplicationName(), getResponse.getKey());

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
            } else if (getResponseItem.getOperationType().equals(Constants.MESSAGE_TYPE_DELETE)) {
                LOGGER.info("[GetResponseHandler] [{}:{}<-->{}:{}] ---> History: Message UUID = {}" +
                                ", Operation Type = {}, Application Name = {}, Key = {}, Last Update = {}" +
                                ", Last Update Server Id = {}."
                        , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                        , remoteAddress.getPort(), getResponseItem.getMessageUuid()
                        , getResponseItem.getOperationType(), getResponseItem.getApplicationName(), getResponseItem.getKey()
                        , SimpleDateFormat.getDateTimeInstance().format(getResponseItem.getLastUpdate()), getResponseItem.getLastUpdateServerId());
            }
        }
        return null;
    }
}
