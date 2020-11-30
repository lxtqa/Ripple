package ripple.server.simulation.core.node.star;

import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.NodeBuilder;
import ripple.server.simulation.core.node.star.client.StarClientNode;
import ripple.server.simulation.core.node.star.server.StarServerNode;

import java.util.List;

/**
 * @author fuxiao.tz
 */
public class StarNodeBuilder implements NodeBuilder {

    @Override
    public AbstractNode buildServerNode(Emulator emulator, Context context, List<String> targetClients) {
        return new StarServerNode(emulator, context, targetClients);
    }

    @Override
    public AbstractNode buildClientNode(Emulator emulator, Context context) {
        return new StarClientNode(emulator, context);
    }
}
