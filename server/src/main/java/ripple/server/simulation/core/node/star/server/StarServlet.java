package ripple.server.simulation.core.node.star.server;

import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractPeer;

import java.util.List;

/**
 * @author qingzhou.sjq
 */
public class StarServlet extends BaseServlet{

    public List<String> getTargetClient() {
        return targetClient;
    }

    public void setTargetClient(List<String> targetClient) {
        this.targetClient = targetClient;
    }

    List<String> targetClient;

    public void setServerPeers(List<AbstractPeer> serverPeers) {
        this.serverPeers = serverPeers;
    }

    List<AbstractPeer> serverPeers;

    public StarServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }
}
