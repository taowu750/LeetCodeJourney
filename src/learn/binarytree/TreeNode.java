package learn.binarytree;

import util.datastructure.BinaryTreeNode;

/**
 * 二叉树结点，和 LeetCode 的二叉树结点名称保持一致
 */
public class TreeNode extends BinaryTreeNode<TreeNode> {

    public TreeNode() {
    }

    public TreeNode(int val) {
        super(val);
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        super(val, left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TreeNode))
            return false;
        TreeNode other = (TreeNode) obj;
        return equals(this, other);
    }

    @Override
    public String toString() {
        return "TreeNode(" +
                + val +
                ')';
    }

    public static TreeNode newTree(Integer... vals) {
        return newTree(TreeNode.class, vals);
    }
}
