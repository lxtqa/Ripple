package ripple.common.storage;

import org.apache.commons.codec.binary.Base32;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StorageHelper {
    private StorageHelper() {

    }

    // Avoid illegal filename
    public static String encodeString(String str) {
        return new String(Base64.getUrlEncoder().encode(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static String decodeString(String str) {
        return new String(Base64.getUrlDecoder().decode(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
