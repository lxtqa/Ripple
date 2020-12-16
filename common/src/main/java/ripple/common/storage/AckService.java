package ripple.common.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhen Tang
 */
public class AckService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Storage storage;
    private ConcurrentHashMap<UUID, Object> locks;

    public Storage getStorage() {
        return storage;
    }

    private void setStorage(Storage storage) {
        this.storage = storage;
    }

    private ConcurrentHashMap<UUID, Object> getLocks() {
        return locks;
    }

    private void setLocks(ConcurrentHashMap<UUID, Object> locks) {
        this.locks = locks;
    }

    public AckService(Storage storage) {
        this.setStorage(storage);
        this.setLocks(new ConcurrentHashMap<>());
    }

    private synchronized Object getLock(UUID messageUuid) {
        if (!this.getLocks().containsKey(messageUuid)) {
            this.getLocks().put(messageUuid, new Object());
        }
        return this.getLocks().get(messageUuid);
    }

    public boolean initAck(UUID messageUuid, List<Integer> nodeList) {
        synchronized (this.getLock(messageUuid)) {
            try {
                String nodeListString = MAPPER.writeValueAsString(nodeList);
                Connection connection = this.getStorage().getConnection();
                String sql = "INSERT OR IGNORE INTO [ack] ([message_uuid], [node_list]) VALUES (?,?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, messageUuid.toString());
                statement.setString(2, nodeListString);
                int count = statement.executeUpdate();
                return count == 1;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

    public boolean recordAck(UUID messageUuid, int serverId) {
        synchronized (this.getLock(messageUuid)) {

            return true;
        }
    }
}
