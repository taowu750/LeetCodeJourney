package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 1339. 分裂二叉树的最大乘积: https://leetcode-cn.com/problems/maximum-product-of-splitted-binary-tree/
 *
 * 给你一棵二叉树，它的根为 root。请你删除 1 条边，使二叉树分裂成两棵子树，且它们子树和的乘积尽可能大。
 *
 * 由于答案可能会很大，请你将结果对 10^9 + 7 取模后再返回。
 *
 * 例 1：
 * 输入：root = [1,2,3,4,5,6]
 * 输出：110
 * 解释：删除红色的边，得到 2 棵子树，和分别为 11 和 10 。它们的乘积是 110 （11*10）
 *
 * 例 2：
 * 输入：root = [1,null,2,3,4,null,null,5,6]
 * 输出：90
 * 解释：移除红色的边，得到 2 棵子树，和分别是 15 和 6 。它们的乘积为 90 （15*6）
 *
 * 例 3：
 * 输入：root = [2,3,9,10,7,8,6,5,4,11,1]
 * 输出：1025
 *
 * 例 4：
 * 输入：root = [1,1]
 * 输出：1
 *
 * 说明：
 * - 每棵树最多有 50000 个节点，且至少有 2 个节点。
 * - 每个节点的值在 [1, 10000] 之间。
 */
public class E1339_Medium_MaximumProductOfSplittedBinaryTree {

    public static void test(ToIntFunction<TreeNode> method) {
        assertEquals(110, method.applyAsInt(newTree(1,2,3,4,5,6)));
        assertEquals(90, method.applyAsInt(newTree(1,null,2,3,4,null,null,5,6)));
        assertEquals(1025, method.applyAsInt(newTree(2,3,9,10,7,8,6,5,4,11,1)));
        assertEquals(1, method.applyAsInt(newTree(1,1)));
        assertEquals(649079758, method.applyAsInt(newTree(43,71,611,287,90,319,null,766,533,null,565,191,844,405,912,1,546,334,780,109,232,997,336,962,null,162,148,562,463,399,238,null,534,156,null,494,null,834,18,null,null,null,null,256,910,null,552,null,null,956,545,859,163,589,454,null,119,null,null,null,null,null,null,null,null,803,188,776,null,407,429,null,850,287,967,299,51,157,903,null,797,616,776,null,null,83,null,null,487,null,null,null,965,null,509,null,null,null,null,null,null,461,795,null,null,null,null,987,503,691,772,399,738,944,822,null,874,null,null,null,null,858,null,null,null,null,null,null,null,null,null,null,917,null,null,621,370,null,null,836,null,null,null,null,null,null,411,null,null,null,null,463,411,149,null,417,69,null,null,null,614,942,283,30,675,null,44,null,null,null,null,139,173,823,null,381,null,null,851,null,null,null,586,null,null,null,null,826,338,null,null,null,247,null,null,null,null,null,846)));
    }

    /**
     * LeetCode 耗时：8 ms - 92.05%
     *          内存消耗：54.4 MB - 39.39%
     */
    public int maxProduct(TreeNode root) {
        long sum = reduceSum(root);
        result = 0;
        maxProduct(sum, root);

        return (int) (result % MOD);
    }

    /**
     * 将左右子树的和累计到 root 中。
     */
    private int reduceSum(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == root.right) {
            return root.val;
        }

        root.val += reduceSum(root.left) + reduceSum(root.right);

        return root.val;
    }

    private static final long MOD = 1000000007;
    private long result;

    private void maxProduct(long sum, TreeNode cur) {
        if (cur == null || cur.left == cur.right) {
            return;
        }
        // 总和减去子树的和 * 子树的和
        if (cur.left != null) {
            result = Math.max(result, (sum - cur.left.val) * cur.left.val);
        }
        if (cur.right != null) {
            result = Math.max(result, (sum - cur.right.val) * cur.right.val);
        }
        maxProduct(sum, cur.left);
        maxProduct(sum, cur.right);
    }

    @Test
    public void testMaxProduct() {
        test(this::maxProduct);
    }


    /**
     * 用到了均值不等式的思想，参见：
     * https://leetcode-cn.com/problems/maximum-product-of-splitted-binary-tree/solution/fen-lie-er-cha-shu-de-zui-da-cheng-ji-by-leetcode-/
     *
     * LeetCode 耗时：6 ms - 100.00%
     *          内存消耗：51.3 MB - 94.32%
     */
    public int mathMethod(TreeNode root) {
        sum = 0;
        best = 0;
        sum(root);
        findBest(root);

        return (int) ((long) (sum - best) * best % MOD);
    }

    private int sum, best;

    private void sum(TreeNode root) {
        if (root != null) {
            sum += root.val;
            sum(root.left);
            sum(root.right);
        }
    }

    private int findBest(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 以 root 为根的子树的和
        int cur = findBest(root.left) + findBest(root.right) + root.val;
        // 使用均值不等式的知识，当 sum 为定值时，cur 越接近 sum 的一半，cur * (sum - cur) 的值越大。
        // 我们只需要存储最接近 sum 的一半的那个 cur
        if (Math.abs(cur * 2 - sum) < Math.abs(best * 2 - sum)) {
            best = cur;
        }

        return cur;
    }

    @Test
    public void testMathMethod() {
        test(this::mathMethod);
    }
}
