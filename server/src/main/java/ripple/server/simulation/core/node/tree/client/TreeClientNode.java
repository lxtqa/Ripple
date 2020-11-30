package ripple.server.simulation.core.node.tree.client;

import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * @author fuxiao.tz
 */
public class TreeClientNode extends AbstractNode {

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {

    }

    public TreeClientNode(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    public void start() {

    }

    @Override
    public void initPeers() {

    }

}
