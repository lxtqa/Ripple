package ripple.server.star;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.AbstractNode;
import ripple.server.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.eclipse.jetty.http.HttpStatus;

public class SubscribeServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeServlet.class);

    public SubscribeServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String callbackAddress = request.getHeader("x-ripple-callback-address");
            int callbackPort = Integer.parseInt(request.getHeader("x-ripple-callback-port"));
            String key = request.getHeader("x-ripple-key");
            logger.info("[SubscribeServlet] Receive request: Callback Address = "
                    + callbackAddress + "; Callback Port = " + callbackPort + "; Key = " + key + ".");
            this.getNode().subscribe(callbackAddress, callbackPort, key);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK_200);
            response.getWriter().println("Success");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
