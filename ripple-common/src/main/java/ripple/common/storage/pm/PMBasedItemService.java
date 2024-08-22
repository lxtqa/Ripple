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
import ripple.common.entity.Item;
import ripple.common.storage.ItemService;
import ripple.common.storage.StorageHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zhen Tang
 */
public class PMBasedItemService implements ItemService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private PMBasedStorage storage;
    private static final String KEY_ITEM_LIST = "item-list";

    public PMBasedStorage getStorage() {
        return storage;
    }

    public void setStorage(PMBasedStorage storage) {
        this.storage = storage;
    }

    public PMBasedItemService(PMBasedStorage storage) {
        this.setStorage(storage);
    }

    private Set<ItemEntry> getItemEntries() {
        try {
            JavaType listType = MAPPER.getTypeFactory().constructCollectionType(HashSet.class, ItemEntry.class);
            byte[] valueBytes = this.getStorage().getPmCacheAdapter()
                    .get(PMBasedItemService.KEY_ITEM_LIST.getBytes(StandardCharsets.UTF_8));
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

    private void writeItemEntries(Set<ItemEntry> itemEntries) {
        try {
            String value = MAPPER.writeValueAsString(itemEntries);
            this.getStorage().getPmCacheAdapter()
                    .put(PMBasedItemService.KEY_ITEM_LIST.getBytes(StandardCharsets.UTF_8)
                            , value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String getEncodedKey(String applicationName, String key) {
        return StorageHelper.encodeString(applicationName) + "-" + StorageHelper.encodeString(key);
    }

    @Override
    public Item getItem(String applicationName, String key) {
        // TODO: Is it necessary to trigger recycling here
        this.getStorage().getRecycleStrategy().recycle(applicationName, key);
        return this.doGetItem(applicationName, key);
    }

    private Item doGetItem(String applicationName, String key) {
        try {
            String encodedKey = this.getEncodedKey(applicationName, key);
            byte[] valueBytes = this.getStorage().getPmCacheAdapter().get(encodedKey.getBytes(StandardCharsets.UTF_8));
            if (valueBytes != null) {
                String value = new String(valueBytes, StandardCharsets.UTF_8);
                System.out.println("doGetItem: " + value);
                return MAPPER.readValue(value, Item.class);
            } else {
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Item> getAllItems() {
        Set<ItemEntry> itemEntries = this.getItemEntries();
        if (itemEntries == null) {
            return null;
        } else {
            List<Item> ret = new ArrayList<>();
            for (ItemEntry entry : itemEntries) {
                ret.add(this.doGetItem(entry.getApplicationName(), entry.getKey()));
            }

            // TODO: Is it necessary to trigger recycling here
            for (Item item : ret) {
                this.getStorage().getRecycleStrategy().recycle(item.getApplicationName(), item.getKey());
            }

            return ret;
        }
    }

    @Override
    public boolean newItem(String applicationName, String key) {
        try {
            Set<ItemEntry> itemEntries = this.getItemEntries();
            if (itemEntries == null) {
                itemEntries = new HashSet<>();
            }
            Item item = this.getItem(applicationName, key);
            if (item != null) {
                return false;
            }
            item = new Item();
            item.setApplicationName(applicationName);
            item.setKey(key);
            String value = MAPPER.writeValueAsString(item);
            String encodedKey = this.getEncodedKey(applicationName, key);
            itemEntries.add(new ItemEntry(applicationName, key));
            this.writeItemEntries(itemEntries);
            this.getStorage().getPmCacheAdapter().put(encodedKey.getBytes(StandardCharsets.UTF_8)
                    , value.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
