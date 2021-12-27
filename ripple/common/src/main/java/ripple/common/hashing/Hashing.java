package ripple.common.hashing;

import java.util.List;

/**
 * @author Zhen Tang
 */
public interface Hashing {
    List<String> hashing(String key, List<String> nodeList);
}
