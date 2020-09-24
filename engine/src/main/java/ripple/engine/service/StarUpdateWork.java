package ripple.engine.service;

import ripple.engine.entity.Node;
import ripple.engine.helper.HttpHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
public class StarUpdateWork implements Runnable {
    private Node node;
    private UUID messageUuid;

    public Node getNode() {
        return node;
    }

    private void setNode(Node node) {
        this.node = node;
    }

    public UUID getMessageUuid() {
        return messageUuid;
    }

    private void setMessageUuid(UUID messageUuid) {
        this.messageUuid = messageUuid;
    }

    public StarUpdateWork(Node node, UUID messageUuid) {
        this.setNode(node);
        this.setMessageUuid(messageUuid);
    }

    @Override
    public void run() {
        try {
            String url = "http://" + node.getAddress() + ":" + node.getPort() + "/Star/Update";
            Map<String, String> headers = new HashMap<>(1);
            headers.put("x-star-message-uuid", messageUuid.toString());
            HttpHelper.post(url, headers);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
