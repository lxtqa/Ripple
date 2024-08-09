// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.sqlite;

import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Constants;
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.UpdateMessage;
import ripple.common.storage.MessageService;

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
public class SqliteMessageService implements MessageService {
    private SqliteStorage storage;

    public SqliteStorage getStorage() {
        return storage;
    }

    public void setStorage(SqliteStorage storage) {
        this.storage = storage;
    }

    public SqliteMessageService(SqliteStorage storage) {
        this.setStorage(storage);
    }

    @Override
    public boolean newMessage(AbstractMessage message) {
        if (message instanceof UpdateMessage) {
            return this.newUpdateMessage((UpdateMessage) message);
        } else if (message instanceof DeleteMessage) {
            return this.newDeleteMessage((DeleteMessage) message);
        } else if (message instanceof IncrementalUpdateMessage) {
            return this.newIncrementalUpdateMessage((IncrementalUpdateMessage) message);
        }
        return false;
    }

    @Override
    public boolean exist(UUID messageUuid) {
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

    private synchronized boolean newIncrementalUpdateMessage(IncrementalUpdateMessage incrementalUpdateMessage) {
        if (this.exist(incrementalUpdateMessage.getUuid())) {
            return false;
        }

        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "INSERT INTO [message] " +
                    "([uuid], [item_application_name], [item_key], [message_type]" +
                    ", [base_message_uuid], [atomic_operation], [new_value]" +
                    ", [last_update], [last_update_id]) " +
                    "VALUES (?,?,?,?,?, ?, ?, ?,?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            int i = 1;
            statement.setString(i++, incrementalUpdateMessage.getUuid().toString());
            statement.setString(i++, incrementalUpdateMessage.getApplicationName());
            statement.setString(i++, incrementalUpdateMessage.getKey());
            statement.setString(i++, incrementalUpdateMessage.getType());
            statement.setString(i++, incrementalUpdateMessage.getBaseMessageUuid().toString());
            statement.setString(i++, incrementalUpdateMessage.getAtomicOperation());
            statement.setString(i++, incrementalUpdateMessage.getValue());
            statement.setLong(i++, incrementalUpdateMessage.getLastUpdate().getTime());
            statement.setInt(i, incrementalUpdateMessage.getLastUpdateServerId());
            int count = statement.executeUpdate();
            return count == 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public AbstractMessage getMessageByUuid(UUID messageUuid) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [message] WHERE [uuid] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, messageUuid.toString());
            ResultSet resultSet = statement.executeQuery();
            AbstractMessage message = null;
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

    @Override
    public List<AbstractMessage> findMessages(String applicationName, String key) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [message] WHERE [item_application_name] = ? AND [item_key] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, applicationName);
            statement.setString(2, key);
            ResultSet resultSet = statement.executeQuery();
            List<AbstractMessage> ret = new ArrayList<>();
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

    private AbstractMessage parseMessage(ResultSet resultSet) throws SQLException {
        AbstractMessage message = null;
        UUID uuid = UUID.fromString(resultSet.getString("uuid"));
        String type = resultSet.getString("message_type");
        String itemApplicationName = resultSet.getString("item_application_name");
        String itemKey = resultSet.getString("item_key");
        Date lastUpdate = new Date(resultSet.getLong("last_update"));
        int lastUpdateServerId = resultSet.getInt("last_update_id");
        if (type.equals(Constants.MESSAGE_TYPE_UPDATE)) {
            String value = resultSet.getString("new_value");
            message = new UpdateMessage();
            message.setUuid(uuid);
            message.setType(type);
            message.setApplicationName(itemApplicationName);
            message.setKey(itemKey);
            ((UpdateMessage) message).setValue(value);
            message.setLastUpdate(lastUpdate);
            message.setLastUpdateServerId(lastUpdateServerId);
        } else if (type.equals(Constants.MESSAGE_TYPE_DELETE)) {
            message = new DeleteMessage();
            message.setUuid(uuid);
            message.setType(type);
            message.setApplicationName(itemApplicationName);
            message.setKey(itemKey);
            message.setLastUpdate(lastUpdate);
            message.setLastUpdateServerId(lastUpdateServerId);
        } else if (type.equals(Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE)) {
            UUID baseMessageUuid = UUID.fromString(resultSet.getString("base_message_uuid"));
            String atomicOperation = resultSet.getString("atomic_operation");
            String value = resultSet.getString("new_value");
            message = new IncrementalUpdateMessage();
            message.setUuid(uuid);
            message.setType(type);
            message.setApplicationName(itemApplicationName);
            message.setKey(itemKey);
            ((IncrementalUpdateMessage) message).setBaseMessageUuid(baseMessageUuid);
            ((IncrementalUpdateMessage) message).setAtomicOperation(atomicOperation);
            ((IncrementalUpdateMessage) message).setValue(value);
            message.setLastUpdate(lastUpdate);
            message.setLastUpdateServerId(lastUpdateServerId);
        }
        return message;
    }
}
