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
public class PutServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(PutServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PutServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String applicationName = request.getHeader("x-ripple-application-name");
        String key = request.getHeader("x-ripple-key");
        String value = request.getHeader("x-ripple-value");
        LOGGER.info("[PutServlet] Receive POST request. Application Name = {}, Key = {}, Value = {}."
                , applicationName, key, value);

        boolean result = this.getNode().put(applicationName, key, value);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(result));
    }
}
