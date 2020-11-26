package ripple.server.core.star;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.AbstractNode;
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

    public SubscribeServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String callbackAddress = request.getHeader("x-ripple-callback-address");
        int callbackPort = Integer.parseInt(request.getHeader("x-ripple-callback-port"));
        String key = request.getHeader("x-ripple-key");
        LOGGER.info("[SubscribeServlet] Receive request: Callback Address = "
                + callbackAddress + "; Callback Port = " + callbackPort + "; Key = " + key + ".");
        this.getNode().subscribe(callbackAddress, callbackPort, key);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(true));
    }
}
