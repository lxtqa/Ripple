package ripple.common.tcp;

import io.netty.buffer.ByteBuf;

public interface Encoder {

    void encode(Message message, ByteBuf out);
}
