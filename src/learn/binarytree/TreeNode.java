package learn.binarytree;

import util.datastructure.BinaryTreeNode;

import java.util.Objects;

/**
 * 二叉树结点，和 LeetCode 的二叉树结点名称保持一致
 */
public class TreeNode extends BinaryTreeNode<TreeNode> implements Comparable<TreeNode> {

    private Integer compareVal;

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
    public int hashCode() {
        return Objects.hash(val, this.left != null ? this.left.hashCode() : 0,
                this.right != null ? this.right.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "(" + val + ')';
    }

    @Override
    public int compareTo(TreeNode o) {
        // 仅测试用，所以 compareVal 只计算一次不再更新
        if (compareVal == null)
            compareVal = hashCode();
        if (o.compareVal == null)
            o.compareVal = o.hashCode();
        return Integer.compare(compareVal, o.compareVal);
    }

    public static TreeNode newTree(Integer... vals) {
        return newTree(TreeNode.class, vals);
    }
}
