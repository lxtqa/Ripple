package ripple.common.tcp;

public interface Handler {
    Message handle(Message message);
}
