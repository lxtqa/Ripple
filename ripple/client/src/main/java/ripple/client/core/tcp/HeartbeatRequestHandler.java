package ripple.client.core.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatRequest;
import ripple.common.tcp.message.HeartbeatResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class HeartbeatRequestHandler implements Handler {
    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        InetSocketAddress address = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        System.out.println("[" + address.getHostString() + ":" + address.getPort() + "] "
                + "Receive heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        System.out.println("[" + address.getHostString() + ":" + address.getPort() + "] "
                + "Send heartbeat response.");
        heartbeatResponse.setUuid(heartbeatRequest.getUuid());
        heartbeatResponse.setSuccess(true);
        return heartbeatResponse;
    }
}
