package ripple.server.core.resolver;

import ripple.common.entity.Item;
import ripple.server.core.Operation;

/**
 * Public interface for a operation based conflict resolver.
 *
 * @author Zhen Tang
 */
public interface OperationBasedConflictResolver {
    /**
     * Apply target operation on local data replica and resolve conflicts for concurrent operations
     *
     * @param current current value of the item
     * @param toApply the operation to apply
     */
    void merge(Item current, Operation toApply);
}
