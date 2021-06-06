package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 129. 求根节点到叶节点数字之和: https://leetcode-cn.com/problems/sum-root-to-leaf-numbers/
 *
 * 给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
 * 每条从根节点到叶节点的路径都代表一个数字：
 * - 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
 *
 * 计算从「根节点到叶节点」生成的「所有数字之和」。
 * 叶节点是指没有子节点的节点。
 *
 * 例 1：
 * 输入：root = [1,2,3]
 * 输出：25
 * 解释：
 *     1
 *    / \
 *   2   3
 * 从根到叶子节点路径 1->2 代表数字 12
 * 从根到叶子节点路径 1->3 代表数字 13
 * 因此，数字总和 = 12 + 13 = 25
 *
 * 例 2：
 * 输入：root = [4,9,0,5,1]
 * 输出：1026
 * 解释：
 *        4
 *       / \
 *      9  0
 *     / \
 *    5  1
 * 从根到叶子节点路径 4->9->5 代表数字 495
 * 从根到叶子节点路径 4->9->1 代表数字 491
 * 从根到叶子节点路径 4->0 代表数字 40
 * 因此，数字总和 = 495 + 491 + 40 = 1026
 *
 * 约束：
 * - 树中节点的数目在范围 [1, 1000] 内
 * - 0 <= Node.val <= 9
 * - 树的深度不超过 10
 */
public class E129_Medium_SumRootToLeafNumbers {

    static void test(ToIntFunction<TreeNode> method) {
        assertEquals(25, method.applyAsInt(newTree(1,2,3)));
        assertEquals(1026, method.applyAsInt(newTree(4,9,0,5,1)));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.9 MB - 46.37%
     */
    public int sumNumbers(TreeNode root) {
        result = 0;
        dfs(root, 0);

        return result;
    }

    private int result;

    private void dfs(TreeNode root, int num) {
        int cur = num * 10 + root.val;
        if (root.left == null && root.right == null) {
            result += cur;
            return;
        }
        if (root.left != null) {
            dfs(root.left, cur);
        }
        if (root.right != null) {
            dfs(root.right, cur);
        }
    }

    @Test
    public void testSumNumbers() {
        test(this::sumNumbers);
    }
}
