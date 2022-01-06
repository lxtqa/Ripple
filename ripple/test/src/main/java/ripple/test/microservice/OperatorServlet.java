package ripple.test.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Constants;
import ripple.common.entity.Item;
import ripple.common.entity.UpdateMessage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperatorServlet extends HttpServlet {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private OperatorService operatorService;

    private OperatorService getOperatorService() {
        return operatorService;
    }

    private void setOperatorService(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    public OperatorServlet(OperatorService operatorService) {
        this.setOperatorService(operatorService);
    }

    public static class MessageComparator implements Comparator<AbstractMessage> {
        @Override
        public int compare(AbstractMessage o1, AbstractMessage o2) {
            return Long.compare(o2.getLastUpdate().getTime(), o1.getLastUpdate().getTime());
        }
    }

    private String getValue(String applicationName, String key) {
        Item item = this.getOperatorService().getClient().get(applicationName, key);
        if (item == null) {
            return null;
        }
        List<AbstractMessage> messageList = this.getOperatorService().getClient().getStorage().getMessageService()
                .findMessages(item.getApplicationName(), item.getKey());
        if (messageList.size() == 0) {
            return null;
        }
        messageList.sort(new MessageComparator());
        return messageList.get(0).getType().equals(Constants.MESSAGE_TYPE_UPDATE)
                ? ((UpdateMessage) messageList.get(0)).getValue() : null;
    }

    private int doGetNumber(String address) {
        try {
            Map<String, String> headers = new ConcurrentHashMap<>(3);
            String url = "http://" + address + "/";
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Integer.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    private String getOneAddress() {
        return this.getValue("testApp", "oneAddress");
    }

    private int getNumberOne() {
        if (this.getOneAddress() != null) {
            return this.doGetNumber(this.getOneAddress());
        } else {
            return -1;
        }
    }

    private String getTwoAddress() {
        return this.getValue("testApp", "twoAddress");
    }

    private int getNumberTwo() {
        if (this.getTwoAddress() != null) {
            return this.doGetNumber(this.getTwoAddress());
        } else {
            return -1;
        }
    }

    private String getFunction() {
        return this.getValue("testApp", "function");
    }

    private int getResult() {
        if (this.getFunction() != null) {
            if (this.getFunction().equals("add")) {
                return this.getNumberOne() + this.getNumberTwo();
            } else if (this.getFunction().equals("subtract")) {
                return this.getNumberOne() - this.getNumberTwo();
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>\n");
        stringBuilder.append("    <head>\n");
        stringBuilder.append("        <title>Operator Service</title>\n");
        stringBuilder.append("    </head>\n");
        stringBuilder.append("    <body>\n");
        stringBuilder.append("        <div>\n");
        stringBuilder.append("            <p>\n");
        stringBuilder.append("                ").append("服务1 访问地址：").append(this.getOneAddress()).append("\n");
        stringBuilder.append("            </p>\n");
        stringBuilder.append("            <p>\n");
        stringBuilder.append("                ").append("服务1 返回值：").append(this.getNumberOne()).append("\n");
        stringBuilder.append("            </p>\n");
        stringBuilder.append("            <p>\n");
        stringBuilder.append("                ").append("服务2 访问地址：").append(this.getTwoAddress()).append("\n");
        stringBuilder.append("            </p>\n");
        stringBuilder.append("            <p>\n");
        stringBuilder.append("                ").append("服务2 返回值：").append(this.getNumberTwo()).append("\n");
        stringBuilder.append("            </p>\n");
        stringBuilder.append("            <p>\n");
        stringBuilder.append("                ").append("计算方法：").append(this.getFunction()).append("\n");
        stringBuilder.append("            </p>\n");
        stringBuilder.append("            <p>\n");
        stringBuilder.append("                ").append("计算值：").append(this.getResult()).append("\n");
        stringBuilder.append("            </p>\n");
        stringBuilder.append("        </div>\n");
        stringBuilder.append("    </body>\n");
        stringBuilder.append("</html>\n");
        String content = stringBuilder.toString();

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println(content);
    }
}
