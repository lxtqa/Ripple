package ripple.client.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.client.core.Item;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Storage(String fileName) {
        this.setFileName(fileName);
    }

    public Item get(String key) {
        try {
            List<Item> items = null;
            if (Files.exists(Paths.get(this.getFileName()))) {
                items = MAPPER.readValue(new File(this.getFileName()), new TypeReference<List<Item>>() {
                });
            } else {
                return null;
            }
            for (Item elem : items) {
                if (elem.getKey().equals(key)) {
                    return elem;
                }
            }
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public synchronized boolean put(Item item) {
        try {
            List<Item> items = null;
            if (Files.exists(Paths.get(this.getFileName()))) {
                items = MAPPER.readValue(new File(this.getFileName()), new TypeReference<List<Item>>() {
                });
            } else {
                items = new ArrayList<>();
            }
            Item toModify = null;
            for (Item elem : items) {
                if (elem.getKey().equals(item.getKey())) {
                    toModify = elem;
                }
            }
            if (toModify != null) {
                toModify.setKey(item.getKey());
                toModify.setLastUpdate(item.getLastUpdate());
                toModify.setLastUpdateServerId(item.getLastUpdateServerId());
                toModify.setValue(item.getValue());
            } else {
                items.add(item);
            }
            MAPPER.writeValue(new File(this.getFileName()), items);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}

