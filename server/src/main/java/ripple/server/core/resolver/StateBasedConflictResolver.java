package ripple.server.core.resolver;

import ripple.server.core.Item;

/**
 * Public interface for a state based conflict resolver.
 *
 * @author lostcharlie
 */
public interface StateBasedConflictResolver {
    /**
     * Apply target state on local data replica and resolve conflicts for concurrent updates
     *
     * @param current current value of the item
     * @param target  target value of the item
     */
    void merge(Item current, Item target);
}

