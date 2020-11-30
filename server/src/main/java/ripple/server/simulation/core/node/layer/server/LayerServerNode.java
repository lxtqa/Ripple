package ripple.server.simulation.core.node.layer.server;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.node.AbstractPeer;
import ripple.server.simulation.core.node.GlobalExecutor;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author qingzhou.sjq
 */
public class LayerServerNode extends AbstractNode {

    /**
     * clusterID为数组下标
     */
    List<InternalCluster> clusterList = new ArrayList<>();

    HashMap<UUID, List<UUID>> edges= new HashMap<>();

    /**
     * 根据目标边的UUID得到目标节点
     */
    HashMap<UUID, LayerPeer> nodes = new HashMap<>();

    List<LayerServlet> servlets;

    private Context context;

    private LayerPeer local;

    public LayerServerNode(Emulator emulator, Context context) {
        super(emulator, context);
        this.context = context;
        local = new LayerPeer(Util.buildAddress(getAddress(), getPort()), getUuid());
        Loggers.LAYER.info("init jetty server, local ip: {}", local.ip);
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        servlets = new ArrayList<>(8);

        LayerServlet publishServlet = new PublishServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(publishServlet), Constants.LAYER_PUB_PATH);
        servlets.add(publishServlet);

        LayerServlet onPublishServlet = new OnPublishServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(onPublishServlet), Constants.LAYER_ON_PUB_PATH);
        servlets.add(onPublishServlet);
    }

    @Override
    public void start() {
        initPeers();
        executor.register(new GetClusterAddress(), GlobalExecutor.ADDRESS_SERVER_UPDATE_INTERVAL_MS);
    }

    @Override
    public void initPeers() {
        List<AbstractPeer> peerListFromEngine = registryHelper.getServerListFromEngine();
        if (localPeerList != null && localPeerList.equals(peerListFromEngine)) {
            Loggers.LAYER.info("no need to update node list");
            return;
        }
        Loggers.LAYER.info("get new cluster config");
        localPeerList = peerListFromEngine;
        buildTopology(localPeerList);
    }



    private void buildTopology(List<AbstractPeer> updatedNodeList) {
        clusterList.clear();
        edges.clear();
        nodes.clear();
        List<LayerPeer> peers = new ArrayList<>();
        for (AbstractPeer node : updatedNodeList) {
            peers.add(new LayerPeer(node.ip, node.getUuid()));
            Loggers.LAYER.info("get new node: {}", node.ip);
        }

        int totalSize = updatedNodeList.size();
        if (totalSize == 0) {
            Loggers.LAYER.info("new node list is empty");
            return;
        }

        // 切分结群
        int internalClusterSize = (int)Math.ceil(Math.pow((double)totalSize, 1.0 / 3.0));
        Loggers.LAYER.info("divide result: {} node per cluster", internalClusterSize);
        boolean full = (totalSize % internalClusterSize) == 0;
        int divide = totalSize / internalClusterSize;
        int clusterNumber = full ? divide : divide + 1;
        for (int i = 0; i < clusterNumber; i++) {
            InternalCluster internalCluster = new InternalCluster();
            internalCluster.id = i;
            for (int j = 0; j < internalClusterSize; j++) {
                int index = j + i * internalClusterSize;
                if (index >= totalSize) {
                    break;
                }
                LayerPeer peer = peers.get(index);
                peer.clusterId = i;
                internalCluster.addNode(peer);
                nodes.put(peer.uuid, peer);
                if (peer.uuid.equals(local.uuid)) {
                    local.clusterId = i;
                }
            }
            clusterList.add(internalCluster);
        }

        // 建立连边
        for (int i = 0; i < clusterList.size() - 1; i++) {
            for (int j = i + 1; j < clusterList.size(); j++) {
                LayerPeer src = clusterList.get(i).getAndRemoveAvailablePeer();
                LayerPeer tar = clusterList.get(j).getAndRemoveAvailablePeer();
                addEdge(src.uuid, tar.uuid);
                addEdge(tar.uuid, src.uuid);
                src.degree++;
                tar.degree++;
                nodes.get(src.uuid).degree++;
                nodes.get(tar.uuid).degree++;
                clusterList.get(i).addNode(src);
                clusterList.get(j).addNode(tar);
            }
        }

        List<LayerPeer> internalNodes = new ArrayList<>(clusterList.get(local.clusterId).getPeerList());
        List<LayerPeer> externalNodes = new ArrayList<>();

        List<UUID> externalNodesUUID = edges.get(local.uuid);
        if (externalNodesUUID != null) {
            for (UUID localTarget : externalNodesUUID) {
                externalNodes.add(nodes.get(localTarget));
            }
        }

        for (LayerServlet servlet : servlets) {
            servlet.setInternalNodes(internalNodes);
            servlet.setExternalNodes(externalNodes);
            servlet.setLocal(local);
        }
        for (LayerPeer peer : internalNodes) {
            Loggers.LAYER.info("{} internal node: {}", local.ip, peer.ip);
        }
        for (LayerPeer peer : externalNodes) {
            Loggers.LAYER.info("{} external node: {}", local.ip, peer.ip);
        }
    }

    private void addEdge(UUID src, UUID tar) {
        List<UUID> targets = edges.get(src);
        if (targets == null) {
            targets = new ArrayList<>();
        }
        targets.add(tar);
        edges.put(src, targets);
    }
}
