package ripple.server.simulation.core.node.raft.server;

import brave.Tracing;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ripple.server.simulation.core.Context;
import ripple.server.simulation.core.emulator.Emulator;
import ripple.server.simulation.helper.HttpClient;
import ripple.server.simulation.helper.TracingHttpAsyncClientHelper;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author qingzhou.sjq
 */
public class PublishServlet extends RaftServlet{

    private static volatile ConcurrentMap<String, Datum> datums = new ConcurrentHashMap<String, Datum>();

    public PublishServlet(Emulator emulator, Context context) {
        super(emulator, context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type", "application/json; charset=" + Util.getAcceptEncoding(req));
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Content-Encode", "gzip");

        String entity = IOUtils.toString(req.getInputStream(), "UTF-8");

        String value = Arrays.asList(entity).toArray(new String[1])[0];
        value = URLDecoder.decode(value, "UTF-8");
        JSONObject json = JSON.parseObject(value);

        try {
            doSignalPublish(json.getString("key"), json.getString("value"), getEmulator());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doSignalPublish(String key, String value, Emulator emulator) throws Exception {
        if (!peers.isLeader()) {
            JSONObject params = new JSONObject();
            params.put("key", key);
            params.put("value", value);
            Map<String, String> parameters = new HashMap<>(1);
            parameters.put("key", key);

            proxyPostLarge(Constants.RAFT_PUB_PATH, params.toJSONString(), parameters);

            return;
        }

        if (!peers.isLeader()) {
            throw new IllegalStateException("I'm not leader, can not handle update/delete operation");
        }

        signalPublish(key, value, emulator);
    }

    public void signalPublish(String key, String value, Emulator emulator) throws Exception {

        long start = System.currentTimeMillis();

        //TODO 发送至 client

        for (String clientIp : targetClient) {
            Loggers.RAFT.info("push to client: {}", clientIp);
        }

        final Datum datum = new Datum();
        datum.key = key;
        datum.value = value;

        if (getDatum(key) == null) {
            datum.timestamp.set(1L);
        } else {
            datum.timestamp.set(getDatum(key).timestamp.incrementAndGet());
        }

        JSONObject json = new JSONObject();
        json.put("datum", datum);
        json.put("source", peers.local());
        json.put("increaseTerm", false);

        onPublish(datum, peers.local(), false, emulator);

        final String content = JSON.toJSONString(json);

        for (final String server : peers.allServersIncludeMyself()) {
            if (peers.isLeader(server)) {
                continue;
            }
            final String url = Util.buildURL(server, Constants.RAFT_ON_PUB_PATH);
            /*HttpClient.asyncHttpPostLarge(url, Arrays.asList("key=" + key), content, new AsyncCompletionHandler<Integer>() {
                @Override
                public Integer onCompleted(Response response) throws Exception {
                    if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
                        Loggers.RAFT.warn("[RAFT] failed to publish data to peer, datumId={}, peer={}, http code={}", datum.key, server, response.getStatusCode());
                        return 1;
                    }
                    return 0;
                }

                @Override
                public STATE onContentWriteCompleted() {
                    return STATE.CONTINUE;
                }
            });*/
            TracingHttpAsyncClientHelper.asyncTracingHttpPostLarge(url,Arrays.asList("key=" + key),content
                    , (Tracing)this.getServletContext().getAttribute("TRACING")
                    , new FutureCallback<HttpResponse>() {
                        @Override
                        public void completed(HttpResponse result) {
                            if (result.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                                Loggers.RAFT.warn("[RAFT] failed to publish data to peer, datumId={}, peer={}, http code={}"
                                        , datum.key, server, result.getStatusLine().getStatusCode());
                            }
                        }

                        @Override
                        public void failed(Exception ex) {
                            Loggers.RAFT.error(ex.getMessage());
                        }

                        @Override
                        public void cancelled() {
                            Loggers.RAFT.warn("[RAFT] publish http request cancelled.");
                        }
                    });

        }

        long end = System.currentTimeMillis();
        Loggers.RAFT.info("signalPublish cost {} ms, key: {}", (end - start), key);

    }

    public void proxyPostLarge(String api, String content, Map<String, String> headers) throws Exception {
        if (peers.isLeader()) {
            throw new IllegalStateException("I'm leader, no need to do proxy");
        }

        if (peers.getLeader() == null) {
            throw new IllegalStateException("No leader at present");
        }

        // do proxy
        String server = peers.getLeader().ip;

        String url = "http://" + server  + api;

        HttpClient.HttpResult result =  HttpClient.httpPostLarge(url, headers, content);
        if (result.code != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("leader failed, caused by: " + result.content);
        }
    }

    public void onPublish(Datum datum, RaftPeer source, boolean increaseTerm, Emulator emulator) throws Exception {
        RaftPeer local = peers.local();
        if (StringUtils.isBlank(datum.value)) {
            Loggers.RAFT.warn("received empty datum");
            throw new IllegalStateException("received empty datum");
        }

        if (!peers.isLeader(source.ip)) {
            Loggers.RAFT.warn("peer {} tried to publish data but wasn't leader, leader: {}",
                    JSON.toJSONString(source), JSON.toJSONString(peers.getLeader()));
            throw new IllegalStateException("peer(" + source.ip + ") tried to publish " +
                    "data but wasn't leader");
        }

        if (source.term.get() < local.term.get()) {
            Loggers.RAFT.warn("out of date publish, pub-term: {}, cur-term: {}",
                    JSON.toJSONString(source), JSON.toJSONString(local));
            throw new IllegalStateException("out of date publish, pub-term:"
                    + source.term.get() + ", cur-term: " + local.term.get());
        }

        local.resetLeaderDue();

        // do apply
        emulator.databaseOperation();

        datums.put(datum.key, datum);

        if (increaseTerm) {
            if (peers.isLeader()) {
                local.term.addAndGet(Constants.PUBLISH_TERM_INCREASE_COUNT);
            } else {
                if (local.term.get() + Constants.PUBLISH_TERM_INCREASE_COUNT > source.term.get()) {
                    //set leader term:
                    peers.getLeader().term.set(source.term.get());
                    local.term.set(peers.getLeader().term.get());
                } else {
                    local.term.addAndGet(Constants.PUBLISH_TERM_INCREASE_COUNT);
                }
            }
        }

        Loggers.RAFT.info("data added/updated, key={}, term={}", datum.key, local.term);
    }


    public int datumSize() {
        return datums.size();
    }

    public void addDatum(Datum datum) {
        datums.put(datum.key, datum);
    }

    public static Datum getDatum(String key) {
        return datums.get(key);
    }
}
