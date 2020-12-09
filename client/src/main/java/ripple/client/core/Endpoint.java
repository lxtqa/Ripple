package ripple.client.core;

/**
 * @author Zhen Tang
 */
public final class Endpoint {
    // Server
    public static final String SERVER_SUBSCRIBE = "/Api/Subscribe";
    public static final String SERVER_UNSUBSCRIBE = "/Api/Unsubscribe";
    public static final String SERVER_GET = "/Api/Get";
    public static final String SERVER_PUT = "/Api/Put";
    public static final String SERVER_DELETE = "/Api/Delete";
    // Client
    public static final String CLIENT_NOTIFY = "/Api/Notify";
    // UI
    public static final String UI_STYLE = "/Style";
    public static final String UI_HOME = "/";
    public static final String UI_GET_CONFIG = "/Config/Get";
    public static final String UI_ADD_CONFIG = "/Config/Add";
    public static final String UI_MODIFY_CONFIG = "/Config/Modify";
    public static final String UI_REMOVE_CONFIG = "/Config/Remove";
    public static final String UI_GET_SUBSCRIPTION = "/Subscription/Get";
    public static final String UI_ADD_SUBSCRIPTION = "/Subscription/Add";
    public static final String UI_REMOVE_SUBSCRIPTION = "/Subscription/Remove";
    public static final String UI_SERVER_INFO = "/Info/Server";
    private Endpoint() {

    }
}
