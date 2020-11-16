package ripple.server.star;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.AbstractNode;
import ripple.server.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;

public class UnsubscribeServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(UnsubscribeServlet.class);

    public UnsubscribeServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String callbackAddress = request.getHeader("x-ripple-callback-address");
            int callbackPort = Integer.parseInt(request.getHeader("x-ripple-callback-port"));
            String key = request.getHeader("x-ripple-key");
            logger.info("[UnsubscribeServlet] Receive request: Callback Address = "
                    + callbackAddress + "; Callback Port = " + callbackPort + "; Key = " + key + ".");
            this.getNode().unsubscribe(callbackAddress, callbackPort, key);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK_200);
            response.getWriter().println("Success");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
