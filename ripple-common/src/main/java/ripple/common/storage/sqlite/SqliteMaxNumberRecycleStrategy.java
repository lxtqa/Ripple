// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.sqlite;

import ripple.common.storage.RecycleStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author Zhen Tang
 */
public class SqliteMaxNumberRecycleStrategy implements RecycleStrategy {
    private SqliteStorage storage;
    private int maxNumberOfMessages;

    public SqliteStorage getStorage() {
        return storage;
    }

    public void setStorage(SqliteStorage storage) {
        this.storage = storage;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }

    public SqliteMaxNumberRecycleStrategy(SqliteStorage storage, int maxNumberOfMessages) {
        this.setStorage(storage);
        this.setMaxNumberOfMessages(maxNumberOfMessages);
    }

    @Override
    public synchronized void recycle(String applicationName, String key) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [message] WHERE [item_application_name] = ? AND [item_key] = ? ORDER BY [last_update] DESC LIMIT ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, applicationName);
            statement.setString(2, key);
            statement.setInt(3, this.getMaxNumberOfMessages());
            ResultSet resultSet = statement.executeQuery();
            Date lastUpdate = null;
            while (resultSet.next()) {
                lastUpdate = new Date(resultSet.getLong("last_update"));
            }
            resultSet.close();
            if (lastUpdate != null) {
                String deleteSql = "DELETE FROM [message] WHERE [item_application_name] = ? AND [item_key] = ? AND [last_update] < ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setString(1, applicationName);
                deleteStatement.setString(2, key);
                deleteStatement.setLong(3, lastUpdate.getTime());
                deleteStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
