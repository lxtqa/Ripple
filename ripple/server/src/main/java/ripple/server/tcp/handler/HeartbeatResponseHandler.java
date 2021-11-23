package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.HeartbeatResponse;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class HeartbeatResponseHandler implements Handler {
    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress removeAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + removeAddress.getHostString() + ":" + removeAddress.getPort() + "] "
                + "Receive heartbeat response.uuid = "
                + heartbeatResponse.getUuid().toString()
                + ", success = " + heartbeatResponse.isSuccess());
        return null;
    }
}
