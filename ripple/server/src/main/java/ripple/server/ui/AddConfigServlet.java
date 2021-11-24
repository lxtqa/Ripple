package ripple.server.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.Endpoint;
import ripple.server.core.Node;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class AddConfigServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddConfigServlet.class);

    public AddConfigServlet(Node node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[AddConfigServlet] Receive GET request.");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <form action=\"").append(Endpoint.UI_ADD_CONFIG).append("\" method=\"post\">\n");
        stringBuilder.append("                    <div class=\"form-group\">\n");
        stringBuilder.append("                        <label for=\"applicationName\">应用名称</label>\n");
        stringBuilder.append("                        <input id=\"applicationName\" name=\"applicationName\" class=\"form-control\" type=\"text\" />\n");
        stringBuilder.append("                    </div>\n");
        stringBuilder.append("                    <div class=\"form-group\">\n");
        stringBuilder.append("                        <label for=\"key\">键</label>\n");
        stringBuilder.append("                        <input id=\"key\" name=\"key\" class=\"form-control\" type=\"text\" />\n");
        stringBuilder.append("                    </div>\n");
        stringBuilder.append("                    <div class=\"form-group\">\n");
        stringBuilder.append("                        <label for=\"value\">值</label>\n");
        stringBuilder.append("                        <input id=\"value\" name=\"value\" class=\"form-control\" type=\"text\" />\n");
        stringBuilder.append("                    </div>\n");
        stringBuilder.append("                    <div class=\"form-group\">\n");
        stringBuilder.append("                        <button type=\"submit\" class=\"form-control btn btn-outline-primary\">添加配置</button>\n");
        stringBuilder.append("                    </div>\n");
        stringBuilder.append("                </form>\n");
        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Server - 添加配置", "添加配置", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationName = request.getParameter("applicationName");
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        LOGGER.info("[AddConfigServlet] Receive POST request. applicationName = {}, key = {}, value = {}."
                , applicationName, key, value);

        boolean success = this.getNode().put(applicationName, key, value);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ");
        if (success) {
            stringBuilder.append("操作已成功完成。");
        } else {
            stringBuilder.append("出现错误，请重试。");
        }
        stringBuilder.append("\n");
        stringBuilder.append("                </p>\n");
        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Server - 添加配置", "添加配置", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
