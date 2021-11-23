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
        InetSocketAddress address = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();
        HeartbeatResponse heartbeatResponse = (HeartbeatResponse) message;
        System.out.println("[" + address.getHostString() + ":" + address.getPort() + "] "
                + "Receive heartbeat response.uuid = "
                + heartbeatResponse.getUuid().toString()
                + ", success = " + heartbeatResponse.isSuccess());
        return null;
    }
}
