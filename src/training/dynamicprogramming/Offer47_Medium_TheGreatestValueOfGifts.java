package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 47. 礼物的最大价值: https://leetcode-cn.com/problems/li-wu-de-zui-da-jie-zhi-lcof/
 *
 * 在一个 m*n 的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于 0）。
 * 你可以从棋盘的左上角开始拿格子里的礼物，并每次向右或者向下移动一格、直到到达棋盘的右下角。
 * 给定一个棋盘及其上面的礼物的价值，请计算你最多能拿到多少价值的礼物？
 *
 * 例 1：
 * 输入:
 * [
 *   [1,3,1],
 *   [1,5,1],
 *   [4,2,1]
 * ]
 * 输出: 12
 * 解释: 路径 1→3→5→2→1 可以拿到最多价值的礼物
 *
 * 约束：
 * - 0 < grid.length <= 200
 * - 0 < grid[0].length <= 200
 */
public class Offer47_Medium_TheGreatestValueOfGifts {

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(12, method.applyAsInt(new int[][]{
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}}));
        assertEquals(13, method.applyAsInt(new int[][]{
                {1, 2},
                {5, 6},
                {1, 1}}));
    }

    public int maxValue(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];

        dp[0][0] = grid[0][0];
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }

        return dp[m - 1][n - 1];
    }

    @Test
    public void testMaxValue() {
        test(this::maxValue);
    }


    /**
     * LeetCode 耗时：2 ms - 98.61%
     *          内存消耗：41.3 MB - 23.12%
     */
    public int compressMethod(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];

        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }

        for (int i = 1; i < m; i++) {
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++) {
                dp[j] = Math.max(dp[j], dp[j - 1]) + grid[i][j];
            }
        }

        return dp[n - 1];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
