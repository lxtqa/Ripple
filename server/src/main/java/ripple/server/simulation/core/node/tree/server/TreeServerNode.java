package ripple.server.simulation.core.node.tree.server;

import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
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

/**
 * @author fuxiao.tz
 */
public class TreeServerNode extends AbstractNode {

    private List<String> targetClient;

    List<TreePeer> peerList = new ArrayList<>();

    List<TreeServlet> servlets;

    private TreePeer local;

    public TreeServerNode(Emulator emulator, Context context, List<String> addressList) {
        super(emulator, context);
        targetClient = new ArrayList<>(addressList);
        local = new TreePeer(Util.buildAddress(getAddress(), getPort()), getUuid());
    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        servlets = new ArrayList<>();

        TreeServlet publishServlet = new PublishServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(publishServlet), Constants.TREE_PUB_PATH);
        servlets.add(publishServlet);

        TreeServlet onPublishServlet = new OnPublishServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(onPublishServlet), Constants.TREE_ON_PUB_PATH);
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
            return;
        }
        Loggers.TREE.info("get new cluster config");
        localPeerList = peerListFromEngine;
        buildTopology(localPeerList);
    }

    private void buildTopology(List<AbstractPeer> updatedNodeList) {
        /**
         * 传播过程中拓扑改变，可能导致之前的消息丢失，此时需要确认业务需求。
         */
        peerList.clear();
        for (int i = 0; i < updatedNodeList.size(); i++) {
            AbstractPeer peer = updatedNodeList.get(i);
            TreePeer peerWithIndex = new TreePeer(peer.ip, peer.uuid);
            peerWithIndex.index = i;
            peerList.add(peerWithIndex);
            if (peer.uuid.equals(getUuid())) {
                local.index = i;
            }
        }

        for (TreeServlet servlet : servlets) {
            servlet.setLocal(local);
            servlet.setPeersAndTarget(peerList);
        }
    }

}
