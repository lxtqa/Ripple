package ripple.client.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.core.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class RemoveConfigServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveConfigServlet.class);

    public RemoveConfigServlet(RippleClient client) {
        super(client);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[RemoveConfigServlet] Get");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ").append("删除配置").append("\n");
        stringBuilder.append("                </p>\n");
        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Client - 删除配置", "删除配置", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
