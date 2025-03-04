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

import ripple.common.entity.NodeMetadata;
import ripple.common.hashing.Hashing;
import ripple.server.core.Node;
import ripple.server.core.overlay.GossipOverlay;
import ripple.server.core.overlay.StarOverlay;
import ripple.server.core.overlay.expander.ExpanderOverlay;
import ripple.server.core.overlay.hashing.HashingBasedOverlay;
import ripple.server.core.overlay.tree.TreeOverlay;

import java.util.List;

/**
 * @author Zhen Tang
 */
public class RippleServer {
    private Node node;

    private RippleServer(Node node) {
        this.setNode(node);
    }

    public static RippleServer starProtocol(int id, String storageLocation) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation));
    }

    public static RippleServer starProtocol(int id, String storageLocation, int apiPort, int uiPort) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation, apiPort, uiPort));
    }

    public static RippleServer starProtocol(int id, String storageLocation, int apiPort, int uiPort, String language) {
        return new RippleServer(new Node(id, new StarOverlay(), storageLocation, apiPort, uiPort,language));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int branch) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int apiPort, int uiPort, int branch) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation, apiPort, uiPort));
    }

    public static RippleServer treeProtocol(int id, String storageLocation, int apiPort, int uiPort, int branch, String language) {
        return new RippleServer(new Node(id, new TreeOverlay(branch), storageLocation, apiPort, uiPort, language));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int scale) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int apiPort, int uiPort, int scale) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation, apiPort, uiPort));
    }

    public static RippleServer expanderProtocol(int id, String storageLocation, int apiPort, int uiPort, int scale, String language) {
        return new RippleServer(new Node(id, new ExpanderOverlay(scale), storageLocation, apiPort, uiPort, language));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int fanout) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int apiPort, int uiPort, int fanout) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation, apiPort, uiPort));
    }

    public static RippleServer gossipProtocol(int id, String storageLocation, int apiPort, int uiPort, int fanout, String language) {
        return new RippleServer(new Node(id, new GossipOverlay(fanout), storageLocation, apiPort, uiPort , language));
    }

    public static RippleServer hashingBasedProtocol(int id, String storageLocation, Hashing hashing) {
        return new RippleServer(new Node(id, new HashingBasedOverlay(hashing), storageLocation));
    }

    public static RippleServer hashingBasedProtocol(int id, String storageLocation, int apiPort, int uiPort, Hashing hashing) {
        return new RippleServer(new Node(id, new HashingBasedOverlay(hashing), storageLocation, apiPort, uiPort));
    }

    public static RippleServer hashingBasedProtocol(int id, String storageLocation, int apiPort, int uiPort, Hashing hashing, String language) {
        return new RippleServer(new Node(id, new HashingBasedOverlay(hashing), storageLocation, apiPort, uiPort, language));
    }

    public Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public boolean start() {
        return this.getNode().start();
    }

    public boolean stop() {
        return this.getNode().stop();
    }

    public boolean isRunning() {
        return this.getNode().isRunning();
    }

    public int getId() {
        return this.getNode().getId();
    }

    public String getAddress() {
        return this.getNode().getAddress();
    }

    public int getApiPort() {
        return this.getNode().getApiPort();
    }

    public int getUiPort() {
        return this.getNode().getUiPort();
    }

    public void initCluster(List<NodeMetadata> nodeList) {
        this.getNode().initCluster(nodeList);
    }
}
