package ripple.server.simulation.core.node.tree.server;

import brave.Tracing;
import ripple.server.simulation.core.BaseServlet;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.helper.HttpHelper;
import ripple.server.simulation.helper.TracingHttpAsyncClientHelper;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.net.HttpURLConnection;
import java.util.*;

/**
 * @author qingzhou.sjq
 */
public class TreeServlet extends BaseServlet{

    /**
     * 这里可以优化，不适用map存，直接使用数组更省空间
     */
    List<TreePeer> peerList;

    Map<Integer, String> targetServerAddress = new HashMap<>();

    public void setPeersAndTarget(List<TreePeer> peerList) {
        this.peerList = peerList;
        targetServerAddress.clear();
//        for (TreePeer treePeer : peerList) {
//            Loggers.TREE.info("{} nodelist:  {}", local.ip, treePeer.ip);
//        }
        for (int i = 1; i <= Constants.TREE_TYPE; i++) {
            int child = local.index * Constants.TREE_TYPE + i;
            if (child >= peerList.size()) {
                break;
            }
            targetServerAddress.put(child, peerList.get(child).ip);
            Loggers.TREE.info("{} child node: {}", local.ip, peerList.get(child).ip);
        }
    }

    public void setLocal(TreePeer local) {
        this.local = local;
    }

    TreePeer local;

    public TreeServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    public void doHttpPostToServer(String content, String server, String key, int index) {
        final String url = Util.buildURL(server, Constants.TREE_ON_PUB_PATH);
        TracingHttpAsyncClientHelper.asyncTracingHttpPostLarge(url, Arrays.asList("key=" + key),content
                , (Tracing)this.getServletContext().getAttribute("TRACING")
                , new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse result) {
                        if (result.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                            Loggers.TREE.warn("[TREE] failed to publish data to peer, messageKey={}, peer={}, http code={}"
                                    , key, url, result.getStatusLine().getStatusCode());
                        }
                        // TODO
//                        sendAckToSrc();
                    }

                    @Override
                    public void failed(Exception ex) {
                        Loggers.TREE.error(ex.getMessage());
                        bypassInvalidNode(index, content, key);
                    }

                    @Override
                    public void cancelled() {
                        Loggers.TREE.warn("[TREE] publish http request cancelled.");
                    }
                });
    }


    private void bypassInvalidNode(int invalidNodeIndex, String content, String key) {
        for(int i = 1; i <= Constants.TREE_TYPE; i++) {
            int bypassIndex = invalidNodeIndex * Constants.TREE_TYPE + i;
            if (bypassIndex >= peerList.size()) {
                break;
            }
            doHttpPostToServer(content, peerList.get(bypassIndex).getIp(), key, bypassIndex);
        }
    }

//    public void sendAckToSrc(int srcIndex) {
//        TreePeer srcPeer = peerList.get(srcIndex);
//
//        final String url = Util.buildURL(srcPeer.ip, Constants.TREE_ACK_PATH);
//    }
}
