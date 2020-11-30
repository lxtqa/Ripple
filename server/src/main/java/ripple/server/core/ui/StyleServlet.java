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
public class StyleServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleServlet.class);

    public StyleServlet(AbstractNode node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[StyleServlet] Get");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@CHARSET \"UTF-8\";\n");
        stringBuilder.append("body {\n");
        stringBuilder.append("    font-family: \"Microsoft YaHei\", Helvetica, Arial;\n");
        stringBuilder.append("    font-size: .875rem;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".feather {\n");
        stringBuilder.append("    width: 16px;\n");
        stringBuilder.append("    height: 16px;\n");
        stringBuilder.append("    vertical-align: text-bottom;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar {\n");
        stringBuilder.append("    position: fixed;\n");
        stringBuilder.append("    top: 0;\n");
        stringBuilder.append("    bottom: 0;\n");
        stringBuilder.append("    left: 0;\n");
        stringBuilder.append("    z-index: 100;\n");
        stringBuilder.append("    padding: 48px 0 0;\n");
        stringBuilder.append("    box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar-sticky {\n");
        stringBuilder.append("    position: relative;\n");
        stringBuilder.append("    top: 0;\n");
        stringBuilder.append("    height: calc(100vh - 48px);\n");
        stringBuilder.append("    padding-top: .5rem;\n");
        stringBuilder.append("    overflow-x: hidden;\n");
        stringBuilder.append("    overflow-y: auto;\n");
        stringBuilder.append("}\n");
        stringBuilder.append("@supports ((position: -webkit-sticky) or (position: sticky)) {\n");
        stringBuilder.append("    .sidebar-sticky {\n");
        stringBuilder.append("        position: -webkit-sticky;\n");
        stringBuilder.append("        position: sticky;\n");
        stringBuilder.append("    }\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar .nav-link {\n");
        stringBuilder.append("    font-weight: 500;\n");
        stringBuilder.append("    color: #333;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar .nav-link .feather {\n");
        stringBuilder.append("    margin-right: 4px;\n");
        stringBuilder.append("    color: #999;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar .nav-link.active {\n");
        stringBuilder.append("    color: #007bff;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar .nav-link:hover .feather,\n");
        stringBuilder.append(".sidebar .nav-link.active .feather {\n");
        stringBuilder.append("    color: inherit;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".sidebar-heading {\n");
        stringBuilder.append("    font-size: .75rem;\n");
        stringBuilder.append("    text-transform: uppercase;\n");
        stringBuilder.append("}\n");
        stringBuilder.append("[role=\"main\"] {\n");
        stringBuilder.append("    padding-top: 48px;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".navbar-brand {\n");
        stringBuilder.append("    padding-top: .75rem;\n");
        stringBuilder.append("    padding-bottom: .75rem;\n");
        stringBuilder.append("    font-size: 1rem;\n");
        stringBuilder.append("    background-color: rgba(0, 0, 0, .25);\n");
        stringBuilder.append("    box-shadow: inset -1px 0 0 rgba(0, 0, 0, .25);\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".navbar .form-control {\n");
        stringBuilder.append("    padding: .75rem 1rem;\n");
        stringBuilder.append("    border-width: 0;\n");
        stringBuilder.append("    border-radius: 0;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".form-control-dark {\n");
        stringBuilder.append("    color: #fff;\n");
        stringBuilder.append("    background-color: rgba(255, 255, 255, .1);\n");
        stringBuilder.append("    border-color: rgba(255, 255, 255, .1);\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".form-control-dark:focus {\n");
        stringBuilder.append("    border-color: transparent;\n");
        stringBuilder.append("    box-shadow: 0 0 0 3px rgba(255, 255, 255, .25);\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".border-top {\n");
        stringBuilder.append("    border-top: 1px solid #e5e5e5;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".border-bottom {\n");
        stringBuilder.append("    border-bottom: 1px solid #e5e5e5;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout {\n");
        stringBuilder.append("    padding: 20px;\n");
        stringBuilder.append("    margin: 20px 0;\n");
        stringBuilder.append("    border: 1px solid #eee;\n");
        stringBuilder.append("    border-left-width: 5px;\n");
        stringBuilder.append("    border-radius: 3px;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout h4 {\n");
        stringBuilder.append("    margin-top: 0;\n");
        stringBuilder.append("    margin-bottom: 5px;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout p:last-child {\n");
        stringBuilder.append("    margin-bottom: 0;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout code {\n");
        stringBuilder.append("    border-radius: 3px;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout+.bs-callout {\n");
        stringBuilder.append("    margin-top: -5px;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-default {\n");
        stringBuilder.append("    border-left-color: #777;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-default h4 {\n");
        stringBuilder.append("    color: #777;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-primary {\n");
        stringBuilder.append("    border-left-color: #428bca;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-primary h4 {\n");
        stringBuilder.append("    color: #428bca;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-success {\n");
        stringBuilder.append("    border-left-color: #5cb85c;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-success h4 {\n");
        stringBuilder.append("    color: #5cb85c;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-danger {\n");
        stringBuilder.append("    border-left-color: #d9534f;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-danger h4 {\n");
        stringBuilder.append("    color: #d9534f;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-warning {\n");
        stringBuilder.append("    border-left-color: #f0ad4e;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-warning h4 {\n");
        stringBuilder.append("    color: #f0ad4e;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-info {\n");
        stringBuilder.append("    border-left-color: #5bc0de;\n");
        stringBuilder.append("}\n");
        stringBuilder.append(".bs-callout-info h4 {\n");
        stringBuilder.append("    color: #5bc0de;\n");
        stringBuilder.append("}\n");

        String pageContent = stringBuilder.toString();

        response.setContentType("text/css;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
