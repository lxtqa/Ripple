package ripple.server.simulation.core.node.star.client;

import ripple.server.simulation.core.node.AbstractNode;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.utils.Constants;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author fuxiao.tz
 */
public class StarClientNode extends AbstractNode {

    @Override
    public void registerHandlers(ServletContextHandler servletContextHandler) {
        ReceiveServlet receiveServlet = new ReceiveServlet(getEmulator(), getContext());
        servletContextHandler.addServlet(new ServletHolder(receiveServlet), Constants.STAR_UPDATE_PATH);
    }

    public StarClientNode(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    public void start() {

    }

    @Override
    public void initPeers() {

    }

}
