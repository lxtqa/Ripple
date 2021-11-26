package ripple.client.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.client.core.BaseServlet;
import ripple.common.Endpoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class ModifyConfigServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyConfigServlet.class);

    public ModifyConfigServlet(RippleClient client) {
        super(client);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[ModifyConfigServlet] Receive GET request.");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <form action=\"").append(Endpoint.UI_MODIFY_CONFIG).append("\" method=\"post\">\n");
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
        stringBuilder.append("                        <button type=\"submit\" class=\"form-control btn btn-outline-primary\">修改配置</button>\n");
        stringBuilder.append("                    </div>\n");
        stringBuilder.append("                </form>\n");
        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Client - 修改配置", "修改配置", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationName = request.getParameter("applicationName");
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        LOGGER.info("[ModifyConfigServlet] Receive POST request. applicationName = {}, key = {}, value = {}."
                , applicationName, key, value);

        this.getClient().put(applicationName, key, value);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ").append("操作已成功提交").append("\n");
        stringBuilder.append("                </p>\n");
        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Client - 修改配置", "修改配置", content);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
