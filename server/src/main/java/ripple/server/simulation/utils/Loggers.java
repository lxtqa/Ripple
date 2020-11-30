package ripple.server.simulation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qingzhou.sjq
 */
public class Loggers {

    public static final Logger RAFT = LoggerFactory.getLogger("ripple.server.simulation.raft");
    public static final Logger LAYER = LoggerFactory.getLogger("ripple.server.simulation.layer");
    public static final Logger STAR = LoggerFactory.getLogger("ripple.server.simulation.star");
    public static final Logger TREE = LoggerFactory.getLogger("ripple.server.simulation.tree");
    public static final Logger REGISTRY_HELPER = LoggerFactory.getLogger("ripple.server.simulation.helper.RegistryHelper");
    public static final Logger SRV_LOG = LoggerFactory.getLogger("ripple.server.simulation.server");

}
