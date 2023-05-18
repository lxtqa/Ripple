package ripple.common.tcp;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Map<MessageType, Handler> handlers;

    public Map<MessageType, Handler> getHandlers() {
        return handlers;
    }

    private void setHandlers(Map<MessageType, Handler> handlers) {
        this.handlers = handlers;
    }

    public MessageHandler() {
        this.setHandlers(new ConcurrentHashMap<>());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        Handler handler = this.getHandlers().get(message.getType());
        if (handler != null) {
            Message result = handler.handle(channelHandlerContext, message);
            if (result != null) {
                channelHandlerContext.writeAndFlush(result).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        LOGGER.info("[MessageHandler] channelRead0(): Complete sending message: {}.", message.getType());
                    }
                });
            }
        } else {
            LOGGER.info("[MessageHandler] channelRead0(): Cannot find the handler for the message type: {}.", message.getType());
        }
    }

    public void registerHandler(MessageType type, Handler handler) {
        this.getHandlers().put(type, handler);
    }
}
