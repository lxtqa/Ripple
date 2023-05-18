// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.entity;

/**
 * @author Zhen Tang
 */
public final class Constants {
    public static final String MESSAGE_TYPE_UPDATE = "update";
    public static final String MESSAGE_TYPE_DELETE = "delete";
    public static final String MESSAGE_TYPE_INCREMENTAL_UPDATE = "incremental_update";

    public static final String ATOMIC_OPERATION_ADD_ENTRY = "add_entry";
    public static final String ATOMIC_OPERATION_REMOVE_ENTRY = "remove_entry";

    private Constants() {

    }
}
