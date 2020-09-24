package ripple.engine.service;

import ripple.engine.entity.Agent;
import ripple.engine.entity.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@Component
public class RegistryService {
    private static Logger logger = LoggerFactory.getLogger(RegistryService.class);
    private Context context;

    public Context getContext() {
        return context;
    }

    @Autowired
    private void setContext(Context context) {
        this.context = context;
    }

    public Agent registerAgent(UUID uuid, String address, int port) {
        Agent agent = new Agent();
        agent.setUuid(uuid);
        agent.setAddress(address);
        agent.setPort(port);
        this.getContext().getAgents().put(uuid, agent);
        logger.info("agent ip = " + address + ", port = " + port);
        return agent;
    }

    public List<Agent> getAgentList() {
        return Collections.list(this.getContext().getAgents().elements());
    }
}
