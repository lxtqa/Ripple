package ripple.server.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[HomeServlet] Get");
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println("<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n" +
                "    <title>Ripple Server</title>\n" +
                "    <link href=\"https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.5.3/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "    <link href=\"/styles/style.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <script src=\"https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.slim.min.js\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\"\n" +
                "            integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\"\n" +
                "            crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js\"\n" +
                "            integrity=\"sha384-o+RDsa0aLu++PJvFqy8fFScvbHFLtbvScb8AjopnFD+iEQ7wo/CG0xlczd+2O/em\"\n" +
                "            crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://unpkg.com/feather-icons/dist/feather.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<nav class=\"navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow\">\n" +
                "    <a class=\"navbar-brand col-sm-3 col-md-2 mr-0\" href=\"#\">Ripple Server</a>\n" +
                "</nav>\n" +
                "<div class=\"container-fluid\">\n" +
                "    <div class=\"row\">\n" +
                "        <nav class=\"col-md-2 d-none d-md-block bg-light sidebar\">\n" +
                "                <div class=\"sidebar-sticky\">\n" +
                "                        <ul class=\"nav flex-column mb-2\">\n" +
                "                                <li class=\"nav-item\">\n" +
                "                                        <a class=\"nav-link active\" href=\"/\">\n" +
                "                                                <span data-feather=\"home\"></span>\n" +
                "                                                主页 <span class=\"sr-only\">(current)</span>\n" +
                "                                        </a>\n" +
                "                                </li>\n" +
                "                        </ul>\n" +
                "                </div>\n" +
                "        </nav>\n" +
                "        <main role=\"main\" class=\"col-md-9 ml-sm-auto col-lg-10 px-4\">\n" +
                "            <div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom\">\n" +
                "                <h2>Ripple Server</h2>\n" +
                "            </div>\n" +
                "            <div>\n" +
                "                <p>\n" +
                "                    Ripple服务器节点\n" +
                "                </p>\n" +
                "            </div>\n" +
                "        </main>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<script>\n" +
                "    feather.replace()\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>");
    }
}
