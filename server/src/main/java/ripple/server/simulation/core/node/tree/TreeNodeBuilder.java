package ripple.server.simulation.core.node.tree;

import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.NodeBuilder;
import ripple.server.simulation.core.node.tree.client.TreeClientNode;
import ripple.server.simulation.core.node.tree.server.TreeServerNode;

import java.util.List;

public class TreeNodeBuilder implements NodeBuilder {

    @Override
    public AbstractNode buildServerNode(Emulator emulator, Context context, List<String> targetClients) {
        return new TreeServerNode(emulator, context, targetClients);
    }

    @Override
    public AbstractNode buildClientNode(Emulator emulator, Context context) {
        return new TreeClientNode(emulator, context);
    }
}