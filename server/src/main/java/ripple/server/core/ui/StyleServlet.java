package ripple.server.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.server.core.AbstractNode;
import ripple.server.core.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StyleServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleServlet.class);

    public StyleServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[StyleServlet] Get");
        response.setContentType("text/css;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println("@CHARSET \"UTF-8\";\n" +
                "\n" +
                "body {\n" +
                "  font-family: \"Microsoft YaHei\", Helvetica, Arial;\n" +
                "  font-size: .875rem;\n" +
                "}\n" +
                "\n" +
                ".feather {\n" +
                "  width: 16px;\n" +
                "  height: 16px;\n" +
                "  vertical-align: text-bottom;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                " * Sidebar\n" +
                " */\n" +
                "\n" +
                ".sidebar {\n" +
                "  position: fixed;\n" +
                "  top: 0;\n" +
                "  bottom: 0;\n" +
                "  left: 0;\n" +
                "  z-index: 100; /* Behind the navbar */\n" +
                "  padding: 48px 0 0; /* Height of navbar */\n" +
                "  box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);\n" +
                "}\n" +
                "\n" +
                ".sidebar-sticky {\n" +
                "  position: relative;\n" +
                "  top: 0;\n" +
                "  height: calc(100vh - 48px);\n" +
                "  padding-top: .5rem;\n" +
                "  overflow-x: hidden;\n" +
                "  overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */\n" +
                "}\n" +
                "\n" +
                "@supports ((position: -webkit-sticky) or (position: sticky)) {\n" +
                "  .sidebar-sticky {\n" +
                "    position: -webkit-sticky;\n" +
                "    position: sticky;\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                ".sidebar .nav-link {\n" +
                "  font-weight: 500;\n" +
                "  color: #333;\n" +
                "}\n" +
                "\n" +
                ".sidebar .nav-link .feather {\n" +
                "  margin-right: 4px;\n" +
                "  color: #999;\n" +
                "}\n" +
                "\n" +
                ".sidebar .nav-link.active {\n" +
                "  color: #007bff;\n" +
                "}\n" +
                "\n" +
                ".sidebar .nav-link:hover .feather,\n" +
                ".sidebar .nav-link.active .feather {\n" +
                "  color: inherit;\n" +
                "}\n" +
                "\n" +
                ".sidebar-heading {\n" +
                "  font-size: .75rem;\n" +
                "  text-transform: uppercase;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                " * Content\n" +
                " */\n" +
                "\n" +
                "[role=\"main\"] {\n" +
                "  padding-top: 48px; /* Space for fixed navbar */\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                " * Navbar\n" +
                " */\n" +
                "\n" +
                ".navbar-brand {\n" +
                "  padding-top: .75rem;\n" +
                "  padding-bottom: .75rem;\n" +
                "  font-size: 1rem;\n" +
                "  background-color: rgba(0, 0, 0, .25);\n" +
                "  box-shadow: inset -1px 0 0 rgba(0, 0, 0, .25);\n" +
                "}\n" +
                "\n" +
                ".navbar .form-control {\n" +
                "  padding: .75rem 1rem;\n" +
                "  border-width: 0;\n" +
                "  border-radius: 0;\n" +
                "}\n" +
                "\n" +
                ".form-control-dark {\n" +
                "  color: #fff;\n" +
                "  background-color: rgba(255, 255, 255, .1);\n" +
                "  border-color: rgba(255, 255, 255, .1);\n" +
                "}\n" +
                "\n" +
                ".form-control-dark:focus {\n" +
                "  border-color: transparent;\n" +
                "  box-shadow: 0 0 0 3px rgba(255, 255, 255, .25);\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                " * Utilities\n" +
                " */\n" +
                "\n" +
                ".border-top { border-top: 1px solid #e5e5e5; }\n" +
                ".border-bottom { border-bottom: 1px solid #e5e5e5; }\n" +
                "\n" +
                ".bs-callout {\n" +
                "    padding: 20px;\n" +
                "    margin: 20px 0;\n" +
                "    border: 1px solid #eee;\n" +
                "    border-left-width: 5px;\n" +
                "    border-radius: 3px;\n" +
                "}\n" +
                "\n" +
                ".bs-callout h4 {\n" +
                "    margin-top: 0;\n" +
                "    margin-bottom: 5px;\n" +
                "}\n" +
                "\n" +
                ".bs-callout p:last-child {\n" +
                "    margin-bottom: 0;\n" +
                "}\n" +
                "\n" +
                ".bs-callout code {\n" +
                "    border-radius: 3px;\n" +
                "}\n" +
                "\n" +
                ".bs-callout+.bs-callout {\n" +
                "    margin-top: -5px;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-default {\n" +
                "    border-left-color: #777;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-default h4 {\n" +
                "    color: #777;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-primary {\n" +
                "    border-left-color: #428bca;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-primary h4 {\n" +
                "    color: #428bca;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-success {\n" +
                "    border-left-color: #5cb85c;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-success h4 {\n" +
                "    color: #5cb85c;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-danger {\n" +
                "    border-left-color: #d9534f;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-danger h4 {\n" +
                "    color: #d9534f;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-warning {\n" +
                "    border-left-color: #f0ad4e;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-warning h4 {\n" +
                "    color: #f0ad4e;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-info {\n" +
                "    border-left-color: #5bc0de;\n" +
                "}\n" +
                "\n" +
                ".bs-callout-info h4 {\n" +
                "    color: #5bc0de;\n" +
                "}");
    }
}
