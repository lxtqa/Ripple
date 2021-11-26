package ripple.client.core.tcp.handler;

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
import ripple.common.tcp.message.SyncRequest;
import ripple.common.tcp.message.SyncResponse;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

/**
 * @author Zhen Tang
 */
public class SyncRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncRequestHandler.class);
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public SyncRequestHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    private void applyMessage(ripple.common.entity.Message message) {
        String applicationName = message.getApplicationName();
        String key = message.getKey();
        Item item = this.getRippleClient().getStorage().getItemService().getItem(applicationName, key);
        if (item == null) {
            this.getRippleClient().getStorage().getItemService().newItem(applicationName, key);
        }
        if (!this.getRippleClient().getStorage().getMessageService().exist(message.getUuid())) {
            this.getRippleClient().getStorage().getMessageService().newMessage(message);
        }
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        SyncRequest syncRequest = (SyncRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        ripple.common.entity.Message msg = null;
        if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            LOGGER.info("[SyncRequestHandler] [{}:{}<-->{}:{}] Receive SYNC request. UUID = {}, Message UUID = {}" +
                            ", Operation Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}" +
                            ", Last Update Server Id = {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), syncRequest.getUuid(), syncRequest.getMessageUuid()
                    , syncRequest.getOperationType(), syncRequest.getApplicationName(), syncRequest.getKey()
                    , syncRequest.getValue(), SimpleDateFormat.getDateTimeInstance().format(syncRequest.getLastUpdate())
                    , syncRequest.getLastUpdateServerId());
            msg = new UpdateMessage(syncRequest.getMessageUuid(), syncRequest.getApplicationName()
                    , syncRequest.getKey(), syncRequest.getValue(), syncRequest.getLastUpdate(), syncRequest.getLastUpdateServerId());
        } else if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_DELETE)) {
            LOGGER.info("[SyncRequestHandler] [{}:{}<-->{}:{}] Receive SYNC request. UUID = {}, Message UUID = {}" +
                            ", Operation Type = {}, Application Name = {}, Key = {}, Last Update = {}" +
                            ", Last Update Server Id = {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), syncRequest.getUuid(), syncRequest.getMessageUuid()
                    , syncRequest.getOperationType(), syncRequest.getApplicationName(), syncRequest.getKey()
                    , SimpleDateFormat.getDateTimeInstance().format(syncRequest.getLastUpdate()), syncRequest.getLastUpdateServerId());
            msg = new DeleteMessage(syncRequest.getMessageUuid(), syncRequest.getApplicationName()
                    , syncRequest.getKey(), syncRequest.getLastUpdate(), syncRequest.getLastUpdateServerId());
        }

        this.applyMessage(msg);

        SyncResponse syncResponse = new SyncResponse();
        syncResponse.setUuid(syncRequest.getUuid());
        syncResponse.setSuccess(true);
        LOGGER.info("[SyncRequestHandler] [{}:{}<-->{}:{}] Send SYNC response. UUID = {}, Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), syncResponse.getUuid(), syncResponse.isSuccess());
        return syncResponse;
    }
}