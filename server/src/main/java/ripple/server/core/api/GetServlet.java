package ripple.server.core.api;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Item;
import ripple.common.Parameter;
import ripple.server.core.BaseServlet;
import ripple.server.core.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class GetServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetServlet.class);

    public GetServlet(Node node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String applicationName = request.getHeader(Parameter.APPLICATION_NAME);
        String key = request.getHeader("x-ripple-key");
        LOGGER.info("[GetServlet] Receive request: Application Name = {}, Key = {}.", applicationName, key);
        Item item = this.getNode().get(applicationName, key);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(this.getNode().getObjectMapper().writeValueAsString(item));
    }
}
