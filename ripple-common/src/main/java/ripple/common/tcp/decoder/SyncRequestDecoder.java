// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.tcp.decoder;

import io.netty.buffer.ByteBuf;
import ripple.common.entity.Constants;
import ripple.common.helper.TypeHelper;
import ripple.common.tcp.Decoder;
import ripple.common.tcp.Message;
import ripple.common.tcp.MessageType;
import ripple.common.tcp.message.SyncRequest;

import java.util.Date;

/**
 * @author Zhen Tang
 */
public class SyncRequestDecoder implements Decoder {
    @Override
    public Message decode(ByteBuf byteBuf, MessageType messageType) {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUuid(TypeHelper.readUuid(byteBuf));
        syncRequest.setMessageUuid(TypeHelper.readUuid(byteBuf));
        syncRequest.setOperationType(TypeHelper.readString(byteBuf));
        syncRequest.setApplicationName(TypeHelper.readString(byteBuf));
        syncRequest.setKey(TypeHelper.readString(byteBuf));
        if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_UPDATE)) {
            syncRequest.setValue(TypeHelper.readString(byteBuf));
        } else if (syncRequest.getOperationType().equals(Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE)) {
            syncRequest.setBaseMessageUuid(TypeHelper.readUuid(byteBuf));
            syncRequest.setAtomicOperation(TypeHelper.readString(byteBuf));
            syncRequest.setValue(TypeHelper.readString(byteBuf));
        }
        syncRequest.setLastUpdate(new Date(byteBuf.readLong()));
        syncRequest.setLastUpdateServerId(byteBuf.readInt());
        return syncRequest;
    }
}