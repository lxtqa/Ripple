package ripple.client.core.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.core.BaseServlet;
import ripple.client.core.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zhen Tang
 */
public class NotifyServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public NotifyServlet(RippleClient client) {
        super(client);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result = false;
        String type = request.getHeader("x-ripple-type");
        if (type.equals(NotifyType.UPDATE)) {
            String applicationName = request.getHeader("x-ripple-application-name");
            String key = request.getHeader("x-ripple-key");
            String value = request.getHeader("x-ripple-value");
            Date lastUpdate = new Date(Long.parseLong(request.getHeader("x-ripple-last-update")));
            int lastUpdateServerId = Integer.parseInt(request.getHeader("x-ripple-last-update-server-id"));
            LOGGER.info("[NotifyServlet] Receive request: Type = {}, Application Name = {}, Key = {}, Value = {}, Last Update = {}, Last Update Server Id = {}."
                    , type, applicationName, key, value, SimpleDateFormat.getDateTimeInstance().format(lastUpdate), lastUpdateServerId);

            // Update local storage
            Item item = this.getClient().getStorage().get(applicationName, key);
            if (item == null) {
                item = new Item();
            }
            synchronized (this) {
                item.setApplicationName(applicationName);
                item.setKey(key);
                item.setValue(value);
                item.setLastUpdate(lastUpdate);
                item.setLastUpdateServerId(lastUpdateServerId);
            }
            this.getClient().getStorage().put(item);

            result = true;
        } else if (type.equals(NotifyType.DELETE)) {
            String applicationName = request.getHeader("x-ripple-application-name");
            String key = request.getHeader("x-ripple-key");
            LOGGER.info("[NotifyServlet] Receive request: Type = {}, Application Name = {}, Key = {}."
                    , type, applicationName, key);

            Item item = this.getClient().getStorage().get(applicationName, key);
            if (item != null) {
                this.getClient().getStorage().delete(item);
            }

            result = true;
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(result));
    }
}
