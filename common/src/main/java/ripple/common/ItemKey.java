package ripple.common;

import java.util.Objects;

/**
 * @author Zhen Tang
 */
public class ItemKey {
    private String applicationName;
    private String key;

    public String getApplicationName() {
        return applicationName;
    }

    private void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    public ItemKey(String applicationName, String key) {
        this.setApplicationName(applicationName);
        this.setKey(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemKey itemKey = (ItemKey) o;
        return Objects.equals(applicationName, itemKey.applicationName) &&
                Objects.equals(key, itemKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationName, key);
    }

    @Override
    public String toString() {
        return "ItemKey{" +
                "applicationName='" + applicationName + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
