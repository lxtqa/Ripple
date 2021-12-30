package ripple.common.hashing;

import org.junit.Test;
import ripple.common.entity.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class HashingTest {
    @Test
    public void testHashing() {
        List<NodeMetadata> nodeList = new ArrayList<>();
        int i = 0;
        for (i = 0; i < 10; i++) {
            nodeList.add(new NodeMetadata(i + 1, "127.0.0.1", i + 8001));
        }
        Hashing hashing = new ModHashing();
        List<NodeMetadata> ret = hashing.hashing("testApp", "test", nodeList);
        for (i = 0; i < ret.size(); i++) {
            System.out.println("Node " + ret.get(i).getId() + ": "
                    + ret.get(i).getAddress() + ":" + ret.get(i).getPort());
        }
    }
}
