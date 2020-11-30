package ripple.server.simulation.core.node.layer;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.node.NodeBuilder;
import ripple.server.simulation.core.node.layer.client.LayerClientNode;
import ripple.server.simulation.core.node.layer.server.LayerServerNode;

import java.util.List;

/**
 * @author qingzhou.sjq
 */
public class LayerNodeBuilder implements NodeBuilder {
    @Override
    public AbstractNode buildServerNode(Emulator emulator, Context context, List<String> targetClients) {
        return new LayerServerNode(emulator, context);
    }

    @Override
    public AbstractNode buildClientNode(Emulator emulator, Context context) {
        return new LayerClientNode(emulator, context);
    }
}
