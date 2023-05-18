// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.resolver;

import ripple.common.entity.Item;

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
