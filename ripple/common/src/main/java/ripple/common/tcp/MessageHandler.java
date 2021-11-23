package ripple.common.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhen Tang
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message> {
    private Map<MessageType, Handler> handlers;

    public Map<MessageType, Handler> getHandlers() {
        return handlers;
    }

    private void setHandlers(Map<MessageType, Handler> handlers) {
        this.handlers = handlers;
    }

    public MessageHandler() {
        this.setHandlers(new HashMap<>());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        Handler handler = this.getHandlers().get(message.getType());
        if (handler != null) {
            Message result = handler.handle(channelHandlerContext, message);
            if (result != null) {
                channelHandlerContext.writeAndFlush(result);
            }
        } else {
            System.out.println("Cannot find the handler for the message type: " + message.getType());
        }
    }

    public void registerHandler(MessageType type, Handler handler) {
        this.getHandlers().put(type, handler);
    }
}
