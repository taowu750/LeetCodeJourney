package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 124. 二叉树中的最大路径和: https://leetcode-cn.com/problems/binary-tree-maximum-path-sum/
 *
 * 路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中「至多出现一次」。
 * 该路径「至少包含一个」节点，且不一定经过根节点。
 *
 * 「路径和」是路径中各节点值的总和。给你一个二叉树的根节点 root ，返回其「最大路径和」。
 *
 * 例 1：
 * 输入：root = [1,2,3]
 * 输出：6
 * 解释：最优路径是 2 -> 1 -> 3 ，路径和为 2 + 1 + 3 = 6
 *      1
 *    /  \
 *   2   3
 *
 * 例 2：
 * 输入：root = [-10,9,20,null,null,15,7]
 * 输出：42
 * 解释：最优路径是 15 -> 20 -> 7 ，路径和为 15 + 20 + 7 = 42
 *     -10
 *    /   \
 *   9    20
 *       /  \
 *      15   7
 *
 * 约束：
 * - 树中节点数目范围是 [1, 3 * 10**4]
 * - -1000 <= Node.val <= 1000
 */
public class E124_Hard_BinaryTreeMaximumPathSum {

    static void test(ToIntFunction<TreeNode> method) {
        assertEquals(6, method.applyAsInt(newTree(1,2,3)));
        assertEquals(42, method.applyAsInt(newTree(-10,9,20,null,null,15,7)));
        assertEquals(43, method.applyAsInt(newTree(-1,9,20,null,null,15,7)));
        assertEquals(2, method.applyAsInt(newTree(2,-1)));
    }

    /**
     * LeetCode 耗时：9 ms - 100.00%
     *          内存消耗：40.6 MB - 7.93%
     */
    public int maxPathSum(TreeNode root) {
        // 一个节点到子节点的最大单条路径和
        Map<TreeNode, Integer> maxSinglePathSum = new HashMap<>();
        return dfs(root, maxSinglePathSum);
    }

    // 返回以 root 为根节点的最大路径和，并且记录它到子节点的最大单条路径和
    private int dfs(TreeNode root, Map<TreeNode, Integer> maxSinglePathSum) {
        int maxPathSum = Integer.MIN_VALUE;
        if (root == null)
            return maxPathSum;

        // 首先选择左右子树的最大路径和
        maxPathSum = Math.max(dfs(root.left, maxSinglePathSum),
                dfs(root.right, maxSinglePathSum));

        int leftMaxSinglePathSum = root.left != null ? maxSinglePathSum.get(root.left) : 0;
        int rightMaxSinglePathSum = root.right != null ? maxSinglePathSum.get(root.right) : 0;
        // maxPathSum 无需和下面的作对比，因为上面的左右子树的最大路径和已经包含了下面的情况
        int leftRightMaxSinglePathSum = Math.max(leftMaxSinglePathSum, rightMaxSinglePathSum);

        // 如果左右最大单条路径和都是负的，那么就不需要它们
        int rootMaxSinglePathSum = root.val + Math.max(leftRightMaxSinglePathSum, 0);
        maxSinglePathSum.put(root, rootMaxSinglePathSum);
        // 和 root 的最大单条路径和最比较
        maxPathSum = Math.max(maxPathSum, rootMaxSinglePathSum);

        // 最后和穿过 root 的路径和作比较
        return Math.max(maxPathSum, root.val + leftMaxSinglePathSum + rightMaxSinglePathSum);
    }

    @Test
    public void testMaxPathSum() {
        test(this::maxPathSum);
    }
}
