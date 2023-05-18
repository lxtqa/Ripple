package ripple.common.tcp;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Zhen Tang
 */
public interface Handler {
    Message handle(ChannelHandlerContext channelHandlerContext, Message message);
}
