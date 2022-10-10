package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 面试题 17.24. 最大子矩阵: https://leetcode-cn.com/problems/max-submatrix-lcci/
 *
 * 给定一个正整数、负整数和 0 组成的 N × M 矩阵，编写代码找出元素总和最大的子矩阵。
 *
 * 返回一个数组 [r1, c1, r2, c2]，其中 r1, c1 分别代表子矩阵左上角的行号和列号，r2, c2 分别代表右下角的行号和列号。
 * 若有多个满足条件的子矩阵，返回任意一个均可。
 *
 * 例 1：
 * 输入：
 * [
 *    [-1,0],
 *    [0,-1]
 * ]
 * 输出：[0,1,0,1]
 * 解释：输入中标粗的元素即为输出所表示的矩阵
 *
 * 说明：
 * - 1 <= matrix.length, matrix[0].length <= 200
 */
public class Mianshi17_24_Hard_MaxSubmatrix {

    public static void test(Function<int[][], int[]> method) {
        assertArrayEquals(new int[]{0,1,0,1}, method.apply(new int[][]{{-1,0}, {0, -1}}));
        assertArrayEquals(new int[]{1,1,1,1}, method.apply(new int[][]{{1,-3}, {-4,9}}));
        assertArrayEquals(new int[]{0,0,2,3}, method.apply(new int[][]{
                {9,-8,1,3,-2},
                {-3,7,6,-2,4},
                {6,-4,-4,8,-7}}));
    }

    /**
     * 可以借用计算一维数组计算最大子数组的方法（参见 {@link E53_Easy_MaximumSubarray}），我们需要将二维转化为一维，
     * 对于矩阵的每一列，我们将其加在一起，成为了一维上的一个数，二维矩阵的和转化为了一维数组的和.
     *
     * 我们固定上界 top 和下界 bottom，求最大子数组和。也就是固定上下两条边，之后从左往右遍历一遍，
     * 找到固定上下边下的最大子序和即可。
     *
     * LeetCode 耗时：41 ms - 92.51%
     *          内存消耗：46.5 MB - 32.27%
     */
    public int[] getMaxMatrix(int[][] matrix) {
        final int m = matrix.length, n = matrix[0].length;
        int[] ret = new int[4];
        int[][] colPrefix = new int[m + 1][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                colPrefix[i + 1][j] = colPrefix[i][j] + matrix[i][j];
            }
        }
        int ans = Integer.MIN_VALUE;
        for (int top = 0; top < m; top++) {
            for (int bottom = top; bottom < m; bottom++) {
                int sum = 0, maxSum = Integer.MIN_VALUE;
                int c0 = 0, c1 = 0, maxC0 = 0, maxC1 = 0;
                for (int j = 0; j < n; j++) {
                    sum += colPrefix[bottom + 1][j] - colPrefix[top][j];
                    if (sum > maxSum) {
                        maxSum = sum;
                        maxC0 = c0;
                        maxC1 = c1;
                    }
                    c1++;
                    if (sum < 0) {
                        sum = 0;
                        c0 = c1 = j + 1;
                    }
                }
                if (maxSum > ans) {
                    ans = maxSum;
                    ret[0] = top;
                    ret[1] = maxC0;
                    ret[2] = bottom;
                    ret[3] = maxC1;
                }
            }
        }

        return ret;
    }

    @Test
    public void testGetMaxMatrix() {
        test(this::getMaxMatrix);
    }
}
