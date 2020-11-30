package ripple.server.simulation.core.node.raft;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.node.NodeBuilder;
import ripple.server.simulation.core.node.raft.client.RaftClientNode;
import ripple.server.simulation.core.node.raft.server.RaftServerNode;

import java.util.List;

/**
 * @author fuxiao.tz
 */
public class RaftNodeBuilder implements NodeBuilder {

    @Override
    public AbstractNode buildServerNode(Emulator emulator, Context context, List<String>targetClients) {
        return new RaftServerNode(emulator, context, targetClients);
    }

    @Override
    public AbstractNode buildClientNode(Emulator emulator, Context context) {
        return new RaftClientNode(emulator, context);
    }
}
