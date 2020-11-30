package ripple.server.simulation.helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ripple.server.simulation.core.node.star.server.UpdateServlet;
import ripple.server.simulation.utils.Constants;
import ripple.server.simulation.utils.Loggers;
import ripple.server.simulation.utils.Util;
import ripple.server.simulation.utils.EngineConfig;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
//import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ripple.server.simulation.core.node.AbstractPeer;

import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegistryHelper {

    private static final Logger logger = LoggerFactory.getLogger(RegistryHelper.class);

//    private EngineConfig engineConfig;
//
//    public EngineConfig getEngineConfig() {
//        return engineConfig;
//    }
//
//    @Autowired
//    public void setEngineConfig(EngineConfig engineConfig){
//        this.engineConfig = engineConfig;
//    }

    public void registerAgent(UUID uuid, String address, int port) {
//        String url = Util.buildURL(Constants.ENGINE_REGISTRY_PREFIX, Constants.ENGINE_REGISTRY_AGENT);
//        System.out.println("engine ip is " + Constants.ENGINE_REGISTRY_PREFIX);
        String url = Util.buildURL(Constants.ENGINE_REGISTRY_PREFIX, Constants.ENGINE_REGISTRY_AGENT);
//        System.out.println("engineip is " + engineConfig.getEngineIp());
        Map<String, String> parameters = new HashMap<>(3);
        parameters.put("x-nacos-simulation-engine-uuid", uuid.toString());
        parameters.put("x-nacos-simulation-engine-address",address);
        parameters.put("x-nacos-simulation-engine-port",Integer.toString(port));

        HttpClient.httpPostLarge(url, parameters,"");
        // TODO:
    }

    public void unregisterAgent(UUID uuid) {
        // TODO:
    }

    public void registerClientNode(UUID uuid, UUID agentUuid, String address, int port) {
        String url = Util.buildURL(Constants.ENGINE_REGISTRY_PREFIX, Constants.ENGINE_REGISTRY_CLIENT);
        Loggers.REGISTRY_HELPER.info("client register to engine: {}", url);
        Map<String, String> parameters = new HashMap<>(4);
        parameters.put("x-nacos-simulation-engine-uuid", uuid.toString());
        parameters.put("x-nacos-simulation-engine-agent-uuid", agentUuid.toString());
        parameters.put("x-nacos-simulation-engine-address",address);
        parameters.put("x-nacos-simulation-engine-port",Integer.toString(port));

        HttpClient.httpPostLarge(url, parameters,"");
        // TODO:
    }

    public void unregisterClientNode(UUID uuid) {
        // TODO:
    }

    public void registerServerNode(UUID uuid, UUID agentUuid, String address, int port) {
        String url = Util.buildURL(Constants.ENGINE_REGISTRY_PREFIX, Constants.ENGINE_REGISTRY_SERVER);
        Loggers.REGISTRY_HELPER.info("server register to engine: {}", url);
        Map<String, String> parameters = new HashMap<>(4);
        parameters.put("x-nacos-simulation-engine-uuid", uuid.toString());
        parameters.put("x-nacos-simulation-engine-agent-uuid", agentUuid.toString());
        parameters.put("x-nacos-simulation-engine-address",address);
        parameters.put("x-nacos-simulation-engine-port",Integer.toString(port));

        HttpClient.httpPostLarge(url, parameters,"");

        // TODO:
    }

    public void unregisterServerNode(UUID uuid) {
        // TODO:
    }

    public List<AbstractPeer> getServerListFromEngine(){
        List<AbstractPeer> servers = new ArrayList<AbstractPeer>();
        String url = Util.buildURL(Constants.ENGINE_REGISTRY_PREFIX, Constants.ENGINE_REGISTRY_SERVER);
        Loggers.REGISTRY_HELPER.info("get server list from engine: {}", url);
        Map<String, String> headers = new HashMap<>(1);
        headers.put("k","v");
        HttpClient.HttpResult result = HttpClient.httpGet(url,null,headers);
        String entity = result.getContent();
        JSONArray jarray = JSONArray.parseArray(entity);
        for(int i=0;i<jarray.size();i++){
            JSONObject job = jarray.getJSONObject(i);
            UUID uuid = UUID.fromString(job.getString("uuid"));
            String ip = job.getString("address") + ":" + job.getString("port");
            AbstractPeer peer = new AbstractPeer(uuid,ip);
            servers.add(peer);
        }

        return servers;
    }
}
