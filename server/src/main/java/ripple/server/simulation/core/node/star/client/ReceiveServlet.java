package ripple.server.simulation.core.node.star.client;

import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qingzhou.sjq
 */
public class ReceiveServlet extends BaseServlet{
    public ReceiveServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
