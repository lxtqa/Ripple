// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);

    private Map<MessageType, Encoder> encoders;

    private Map<MessageType, Encoder> getEncoders() {
        return encoders;
    }

    private void setEncoders(Map<MessageType, Encoder> encoders) {
        this.encoders = encoders;
    }

    public MessageEncoder() {
        this.setEncoders(new ConcurrentHashMap<>());
    }

    public void registerEncoder(MessageType type, Encoder encoder) {
        this.getEncoders().put(type, encoder);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) {
        Encoder encoder = this.getEncoders().get(message.getType());
        if (encoder != null) {
            encoder.encode(message, out);
        } else {
            LOGGER.info("[MessageEncoder] encode(): Cannot find the encoder for the message type: {}.", message.getType());
        }
    }
}
