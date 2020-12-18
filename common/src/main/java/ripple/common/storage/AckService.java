package ripple.common.storage;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.entity.Ack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
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
                String nodeListString = MAPPER.writeValueAsString(new HashSet<>(nodeList));
                String ackNodesString = MAPPER.writeValueAsString(new HashSet<>());
                Connection connection = this.getStorage().getConnection();
                String sql = "INSERT INTO [ack] ([message_uuid], [node_list], [ack_nodes]) VALUES (?,?,?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, messageUuid.toString());
                statement.setString(2, nodeListString);
                statement.setString(3, ackNodesString);
                int count = statement.executeUpdate();
                return count == 1;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

    public Ack getAck(UUID messageUuid) {
        synchronized (this.getLock(messageUuid)) {
            try {
                Connection connection = this.getStorage().getConnection();
                String sql = "SELECT * FROM [ack] WHERE [message_uuid] = ?;";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, messageUuid.toString());
                ResultSet resultSet = statement.executeQuery();

                JavaType listType = MAPPER.getTypeFactory().constructCollectionType(HashSet.class, Integer.class);
                Ack ack = null;
                if (resultSet.next()) {
                    ack = new Ack();
                    ack.setMessageUuid(UUID.fromString(resultSet.getString("message_uuid")));
                    ack.setNodeList(MAPPER.readValue(resultSet.getString("node_list"), listType));
                    ack.setAckNodes(MAPPER.readValue(resultSet.getString("ack_nodes"), listType));
                }
                resultSet.close();
                return ack;
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    public boolean recordAck(UUID messageUuid, int serverId) {
        synchronized (this.getLock(messageUuid)) {
            try {
                Ack ack = this.getAck(messageUuid);
                if (!ack.getAckNodes().contains(serverId)) {
                    ack.getAckNodes().add(serverId);
                }
                String newAckNodes = MAPPER.writeValueAsString(ack.getAckNodes());
                Connection connection = this.getStorage().getConnection();
                String sql = "UPDATE [ack] SET [ack_nodes] = ? WHERE [message_uuid] = ?;";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newAckNodes);
                statement.setString(2, messageUuid.toString());
                int count = statement.executeUpdate();
                return count == 1;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }
}
