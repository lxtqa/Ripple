package ripple.server.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.common.Item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class Storage {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String fileName;

    public Storage(String fileName) {
        this.setFileName(fileName);
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Item get(String applicationName, String key) {
        try {
            List<Item> items = null;
            if (Files.exists(Paths.get(this.getFileName()))) {
                items = MAPPER.readValue(new File(this.getFileName()), new TypeReference<List<Item>>() {
                });
            } else {
                return null;
            }
            for (Item elem : items) {
                if (elem.getApplicationName().equals(applicationName)
                        && elem.getKey().equals(key)) {
                    return elem;
                }
            }
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public List<Item> getAll() {
        try {
            if (Files.exists(Paths.get(this.getFileName()))) {
                return MAPPER.readValue(new File(this.getFileName()), new TypeReference<List<Item>>() {
                });
            } else {
                return null;
            }
        } catch (IOException exception) {
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
                if (elem.getApplicationName().equals(item.getApplicationName())
                        && elem.getKey().equals(item.getKey())) {
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

    public synchronized boolean delete(Item item) {
        try {
            List<Item> items = null;
            if (Files.exists(Paths.get(this.getFileName()))) {
                items = MAPPER.readValue(new File(this.getFileName()), new TypeReference<List<Item>>() {
                });
            } else {
                return false;
            }
            Item toDelete = null;
            for (Item elem : items) {
                if (elem.getApplicationName().equals(item.getApplicationName())
                        && elem.getKey().equals(item.getKey())) {
                    toDelete = elem;
                }
            }
            if (toDelete != null) {
                items.remove(toDelete);
            }
            MAPPER.writeValue(new File(this.getFileName()), items);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}

