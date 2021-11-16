package ripple.common.storage;

import org.junit.Test;

/**
 * @author Zhen Tang
 */
public class StorageTest {
    @Test
    public void testInit() {
        String database = ":memory:";
        Storage storage = new Storage(database);
        storage.close();
    }
}
