package ripple.client.core.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SubscribeResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class SubscribeResponseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeResponseHandler.class);
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public SubscribeResponseHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        SubscribeResponse subscribeResponse = (SubscribeResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        LOGGER.info("[SubscribeResponseHandler] [{}:{}<-->{}:{}] Receive SUBSCRIBE response. UUID = {}, Success = {}"
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), subscribeResponse.getUuid(), subscribeResponse.isSuccess());
        return null;
    }
}