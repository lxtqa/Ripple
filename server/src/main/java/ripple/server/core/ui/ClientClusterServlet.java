package ripple.server.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.Node;
import ripple.server.core.BaseServlet;
import ripple.server.core.ClientMetadata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class ClientClusterServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientClusterServlet.class);

    public ClientClusterServlet(Node node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[ClientClusterServlet] Receive GET request.");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ")
                .append("共有 <strong>")
                .append(this.getNode().getConnectedClients().size())
                .append("</strong> 个客户端连接到当前服务器节点并订阅配置。")
                .append("\n");
        stringBuilder.append("                </p>\n");
        if (this.getNode().getConnectedClients().size() > 0) {
            stringBuilder.append("                <table class=\"table table-striped\">\n");
            stringBuilder.append("                    <thead>\n");
            stringBuilder.append("                    <tr>\n");
            stringBuilder.append("                        <th>序号</th>\n");
            stringBuilder.append("                        <th>客户端地址</th>\n");
            stringBuilder.append("                        <th>客户端端口号</th>\n");
            stringBuilder.append("                    </tr>\n");
            stringBuilder.append("                    </thead>\n");
            stringBuilder.append("                    <tbody>\n");

            int i = 0;
            for (ClientMetadata metadata : this.getNode().getConnectedClients()) {
                stringBuilder.append("                    <tr>\n");
                stringBuilder.append("                        <td>")
                        .append(i + 1)
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(metadata.getAddress())
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(metadata.getPort())
                        .append("</td>\n");
                stringBuilder.append("                    </tr>\n");
                i++;
            }

            stringBuilder.append("                    </tbody>\n");
            stringBuilder.append("                </table>\n");
        }

        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Server - 已连接的客户端", "已连接的客户端", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
