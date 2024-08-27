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

import ripple.common.entity.AbstractMessage;
import ripple.common.storage.RecycleStrategy;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class PMBasedMaxNumberRecycleStrategy implements RecycleStrategy {
    private PMBasedStorage storage;
    private int maxNumberOfMessages;

    public PMBasedStorage getStorage() {
        return storage;
    }

    public void setStorage(PMBasedStorage storage) {
        this.storage = storage;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }

    public PMBasedMaxNumberRecycleStrategy(PMBasedStorage storage, int maxNumberOfMessages) {
        this.setStorage(storage);
        this.setMaxNumberOfMessages(maxNumberOfMessages);
    }

    @Override
    public synchronized void recycle(String applicationName, String key) {
        Set<UUID> messageList = ((PMBasedMessageService) this.getStorage().getMessageService()).getMessageUuidList(applicationName, key);
        if (messageList == null) {
            return;
        }
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        List<AbstractMessage> messages = new ArrayList<>();
        for (UUID uuid : messageList) {
            AbstractMessage message = this.getStorage().getMessageService().getMessageByUuid(uuid);
            Long lastUpdate = message.getLastUpdate().getTime();
            if (minHeap.size() < this.getMaxNumberOfMessages()) {
                minHeap.offer(lastUpdate);
            } else if (minHeap.peek() < lastUpdate) {
                minHeap.poll();
                minHeap.offer(lastUpdate);
            }
            messages.add(message);
        }
        Set<UUID> newMessageList = new HashSet<>();
        for (AbstractMessage message : messages) {
            if (!minHeap.contains(message.getLastUpdate().getTime())) {
                String messageKey = ((PMBasedMessageService) this.getStorage().getMessageService()).getKeyForMessage(message.getUuid());
                this.getStorage().getPmCacheAdapter().delete(messageKey.getBytes(StandardCharsets.UTF_8));
                this.getStorage().getAckService().removeAck(message.getUuid());
            } else {
                newMessageList.add(message.getUuid());
            }
        }
        ((PMBasedMessageService) this.getStorage().getMessageService()).writeMessageUuidList(applicationName, key, newMessageList);
    }
}
