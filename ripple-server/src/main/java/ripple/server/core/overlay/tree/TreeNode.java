// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.overlay.tree;

import ripple.common.entity.NodeMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class TreeNode {
    private NodeMetadata nodeMetadata;
    private List<TreeNode> children;

    public TreeNode(NodeMetadata nodeMetadata) {
        this.setNodeMetadata(nodeMetadata);
        this.setChildren(new ArrayList<>());
    }

    public NodeMetadata getNodeMetadata() {
        return nodeMetadata;
    }

    private void setNodeMetadata(NodeMetadata nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    private void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
