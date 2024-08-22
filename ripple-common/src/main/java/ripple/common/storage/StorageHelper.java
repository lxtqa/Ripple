package ripple.common.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.entity.AbstractMessage;
import ripple.common.entity.Constants;
import ripple.common.entity.DeleteMessage;
import ripple.common.entity.IncrementalUpdateMessage;
import ripple.common.entity.UpdateMessage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StorageHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private StorageHelper() {

    }

    // Avoid illegal filename
    public static String encodeString(String str) {
        return new String(Base64.getUrlEncoder().encode(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static String decodeString(String str) {
        return new String(Base64.getUrlDecoder().decode(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static String serializeMessage(AbstractMessage message) {
        try {
            return MAPPER.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static AbstractMessage deserializeMessage(String content) {
        try {
            String type = MAPPER.readTree(content).get("type").asText();
            switch (type) {
                case Constants.MESSAGE_TYPE_DELETE:
                    return MAPPER.readValue(content, DeleteMessage.class);
                case Constants.MESSAGE_TYPE_UPDATE:
                    return MAPPER.readValue(content, UpdateMessage.class);
                case Constants.MESSAGE_TYPE_INCREMENTAL_UPDATE:
                    return MAPPER.readValue(content, IncrementalUpdateMessage.class);
            }
            return null;
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
