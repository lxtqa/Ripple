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
import ripple.common.entity.AbstractMessage;
import ripple.common.storage.MessageService;
import ripple.common.storage.StorageHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class PMBasedMessageService implements MessageService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private PMBasedStorage storage;

    public PMBasedStorage getStorage() {
        return storage;
    }

    public void setStorage(PMBasedStorage storage) {
        this.storage = storage;
    }

    public PMBasedMessageService(PMBasedStorage storage) {
        this.setStorage(storage);
    }

    private String getEncodedKeyForMessageList(String applicationName, String key) {
        return "messages+" + StorageHelper.encodeString(applicationName) + "+" + StorageHelper.encodeString(key);
    }

    private Set<UUID> getMessageUuidList(String applicationName, String key) {
        try {
            JavaType listType = MAPPER.getTypeFactory().constructCollectionType(HashSet.class, UUID.class);
            byte[] valueBytes = this.getStorage().getPmCacheAdapter()
                    .get(this.getEncodedKeyForMessageList(applicationName, key).getBytes(StandardCharsets.UTF_8));
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

    private void writeMessageUuidList(String applicationName, String key, Set<UUID> uuids) {
        try {
            String value = MAPPER.writeValueAsString(uuids);
            this.getStorage().getPmCacheAdapter()
                    .put(this.getEncodedKeyForMessageList(applicationName, key).getBytes(StandardCharsets.UTF_8)
                            , value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String getKeyForMessage(UUID messageUuid) {
        return "message+" + messageUuid.toString();
    }

    @Override
    public boolean newMessage(AbstractMessage message) {
        this.getStorage().getItemService().newItem(message.getApplicationName(), message.getKey());
        String key = this.getKeyForMessage(message.getUuid());
        String value = StorageHelper.serializeMessage(message);
        Set<UUID> messageList = this.getMessageUuidList(message.getApplicationName(), message.getKey());
        if (messageList == null) {
            messageList = new HashSet<>();
        }
        messageList.add(message.getUuid());
        this.writeMessageUuidList(message.getApplicationName(), message.getKey(), messageList);
        this.getStorage().getPmCacheAdapter().put(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
        return true;
    }

    @Override
    public boolean exist(UUID messageUuid) {
        String key = this.getKeyForMessage(messageUuid);
        byte[] value = this.getStorage().getPmCacheAdapter().get(key.getBytes(StandardCharsets.UTF_8));
        return (value != null);
    }

    @Override
    public AbstractMessage getMessageByUuid(UUID messageUuid) {
        String key = this.getKeyForMessage(messageUuid);
        byte[] valueBytes = this.getStorage().getPmCacheAdapter().get(key.getBytes(StandardCharsets.UTF_8));
        if (valueBytes == null) {
            return null;
        } else {
            String value = new String(valueBytes, StandardCharsets.UTF_8);
            return StorageHelper.deserializeMessage(value);
        }
    }

    @Override
    public List<AbstractMessage> findMessages(String applicationName, String key) {
        // TODO: Is it necessary to trigger recycling here
        this.getStorage().getRecycleStrategy().recycle(applicationName, key);

        Set<UUID> messageList = this.getMessageUuidList(applicationName, key);
        if (messageList == null) {
            return null;
        }

        List<AbstractMessage> ret = new ArrayList<>();
        for (UUID uuid : messageList) {
            AbstractMessage message = this.getMessageByUuid(uuid);
            if (message != null) {
                ret.add(message);
            }
        }
        return ret;
    }
}
