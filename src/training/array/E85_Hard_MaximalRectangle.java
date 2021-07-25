package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 85. 最大矩形: https://leetcode-cn.com/problems/maximal-rectangle/
 *
 * 给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
 *
 * 例 1：
 * 输入：matrix = [["1","0","1","0","0"],
 *                ["1","0","1","1","1"],
 *                ["1","1","1","1","1"],
 *                ["1","0","0","1","0"]]
 * 输出：6
 * 解释：最大矩形如上图所示。
 *
 * 例 2：
 * 输入：matrix = []
 * 输出：0
 *
 * 例 3：
 * 输入：matrix = [["0"]]
 * 输出：0
 *
 * 例 4：
 * 输入：matrix = [["1"]]
 * 输出：1
 *
 * 例 5：
 * 输入：matrix = [["0","0"]]
 * 输出：0
 *
 * 约束：
 * - rows == matrix.length
 * - cols == matrix[0].length
 * - 0 <= row, cols <= 200
 * - matrix[i][j] 为 '0' 或 '1'
 */
public class E85_Hard_MaximalRectangle {

    static void test(ToIntFunction<char[][]> method) {
        assertEquals(6, method.applyAsInt(new char[][]{
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'},
        }));
        assertEquals(0, method.applyAsInt(new char[][]{}));
        assertEquals(0, method.applyAsInt(new char[][]{{'0'}}));
        assertEquals(1, method.applyAsInt(new char[][]{{'1'}}));
        assertEquals(0, method.applyAsInt(new char[][]{{'0', '0'}}));
        assertEquals(6, method.applyAsInt(new char[][]{
                {'1','0','1','1','1'},
                {'0','1','0','1','0'},
                {'1','1','0','1','1'},
                {'1','1','0','1','1'},
                {'0','1','1','1','1'},
        }));
    }

    /**
     * LeetCode 耗时：6 ms - 81.31%
     *          内存消耗：42 MB - 9.81%
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int m = matrix.length, n = matrix[0].length, max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 遍历每个 '1'，以它作为矩形的起始点
                if (matrix[i][j] == '0') {
                    continue;
                }
                // 找到最右边的边界
                int right = j;
                while (right + 1 <= n - 1 && matrix[i][right + 1] == '1') {
                    right++;
                }
                max = Math.max(max, right - j + 1);

                // 每往下扩展一行，就计算新的矩形的面积
                int bottom = i + 1;
                while (bottom < m && matrix[bottom][j] == '1') {
                    int k = j;
                    while (k + 1 <= right && matrix[bottom][k + 1] == '1') {
                        k++;
                    }
                    // 记住要收缩边界
                    right = k;
                    max = Math.max(max, (bottom - i + 1) * (k - j + 1));
                    bottom++;
                }
            }
        }

        return max;
    }

    @Test
    public void testMaximalRectangle() {
        test(this::maximalRectangle);
    }
}
