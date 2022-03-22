package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 96. 不同的二叉搜索树: https://leetcode-cn.com/problems/unique-binary-search-trees/
 *
 * 给你一个整数 n ，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的「二叉搜索树」有多少种？返回满足题意的二叉搜索树的种数。
 *
 * 例 1：
 * 输入：n = 3
 * 输出：5
 *
 * 例 2：
 * 输入：n = 1
 * 输出：1
 *
 * 提示：
 * - 1 <= n <= 19
 */
public class E96_Medium_UniqueBinarySearchTrees {

    public static void test(IntUnaryOperator method) {
        assertEquals(5, method.applyAsInt(3));
        assertEquals(1, method.applyAsInt(1));
        assertEquals(14, method.applyAsInt(4));
        assertEquals(42, method.applyAsInt(5));
        assertEquals(132, method.applyAsInt(6));
        assertEquals(1767263190, method.applyAsInt(19));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.1 MB - 54.97%
     */
    public int numTrees(int n) {
        Map<Integer, Integer> cache = new HashMap<>();
        return dfs(1, n, cache);
    }

    private int dfs(int lo, int hi, Map<Integer, Integer> cache) {
        if (lo >= hi) {
            return 1;
        }

        int sum = cache.getOrDefault(hi - lo, 0);
        if (sum != 0) {
            return sum;
        }

        // 选择 lo 到 hi 的每个数作为根节点
        for (int i = lo; i <= hi; i++) {
            // 左子树的数量
            int left = dfs(lo, i - 1, cache);
            // 右子树的数量
            int right = dfs(i + 1, hi, cache);
            sum += left * right;
        }
        cache.put(hi - lo, sum);

        return sum;
    }

    @Test
    public void testNumTrees() {
        test(this::numTrees);
    }


    /**
     * 动态规划解法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：34.8 MB - 96.41%
     */
    public int dpMethod(int n) {
        // dp[i][j] 表示从 i..j 范围内不同二叉树的数量
        int[][] dp = new int[n + 2][n + 2];
        dp[0][0] = 1;
        for (int i = 1; i < n + 2; i++) {
            dp[i][i] = 1;
            // 为了处理 i..i-1 的特殊情况
            dp[i][i - 1] = 1;
        }

        for (int i = n - 1; i >= 1; i--) {
            for (int j = i + 1; j <= n; j++) {
                for (int root = i; root <= j; root++) {
                    dp[i][j] += dp[i][root - 1] * dp[root + 1][j];
                }
            }
        }

        return dp[1][n];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
