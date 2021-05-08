package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 543. 二叉树的直径: https://leetcode-cn.com/problems/diameter-of-binary-tree/
 *
 * 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
 * 这条路径可能穿过也可能不穿过根结点。
 *
 * 注意，两结点之间的路径长度是以它们之间边的数目表示。
 *
 * 例 1：
 * 给定二叉树
 *
 *           1
 *          / \
 *         2   3
 *        / \
 *       4   5
 * 返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
 */
public class E543_Easy_DiameterOfBinaryTree {

    static void test(ToIntFunction<TreeNode> method) {
        assertEquals(3, method.applyAsInt(newTree(1, 2, 3, 4, 5)));
        assertEquals(0, method.applyAsInt(null));
        assertEquals(0, method.applyAsInt(newTree(1)));

        /*

                    1
                   / \
                  2   3
                 / \
                4   5
               /     \
              6       7
             /         \
            8           9
         */
        assertEquals(6, method.applyAsInt(newTree(1, 2, 3, 4, 5, null, null, 6, null, null, 7,
                8, null, null, 9)));
    }

    /**
     * 类似于 {@link E124_Hard_BinaryTreeMaximumPathSum} 的解法。
     *
     * LeetCode 耗时：1 ms - 17.84%
     *          内存消耗：38.4 MB - 61.93%
     */
    public int diameterOfBinaryTree(TreeNode root) {
        return dfs(root)[0];
    }

    private int[] dfs(TreeNode root) {
        if (root == null || (root.left == null && root.right == null))
            return new int[]{0, 0};

        int[] left = dfs(root.left), right = dfs(root.right);
        int subMaxPath = Math.max(left[0], right[0]);

        int leftSinglePath = root.left != null ? left[1] + 1 : 0;
        int rightSinglePath = root.right != null ? right[1] + 1 : 0;
        int rootPath = leftSinglePath + rightSinglePath;

        return new int[]{Math.max(subMaxPath, rootPath), Math.max(leftSinglePath, rightSinglePath)};
    }

    @Test
    public void testDiameterOfBinaryTree() {
        test(this::diameterOfBinaryTree);
    }


    private int maxPathLength;

    /**
     * 保存最大长度。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.2 MB - 83.80%
     */
    public int betterMethod(TreeNode root) {
        if (root == null)
            return 0;

        maxPathLength = 0;
        depth(root);
        return maxPathLength;
    }

    private int depth(TreeNode root) {
        if (root.left == null && root.right == null)
            return 0;
        int leftDepth = root.left != null ? depth(root.left) + 1 : 0;
        int rightDepth = root.right != null ? depth(root.right) + 1 : 0;
        if (leftDepth + rightDepth > maxPathLength)
            maxPathLength = leftDepth + rightDepth;

        return Math.max(leftDepth, rightDepth);
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
