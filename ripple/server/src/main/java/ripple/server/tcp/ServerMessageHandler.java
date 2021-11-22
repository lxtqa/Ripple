package ripple.server.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import ripple.common.tcp.MessageHandler;
import ripple.common.tcp.message.HeartbeatRequest;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhen Tang
 */
public class ServerMessageHandler extends MessageHandler {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        executor.execute(new MessageSender(ctx));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
                heartbeatRequest.setUuid(UUID.randomUUID());
                System.out.println("Send heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
                ctx.writeAndFlush(heartbeatRequest);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.close();
            }
        }
    }

    private static final class MessageSender implements Runnable {
        private volatile ChannelHandlerContext ctx;

        public MessageSender(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
                    heartbeatRequest.setUuid(UUID.randomUUID());
                    System.out.println("Send heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
                    ctx.writeAndFlush(heartbeatRequest);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
