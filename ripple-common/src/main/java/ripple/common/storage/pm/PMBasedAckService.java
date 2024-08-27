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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.entity.Ack;
import ripple.common.storage.AckService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class PMBasedAckService implements AckService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private PMBasedStorage storage;
    private ConcurrentHashMap<UUID, Object> locks;
    private static final String KEY_ACK_LIST = "ack-list";

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

    private String getKeyForAck(UUID messageUuid) {
        return "ack+" + messageUuid.toString();
    }

    private Set<UUID> getAckList() {
        try {
            JavaType listType = MAPPER.getTypeFactory().constructCollectionType(HashSet.class, UUID.class);
            byte[] valueBytes = this.getStorage().getPmCacheAdapter()
                    .get(PMBasedAckService.KEY_ACK_LIST.getBytes(StandardCharsets.UTF_8));
            if (valueBytes != null) {
                String value = new String(valueBytes, StandardCharsets.UTF_8);
                return MAPPER.readValue(value, listType);
            } else {
                return new HashSet<>();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void writeAckList(Set<UUID> uuids) {
        try {
            String value = MAPPER.writeValueAsString(uuids);
            this.getStorage().getPmCacheAdapter()
                    .put(PMBasedAckService.KEY_ACK_LIST.getBytes(StandardCharsets.UTF_8)
                            , value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean initAck(UUID messageUuid, List<Integer> nodeList) {
        synchronized (this.getLock(messageUuid)) {
            try {
                Set<UUID> ackList = this.getAckList();
                ackList.add(messageUuid);
                this.writeAckList(ackList);

                Ack ack = new Ack();
                ack.setMessageUuid(messageUuid);
                ack.setNodeList(new HashSet<>(nodeList));
                ack.setAckNodes(new HashSet<>());

                String key = this.getKeyForAck(messageUuid);
                String value = MAPPER.writeValueAsString(ack);
                this.getStorage().getPmCacheAdapter().put(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public Ack getAck(UUID messageUuid) {
        synchronized (this.getLock(messageUuid)) {
            try {
                String key = this.getKeyForAck(messageUuid);
                byte[] valueBytes = this.getStorage().getPmCacheAdapter().get(key.getBytes(StandardCharsets.UTF_8));
                if (valueBytes == null) {
                    return null;
                }
                String value = new String(valueBytes, StandardCharsets.UTF_8);
                return MAPPER.readValue(value, Ack.class);
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public List<Ack> getAllAcks() {
        try {
            Set<UUID> ackList = this.getAckList();
            List<Ack> ret = new ArrayList<>();
            if (ackList != null) {
                for (UUID uuid : ackList) {
                    ret.add(this.getAck(uuid));
                }
            }
            return ret;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean recordAck(UUID messageUuid, int serverId) {
        synchronized (this.getLock(messageUuid)) {
            try {
                Ack ack = this.getAck(messageUuid);
                if (!ack.getAckNodes().contains(serverId)) {
                    ack.getAckNodes().add(serverId);
                }
                String value = MAPPER.writeValueAsString(ack);
                String key = this.getKeyForAck(messageUuid);
                return this.getStorage().getPmCacheAdapter().put(key.getBytes(StandardCharsets.UTF_8)
                        , value.getBytes(StandardCharsets.UTF_8));
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean removeAck(UUID messageUuid) {
        synchronized (this.getLock(messageUuid)) {
            try {
                Ack ack = this.getAck(messageUuid);
                if (ack == null) {
                    return false;
                }
                String key = this.getKeyForAck(messageUuid);
                return this.getStorage().getPmCacheAdapter().delete(key.getBytes(StandardCharsets.UTF_8));
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
}
