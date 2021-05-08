package training.binarysearchtree;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.BinaryTreeNode.*;

/**
 * 给定一个整数数组 nums，其中元素按升序排序，将其转换为高度平衡的二叉搜索树。
 * 高度平衡二叉树是一种二叉树，其中每个节点的两个子树的深度相差不超过一个。
 *
 * 例 1：
 * Input: nums = [-10,-3,0,5,9]
 * Output: [0,-3,9,-10,null,5]
 * Explanation:
 *        0            0
 *      /  \         /  \
 *    -3    9  或  -10   5
 *   /    /          \    \
 * -10   5           -3    9
 *
 * 例 2：
 * Input: nums = [1,3]
 * Output: [3,1]
 * Explanation: [1,3] 和 [3,1] 都是平衡二叉树。
 *
 * 约束：
 * - 1 <= nums.length <= 10**4
 * - -10**4 <= nums[i] <= 10**4
 * - nums 是以严格升序的顺序排序的
 */
public class E108_Easy_ConvertSortedArrayToBinarySearchTree {

    static void test(Function<int[], TreeNode> method) {
        TreeNode root = method.apply(new int[]{-10,-3,0,5,9});
        assertTrue(isValid(root));
        assertTrue(isBalanced(root));
        assertTrue(contentEquals(root, newTree(0,-3,9,-10,null,5)));

        root = method.apply(new int[]{1,3});
        assertTrue(isValid(root));
        assertTrue(isBalanced(root));
        assertTrue(contentEquals(root, newTree(1,3)));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return toBST(nums, 0, nums.length - 1);
    }

    private TreeNode toBST(int[] nums, int lo, int hi) {
        if (lo == hi) {
            return new TreeNode(nums[lo]);
        } else if (lo > hi)
            return null;

        int mid = (lo + hi) >>> 1;
        TreeNode curRoot = new TreeNode(nums[mid]);
        curRoot.left = toBST(nums, lo, mid - 1);
        curRoot.right = toBST(nums, mid + 1, hi);
        return curRoot;
    }

    @Test
    public void testSortedArrayToBST() {
        test(this::sortedArrayToBST);
    }
}
