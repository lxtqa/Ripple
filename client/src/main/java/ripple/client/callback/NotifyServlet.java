package ripple.client.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.entity.Item;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotifyServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private RippleClient rippleClient;

    public RippleClient getRippleClient() {
        return rippleClient;
    }

    public void setRippleClient(RippleClient rippleClient) {
        this.rippleClient = rippleClient;
    }

    public NotifyServlet(RippleClient rippleClient) {
        this.setRippleClient(rippleClient);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getHeader("x-ripple-key");
        String value = request.getHeader("x-ripple-value");
        Date lastUpdate = new Date(Long.parseLong(request.getHeader("x-ripple-last-update")));
        int lastUpdateServerId = Integer.parseInt(request.getHeader("x-ripple-last-update-server-id"));
        LOGGER.info("[NotifyServlet] Receive request: Key = " + key
                + ", Value = " + value
                + ", Last Update = " + SimpleDateFormat.getDateTimeInstance().format(lastUpdate)
                + ", Last Update Server Id = " + lastUpdateServerId);

        // Update local storage
        Item item = null;
        if (!this.getRippleClient().getStorage().containsKey(key)) {
            item = new Item();
            this.getRippleClient().getStorage().put(key, item);
        }
        synchronized (item = this.getRippleClient().getStorage().get(key)) {
            item.setKey(key);
            item.setValue(value);
            item.setLastUpdate(lastUpdate);
            item.setLastUpdateServerId(lastUpdateServerId);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(MAPPER.writeValueAsString(true));
    }
}
