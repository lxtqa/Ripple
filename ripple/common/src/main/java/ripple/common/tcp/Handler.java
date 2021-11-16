package ripple.common.tcp;

public interface Handler {
    Message handle(Message request);

    boolean canHandle(Message request);
}
