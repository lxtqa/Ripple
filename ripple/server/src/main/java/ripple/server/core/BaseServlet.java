package ripple.server.core;

import javax.servlet.http.HttpServlet;

/**
 * @author Zhen Tang
 */
public class BaseServlet extends HttpServlet {
    private Node node;

    public BaseServlet(Node node) {
        this.setNode(node);
    }

    public Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

}
