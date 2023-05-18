// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.entity;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class IncrementalUpdateMessage extends AbstractMessage {
    private UUID baseMessageUuid;
    private String atomicOperation;
    private String value;

    public UUID getBaseMessageUuid() {
        return baseMessageUuid;
    }

    public void setBaseMessageUuid(UUID baseMessageUuid) {
        this.baseMessageUuid = baseMessageUuid;
    }

    public String getAtomicOperation() {
        return atomicOperation;
    }

    public void setAtomicOperation(String atomicOperation) {
        this.atomicOperation = atomicOperation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IncrementalUpdateMessage() {

    }

    public IncrementalUpdateMessage(String applicationName, String key, UUID baseMessageUuid
            , String atomicOperation, String value, Date lastUpdate, int lastUpdateServerId) {
        this(UUID.randomUUID(), applicationName, key, baseMessageUuid
                , atomicOperation, value, lastUpdate, lastUpdateServerId);
    }

    public IncrementalUpdateMessage(UUID uuid, String applicationName, String key, UUID baseMessageUuid
            , String atomicOperation, String value, Date lastUpdate, int lastUpdateServerId) {
        super(uuid, Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE, applicationName, key, lastUpdate, lastUpdateServerId);
        this.setBaseMessageUuid(baseMessageUuid);
        this.setAtomicOperation(atomicOperation);
        this.setValue(value);
    }
}
