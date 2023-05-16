package ripple.server.tcp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.tcp.Handler;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SystemInfoRequest;
import ripple.common.tcp.message.SystemInfoResponse;
import ripple.server.core.Node;

import java.net.InetSocketAddress;

/**
 * @author Zhen Tang
 */
public class SystemInfoRequestHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoRequestHandler.class);
    private Node node;

    private Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public SystemInfoRequestHandler(Node node) {
        this.setNode(node);
    }

    @Override
    public Message handle(ChannelHandlerContext channelHandlerContext, Message message) {
        SystemInfoRequest systemInfoRequest = (SystemInfoRequest) message;
        InetSocketAddress localAddress = ((NioSocketChannel) channelHandlerContext.channel()).localAddress();
        InetSocketAddress remoteAddress = ((NioSocketChannel) channelHandlerContext.channel()).remoteAddress();

        LOGGER.info("[SystemInfoRequestHandler] [{}:{}<-->{}:{}] Receive SYSTEM_INFO request. UUID = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), systemInfoRequest.getUuid());

        double cpuUsage = this.getNode().getCurrentCpuLoad();

        SystemInfoResponse systemInfoResponse = new SystemInfoResponse();
        systemInfoResponse.setUuid(systemInfoRequest.getUuid());
        systemInfoResponse.setCpuUsage(cpuUsage);

        LOGGER.info("[SystemInfoRequestHandler] [{}:{}<-->{}:{}] Send SYSTEM_INFO response. CPU Usage = {}."
                , localAddress.getHostString(), localAddress.getPort(), remoteAddress.getHostString()
                , remoteAddress.getPort(), systemInfoResponse.getCpuUsage());
        return systemInfoResponse;
    }
}
