package ripple.common.storage;

import ripple.common.entity.Item;

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
public class ItemService {
    private Storage storage;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public ItemService(Storage storage) {
        this.setStorage(storage);
    }

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
