package ripple.common.tcp;

public interface Resolver {
    boolean support(Message message);

    Message resolve(Message message);
}