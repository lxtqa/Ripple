// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.encoder;

import io.netty.buffer.ByteBuf;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.IncrementalUpdateRequest;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        IncrementalUpdateRequest incrementalUpdateRequest = (IncrementalUpdateRequest) message;
        byteBuf.writeByte(incrementalUpdateRequest.getType().getValue());
        TypeHelper.writeUuid(incrementalUpdateRequest.getUuid(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getKey(), byteBuf);
        TypeHelper.writeUuid(incrementalUpdateRequest.getBaseMessageUuid(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getAtomicOperation(), byteBuf);
        TypeHelper.writeString(incrementalUpdateRequest.getValue(), byteBuf);
    }
}