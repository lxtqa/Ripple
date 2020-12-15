package ripple.common;

import java.util.Objects;

/**
 * @author Zhen Tang
 */
public class Item {
    private String applicationName;
    private String key;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(applicationName, item.applicationName) && Objects.equals(key, item.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, key);
    }

    @Override
    public String toString() {
        return "Item{" +
                "applicationName='" + applicationName + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public Item(String applicationName, String key) {
        this.setApplicationName(applicationName);
        this.setKey(key);
    }

    public Item() {

    }
}