package ripple.server.core.resolver;

import ripple.common.entity.Item;
import ripple.server.core.Operation;

/**
 * An Add-win resolver which guarantee the "Add win" semantics.
 * Currently, it solves conflicts by calculating union of two sets, and discards removal operations.
 * Still needs to be improved for a production-ready CRDT (Conflict-free Replicated Data Type).
 *
 * @author Zhen Tang
 */
public class AddWinConflictResolver implements OperationBasedConflictResolver {
    private long maxTimeDifference;

    public AddWinConflictResolver(long maxTimeDifference) {
        this.setMaxTimeDifference(maxTimeDifference);
    }

    public long getMaxTimeDifference() {
        return maxTimeDifference;
    }

    private void setMaxTimeDifference(long maxTimeDifference) {
        this.maxTimeDifference = maxTimeDifference;
    }

    @Override
    public void merge(Item current, Operation toApply) {
        // TODO
//        if (!(current.getValue() instanceof String)) {
//            // Only merge String now
//            return;
//        }
//        long timeDifference = toApply.getTimestamp().getTime() - current.getLastUpdate().getTime();
//        if (timeDifference < 0 && (-timeDifference) > this.getMaxTimeDifference()) {
//            // Received obsoleted data, discard
//            return;
//        }
//        if (timeDifference > 0 && timeDifference > this.getMaxTimeDifference()) {
//            // Accept operation
//            String value = current.getValue();
//            if (toApply.getOperationType() == OperationType.ADD_ENTRY) {
//                this.doAddEntry(value, toApply);
//            } else if (toApply.getOperationType() == OperationType.REMOVE_ENTRY) {
//                this.doRemoveEntry(value, toApply);
//            }
//        } else {
//            // Resolve conflict
//            String value = current.getValue();
//            if (toApply.getOperationType() == OperationType.ADD_ENTRY) {
//                this.doAddEntry(value, toApply);
//            }
//            // Discard conflict removal operation
//
//        }
//        if (timeDifference > 0) {
//            current.getLastUpdate().setTime(toApply.getTimestamp().getTime());
//        }
    }

    private void doAddEntry(String value, Operation toApply) {
        // Add entry here

    }

    private void doRemoveEntry(String value, Operation toApply) {
        // Remove entry here

    }
}

