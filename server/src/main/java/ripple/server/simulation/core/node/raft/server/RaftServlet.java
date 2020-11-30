package ripple.server.simulation.core.node.raft.server;

import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;

import java.util.List;

/**
 * @author qingzhou.sjq
 */
public class RaftServlet extends BaseServlet{

    PeerSet peers;

    public List<String> getTargetClient() {
        return targetClient;
    }

    public void setTargetClient(List<String> targetClient) {
        this.targetClient = targetClient;
    }

    List<String> targetClient;

    public RaftServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    public PeerSet getPeers() {
        return peers;
    }

    public void setPeers(PeerSet peers) {
        this.peers = peers;
    }
}
