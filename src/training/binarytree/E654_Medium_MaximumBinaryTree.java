package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个不重复的整数数组 nums。可以使用以下算法从 nums 递归地构建最大二叉树：
 * 1. 创建一个根节点，它的值是 nums 中的最大值。
 * 2. 递归地在最大值左边的子数组上构建左子树。
 * 3. 递归地在最大值右边的子数组上构建右子树。
 *
 * 返回从 nums 生成的最大二叉树。
 *
 * 例 1：
 * Input: nums = [3,2,1,6,0,5]
 * Output: [6,3,5,null,2,0,null,null,1]
 * Explanation:
 *       6
 *     /   \
 *   3      5
 *    \    /
 *     2  0
 *      \
 *       1
 *
 * 例 2:
 * Input: nums = [3,2,1]
 * Output: [3,null,2,null,1]
 * Explanation:
 *   3
 *    \
 *     2
 *      \
 *       1
 *
 * 约束：
 * - 1 <= nums.length <= 1000
 * - 0 <= nums[i] <= 1000
 * - 所有整数是唯一的
 */
public class E654_Medium_MaximumBinaryTree {

    static void test(Function<int[], TreeNode> method) {
        assertTrue(TreeNode.equals(method.apply(new int[]{3,2,1,6,0,5}),
                newTree(6,3,5,null,2,0,null,null,1)));

        assertTrue(TreeNode.equals(method.apply(new int[]{3,2,1}),
                newTree(3,null,2,null,1)));
    }

    /**
     * LeetCode 耗时：1ms - 100%
     *          内存消耗：38.8MB - 93.99%
     */
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        return recur(nums, 0, nums.length - 1);
    }

    private TreeNode recur(int[] nums, int lo, int hi) {
        if (lo > hi)
            return null;
        else if (lo == hi)
            return new TreeNode(nums[lo]);
        int max = nums[lo], idx = lo;
        for (int i = lo + 1; i <= hi; i++) {
            if (max < nums[i]) {
                max = nums[i];
                idx = i;
            }
        }
        TreeNode root = new TreeNode(max);
        root.left = recur(nums, lo, idx - 1);
        root.right = recur(nums, idx + 1, hi);
        return root;
    }

    @Test
    public void testConstructMaximumBinaryTree() {
        test(this::constructMaximumBinaryTree);
    }
}
