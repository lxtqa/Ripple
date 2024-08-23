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

import ripple.common.storage.RecycleStrategy;
import ripple.common.storage.StorageHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * @author Zhen Tang
 */
public class DirBasedMaxNumberRecycleStrategy implements RecycleStrategy {
    private DirBasedStorage storage;
    private int maxNumberOfMessages;

    public DirBasedStorage getStorage() {
        return storage;
    }

    public void setStorage(DirBasedStorage storage) {
        this.storage = storage;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }

    public DirBasedMaxNumberRecycleStrategy(DirBasedStorage storage, int maxNumberOfMessages) {
        this.setStorage(storage);
        this.setMaxNumberOfMessages(maxNumberOfMessages);
    }

    @Override
    public synchronized void recycle(String applicationName, String key) {
        Path messageRoot = Paths.get(this.getStorage().getLocation(), "data", StorageHelper.encodeString(applicationName)
                , StorageHelper.encodeString(key));
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        try {
            Files.list(messageRoot).forEach(elem -> {
                String fileName = elem.getFileName().toString();
                Long lastUpdate = Long.valueOf(fileName.substring(fileName.indexOf('+') + 1));
                if (minHeap.size() < this.getMaxNumberOfMessages()) {
                    minHeap.offer(lastUpdate);
                } else if (minHeap.peek() < lastUpdate) {
                    minHeap.poll();
                    minHeap.offer(lastUpdate);
                }
            });
            List<Path> messages = Files.list(messageRoot).collect(Collectors.toList());
            for (Path message : messages) {
                String fileName = message.getFileName().toString();
                Long lastUpdate = Long.valueOf(fileName.substring(fileName.indexOf('+') + 1));
                if (!minHeap.contains(lastUpdate)) {
                    Files.deleteIfExists(message);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
