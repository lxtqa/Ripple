package ripple.server.core.helper;

import org.junit.Test;
import ripple.server.helper.SqliteStorage;

/**
 * @author Zhen Tang
 */
public class SqliteStorageTest {
    @Test
    public void testInit() {
        String database = ":memory:";
        SqliteStorage sqliteStorage = new SqliteStorage(database);
        sqliteStorage.init();
        sqliteStorage.close();
    }
}
