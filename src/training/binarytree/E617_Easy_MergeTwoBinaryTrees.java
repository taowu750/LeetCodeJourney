package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.BinaryOperator;

import static training.binarytree.TreeNode.newTree;

/**
 * 617. 合并二叉树: https://leetcode-cn.com/problems/merge-two-binary-trees/
 *
 * 给定两个二叉树，想象当你将它们中的一个覆盖到另一个上时，两个二叉树的一些节点便会重叠。
 *
 * 你需要将他们合并为一个新的二叉树。合并的规则是如果两个节点重叠，那么将他们的值相加作为节点合并后的新值，
 * 否则不为 NULL 的节点将直接作为新二叉树的节点。合并必须从两个树的根节点开始。
 *
 * 例 1：
 * 输入:
 * 	       Tree 1                    Tree 2
 *           1                         2
 *          / \                       / \
 *         3   2                     1   3
 *        /                           \   \
 *       5                             4   7
 * 输出:
 * 合并后的树:
 * 	     3
 * 	    / \
 * 	   4   5
 * 	  / \   \
 * 	 5   4   7
 */
public class E617_Easy_MergeTwoBinaryTrees {

    static void test(BinaryOperator<TreeNode> method) {
        TreeNode.equals(newTree(3,4,5,5,4,null,7), method.apply(newTree(1,3,2,5), newTree(2,1,3,null,4,null,7)));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 86.48%
     */
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        if (root1 == null) {
            return root2;
        } else if (root2 == null) {
            return root1;
        } else {
            root1.val += root2.val;
            root1.left = mergeTrees(root1.left, root2.left);
            root1.right = mergeTrees(root1.right, root2.right);

            return root1;
        }
    }

    @Test
    public void testMergeTrees() {
        test(this::mergeTrees);
    }
}
