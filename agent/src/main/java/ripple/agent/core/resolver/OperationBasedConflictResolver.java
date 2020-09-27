package ripple.agent.core.resolver;

import ripple.agent.core.hlc.Datum;
import ripple.agent.core.hlc.Operation;

/**
 * Public interface for a operation based conflict resolver.
 *
 * @author lostcharlie
 */
public interface OperationBasedConflictResolver {
    /**
     * Apply target operation on local data replica and resolve conflicts for concurrent operations
     *
     * @param current current value of the datum
     * @param toApply the operation to apply
     */
    void merge(Datum current, Operation toApply);
}
