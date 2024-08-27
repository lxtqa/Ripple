// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.dir;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.entity.Ack;
import ripple.common.storage.AckService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class DirBasedAckService implements AckService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private DirBasedStorage storage;
    private ConcurrentHashMap<UUID, Object> locks;

    public DirBasedStorage getStorage() {
        return storage;
    }

    private void setStorage(DirBasedStorage storage) {
        this.storage = storage;
    }

    private ConcurrentHashMap<UUID, Object> getLocks() {
        return locks;
    }

    private void setLocks(ConcurrentHashMap<UUID, Object> locks) {
        this.locks = locks;
    }

    public DirBasedAckService(DirBasedStorage storage) {
        this.setStorage(storage);
        this.setLocks(new ConcurrentHashMap<>());
    }

    private synchronized Object getLock(UUID messageUuid) {
        if (!this.getLocks().containsKey(messageUuid)) {
            this.getLocks().put(messageUuid, new Object());
        }
        return this.getLocks().get(messageUuid);
    }

    private Path getPath(UUID messageUuid) {
        return Paths.get(this.getStorage().getLocation(), "ack", messageUuid.toString());
    }

    @Override
    public boolean initAck(UUID messageUuid, List<Integer> nodeList) {
        synchronized (this.getLock(messageUuid)) {
            try {
                Ack ack = new Ack();
                ack.setMessageUuid(messageUuid);
                ack.setNodeList(new HashSet<>(nodeList));
                ack.setAckNodes(new HashSet<>());
                Path fileName = this.getPath(messageUuid);
                String content = MAPPER.writeValueAsString(ack);
                Files.write(fileName, content.getBytes(StandardCharsets.UTF_8));
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
                Path fileName = this.getPath(messageUuid);
                if (!Files.exists(fileName)) {
                    return null;
                }
                String content = new String(Files.readAllBytes(fileName), StandardCharsets.UTF_8);
                return MAPPER.readValue(content, Ack.class);
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public List<Ack> getAllAcks() {
        try {
            List<Ack> ret = new ArrayList<>();
            Path root = Paths.get(this.getStorage().getLocation(), "ack");
            if (Files.isDirectory(root)) {
                Files.list(root).forEach(ack -> ret.add(this.getAck(UUID.fromString(ack.getFileName().toString()))));
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
                String newAck = MAPPER.writeValueAsString(ack);
                Path fileName = this.getPath(messageUuid);
                Files.write(fileName, newAck.getBytes(StandardCharsets.UTF_8));
                return true;
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
                Path fileName = this.getPath(messageUuid);
                if (Files.exists(fileName)) {
                    Files.deleteIfExists(fileName);
                }
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
}
