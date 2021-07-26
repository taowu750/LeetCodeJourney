package training.backtracking;

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

    static void test(IntUnaryOperator method) {
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
        if (lo > hi) {
            return 0;
        } else if (lo == hi) {
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

            if (left == 0 || right == 0) {
                sum += Math.max(left, right);
            } else {
                sum += left * right;
            }
        }
        cache.put(hi - lo, sum);

        return sum;
    }

    @Test
    public void testNumTrees() {
        test(this::numTrees);
    }
}
