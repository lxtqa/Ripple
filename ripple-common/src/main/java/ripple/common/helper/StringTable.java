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
public interface StringTable {
    String applicationName();

    String key();

    String value();

    String addConfig();

    String htmlLanguage();

    String operationSubmitted();

    String addSubscription();

    String messageTypeDelete();

    String messageTypeUpdate();

    String messageTypeIncrementalUpdate();

    String getConfig();

    String totalNumberOfConfiguration();

    String home();

    String configManagement();

    String modifyConfig();

    String incrementalUpdate();

    String removeConfig();

    String subscriptionManagement();

    String clientGetSubscription();

    String removeSubscription();

    String basicInformation();

    String connectedServer();

    String lineNumber();

    String serverIpAddress();

    String serverApiPort();

    String serverUiPort();

    String history();

    String clientIpAddress();

    String clientApiPort();

    String clientUiPort();

    String type();

    String baseVersion();

    String atomicOperation();

    String lastUpdate();

    String serverId();

    String clientNumberOfSubscription();

    String clientNumberOfServerConnection();

    String success();

    String error();

    String serverGetSubscription();

    String clusterManagement();

    String serverCluster();

    String connectedClients();

    String ackServer();

    String totalNumberOfClients();

    String totalNumberOfSubscriptions();

    String totalNumberOfServers();
}
