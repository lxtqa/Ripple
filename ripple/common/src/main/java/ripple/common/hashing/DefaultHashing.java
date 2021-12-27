package ripple.common.hashing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class DefaultHashing implements Hashing {
    // TODO: Fix this

    @Override
    public List<String> hashing(String key, List<String> nodeList) {
        return new ArrayList<>(nodeList);
    }
}
