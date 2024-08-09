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

import ripple.common.entity.Item;
import ripple.common.storage.ItemService;

import java.util.Collections;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class PMBasedItemService implements ItemService {
    @Override
    public Item getItem(String applicationName, String key) {
        // TODO
        return null;
    }

    @Override
    public List<Item> getAllItems() {
        // TODO
        return Collections.emptyList();
    }

    @Override
    public boolean newItem(String applicationName, String key) {
        // TODO
        return false;
    }
}
