package ripple.common.storage;

/**
 * @author Zhen Tang
 */
public class AckService {
    private Storage storage;

    public Storage getStorage() {
        return storage;
    }

    private void setStorage(Storage storage) {
        this.storage = storage;
    }

    public AckService(Storage storage) {
        this.setStorage(storage);
    }
}
