package ripple.client.core.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.UnsubscribeResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class UnsubscribeResponseHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeResponseHandler.class);
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public UnsubscribeResponseHandler(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        UnsubscribeResponse unsubscribeResponse = (UnsubscribeResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        LOGGER.info("[UnsubscribeResponseHandler] [{}:{}<-->{}:{}] Receive UNSUBSCRIBE response. UUID = {}, Success = {}"
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), unsubscribeResponse.getUuid(), unsubscribeResponse.isSuccess());
        return null;
    }
}