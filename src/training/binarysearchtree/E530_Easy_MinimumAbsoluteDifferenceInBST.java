package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import training.binarytree.TreeNode;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 530. 二叉搜索树的最小绝对差: https://leetcode-cn.com/problems/minimum-absolute-difference-in-bst/
 *
 * 给你一个二叉搜索树的根节点 root ，返回树中任意两不同节点值之间的最小差值。
 * 差值是一个正数，其数值等于两值之差的绝对值。
 *
 * 例 1：
 * 输入：root = [4,2,6,1,3]
 * 输出：1
 *
 * 例 2：
 * 输入：root = [1,0,48,null,null,12,49]
 * 输出：1
 *
 * 说明：
 * - 树中节点的数目范围是 [2, 10^4]
 * - 0 <= Node.val <= 10^5
 */
public class E530_Easy_MinimumAbsoluteDifferenceInBST {

    public static void test(ToIntFunction<TreeNode> method) {
        assertEquals(1, method.applyAsInt(newTree(4,2,6,1,3)));
        assertEquals(1, method.applyAsInt(newTree(1,0,48,null,null,12,49)));
        /*
               236
              /   \
            104   701
              \     \
              227   911
         */
        assertEquals(9, method.applyAsInt(newTree(236,104,701,null,227,null,911)));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.1 MB - 67.80%
     */
    public int getMinimumDifference(TreeNode root) {
        result = Integer.MAX_VALUE;
        dfs(root);

        return result;
    }

    private int result;

    private void dfs(TreeNode root) {
        if (root.left != null) {
            result = Math.min(result, Math.abs(root.val - leftMax(root)));
            dfs(root.left);
        }
        if (root.right != null) {
            result = Math.min(result, Math.abs(root.val - rightMin(root)));
            dfs(root.right);
        }
    }

    private int leftMax(TreeNode root) {
        root = root.left;
        while (root.right != null) {
            root = root.right;
        }

        return root.val;
    }

    private int rightMin(TreeNode root) {
        root = root.right;
        while (root.left != null) {
            root = root.left;
        }

        return root.val;
    }

    @Test
    public void testGetMinimumDifference() {
        test(this::getMinimumDifference);
    }
}
