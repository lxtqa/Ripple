package ripple.client.core;

import ripple.client.RippleClient;

import javax.servlet.http.HttpServlet;

/**
 * @author Zhen Tang
 */
public class BaseServlet extends HttpServlet {
    private RippleClient client;

    public BaseServlet(RippleClient client) {
        this.setClient(client);
    }

    public RippleClient getClient() {
        return client;
    }

    private void setClient(RippleClient client) {
        this.client = client;
    }
}
