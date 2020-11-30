package ripple.server.simulation.core.node.layer.client;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * @author qingzhou.sjq
 */
public class LayerClientNode extends AbstractNode {

    public LayerClientNode(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    public void start() {

    }

    @Override
    public void initPeers() {

    }

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {

    }
}
