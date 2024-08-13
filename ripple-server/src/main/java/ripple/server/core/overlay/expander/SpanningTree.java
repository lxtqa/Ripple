// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.overlay.expander;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class SpanningTree {
    private TreeNode root;
    private List<TreeNode> nodeList;

    public SpanningTree(TreeNode root) {
        this.setRoot(root);
        this.setNodeList(new ArrayList<>());
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<TreeNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<TreeNode> nodeList) {
        this.nodeList = nodeList;
    }

}
