package ripple.server.core.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.Node;
import ripple.server.core.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class SubscribeServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public SubscribeServlet(Node node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String applicationName = request.getHeader("x-ripple-application-name");
        String callbackAddress = request.getHeader("x-ripple-callback-address");
        int callbackPort = Integer.parseInt(request.getHeader("x-ripple-callback-port"));
        String key = request.getHeader("x-ripple-key");
        LOGGER.info("[SubscribeServlet] Receive POST request. Callback Address = {}, Callback Port = {}, Application Name = {}, Key = {}."
                , callbackAddress, callbackPort, applicationName, key);
        this.getNode().subscribe(callbackAddress, callbackPort, applicationName, key);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(true));
    }
}
