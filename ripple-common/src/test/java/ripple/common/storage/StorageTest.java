// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage;

import org.junit.Test;
import ripple.common.entity.Constants;
import ripple.common.entity.IncrementalUpdateMessage;

import java.util.Date;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class StorageTest {
    @Test
    public void testInit() {
        String database = ":memory:";
        Storage storage = new Storage(database);
        storage.getItemService().newItem("test", "test");
        storage.getMessageService().newMessage(new IncrementalUpdateMessage("test", "test", UUID.randomUUID()
                , Constants.ATOMIC_OPERATION_ADD_ENTRY, "test", new Date(System.currentTimeMillis()), 1));
        storage.close();
    }
}
