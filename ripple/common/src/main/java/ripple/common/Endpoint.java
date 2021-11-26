package ripple.common;

/**
 * @author Zhen Tang
 */
public final class Endpoint {
    // API
    public static final String API_UNSUBSCRIBE = "/Api/Unsubscribe";

    // Common UI
    public static final String UI_STYLE = "/Style";
    public static final String UI_HOME = "/";
    public static final String UI_GET_CONFIG = "/Config/Get";
    public static final String UI_ADD_CONFIG = "/Config/Add";
    public static final String UI_MODIFY_CONFIG = "/Config/Modify";
    public static final String UI_REMOVE_CONFIG = "/Config/Remove";
    public static final String UI_GET_SUBSCRIPTION = "/Subscription/Get";

    // Server UI
    public static final String UI_SERVER_CLUSTER = "/Cluster/Server";
    public static final String UI_CLIENT_CLUSTER = "/Cluster/Client";

    // Client UI
    public static final String UI_ADD_SUBSCRIPTION = "/Subscription/Add";
    public static final String UI_REMOVE_SUBSCRIPTION = "/Subscription/Remove";
    public static final String UI_SERVER_INFO = "/Info/Server";

    private Endpoint() {

    }
}
