package ripple.engine.api.controller;

import ripple.engine.api.model.AgentModel;
import ripple.engine.entity.Agent;
import ripple.engine.helper.ModelHelper;
import ripple.engine.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fuxiao.tz
 */
@RestController
public class RegistryController {
    private RegistryService registryService;
    private ModelHelper modelHelper;

    private RegistryService getRegistryService() {
        return registryService;
    }

    @Autowired
    private void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    private ModelHelper getModelHelper() {
        return modelHelper;
    }

    @Autowired
    private void setModelHelper(ModelHelper modelHelper) {
        this.modelHelper = modelHelper;
    }


    @RequestMapping(value = {"/Api/Registry/Agent"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public AgentModel registerAgent(@RequestHeader("x-ripple-engine-uuid") UUID uuid
            , @RequestHeader("x-ripple-engine-address") String address
            , @RequestHeader("x-ripple-engine-port") int port) {
        return this.getModelHelper().parseAgent(
                this.getRegistryService().registerAgent(uuid, address, port));
    }

    @RequestMapping(value = {"/Api/Registry/Agent"}, method = {RequestMethod.GET})
    public List<AgentModel> getAgentList() {
        List<AgentModel> ret = new ArrayList<>();
        List<Agent> list = this.getRegistryService().getAgentList();
        for (Agent agent : list) {
            ret.add(this.getModelHelper().parseAgent(agent));
        }
        return ret;
    }

}
