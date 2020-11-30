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
public class PublishServlet extends TreeServlet{
    public PublishServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getEmulator().databaseOperation();
        Loggers.TREE.info("write db, start sync... ");

        resp.setHeader("Content-Type", "application/json; charset=" + Util.getAcceptEncoding(req));
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Content-Encode", "gzip");

        String entity = IOUtils.toString(req.getInputStream(), "UTF-8");

        String value = Arrays.asList(entity).toArray(new String[1])[0];
        value = URLDecoder.decode(value, "UTF-8");
        JSONObject json = JSON.parseObject(value);

        try {
            signalPublish(json.getString("key"), json.getString("value"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signalPublish(String key, String value) throws Exception {

        long start = System.currentTimeMillis();

        //TODO 发送至 client

        // 构造发送至内部集群节点的消息
        final TreeMessage messageToInternal = new TreeMessage();
        messageToInternal.key = key;
        messageToInternal.value = value;
        JSONObject jsonToInternal = new JSONObject();
        jsonToInternal.put("message", messageToInternal);
        jsonToInternal.put("source", local.ip);
        final String contentToServer = JSON.toJSONString(jsonToInternal);

        // 根节点转发
        if (local.index == 0) {
            for (Map.Entry<Integer, String> ipWithIndex: targetServerAddress.entrySet()) {
                if (!local.ip.equals(ipWithIndex.getValue())) {
                    doHttpPostToServer(contentToServer, ipWithIndex.getValue(), key, ipWithIndex.getKey());
                }
            }

        } else {
            doHttpPostToServer(contentToServer, peerList.get(0).ip, key, 0);
        }



        long end = System.currentTimeMillis();
        Loggers.TREE.info("signalPublish cost {} ms, key: {}", (end - start), key);

    }
}
