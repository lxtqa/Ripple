package ripple.client.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.entity.NodeMetadata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class ServerInfoServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoServlet.class);

    public ServerInfoServlet(RippleClient client) {
        super(client);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[ServerInfoServlet] Receive GET request.");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ")
                .append("当前客户端节点共连接到了 <strong>")
                .append(this.getClient().getConnections().size())
                .append("</strong> 台服务器。")
                .append("\n");
        stringBuilder.append("                </p>\n");
        if (this.getClient().getSubscriptions().size() > 0) {
            stringBuilder.append("                <table class=\"table table-striped\">\n");
            stringBuilder.append("                    <thead>\n");
            stringBuilder.append("                    <tr>\n");
            stringBuilder.append("                        <th>序号</th>\n");
            stringBuilder.append("                        <th>服务器ID</th>\n");
            stringBuilder.append("                        <th>服务器IP地址</th>\n");
            stringBuilder.append("                        <th>服务器端口号</th>\n");
            stringBuilder.append("                    </tr>\n");
            stringBuilder.append("                    </thead>\n");
            stringBuilder.append("                    <tbody>\n");

            int i = 0;
            for (NodeMetadata nodeMetadata : this.getClient().getConnections().keySet()) {
                stringBuilder.append("                    <tr>\n");
                stringBuilder.append("                        <td>")
                        .append(i + 1)
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(nodeMetadata.getId())
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(nodeMetadata.getAddress())
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(nodeMetadata.getPort())
                        .append("</td>\n");
                stringBuilder.append("                    </tr>\n");
                i++;
            }

            stringBuilder.append("                    </tbody>\n");
            stringBuilder.append("                </table>\n");
        }

        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Client - 已连接的服务器", "已连接的服务器", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
