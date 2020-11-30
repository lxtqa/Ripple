package ripple.server.simulation.core.node.layer.server;

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

/**
 * @author qingzhou.sjq
 * 绑定在消息源节点
 */
public class PublishServlet extends LayerServlet {
    public PublishServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getEmulator().databaseOperation();
        Loggers.LAYER.info("write db, start sync... ");

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
        final LayerMessage messageToInternal = new LayerMessage();
        messageToInternal.key = key;
        messageToInternal.value = value;
        messageToInternal.fromExternal = false;
        messageToInternal.fromSource = true;
        JSONObject jsonToInternal = new JSONObject();
        jsonToInternal.put("message", messageToInternal);
        jsonToInternal.put("source", local.ip);
        final String contentToInternal = JSON.toJSONString(jsonToInternal);


        // 构造发送至外部集群节点的消息
        final LayerMessage messageToExternal = new LayerMessage();
        messageToExternal.key = key;
        messageToExternal.value = value;
        messageToExternal.fromExternal = true;
        messageToExternal.fromSource = true;
        JSONObject jsonToExternal = new JSONObject();
        jsonToExternal.put("message", messageToExternal);
        jsonToExternal.put("source", local.ip);
        final String contentToExternal = JSON.toJSONString(jsonToExternal);

        for (LayerPeer peer : internalNodes) {
            if (!local.ip.equals(peer.ip)) {
                doHttpPostToServer(contentToInternal, peer.ip, key);
            }
        }

        for (LayerPeer peer : externalNodes) {
            doHttpPostToServer(contentToExternal, peer.ip, key);
        }

        long end = System.currentTimeMillis();
        Loggers.LAYER.info("signalPublish cost {} ms, key: {}", (end - start), key);

    }

}
