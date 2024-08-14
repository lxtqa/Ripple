// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.common.entity.NodeMetadata;
import ripple.server.core.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhen Tang
 */
public class ServerClusterServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerClusterServlet.class);

    public ServerClusterServlet(Node node) {
        super(node);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[ServerClusterServlet] Receive GET request.");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ")
                .append("当前服务器集群共包含 <strong>")
                .append(this.getNode().getNodeList().size())
                .append("</strong> 个节点。")
                .append("\n");
        stringBuilder.append("                </p>\n");
        if (this.getNode().getNodeList().size() > 0) {
            stringBuilder.append("                <table class=\"table table-striped\">\n");
            stringBuilder.append("                    <thead>\n");
            stringBuilder.append("                    <tr>\n");
            stringBuilder.append("                        <th>序号</th>\n");
            stringBuilder.append("                        <th>服务器ID</th>\n");
            stringBuilder.append("                        <th>服务器地址</th>\n");
            stringBuilder.append("                        <th>服务器API端口号</th>\n");
            stringBuilder.append("                    </tr>\n");
            stringBuilder.append("                    </thead>\n");
            stringBuilder.append("                    <tbody>\n");

            int i = 0;
            for (NodeMetadata metadata : this.getNode().getNodeList()) {
                stringBuilder.append("                    <tr>\n");
                stringBuilder.append("                        <td>")
                        .append(i + 1)
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(metadata.getId())
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

        String pageContent = PageGenerator.buildPage("Ripple Server - 服务器集群信息", "服务器集群信息", content, this.getNode().getStringTable());

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
