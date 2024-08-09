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

import ripple.common.entity.Item;
import ripple.common.storage.ItemService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class SqliteItemService implements ItemService {
    private SqliteStorage storage;

    public SqliteStorage getStorage() {
        return storage;
    }

    public void setStorage(SqliteStorage storage) {
        this.storage = storage;
    }

    public SqliteItemService(SqliteStorage storage) {
        this.setStorage(storage);
    }

    @Override
    public Item getItem(String applicationName, String key) {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [item] WHERE [application_name] = ? AND [key] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, applicationName);
            statement.setString(2, key);
            ResultSet resultSet = statement.executeQuery();
            Item item = null;
            if (resultSet.next()) {
                item = new Item(
                        resultSet.getString("application_name")
                        , resultSet.getString("key"));
            }
            resultSet.close();
            return item;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Item> getAllItems() {
        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "SELECT * FROM [item];";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Item> ret = new ArrayList<>();
            while (resultSet.next()) {
                Item item = new Item(
                        resultSet.getString("application_name")
                        , resultSet.getString("key"));
                ret.add(item);
            }
            resultSet.close();
            return ret;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public synchronized boolean newItem(String applicationName, String key) {
        Item item = this.getItem(applicationName, key);
        if (item != null) {
            return false;
        }

        try {
            Connection connection = this.getStorage().getConnection();
            String sql = "INSERT INTO [item] ([application_name], [key]) VALUES (?,?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, applicationName);
            statement.setString(2, key);
            int count = statement.executeUpdate();
            return count == 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
