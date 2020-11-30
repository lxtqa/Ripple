package ripple.server.core.ui;

/**
 * @author Zhen Tang
 */
public final class PageGenerator {
    private PageGenerator() {

    }

    private static void appendHtmlStartTag(StringBuilder stringBuilder) {
        stringBuilder.append("<!DOCTYPE html>\n");
        stringBuilder.append("<html lang=\"zh-CN\">\n");
    }

    private static void appendHtmlEndTag(StringBuilder stringBuilder) {
        stringBuilder.append("</html>\n");
    }

    private static void appendBodyStartTag(StringBuilder stringBuilder) {
        stringBuilder.append("<body>\n");
    }

    private static void appendBodyEndTag(StringBuilder stringBuilder) {
        stringBuilder.append("</body>\n");
    }

    private static void appendHeadSection(StringBuilder stringBuilder, String title) {
        stringBuilder.append("<head>\n");
        stringBuilder.append("    <meta charset=\"utf-8\"/>\n");
        stringBuilder.append("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/>\n");
        stringBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n");
        stringBuilder.append("    <title>").append(title).append("</title>\n");
        stringBuilder.append("    <link href=\"https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.5.3/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        stringBuilder.append("    <link href=\"styles/style.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        stringBuilder.append("    <script src=\"https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.slim.min.js\"></script>\n");
        stringBuilder.append("    <script src=\"https://cdn.bootcdn.net/ajax/libs/popper.js/2.5.4/umd/popper.min.js\"></script>\n");
        stringBuilder.append("    <script src=\"https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.5.3/js/bootstrap.min.js\"></script>\n");
        stringBuilder.append("    <script src=\"https://unpkg.com/feather-icons/dist/feather.min.js\"></script>\n");
        stringBuilder.append("</head>\n");
    }

    private static void appendFeatherScriptSection(StringBuilder stringBuilder) {
        stringBuilder.append("<script>\n");
        stringBuilder.append("    feather.replace()\n");
        stringBuilder.append("</script>\n");
    }

    private static void appendNavigationBar(StringBuilder stringBuilder, String banner) {
        stringBuilder.append("<nav class=\"navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow\">\n");
        stringBuilder.append("    <a class=\"navbar-brand col-sm-3 col-md-2 mr-0\" href=\"/\">").append(banner).append("</a>\n");
        stringBuilder.append("</nav>\n");
    }

    private static void appendDivStartTag(StringBuilder stringBuilder) {
        stringBuilder.append("<div class=\"container-fluid\">\n");
        stringBuilder.append("    <div class=\"row\">\n");
    }

    private static void appendSideBar(StringBuilder stringBuilder, String currentFunction) {
        stringBuilder.append("        <nav class=\"col-md-2 d-none d-md-block bg-light sidebar\">\n");
        stringBuilder.append("            <div class=\"sidebar-sticky\">\n");
        stringBuilder.append("                <ul class=\"nav flex-column mb-2\">\n");
        PageGenerator.buildSidebarItem(stringBuilder, "主页", "/", "home", currentFunction);
        stringBuilder.append("                </ul>\n");
        PageGenerator.buildSidebarTitle(stringBuilder, "配置管理");
        stringBuilder.append("                <ul class=\"nav flex-column mb-2\">\n");
        PageGenerator.buildSidebarItem(stringBuilder, "查询配置", "/Config/Get", "search", currentFunction);
        PageGenerator.buildSidebarItem(stringBuilder, "添加配置", "/Config/New", "file-plus", currentFunction);
        PageGenerator.buildSidebarItem(stringBuilder, "修改配置", "/Config/Modify", "edit", currentFunction);
        PageGenerator.buildSidebarItem(stringBuilder, "删除配置", "/Config/Delete", "trash-2", currentFunction);
        stringBuilder.append("                </ul>\n");
        PageGenerator.buildSidebarTitle(stringBuilder, "订阅管理");
        stringBuilder.append("                <ul class=\"nav flex-column mb-2\">\n");
        PageGenerator.buildSidebarItem(stringBuilder, "查询订阅关系", "/Subscription/Get", "share-2", currentFunction);
        stringBuilder.append("                </ul>\n");
        PageGenerator.buildSidebarTitle(stringBuilder, "集群管理");
        stringBuilder.append("                <ul class=\"nav flex-column mb-2\">\n");
        PageGenerator.buildSidebarItem(stringBuilder, "服务器集群信息", "/Cluster/Server", "server", currentFunction);
        PageGenerator.buildSidebarItem(stringBuilder, "已连接的客户端", "/Cluster/Client", "list", currentFunction);
        stringBuilder.append("                </ul>\n");
        stringBuilder.append("            </div>\n");
        stringBuilder.append("        </nav>\n");
    }

    private static void buildSidebarTitle(StringBuilder stringBuilder, String title) {
        stringBuilder.append("                <h6 class=\"sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted\">\n");
        stringBuilder.append("                    <span>").append(title).append("</span>\n");
        stringBuilder.append("                </h6>\n");
    }

    private static void buildSidebarItem(StringBuilder stringBuilder, String function, String link, String logo, String currentFunction) {
        stringBuilder.append("                    <li class=\"nav-item\">\n");
        if (currentFunction.equals(function)) {
            stringBuilder.append("                        <a class=\"nav-link active\" href=\"").append(link).append("\">\n");
        } else {
            stringBuilder.append("                        <a class=\"nav-link\" href=\"").append(link).append("/\">\n");
        }
        stringBuilder.append("                            <span data-feather=\"").append(logo).append("\"></span>\n");
        if (currentFunction.equals(function)) {
            stringBuilder.append("                            ").append(function).append(" <span class=\"sr-only\">(current)</span>\n");
        } else {
            stringBuilder.append("                            ").append(function).append("\n");
        }
        stringBuilder.append("                        </a>\n");
        stringBuilder.append("                    </li>\n");
    }

    private static void appendDivEndTag(StringBuilder stringBuilder) {
        stringBuilder.append("    </div>\n");
        stringBuilder.append("</div>\n");
    }

    private static void appendMainSection(StringBuilder stringBuilder, String title, String content) {
        stringBuilder.append("        <main role=\"main\" class=\"col-md-9 ml-sm-auto col-lg-10 px-4\">\n");
        stringBuilder.append("            <div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom\">\n");
        stringBuilder.append("                <h2>").append(title).append("</h2>\n");
        stringBuilder.append("            </div>\n");
        stringBuilder.append("            <div>\n");
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ").append(content.trim()).append("\n");
        stringBuilder.append("                </p>\n");
        stringBuilder.append("            </div>\n");
        stringBuilder.append("        </main>\n");
    }

    public static String buildPage(String title, String currentFunction, String content) {
        StringBuilder stringBuilder = new StringBuilder();
        PageGenerator.appendHtmlStartTag(stringBuilder);
        PageGenerator.appendHeadSection(stringBuilder, title);
        PageGenerator.appendBodyStartTag(stringBuilder);
        PageGenerator.appendNavigationBar(stringBuilder, "Ripple Server");
        PageGenerator.appendDivStartTag(stringBuilder);
        PageGenerator.appendSideBar(stringBuilder, currentFunction);
        PageGenerator.appendMainSection(stringBuilder, title, content);
        PageGenerator.appendDivEndTag(stringBuilder);
        PageGenerator.appendFeatherScriptSection(stringBuilder);
        PageGenerator.appendBodyEndTag(stringBuilder);
        PageGenerator.appendHtmlEndTag(stringBuilder);
        return stringBuilder.toString();
    }
}
