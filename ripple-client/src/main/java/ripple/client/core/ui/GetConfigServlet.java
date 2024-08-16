// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.client.core.ui;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ripple.client.RippleClient;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Constants;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.Item;
import ripple.common.entity.UpdateMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class GetConfigServlet extends BaseServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetConfigServlet.class);

    public GetConfigServlet(RippleClient client) {
        super(client);
    }

    private String parseMessageType(String messageType) {
        switch (messageType) {
            case Constants.MESSAGE_TYPE_DELETE:
                return this.getClient().getStringTable().messageTypeDelete();
            case Constants.MESSAGE_TYPE_UPDATE:
                return this.getClient().getStringTable().messageTypeUpdate();
            case Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE:
                return this.getClient().getStringTable().messageTypeIncrementalUpdate();
            default:
                return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("[GetConfigServlet] Receive GET request.");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        List<Item> allConfigs = this.getClient().getStorage().getItemService().getAllItems();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("                <p>\n");
        stringBuilder.append("                    ");
        stringBuilder.append(this.getClient().getStringTable().totalNumberOfConfiguration());
        stringBuilder.append(" <strong>");
        stringBuilder.append(allConfigs.size());
        stringBuilder.append("</strong>");
        stringBuilder.append("\n");
        stringBuilder.append("                </p>\n");
        if (allConfigs.size() > 0) {
            stringBuilder.append("                <table class=\"table table-striped\">\n");
            stringBuilder.append("                    <thead>\n");
            stringBuilder.append("                    <tr>\n");
            stringBuilder.append("                        <th>");
            stringBuilder.append(this.getClient().getStringTable().lineNumber());
            stringBuilder.append("</th>\n");
            stringBuilder.append("                        <th>");
            stringBuilder.append(this.getClient().getStringTable().applicationName());
            stringBuilder.append("</th>\n");
            stringBuilder.append("                        <th>");
            stringBuilder.append(this.getClient().getStringTable().key());
            stringBuilder.append("</th>\n");
            stringBuilder.append("                        <th>");
            stringBuilder.append(this.getClient().getStringTable().value());
            stringBuilder.append("</th>\n");
            stringBuilder.append("                        <th>");
            stringBuilder.append(this.getClient().getStringTable().history());
            stringBuilder.append("</th>\n");
            stringBuilder.append("                    </tr>\n");
            stringBuilder.append("                    </thead>\n");
            stringBuilder.append("                    <tbody>\n");

            int i = 0;
            for (i = 0; i < allConfigs.size(); i++) {
                Item item = allConfigs.get(i);
                List<AbstractMessage> messageList = this.getClient().getStorage().getMessageService()
                        .findMessages(item.getApplicationName(), item.getKey());
                this.getClient().getResolver().merge(item, messageList);

                String history = "";
                for (AbstractMessage message : messageList) {
                    history += "                            <p>";
                    history += "                                <span>UUID: " + message.getUuid() + "; </span> <br />";
                    history += "                                <span>" + this.getClient().getStringTable().type() + ": " + this.parseMessageType(message.getType()) + "; </span> <br />";
                    if (message instanceof UpdateMessage) {
                        history += "                                <span>" + this.getClient().getStringTable().value() + ": " + ((UpdateMessage) message).getValue() + "; </span> <br />";
                    } else if (message instanceof IncrementalUpdateMessage) {
                        history += "                                <span>" + this.getClient().getStringTable().baseVersion() + ": " + ((IncrementalUpdateMessage) message).getBaseMessageUuid() + "; </span> <br />";
                        history += "                                <span>" + this.getClient().getStringTable().atomicOperation() + ": " + ((IncrementalUpdateMessage) message).getAtomicOperation() + "; </span> <br />";
                        history += "                                <span>" + this.getClient().getStringTable().value() + ": " + ((IncrementalUpdateMessage) message).getValue() + "; </span> <br />";
                    }
                    history += "                                <span>" + this.getClient().getStringTable().lastUpdate() + ": " + simpleDateFormat.format(message.getLastUpdate()) + "; </span> <br />";
                    history += "                                <span>" + this.getClient().getStringTable().serverId() + ": " + message.getLastUpdateServerId() + "; </span> <br />";
                    history += "                            </p>";
                }

                stringBuilder.append("                    <tr>\n");
                stringBuilder.append("                        <td>")
                        .append(i + 1)
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(item.getApplicationName())
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(item.getKey())
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(item.getValue())
                        .append("</td>\n");
                stringBuilder.append("                        <td>")
                        .append(history)
                        .append("</td>\n");
                stringBuilder.append("                    </tr>\n");
            }

            stringBuilder.append("                    </tbody>\n");
            stringBuilder.append("                </table>\n");
        }

        String content = stringBuilder.toString();

        String pageContent = PageGenerator.buildPage("Ripple Client - " + this.getClient().getStringTable().getConfig()
                , this.getClient().getStringTable().getConfig(), content, this.getClient().getStringTable());

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(pageContent);
    }
}
