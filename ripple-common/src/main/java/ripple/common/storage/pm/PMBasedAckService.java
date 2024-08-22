// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.pm;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.entity.Ack;
import ripple.common.storage.AckService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class PMBasedAckService implements AckService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private PMBasedStorage storage;
    private ConcurrentHashMap<UUID, Object> locks;

    public PMBasedStorage getStorage() {
        return storage;
    }

    private void setStorage(PMBasedStorage storage) {
        this.storage = storage;
    }

    private ConcurrentHashMap<UUID, Object> getLocks() {
        return locks;
    }

    private void setLocks(ConcurrentHashMap<UUID, Object> locks) {
        this.locks = locks;
    }

    public PMBasedAckService(PMBasedStorage storage) {
        this.setStorage(storage);
        this.setLocks(new ConcurrentHashMap<>());
    }

    private synchronized Object getLock(UUID messageUuid) {
        if (!this.getLocks().containsKey(messageUuid)) {
            this.getLocks().put(messageUuid, new Object());
        }
        return this.getLocks().get(messageUuid);
    }

    @Override
    public boolean initAck(UUID messageUuid, List<Integer> nodeList) {
        // TODO
        return false;
    }

    @Override
    public Ack getAck(UUID messageUuid) {
        // TODO
        return null;
    }

    @Override
    public List<Ack> getAllAcks() {
        // TODO
        return Collections.emptyList();
    }

    @Override
    public boolean recordAck(UUID messageUuid, int serverId) {
        // TODO
        return false;
    }
}
