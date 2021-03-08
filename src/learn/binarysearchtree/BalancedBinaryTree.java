package learn.binarysearchtree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个二叉树，确定它是否高度平衡。对于此问题，高度平衡二叉树定义为：
 * - 一种二叉树，其中每个节点的左、右子树的高度相差不超过 1。
 *
 * 例 1：
 * Input: root = [3,9,20,null,null,15,7]
 * Output: true
 * Explanation:
 *     3
 *   /   \
 *  9    20
 *     /   \
 *    15    7
 *
 * 例 2：
 * Input: root = [1,2,2,3,3,null,null,4,4]
 * Output: false
 * Explanation:
 *             1
 *           /  \
 *         2     2
 *       /  \
 *     3    3
 *   /  \
 *  4   4
 *
 * 例 3：
 * Input: root = []
 * Output: true
 *
 * 约束：
 * - 树中结点数范围为 [0, 5000]
 * - -10**4 <= Node.val <= 10**4
 */
public class BalancedBinaryTree {

    static void test(Predicate<TreeNode> method) {
        assertTrue(method.test(newTree(3,9,20,null,null,15,7)));

        assertFalse(method.test(newTree(1,2,2,3,3,null,null,4,4)));

        assertTrue(method.test(null));
    }

    boolean balanced;

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public boolean isBalanced(TreeNode root) {
        balanced = true;
        heightCheck(root);
        return balanced;
    }

    private int heightCheck(TreeNode root) {
        if (root == null)
            return 0;
        if (balanced) {
            int leftHeight = heightCheck(root.left);
            int rightHeight = heightCheck(root.right);
            if (Math.abs(leftHeight - rightHeight) > 1)
                balanced = false;
            return Math.max(leftHeight, rightHeight) + 1;
        }

        return 0;
    }

    @Test
    public void testIsBalanced() {
        test(this::isBalanced);
    }
}
