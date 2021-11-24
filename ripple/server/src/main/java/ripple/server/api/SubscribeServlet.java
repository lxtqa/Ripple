package ripple.server.api;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Parameter;
import ripple.server.ui.BaseServlet;
import ripple.server.core.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class SubscribeServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeServlet.class);

    public SubscribeServlet(Node node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String applicationName = request.getHeader(Parameter.APPLICATION_NAME);
        String key = request.getHeader(Parameter.KEY);
        String callbackAddress = request.getHeader(Parameter.CALLBACK_ADDRESS);
        int callbackPort = Integer.parseInt(request.getHeader(Parameter.CALLBACK_PORT));

        LOGGER.info("[SubscribeServlet] Receive POST request. Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
                , callbackAddress, callbackPort, applicationName, key);
        this.getNode().subscribe(callbackAddress, callbackPort, applicationName, key);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(this.getNode().getObjectMapper().writeValueAsString(true));
    }
}
