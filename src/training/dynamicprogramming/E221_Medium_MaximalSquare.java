package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 221. 最大正方形: https://leetcode-cn.com/problems/maximal-square/
 *
 * 例 1：
 * 输入：matrix = [["1","0","1","0","0"],
 *                ["1","0","1","1","1"],
 *                ["1","1","1","1","1"],
 *                ["1","0","0","1","0"]]
 * 输出：4
 *
 * 例 2：
 * 输入：matrix = [["0","1"],["1","0"]]
 * 输出：1
 *
 * 例 3：
 * 输入：matrix = [["0"]]
 * 输出：0
 *
 * 约束：
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 300
 * - matrix[i][j] 为 '0' 或 '1'
 */
public class E221_Medium_MaximalSquare {

    static void test(ToIntFunction<char[][]> method) {
        assertEquals(4, method.applyAsInt(new char[][]{
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'},
        }));

        assertEquals(1, method.applyAsInt(new char[][]{
                {'0', '1'},
                {'1', '0'},
        }));

        assertEquals(0, method.applyAsInt(new char[][]{{'0'}}));
    }

    /**
     * 类似于 {@link training.array.E85_Hard_MaximalRectangle} 的暴力方法。
     *
     * LeetCode 耗时：11 ms - 10%
     *          内存消耗：41.9 MB - 8%
     */
    public int maximalSquare(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int m = matrix.length, n = matrix[0].length, max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 遍历每个 '1'，以它作为正方形的起始点
                if (matrix[i][j] == '0') {
                    continue;
                }
                // 找到最右边的边界
                int right = j;
                while (right + 1 <= n - 1 && matrix[i][right + 1] == '1') {
                    right++;
                }
                max = Math.max(max, 1);

                // 每往下扩展一行，就计算新的正方形的面积
                int bottom = i + 1, limit = Math.min(m, i + right - j + 1);
                while (bottom < limit && matrix[bottom][j] == '1') {
                    int k = j;
                    while (k + 1 <= right && matrix[bottom][k + 1] == '1') {
                        k++;
                    }
                    // 记住要收缩边界
                    right = k;
                    limit = Math.min(m, i + right - j + 1);
                    // 如果不能构成正方形了，就跳出
                    if (right - j + 1 < bottom - i + 1) {
                        break;
                    }
                    max = Math.max(max, (bottom - i + 1) * (bottom - i + 1));
                    bottom++;
                }
            }
        }

        return max;
    }

    @Test
    public void testMaximalSquare() {
        test(this::maximalSquare);
    }


    /**
     * LeetCode 耗时：5 ms - 95%
     *          内存消耗：41.8 MB - 17%
     */
    public int dpMethod(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int m = matrix.length, n = matrix[0].length, maxSlide = 0;
        // dp[i][j] 表示以 (i, j) 为右下角正方形的边长
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '0') {
                    continue;
                }
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    // 对角线的边长、左边长、上边长的最小值
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j])) + 1;
                }
                maxSlide = Math.max(maxSlide, dp[i][j]);
            }
        }

        return maxSlide * maxSlide;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：5 ms - 94.84%
     *          内存消耗：41.6 MB - 50.23%
     */
    public int compressedMethod(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int m = matrix.length, n = matrix[0].length, maxSlide = 0;
        int[] dp = new int[n];
        for (int i = 0; i < m; i++) {
            int prev = 0;
            for (int j = 0; j < n; j++) {
                int tmp = dp[j];
                if (matrix[i][j] == '0') {
                    dp[j] = 0;
                } else {
                    if (i == 0 || j == 0) {
                        dp[j] = 1;
                    } else {
                        // 对角线的边长、左边长、上边长的最小值
                        dp[j] = Math.min(prev, Math.min(dp[j - 1], dp[j])) + 1;
                    }
                }
                prev = tmp;

                maxSlide = Math.max(maxSlide, dp[j]);
            }
        }

        return maxSlide * maxSlide;
    }

    @Test
    public void testCompressedMethod() {
        test(this::compressedMethod);
    }
}
