package ripple.server.core.resolver;

import ripple.common.Item;

/**
 * A simple grow-only conflict resolver.
 * For concurrent updates which can not be ordered, it calculates the union
 *
 * @author Zhen Tang
 */
public class GrowOnlyConflictResolver implements StateBasedConflictResolver {
    private long maxTimeDifference;

    public long getMaxTimeDifference() {
        return maxTimeDifference;
    }

    private void setMaxTimeDifference(long maxTimeDifference) {
        this.maxTimeDifference = maxTimeDifference;
    }

    public GrowOnlyConflictResolver(long maxTimeDifference) {
        this.setMaxTimeDifference(maxTimeDifference);
    }

    @Override
    public void merge(Item current, Item target) {
        if (!(current.getValue() instanceof String)) {
            // Only merge instances now
            return;
        }
        long timeDifference = target.getLastUpdate().getTime() - current.getLastUpdate().getTime();
        if (timeDifference < 0 && (-timeDifference) > this.getMaxTimeDifference()) {
            // Received obsoleted data, discard
            return;
        }
        if (timeDifference > 0 && timeDifference > this.getMaxTimeDifference()) {
            // Accept
            current.setValue(target.getValue());
            current.getLastUpdate().setTime(target.getLastUpdate().getTime());
        } else {
            // Resolve conflict. Merge two instance lists
            String currentValue = current.getValue();
            String toMergeValue = target.getValue();
            String mergedValue = this.doMerge(currentValue, toMergeValue);
            current.setValue(mergedValue);
            if (timeDifference > 0) {
                current.getLastUpdate().setTime(target.getLastUpdate().getTime());
            }
        }
    }

    private String doMerge(String currentValue, String toMergeValue) {
        // Merge values here
        return toMergeValue;
    }
}

