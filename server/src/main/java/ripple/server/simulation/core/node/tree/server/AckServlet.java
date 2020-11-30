package ripple.server.simulation.core.node.tree.server;

import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qingzhou.sjq
 */
public class AckServlet extends TreeServlet {
    public AckServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    }
}
