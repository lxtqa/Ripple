package ripple.common.storage;

import ripple.common.entity.DeleteMessage;
import ripple.common.entity.Message;
import ripple.common.entity.MessageType;
import ripple.common.entity.UpdateMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class MessageService {
    private Storage storage;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public MessageService(Storage storage) {
        this.setStorage(storage);
    }

    public boolean newMessage(Message message) {
        if (message instanceof UpdateMessage) {
            return this.newUpdateMessage((UpdateMessage) message);
        } else if (message instanceof DeleteMessage) {
            return this.newDeleteMessage((DeleteMessage) message);
        }
        return false;
    }

    private boolean exist(UUID messageUuid) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [message] WHERE [uuid] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, messageUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            boolean ret = resultSet.next();
            resultSet.close();
            return ret;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private synchronized boolean newUpdateMessage(UpdateMessage updateMessage) {
        if (this.exist(updateMessage.getUuid())) {
            return false;
        }

        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "INSERT INTO [message] " +
                    "([uuid], [item_application_name], [item_key], " +
                    "[message_type], [new_value], [last_update], [last_update_id]) " +
                    "VALUES (?,?,?,?,?,?,?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            int i = 1;
            statement.setString(i++, updateMessage.getUuid().toString());
            statement.setString(i++, updateMessage.getApplicationName());
            statement.setString(i++, updateMessage.getKey());
            statement.setString(i++, updateMessage.getType());
            statement.setString(i++, updateMessage.getValue());
            statement.setLong(i++, updateMessage.getLastUpdate().getTime());
            statement.setInt(i, updateMessage.getLastUpdateServerId());
            int count = statement.executeUpdate();
            return count == 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private synchronized boolean newDeleteMessage(DeleteMessage deleteMessage) {
        if (this.exist(deleteMessage.getUuid())) {
            return false;
        }

        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "INSERT INTO [message] " +
                    "([uuid], [item_application_name], [item_key], " +
                    "[message_type], [last_update], [last_update_id]) " +
                    "VALUES (?,?,?,?,?,?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            int i = 1;
            statement.setString(i++, deleteMessage.getUuid().toString());
            statement.setString(i++, deleteMessage.getApplicationName());
            statement.setString(i++, deleteMessage.getKey());
            statement.setString(i++, deleteMessage.getType());
            statement.setLong(i++, deleteMessage.getLastUpdate().getTime());
            statement.setInt(i, deleteMessage.getLastUpdateServerId());
            int count = statement.executeUpdate();
            return count == 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Message getMessageByUuid(UUID messageUuid) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [message] WHERE [uuid] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, messageUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            Message message = null;
            if (resultSet.next()) {
                message = (this.parseMessage(resultSet));
            }
            resultSet.close();
            return message;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public List<Message> findMessages(String applicationName, String key) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [message] WHERE [item_application_name] = ? AND [item_key] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, applicationName);
            statement.setString(2, key);
            ResultSet resultSet = statement.executeQuery();
            List<Message> ret = new ArrayList<>();
            while (resultSet.next()) {
                ret.add(this.parseMessage(resultSet));
            }
            resultSet.close();
            return ret;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private Message parseMessage(ResultSet resultSet) throws SQLException {
        Message message = null;
        UUID uuid = UUID.fromString(resultSet.getString("uuid"));
        String type = resultSet.getString("message_type");
        String itemApplicationName = resultSet.getString("item_application_name");
        String itemKey = resultSet.getString("item_key");
        Date lastUpdate = new Date(resultSet.getLong("last_update"));
        int lastUpdateServerId = resultSet.getInt("last_update_id");
        if (type.equals(MessageType.UPDATE)) {
            String value = resultSet.getString("new_value");
            message = new UpdateMessage();
            message.setUuid(uuid);
            message.setType(type);
            message.setApplicationName(itemApplicationName);
            message.setKey(itemKey);
            ((UpdateMessage) message).setValue(value);
            message.setLastUpdate(lastUpdate);
            message.setLastUpdateServerId(lastUpdateServerId);
        } else if (type.equals(MessageType.DELETE)) {
            message = new DeleteMessage();
            message.setUuid(uuid);
            message.setType(type);
            message.setApplicationName(itemApplicationName);
            message.setKey(itemKey);
            message.setLastUpdate(lastUpdate);
            message.setLastUpdateServerId(lastUpdateServerId);
        }
        return message;
    }
}
