package ripple.common.tcp;

/**
 * @author Zhen Tang
 */
public interface Handler {
    Message handle(Message message);
}
