package ripple.common.tcp.handler;

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
        HeartbeatRequest heartbeatRequest = (HeartbeatRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Receive heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + remoteAddress.getHostString() + ":" + remoteAddress.getPort() + "] "
                + "Send heartbeat response.");
        heartbeatResponse.setUuid(heartbeatRequest.getUuid());
        heartbeatResponse.setSuccess(true);
        return heartbeatResponse;
    }
}
