package ripple.server.core.star;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.AbstractNode;
import ripple.server.core.BaseServlet;
import ripple.server.core.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class GetServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public GetServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getHeader("x-ripple-key");
        LOGGER.info("[GetServlet] Receive request: Key = " + key + ".");
        Item item = this.getNode().getStorage().get(key);
        if (item != null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK_200);
            response.getWriter().println(MAPPER.writeValueAsString(item));
        } else {
            response.sendError(HttpStatus.NOT_FOUND_404);
        }
    }
}
