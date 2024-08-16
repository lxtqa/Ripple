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

import ripple.common.entity.Item;
import ripple.common.storage.ItemService;
import ripple.common.storage.StorageHelper;
import ripple.common.storage.sqlite.SqliteStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class DirBasedItemService implements ItemService {
    private DirBasedStorage storage;

    public DirBasedStorage getStorage() {
        return storage;
    }

    public void setStorage(DirBasedStorage storage) {
        this.storage = storage;
    }

    public DirBasedItemService(DirBasedStorage storage) {
        this.setStorage(storage);
    }

    @Override
    public Item getItem(String applicationName, String key) {
        // TODO: Is it necessary to trigger recycling here
        this.getStorage().getRecycleStrategy().recycle(applicationName, key);

        String encodedApplicationName = StorageHelper.encodeString(applicationName);
        String encodedKey = StorageHelper.encodeString(key);
        Path itemPath = Paths.get(this.getStorage().getLocation(), encodedApplicationName, encodedKey);
        if (Files.exists(itemPath)) {
            return new Item(applicationName, key);
        } else {
            return null;
        }
    }

    @Override
    public List<Item> getAllItems() {
        try {
            List<Item> ret = new ArrayList<>();
            Path rootPath = Paths.get(this.getStorage().getLocation());
            Files.list(rootPath).forEach(file -> {
                try {
                    if (Files.isDirectory(file)) {
                        String applicationName = file.getFileName().toString();
                        Files.list(file).forEach(elem -> {
                            if (Files.isDirectory(elem)) {
                                String key = elem.getFileName().toString();
                                ret.add(new Item(StorageHelper.decodeString(applicationName), StorageHelper.decodeString(key)));
                            }
                        });
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            // TODO: Is it necessary to trigger recycling here
            for (Item item : ret) {
                this.getStorage().getRecycleStrategy().recycle(item.getApplicationName(), item.getKey());
            }
            return ret;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean newItem(String applicationName, String key) {
        try {
            String encodedApplicationName = StorageHelper.encodeString(applicationName);
            String encodedKey = StorageHelper.encodeString(key);
            Path applicationNamePath = Paths.get(this.getStorage().getLocation(), encodedApplicationName);
            Path keyPath = Paths.get(applicationNamePath.toString(), encodedKey);
            if (!Files.exists(applicationNamePath)) {
                Files.createDirectories(applicationNamePath);
            }
            if (!Files.exists(keyPath)) {
                Files.createDirectories(keyPath);
            }
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
