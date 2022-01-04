package ripple.client.core.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Constants;
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.Item;
import ripple.common.entity.UpdateMessage;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.DispatchRequest;
import ripple.common.tcp.message.DispatchResponse;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

/**
 * @author Zhen Tang
 */
public class DispatchRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchRequestHandler.class);
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public DispatchRequestHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    private void applyMessage(AbstractMessage message) {
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
        // TODO: Get client list and dispatch messages

        DispatchRequest dispatchRequest = (DispatchRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        AbstractMessage msg = null;
        if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            LOGGER.info("[DispatchRequestHandler] [{}:{}<-->{}:{}] Receive DISPATCH request. UUID = {}, Client List Signature = {}, Message UUID = {}" +
                            ", Operation Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}" +
                            ", Last Update Server Id = {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), dispatchRequest.getUuid(), dispatchRequest.getClientListSignature(), dispatchRequest.getMessageUuid()
                    , dispatchRequest.getOperationType(), dispatchRequest.getApplicationName(), dispatchRequest.getKey()
                    , dispatchRequest.getValue(), SimpleDateFormat.getDateTimeInstance().format(dispatchRequest.getLastUpdate())
                    , dispatchRequest.getLastUpdateServerId());
            msg = new UpdateMessage(dispatchRequest.getMessageUuid(), dispatchRequest.getApplicationName()
                    , dispatchRequest.getKey(), dispatchRequest.getValue(), dispatchRequest.getLastUpdate(), dispatchRequest.getLastUpdateServerId());
        } else if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_DELETE)) {
            LOGGER.info("[DispatchRequestHandler] [{}:{}<-->{}:{}] Receive DISPATCH request. UUID = {}, Client List Signature = {}, Message UUID = {}" +
                            ", Operation Type = {}, Application Name = {}, Key = {}, Last Update = {}" +
                            ", Last Update Server Id = {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), dispatchRequest.getUuid(), dispatchRequest.getClientListSignature(), dispatchRequest.getMessageUuid()
                    , dispatchRequest.getOperationType(), dispatchRequest.getApplicationName(), dispatchRequest.getKey()
                    , SimpleDateFormat.getDateTimeInstance().format(dispatchRequest.getLastUpdate()), dispatchRequest.getLastUpdateServerId());
            msg = new DeleteMessage(dispatchRequest.getMessageUuid(), dispatchRequest.getApplicationName()
                    , dispatchRequest.getKey(), dispatchRequest.getLastUpdate(), dispatchRequest.getLastUpdateServerId());
        } else if (dispatchRequest.getOperationType().equals(Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE)) {
            LOGGER.info("[DispatchRequestHandler] [{}:{}<-->{}:{}] Receive DISPATCH request. UUID = {}, Client List Signature = {}, Message UUID = {}" +
                            ", Operation Type = {}, Application Name = {}, Key = {}, Base Message UUID = {}" +
                            ", Atomic Operation = {}, Value = {}, Last Update = {}, Last Update Server Id = {}."
                    , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                    , remoteAddress.getPort(), dispatchRequest.getUuid(), dispatchRequest.getClientListSignature(), dispatchRequest.getMessageUuid()
                    , dispatchRequest.getOperationType(), dispatchRequest.getApplicationName(), dispatchRequest.getKey()
                    , dispatchRequest.getBaseMessageUuid(), dispatchRequest.getAtomicOperation(), dispatchRequest.getValue()
                    , SimpleDateFormat.getDateTimeInstance().format(dispatchRequest.getLastUpdate()), dispatchRequest.getLastUpdateServerId());
            msg = new IncrementalUpdateMessage(dispatchRequest.getMessageUuid(), dispatchRequest.getApplicationName()
                    , dispatchRequest.getKey(), dispatchRequest.getBaseMessageUuid(), dispatchRequest.getAtomicOperation()
                    , dispatchRequest.getValue(), dispatchRequest.getLastUpdate(), dispatchRequest.getLastUpdateServerId());
        }

        this.applyMessage(msg);

        DispatchResponse dispatchResponse = new DispatchResponse();
        dispatchResponse.setUuid(dispatchRequest.getUuid());
        dispatchResponse.setSuccess(true);
        LOGGER.info("[DispatchRequestHandler] [{}:{}<-->{}:{}] Send DISPATCH response. UUID = {}, Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), dispatchResponse.getUuid(), dispatchResponse.isSuccess());
        return dispatchResponse;
    }
}
