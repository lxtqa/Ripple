package ripple.server.simulation.core.node.layer.server;

import brave.Tracing;
import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.helper.TracingHttpAsyncClientHelper;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * @author qingzhou.sjq
 */
public class LayerServlet extends BaseServlet{


    public void setInternalNodes(List<LayerPeer> internalNodes) {
        this.internalNodes = internalNodes;
    }

    public void setExternalNodes(List<LayerPeer> externalNodes) {
        this.externalNodes = externalNodes;
    }

    List<LayerPeer> internalNodes;
    List<LayerPeer> externalNodes;

    public void setLocal(LayerPeer local) {
        this.local = local;
    }

    LayerPeer local;

    public LayerServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    public void doHttpPostToServer(String content, String server, String key) {
        final String url = Util.buildURL(server, Constants.LAYER_ON_PUB_PATH);
        TracingHttpAsyncClientHelper.asyncTracingHttpPostLarge(url, Arrays.asList("key=" + key),content
                , (Tracing)this.getServletContext().getAttribute("TRACING")
                , new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse result) {
                        if (result.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                            Loggers.LAYER.warn("[LAYER] failed to publish data to peer, messageKey={}, peer={}, http code={}"
                                    , key, url, result.getStatusLine().getStatusCode());
                        }
                    }

                    @Override
                    public void failed(Exception ex) {
                        Loggers.LAYER.error(ex.getMessage());
                    }

                    @Override
                    public void cancelled() {
                        Loggers.LAYER.warn("[LAYER] publish http request cancelled.");
                    }
                });
    }
}
