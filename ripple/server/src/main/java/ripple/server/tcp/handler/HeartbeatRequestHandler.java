package ripple.server.tcp.handler;

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
        InetSocketAddress removeAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + removeAddress.getHostString() + ":" + removeAddress.getPort() + "] "
                + "Receive heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + removeAddress.getHostString() + ":" + removeAddress.getPort() + "] "
                + "Send heartbeat response.");
        heartbeatResponse.setUuid(heartbeatRequest.getUuid());
        heartbeatResponse.setSuccess(true);
        return heartbeatResponse;
    }
}
