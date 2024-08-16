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

import ripple.common.entity.AbstractMessage;
import ripple.common.storage.MessageService;
import ripple.common.storage.StorageHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Zhen Tang
 */
public class DirBasedMessageService implements MessageService {
    private DirBasedStorage storage;

    public DirBasedStorage getStorage() {
        return storage;
    }

    public void setStorage(DirBasedStorage storage) {
        this.storage = storage;
    }

    public DirBasedMessageService(DirBasedStorage storage) {
        this.setStorage(storage);
    }

    private Path getFile(String applicationName, String key, UUID messageUuid, Date lastUpdate) {
        String fileName = messageUuid.toString() + "+" + lastUpdate.getTime();
        return Paths.get(this.getStorage().getLocation(), StorageHelper.encodeString(applicationName)
                , StorageHelper.encodeString(key), fileName);
    }

    @Override
    public boolean newMessage(AbstractMessage message) {
        try {
            this.getStorage().getItemService().newItem(message.getApplicationName(), message.getKey());
            String content = StorageHelper.serializeMessage(message);
            Files.write(this.getFile(message.getApplicationName(), message.getKey(), message.getUuid(), message.getLastUpdate())
                    , content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean exist(UUID messageUuid) {
        return (this.doFindMessage(messageUuid) != null);
    }

    private String doFindMessage(UUID messageUuid) {
        try {
            Path root = Paths.get(this.getStorage().getLocation());
            List<Path> applicationNames = Files.list(root).collect(Collectors.toList());
            for (Path applicationName : applicationNames) {
                if (Files.isDirectory(applicationName)) {
                    List<Path> keys = Files.list(applicationName).collect(Collectors.toList());
                    for (Path key : keys) {
                        if (Files.isDirectory(key)) {
                            List<Path> messages = Files.list(key).collect(Collectors.toList());
                            for (Path message : messages) {
                                String str = message.getFileName().toString();
                                String uuid = str.substring(0, str.indexOf('+'));
                                if (UUID.fromString(uuid).equals(messageUuid)) {
                                    return new String(Files.readAllBytes(message), StandardCharsets.UTF_8);
                                }
                            }
                        }
                    }
                }
            }
            return null;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }


    @Override
    public AbstractMessage getMessageByUuid(UUID messageUuid) {
        String content = this.doFindMessage(messageUuid);
        if (content != null) {
            return StorageHelper.deserializeMessage(content);
        } else {
            return null;
        }
    }

    @Override
    public List<AbstractMessage> findMessages(String applicationName, String key) {
        Path messageRoot = Paths.get(this.getStorage().getLocation(), StorageHelper.encodeString(applicationName)
                , StorageHelper.encodeString(key));
        List<AbstractMessage> ret = new ArrayList<>();
        try {
            Files.list(messageRoot).forEach(elem -> {
                try {
                    String content = new String(Files.readAllBytes(elem), StandardCharsets.UTF_8);
                    ret.add(StorageHelper.deserializeMessage(content));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
        return ret;
    }
}
