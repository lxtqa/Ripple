package ripple.server.core.ui;

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
public class NewConfigServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewConfigServlet.class);

    public NewConfigServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[NewConfigServlet] Get");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ").append("添加配置").append("\n");
        stringBuilder.append("                </p>\n");
        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Server - 添加配置", "添加配置", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
