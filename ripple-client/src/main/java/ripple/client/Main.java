// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.core.HashingBasedSelector;
import ripple.client.core.LoadBalancedSelector;
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

        String nodeSelector = System.getProperty("nodeSelector");
        if (nodeSelector == null) {
            LOGGER.info("[Main] Missing parameter: nodeSelector (-DnodeSelector). Using HASHING selector.");
            nodeSelector = "hashing";
        }
        if (!nodeSelector.equals("hashing") && !nodeSelector.equals("loadbalance")) {
            LOGGER.info("[Main] Incorrect parameter value: nodeSelector (-DnodeSelector). Allowed: hashing, loadbalance. Using HASHING selector.");
            nodeSelector = "hashing";
        }

        String nodes = System.getProperty("nodes");
        if (nodes == null) {
            LOGGER.info("[Main] Missing parameter: nodes (-Dnodes).");
            return;
        }

        List<NodeMetadata> nodeList = new ArrayList<>();
        String[] address = nodes.split(",");
        for (String item : address) {
            String[] result = item.split(":");
            NodeMetadata metadata = new NodeMetadata(Integer.parseInt(result[0]), result[1], Integer.parseInt(result[2]));
            nodeList.add(metadata);
        }

        RippleClient client = null;
        if (nodeSelector.equals("hashing")) {
            String divisor = System.getProperty("divisor");
            String candidateCount = System.getProperty("candidateCount");
            if (divisor == null) {
                String defaultDivisor = "200";
                LOGGER.info("[Main] Missing parameter: divisor for HASHING selector (-Ddivisor). Using default: " + defaultDivisor);
                divisor = defaultDivisor;
            }
            if (candidateCount == null) {
                String defaultCandidateCount = "3";
                LOGGER.info("[Main] Missing parameter: candidateCount for HASHING selector (-DcandidateCount). Using default: " + defaultCandidateCount);
                candidateCount = defaultCandidateCount;
            }
            client = new RippleClient(nodeList, new HashingBasedSelector(new ModHashing(Integer.parseInt(candidateCount), Integer.parseInt(divisor))), storageLocation);
        } else if (nodeSelector.equals("loadbalance")) {
            String divisor = System.getProperty("divisor");
            String candidateCount = System.getProperty("candidateCount");
            String backupRatio = System.getProperty("backupRatio");
            String cpuThreshold = System.getProperty("cpuThreshold");
            if (divisor == null) {
                String defaultDivisor = "200";
                LOGGER.info("[Main] Missing parameter: divisor for LOAD_BALANCED selector (-Ddivisor). Using default: " + defaultDivisor);
                divisor = defaultDivisor;
            }
            if (candidateCount == null) {
                String defaultCandidateCount = "3";
                LOGGER.info("[Main] Missing parameter: candidateCount for LOAD_BALANCED selector (-DcandidateCount). Using default: " + defaultCandidateCount);
                candidateCount = defaultCandidateCount;
            }
            if (backupRatio == null) {
                String defaultBackupRatio = "0.5";
                LOGGER.info("[Main] Missing parameter: backupRatio for LOAD_BALANCED selector (-DbackupRatio). Using default: " + defaultBackupRatio);
                backupRatio = defaultBackupRatio;
            }
            if (cpuThreshold == null) {
                String defaultCpuThreshold = "0.8";
                LOGGER.info("[Main] Missing parameter: cpuThreshold for LOAD_BALANCED selector (-DcpuThreshold). Using default: " + defaultCpuThreshold);
                cpuThreshold = defaultCpuThreshold;
            }
            LoadBalancedSelector selector = new LoadBalancedSelector(new ModHashing(Integer.parseInt(candidateCount), Integer.parseInt(divisor))
                    , Double.parseDouble(backupRatio), Double.parseDouble(cpuThreshold));
            client = new RippleClient(nodeList, selector, storageLocation);
            selector.setRippleClient(client);
        }
        client.setUiPort(Integer.parseInt(uiPort));
        client.setApiPort(Integer.parseInt(apiPort));
        client.start();
        LOGGER.info("[Main] Ripple Client started. The API port is {}. The UI port is {}. The storage location is {}."
                , client.getApiPort(), client.getUiPort(), client.getStorage().getLocation());
        LOGGER.info("[Main] Nodes in the server cluster:");
        for (NodeMetadata nodeMetadata : client.getNodeList()) {
            LOGGER.info("[Main] --> Id = {}, Address = {}, API port = {}", nodeMetadata.getId(), nodeMetadata.getAddress(), nodeMetadata.getPort());
        }
    }
}
