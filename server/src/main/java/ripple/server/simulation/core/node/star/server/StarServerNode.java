package ripple.server.simulation.core.node.star.server;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.node.AbstractPeer;
import ripple.server.simulation.core.node.GlobalExecutor;
import ripple.server.simulation.core.node.raft.server.RaftServlet;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuxiao.tz
 */
public class StarServerNode extends AbstractNode {

    private List<String> targetClient;

    private List<StarServlet> servlets;

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        servlets = new ArrayList<>(8);
        UpdateServlet updateServlet = new UpdateServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(updateServlet), Constants.STAR_UPDATE_PATH);
        servlets.add(updateServlet);
        SyncServlet syncServlet = new SyncServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(syncServlet), Constants.STAR_SERVER_SYNC_PATH);
        servlets.add(syncServlet);
    }

    public StarServerNode(Emulator emulator, Context context, List<String> addressList) {
        super(emulator, context);
        targetClient = addressList;
    }

    @Override
    public void start() {
        initPeers();
        executor.register(new GetClusterAddress(), GlobalExecutor.ADDRESS_SERVER_UPDATE_INTERVAL_MS);
        for (StarServlet starServlet : servlets) {
            starServlet.setTargetClient(targetClient);
        }
    }

    @Override
    public void initPeers() {
        List<AbstractPeer> peerListFromEngine = registryHelper.getServerListFromEngine();
        if (localPeerList != null && localPeerList.equals(peerListFromEngine)) {
            return;
        }
        Loggers.STAR.info("get new cluster config");
        localPeerList = peerListFromEngine;
        for (StarServlet starServlet : servlets) {
            starServlet.setServerPeers(localPeerList);
        }
    }
}
