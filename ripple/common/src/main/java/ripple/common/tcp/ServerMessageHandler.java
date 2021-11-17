package ripple.common.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {
    private MessageResolverFactory resolverFactory = MessageResolverFactory.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) {
        Resolver resolver = resolverFactory.getMessageResolver(message);
        Message result = resolver.resolve(message);
        if (result != null) {
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        resolverFactory.registerResolver(new RequestMessageResolver());
        resolverFactory.registerResolver(new ResponseMessageResolver());
    }
}
