package ripple.server.simulation.core.node.raft.client;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.core.node.AbstractNode;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * @author fuxiao.tz
 */
public class RaftClientNode extends AbstractNode {

    @Override
    public void registerHandlers(ServletContextHandler servletHandler) {

    }

    public RaftClientNode(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    public void start() {

    }

    @Override
    public void initPeers() {

    }

}
