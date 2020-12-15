package ripple.common.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Zhen Tang
 */
public class Storage {
    private String fileName;
    private Connection connection;
    private ItemService itemService;
    private MessageService messageService;
    private AckService ackService;

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ItemService getItemService() {
        return itemService;
    }

    private void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    private void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public AckService getAckService() {
        return ackService;
    }

    private void setAckService(AckService ackService) {
        this.ackService = ackService;
    }

    public Storage(String fileName) {
        this.setFileName(fileName);
        this.setItemService(new ItemService(this));
        this.setMessageService(new MessageService(this));
        this.setAckService(new AckService(this));
        this.init();
    }

    private void init() {
        try {
            this.setConnection(DriverManager.getConnection("jdbc:sqlite:" + this.getFileName()));
            this.initTables();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void initTables() {
        try {
            Statement statement = this.getConnection().createStatement();
            statement.execute("PRAGMA foreign_keys = ON;");

            // Item: application_name, item_key
            statement.execute("CREATE TABLE IF NOT EXISTS [item] " +
                    "([application_name] TEXT NOT NULL," +
                    " [key] TEXT NOT NULL, " +
                    "PRIMARY KEY([application_name], [key]));");

            // Message: uuid, item_application_name, item_key, message_type, new_value, last_update, last_update_id
            statement.execute("CREATE TABLE IF NOT EXISTS [message] " +
                    "([uuid] TEXT PRIMARY KEY, " +
                    "[item_application_name] TEXT NOT NULL, " +
                    "[item_key] TEXT NOT NULL, " +
                    "[message_type] TEXT NOT NULL, " +
                    "[new_value] TEXT, " +
                    "[last_update] INTEGER NOT NULL, " +
                    "[last_update_id] INTEGER NOT NULL, " +
                    "FOREIGN KEY ([item_application_name], [item_key]) REFERENCES [item]([application_name], [key]) ON DELETE CASCADE ON UPDATE CASCADE);");

            // Ack: message_uuid, node_list, ack_nodes
            statement.execute("CREATE TABLE IF NOT EXISTS [ack] " +
                    "([message_uuid] TEXT NOT NULL," +
                    " [node_list] TEXT NOT NULL, [ack_nodes] TEXT, " +
                    "PRIMARY KEY([message_uuid]), " +
                    "FOREIGN KEY ([message_uuid]) REFERENCES [message]([uuid]) ON DELETE CASCADE ON UPDATE CASCADE);");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.getConnection() != null) {
                this.getConnection().close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
