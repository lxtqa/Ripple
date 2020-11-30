package ripple.server.simulation.core.node.layer.server;

import java.util.UUID;

/**
 * @author qingzhou.sjq
 */
public class LayerMessage {
    public UUID msgUUID;
    public boolean fromSource = false;
    public boolean fromExternal = false;
    public String key;
    public String value;
}
