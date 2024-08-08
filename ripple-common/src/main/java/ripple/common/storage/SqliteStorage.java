// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Zhen Tang
 */
public class SqliteStorage {
    private String fileName;
    private Connection connection;
    private SqliteItemService itemService;
    private SqliteMessageService messageService;
    private SqliteAckService ackService;

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

    public SqliteItemService getItemService() {
        return itemService;
    }

    private void setItemService(SqliteItemService itemService) {
        this.itemService = itemService;
    }

    public SqliteMessageService getMessageService() {
        return messageService;
    }

    private void setMessageService(SqliteMessageService messageService) {
        this.messageService = messageService;
    }

    public SqliteAckService getAckService() {
        return ackService;
    }

    private void setAckService(SqliteAckService ackService) {
        this.ackService = ackService;
    }

    public SqliteStorage(String fileName) {
        this.setFileName(fileName);
        this.setItemService(new SqliteItemService(this));
        this.setMessageService(new SqliteMessageService(this));
        this.setAckService(new SqliteAckService(this));
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
                    "PRIMARY KEY ([application_name], [key]));");

            // Message: uuid, item_application_name, item_key, message_type, base_message_uuid, atomic_operation, new_value, last_update, last_update_id
            statement.execute("CREATE TABLE IF NOT EXISTS [message] " +
                    "([uuid] TEXT NOT NULL, " +
                    "[item_application_name] TEXT NOT NULL, " +
                    "[item_key] TEXT NOT NULL, " +
                    "[message_type] TEXT NOT NULL, " +
                    "[base_message_uuid] TEXT, " +
                    "[atomic_operation] TEXT, " +
                    "[new_value] TEXT, " +
                    "[last_update] INTEGER NOT NULL, " +
                    "[last_update_id] INTEGER NOT NULL, " +
                    "PRIMARY KEY ([uuid]), " +
                    "FOREIGN KEY ([item_application_name], [item_key]) REFERENCES [item]([application_name], [key]) ON DELETE CASCADE ON UPDATE CASCADE);");

            // Ack: message_uuid, node_list, ack_nodes
            statement.execute("CREATE TABLE IF NOT EXISTS [ack] " +
                    "([message_uuid] TEXT NOT NULL, " +
                    "[node_list] TEXT NOT NULL, " +
                    "[ack_nodes] TEXT, " +
                    "PRIMARY KEY ([message_uuid]), " +
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
