package ripple.server.simulation.core.node.tree.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;

/**
 * @author qingzhou.sjq
 */
public class OnPublishServlet extends TreeServlet{
    public OnPublishServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Content-Type", "application/json; charset=" + Util.getAcceptEncoding(req));
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Content-Encode", "gzip");

        getEmulator().databaseOperation();

        String entity = IOUtils.toString(req.getInputStream(), "UTF-8");

        String value = Arrays.asList(entity).toArray(new String[1])[0];
        value = URLDecoder.decode(value, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(value);

        TreeMessage message = JSON.parseObject(jsonObject.getString("message"), TreeMessage.class);
        String source = jsonObject.getString("source");
        Loggers.TREE.info(" {} get message from {}", local.ip, source);
        signalPublish(message);
    }

    private void signalPublish(TreeMessage message) {

        JSONObject jsonToExternal = new JSONObject();
        jsonToExternal.put("message", message);
        jsonToExternal.put("source", local.ip);
        final String contentToExternal = JSON.toJSONString(jsonToExternal);

        for (Map.Entry<Integer, String> ipWithIndex: targetServerAddress.entrySet()) {
            if (!local.ip.equals(ipWithIndex.getValue())) {
                doHttpPostToServer(contentToExternal, ipWithIndex.getValue(), message.key, ipWithIndex.getKey());
            }
        }
    }
}
