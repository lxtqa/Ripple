package ripple.server.simulation.core.node.layer.server;

import brave.Tracing;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.helper.TracingHttpAsyncClientHelper;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * @author qingzhou.sjq
 */
public class OnPublishServlet extends LayerServlet{
    public OnPublishServlet(Emulator emulator, Context context) {
        super(emulator, context);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "application/json; charset=" + Util.getAcceptEncoding(request));
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Encode", "gzip");

        getEmulator().databaseOperation();


        String entity = IOUtils.toString(request.getInputStream(), "UTF-8");

        String value = Arrays.asList(entity).toArray(new String[1])[0];
        value = URLDecoder.decode(value, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(value);

        LayerMessage message = JSON.parseObject(jsonObject.getString("message"), LayerMessage.class);
        String source = jsonObject.getString("source");
        Loggers.LAYER.info(" {} get message from {}", local.ip, source);
        signalPublish(message);
    }

    private void signalPublish(LayerMessage message) {
        if (message.fromExternal) {
            message.fromExternal = false;
            message.fromSource = false;
            JSONObject jsonToInternal = new JSONObject();
            jsonToInternal.put("message", message);
            jsonToInternal.put("source", local.ip);
            final String contentToInternal = JSON.toJSONString(jsonToInternal);
            for (LayerPeer peer : internalNodes) {
                if (local.ip.equals(peer.ip)) {
                    continue;
                }
                doHttpPostToServer(contentToInternal, peer.ip, message.key);
            }

        } else if (message.fromSource) {
            message.fromExternal = true;
            message.fromSource = false;
            JSONObject jsonToExternal = new JSONObject();
            jsonToExternal.put("message", message);
            jsonToExternal.put("source", local.ip);
            final String contentToExternal = JSON.toJSONString(jsonToExternal);
            for (LayerPeer peer : externalNodes) {

                doHttpPostToServer(contentToExternal, peer.ip, message.key);
            }
        }
    }

}
