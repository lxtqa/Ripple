// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.helper;

/**
 * @author Zhen Tang
 */
public class PageHelper {
    private PageHelper() {

    }

    public static void appendHtmlStartTag(StringBuilder stringBuilder, String language) {
        stringBuilder.append("<!DOCTYPE html>\n");
        stringBuilder.append("<html lang=\"");
        stringBuilder.append(language);
        stringBuilder.append("\">\n");
    }

    public static void appendHtmlEndTag(StringBuilder stringBuilder) {
        stringBuilder.append("</html>\n");
    }

    public static void appendBodyStartTag(StringBuilder stringBuilder) {
        stringBuilder.append("<body>\n");
    }

    public static void appendBodyEndTag(StringBuilder stringBuilder) {
        stringBuilder.append("</body>\n");
    }

    public static void appendHeadSection(StringBuilder stringBuilder, String title) {
        stringBuilder.append("<head>\n");
        stringBuilder.append("    <meta charset=\"utf-8\"/>\n");
        stringBuilder.append("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"/>\n");
        stringBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n");
        stringBuilder.append("    <title>").append(title).append("</title>\n");
        stringBuilder.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        stringBuilder.append("    <link href=\"/Style\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        stringBuilder.append("    <script src=\"https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js\"></script>\n");
        stringBuilder.append("    <script src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js\"></script>\n");
        stringBuilder.append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js\"></script>\n");
        stringBuilder.append("    <script src=\"https://cdn.jsdelivr.net/npm/feather-icons@4.28.0/dist/feather.min.js\"></script>\n");
        stringBuilder.append("</head>\n");
    }

    public static void appendFeatherScriptSection(StringBuilder stringBuilder) {
        stringBuilder.append("<script>\n");
        stringBuilder.append("    feather.replace()\n");
        stringBuilder.append("</script>\n");
    }

    public static void appendNavigationBar(StringBuilder stringBuilder, String banner) {
        stringBuilder.append("<nav class=\"navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow\">\n");
        stringBuilder.append("    <a class=\"navbar-brand col-sm-3 col-md-2 mr-0\" href=\"/\">").append(banner).append("</a>\n");
        stringBuilder.append("</nav>\n");
    }

    public static void appendDivStartTag(StringBuilder stringBuilder) {
        stringBuilder.append("<div class=\"container-fluid\">\n");
        stringBuilder.append("    <div class=\"row\">\n");
    }

    public static void buildSidebarTitle(StringBuilder stringBuilder, String title) {
        stringBuilder.append("                <h6 class=\"sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted\">\n");
        stringBuilder.append("                    <span>").append(title).append("</span>\n");
        stringBuilder.append("                </h6>\n");
    }

    public static void buildSidebarItem(StringBuilder stringBuilder, String function, String link, String logo, String currentFunction) {
        stringBuilder.append("                    <li class=\"nav-item\">\n");
        if (currentFunction.equals(function)) {
            stringBuilder.append("                        <a class=\"nav-link active\" href=\"").append(link).append("\">\n");
        } else {
            stringBuilder.append("                        <a class=\"nav-link\" href=\"").append(link).append("\">\n");
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

    public static void appendDivEndTag(StringBuilder stringBuilder) {
        stringBuilder.append("    </div>\n");
        stringBuilder.append("</div>\n");
    }

    public static void appendMainSection(StringBuilder stringBuilder, String title, String content) {
        stringBuilder.append("        <main role=\"main\" class=\"col-md-9 ml-sm-auto col-lg-10 px-4\">\n");
        stringBuilder.append("            <div class=\"d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom\">\n");
        stringBuilder.append("                <h2>").append(title).append("</h2>\n");
        stringBuilder.append("            </div>\n");
        stringBuilder.append("            <div>\n");
        stringBuilder.append(content);
        stringBuilder.append("            </div>\n");
        stringBuilder.append("        </main>\n");
    }
}
