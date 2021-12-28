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
