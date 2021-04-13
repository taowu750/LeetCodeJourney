package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 说明：每次只能「向下」或者「向右」移动一步。
 *
 * 例 1：
 * 输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
 * 输出：7
 * 解释：因为路径 1→3→1→1→1 的总和最小。
 *
 * 例 2：
 * 输入：grid = [[1,2,3],[4,5,6]]
 * 输出：12
 *
 * 约束：
 * - m == grid.length
 * - n == grid[i].length
 * - 1 <= m, n <= 200
 * - 0 <= grid[i][j] <= 100
 */
public class E64_Medium_MinimumPathSum {

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(7, method.applyAsInt(new int[][]{{1,3,1}, {1,5,1}, {4,2,1}}));
        assertEquals(12, method.applyAsInt(new int[][]{{1,2,3}, {4,5,6}}));
    }

    /**
     * LeetCode 耗时：2 ms - 98.17%
     *          内存消耗：41.3 MB - 31.78%
     */
    public int minPathSum(int[][] grid) {
        final int m = grid.length, n = grid[0].length;
        final int[] dp = new int[n];
        dp[0] = grid[0][0];
        for (int i = 1; i < n; i++)
            dp[i] = dp[i - 1] + grid[0][i];

        for (int i = 1; i < m; i++) {
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++) {
                dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        }

        return dp[n - 1];
    }

    @Test
    public void testMinPathSum() {
        test(this::minPathSum);
    }
}
