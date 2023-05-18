// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.ModHashing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 以默认端口启动，数据库位于同目录下：java -jar -Did="1" -Dprotocol="tree" -Dbranch="3" -Dnodes="[id]:[address]:[port],2:192.168.1.3:2345" ripple-server.jar
        // 以指定端口和数据库文件位置启动：java -DapiPort=xxx -DuiPort=xxx -DstorageLocation=xxx -jar ripple-server.jar

        String storageLocation = System.getProperty("storageLocation");
        if (storageLocation == null) {
            String defaultStorageLocation = "database.sqlite";
            LOGGER.info("[Main] Missing parameter: storageLocation (-DstorageLocation). Using default: " + defaultStorageLocation);
            storageLocation = defaultStorageLocation;
        }

        String apiPort = System.getProperty("apiPort");
        if (apiPort == null) {
            LOGGER.info("[Main] Missing parameter: API port (-DapiPort). Using random port.");
            apiPort = "0";
        }

        String uiPort = System.getProperty("uiPort");
        if (uiPort == null) {
            LOGGER.info("[Main] Missing parameter: UI port (-DuiPort). Using random port.");
            uiPort = "0";
        }

        String protocol = System.getProperty("protocol");
        if (protocol == null) {
            LOGGER.info("[Main] Missing parameter: protocol (-Dprotocol). Using STAR protocol.");
            protocol = "star";
        }

        String id = System.getProperty("id");
        if (id == null) {
            LOGGER.info("[Main] Missing parameter: id (-Did).");
            return;
        }

        String nodes = System.getProperty("nodes");
        if (nodes == null) {
            LOGGER.info("[Main] Missing parameter: nodes (-Dnodes).");
            return;
        }

        RippleServer server = null;
        if (protocol.equals("tree")) {
            String branch = System.getProperty("branch");
            if (branch == null) {
                String defaultBranch = "3";
                LOGGER.info("[Main] Missing parameter: branch for TREE protocol (-Dbranch). Using default: " + defaultBranch);
                branch = defaultBranch;
            }
            server = RippleServer.treeProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(apiPort), Integer.parseInt(uiPort), Integer.parseInt(branch));
        } else if (protocol.equals("star")) {
            server = RippleServer.starProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(apiPort), Integer.parseInt(uiPort));
        } else if (protocol.equals("hashing")) {
            String divisor = System.getProperty("divisor");
            String candidateCount = System.getProperty("candidateCount");
            if (divisor == null) {
                String defaultDivisor = "200";
                LOGGER.info("[Main] Missing parameter: divisor for HASHING protocol (-Ddivisor). Using default: " + defaultDivisor);
                divisor = defaultDivisor;
            }
            if (candidateCount == null) {
                String defaultCandidateCount = "3";
                LOGGER.info("[Main] Missing parameter: candidateCount for HASHING protocol (-DcandidateCount). Using default: " + defaultCandidateCount);
                candidateCount = defaultCandidateCount;
            }
            server = RippleServer.hashingBasedProtocol(Integer.parseInt(id), storageLocation, Integer.parseInt(apiPort)
                    , Integer.parseInt(uiPort), new ModHashing(Integer.parseInt(candidateCount), Integer.parseInt(divisor)));
        }

        if (server != null) {
            List<NodeMetadata> nodeList = new ArrayList<>();
            String[] address = nodes.split(",");
            for (String item : address) {
                String[] result = item.split(":");
                NodeMetadata metadata = new NodeMetadata(Integer.parseInt(result[0]), result[1], Integer.parseInt(result[2]));
                nodeList.add(metadata);
            }
            server.getNode().setNodeList(nodeList);
            server.start();
            LOGGER.info("[Main] Ripple Server started. The API port is {}. The UI port is {}. The storage location is {}."
                    , server.getApiPort(), server.getUiPort(), server.getNode().getStorage().getFileName());
            LOGGER.info("[Main] Nodes in the cluster:");
            for (NodeMetadata nodeMetadata : server.getNode().getNodeList()) {
                LOGGER.info("[Main] --> Id = {}, Address = {}, API port = {}", nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
            }
            server.initCluster(server.getNode().getNodeList());
        }
    }
}
