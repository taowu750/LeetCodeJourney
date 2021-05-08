package training.binarytree;

import org.junit.jupiter.api.Test;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定二叉树的根和整数 targetSum，如果树具有从根到叶的路径（沿路径的所有值加起来等于 targetSum），
 * 则返回 true。
 *
 * 例 1：
 * Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
 * Output: true
 * Explanation:
 *      (5)
 *      / \
 *    (4)  8
 *    /   / \
 *  (11) 13  4
 *  /  \      \
 * 7   (2)     1
 *
 * 例 2：
 * Input: root = [1,2,3], targetSum = 5
 * Output: false
 * Explanation:
 *   1
 *  / \
 * 2   3
 *
 * 例 3：
 * Input: root = [1,2], targetSum = 0
 * Output: false
 *
 * 约束：
 * - 结点数范围为 [0,5000]
 * - -1000 <= Node.val <= 1000
 * - -1000 <= targetSum <= 1000
 */
public class E112_Easy_PathSum {

    interface HasPathSumMethod {
        boolean hasPathSum(TreeNode root, int targetSum);
    }

    static void test(HasPathSumMethod method) {
        TreeNode root = newTree(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1);
        assertTrue(method.hasPathSum(root, 22));

        root = newTree(1, 2, 3);
        assertFalse(method.hasPathSum(root, 5));

        root = newTree(1, 2, 3);
        assertTrue(method.hasPathSum(root, 4));

        root = newTree(1, 2);
        assertFalse(method.hasPathSum(root, 0));

        root = newTree(1, 2);
        assertFalse(method.hasPathSum(root, 1));
    }

    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null)
            return false;
        if (root.left == null && root.right == null && root.val == targetSum)
            return true;
        else
            return hasPathSum(root.left, targetSum - root.val)
                || hasPathSum(root.right, targetSum - root.val);
    }

    @Test
    public void testHasPathSum() {
        test(this::hasPathSum);
    }
}
