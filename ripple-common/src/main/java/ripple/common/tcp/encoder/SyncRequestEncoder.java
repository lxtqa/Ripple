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
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Encoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.message.SyncRequest;

/**
 * @author Zhen Tang
 */
public class SyncRequestEncoder implements Encoder {
    @Override
    public void encode(Message message, ByteBuf byteBuf) {
        SyncRequest syncRequest = (SyncRequest) message;
        byteBuf.writeByte(syncRequest.getType().getValue());
        TypeHelper.writeUuid(syncRequest.getUuid(), byteBuf);
        TypeHelper.writeUuid(syncRequest.getMessageUuid(), byteBuf);
        TypeHelper.writeString(syncRequest.getOperationType(), byteBuf);
        TypeHelper.writeString(syncRequest.getApplicationName(), byteBuf);
        TypeHelper.writeString(syncRequest.getKey(), byteBuf);
        if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            TypeHelper.writeString(syncRequest.getValue(), byteBuf);
        } else if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE)) {
            TypeHelper.writeUuid(syncRequest.getBaseMessageUuid(), byteBuf);
            TypeHelper.writeString(syncRequest.getAtomicOperation(), byteBuf);
            TypeHelper.writeString(syncRequest.getValue(), byteBuf);
        }
        byteBuf.writeLong(syncRequest.getLastUpdate().getTime());
        byteBuf.writeInt(syncRequest.getLastUpdateServerId());
    }
}