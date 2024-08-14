// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.helper;

/**
 * @author Zhen Tang
 */
public class EnglishStringTable implements StringTable {
    @Override
    public String applicationName() {
        return "Application Name";
    }

    @Override
    public String key() {
        return "Key";
    }

    @Override
    public String value() {
        return "Value";
    }

    @Override
    public String addConfig() {
        return "Add Configuration";
    }

    @Override
    public String htmlLanguage() {
        return "en-US";
    }

    @Override
    public String operationSubmitted() {
        return "The operation was successfully submitted.";
    }

    @Override
    public String addSubscription() {
        return "Add Subscription";
    }

    @Override
    public String messageTypeDelete() {
        return "Delete";
    }

    @Override
    public String messageTypeUpdate() {
        return "Update";
    }

    @Override
    public String messageTypeIncrementalUpdate() {
        return "Incremental Update";
    }

    @Override
    public String getConfig() {
        return "Get Configuration";
    }

    @Override
    public String totalNumberOfConfiguration() {
        return "The total number of configurations in local storage is";
    }

    @Override
    public String home() {
        return "Home";
    }

    @Override
    public String configManagement() {
        return "Configuration Management";
    }

    @Override
    public String modifyConfig() {
        return "Modify Configuration";
    }

    @Override
    public String incrementalUpdate() {
        return "Incremental Update";
    }

    @Override
    public String removeConfig() {
        return "Remove Configuration";
    }

    @Override
    public String subscriptionManagement() {
        return "Subscription Management";
    }

    @Override
    public String clientGetSubscription() {
        return "Get Subscription";
    }

    @Override
    public String removeSubscription() {
        return "Remove Subscription";
    }

    @Override
    public String basicInformation() {
        return "Basic Information";
    }

    @Override
    public String connectedServer() {
        return "Connected Server";
    }

    @Override
    public String lineNumber() {
        return "No.";
    }

    @Override
    public String serverIpAddress() {
        return "Server IP Address: ";
    }

    @Override
    public String serverApiPort() {
        return "Server API Port: ";
    }

    @Override
    public String serverUiPort() {
        return "Server UI Port: ";
    }

    @Override
    public String history() {
        return "History";
    }

    @Override
    public String clientIpAddress() {
        return "Client IP Address: ";
    }

    @Override
    public String clientApiPort() {
        return "Client API Port: ";
    }

    @Override
    public String clientUiPort() {
        return "Client UI Port: ";
    }

    @Override
    public String type() {
        return "Type";
    }

    @Override
    public String baseVersion() {
        return "Base Version";
    }

    @Override
    public String atomicOperation() {
        return "Atomic Operation";
    }

    @Override
    public String lastUpdate() {
        return "Last Update";
    }

    @Override
    public String serverId() {
        return "Server ID";
    }

    @Override
    public String clientNumberOfSubscription() {
        return "The total number of subscriptions by this client is";
    }

    @Override
    public String clientNumberOfServerConnection() {
        return "The total number of server connections by this client is";
    }

    @Override
    public String success() {
        return "The operation completed successfully.";
    }

    @Override
    public String error() {
        return "An error occurred. Please try again.";
    }

    @Override
    public String serverGetSubscription() {
        return "Get Subscription";
    }

    @Override
    public String clusterManagement() {
        return "Cluster Management";
    }

    @Override
    public String serverCluster() {
        return "Server Cluster";
    }

    @Override
    public String connectedClients() {
        return "Connected Clients";
    }

    @Override
    public String ackServer() {
        return "Acknowledged Servers";
    }

    @Override
    public String totalNumberOfClients() {
        return "The total number of clients connected to this server and added subscriptions is";
    }

    @Override
    public String totalNumberOfSubscriptions() {
        return "The total number of subscriptions in this server is";
    }

    @Override
    public String totalNumberOfServers() {
        return "The total number of servers in this cluster is";
    }
}
