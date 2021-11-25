package ripple.common.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.message.HeartbeatResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatRequestHandler.class);

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[HeartbeatRequestHandler] [{}:{}<-->{}:{}] Receive HEARTBEAT request. UUID = {}"
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), heartbeatRequest.getUuid());

        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        heartbeatResponse.setUuid(heartbeatRequest.getUuid());
        heartbeatResponse.setSuccess(true);

        LOGGER.info("[HeartbeatRequestHandler] [{}:{}<-->{}:{}] Send HEARTBEAT response. Success = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), heartbeatResponse.isSuccess());
        
        return heartbeatResponse;
    }
}
