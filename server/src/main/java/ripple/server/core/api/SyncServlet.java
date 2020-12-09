package ripple.server.core.api;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.BaseServlet;
import ripple.server.core.Node;
import ripple.server.core.SyncType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zhen Tang
 */
public class SyncServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncServlet.class);

    public SyncServlet(Node node) {
        super(node);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result = false;
        String type = request.getHeader("x-ripple-type");
        if (type.equals(SyncType.UPDATE)) {
            String applicationName = request.getHeader("x-ripple-application-name");
            String key = request.getHeader("x-ripple-key");
            String value = request.getHeader("x-ripple-value");
            Date lastUpdate = new Date(Long.parseLong(request.getHeader("x-ripple-last-update")));
            int lastUpdateServerId = Integer.parseInt(request.getHeader("x-ripple-last-update-server-id"));
            LOGGER.info("[SyncServlet] Receive POST request. Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}, Last Update Server Id = {}."
                    , type, applicationName, key, value, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);
            result = this.getNode().onSyncUpdateReceived(applicationName, key, value, lastUpdate, lastUpdateServerId);
        } else if (type.equals(SyncType.DELETE)) {
            String applicationName = request.getHeader("x-ripple-application-name");
            String key = request.getHeader("x-ripple-key");
            Date lastUpdate = new Date(Long.parseLong(request.getHeader("x-ripple-last-update")));
            int lastUpdateServerId = Integer.parseInt(request.getHeader("x-ripple-last-update-server-id"));
            LOGGER.info("[SyncServlet] Receive POST request. Tyep = {}, Application Name = {}, Key = {}, Last Update = {}, Last Update Server Id = {}."
                    , type, applicationName, key, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);
            result = this.getNode().onSyncDeleteReceived(applicationName, key, lastUpdate, lastUpdateServerId);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(this.getNode().getObjectMapper().writeValueAsString(result));
    }
}
