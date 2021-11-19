package ripple.common.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ClientMessageHandler extends MessageHandler {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        executor.execute(new MessageSender(ctx));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("Send heartbeat message.");
                HeartbeatMessage heartbeatMessage = new HeartbeatMessage();
                ctx.writeAndFlush(heartbeatMessage);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                ctx.close();
            }
        }
    }

    private static final class MessageSender implements Runnable {
        private static final AtomicLong counter = new AtomicLong(1);
        private volatile ChannelHandlerContext ctx;

        public MessageSender(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Send heartbeat message.");
                    HeartbeatMessage heartbeatMessage = new HeartbeatMessage();
                    ctx.writeAndFlush(heartbeatMessage);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}