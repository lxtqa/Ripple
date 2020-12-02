package ripple.server.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.AbstractNode;
import ripple.server.core.BaseServlet;
import ripple.server.core.ClientMetadata;
import ripple.server.core.ItemKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Zhen Tang
 */
public class GetSubscriptionServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetSubscriptionServlet.class);

    public GetSubscriptionServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[GetSubscriptionServlet] Receive GET request.");

        int count = 0;
        for (Set<ClientMetadata> clients : this.getNode().getSubscription().values()) {
            count += clients.size();
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ")
                .append("当前服务器节点中共检索到 <strong>")
                .append(count)
                .append("</strong> 条订阅信息。")
                .append("\n");
        stringBuilder.append("                </p>\n");

        if (count > 0) {
            stringBuilder.append("                <table class=\"table table-striped\">\n");
            stringBuilder.append("                    <thead>\n");
            stringBuilder.append("                    <tr>\n");
            stringBuilder.append("                        <th>序号</th>\n");
            stringBuilder.append("                        <th>应用名称</th>\n");
            stringBuilder.append("                        <th>键</th>\n");
            stringBuilder.append("                        <th>客户端地址</th>\n");
            stringBuilder.append("                        <th>客户端端口号</th>\n");
            stringBuilder.append("                    </tr>\n");
            stringBuilder.append("                    </thead>\n");
            stringBuilder.append("                    <tbody>\n");
            int i = 0;
            for (ItemKey itemKey : this.getNode().getSubscription().keySet()) {
                Set<ClientMetadata> clients = this.getNode().getSubscription().get(itemKey);
                if (clients.size() > 0) {
                    for (ClientMetadata metadata : clients) {
                        stringBuilder.append("                    <tr>\n");
                        stringBuilder.append("                        <td>")
                                .append(i + 1)
                                .append("</td>\n");
                        stringBuilder.append("                        <td>")
                                .append(itemKey.getApplicationName())
                                .append("</td>\n");
                        stringBuilder.append("                        <td>")
                                .append(itemKey.getKey())
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
                }
            }
            stringBuilder.append("                    </tbody>\n");
            stringBuilder.append("                </table>\n");
        }

        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Server - 查询订阅关系", "查询订阅关系", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
