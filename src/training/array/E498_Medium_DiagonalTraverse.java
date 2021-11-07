package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 498. 对角线遍历: https://leetcode-cn.com/problems/diagonal-traverse/
 *
 * 给你一个大小为 m x n 的矩阵 mat ，请以对角线遍历的顺序，用一个数组返回这个矩阵中的所有元素。
 *
 * 例 1：
 * 输入：mat = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出：[1,2,4,7,5,3,6,8,9]
 *
 * 例 2：
 * 输入：mat = [[1,2],[3,4]]
 * 输出：[1,2,3,4]
 *
 * 说明：
 * - m == mat.length
 * - n == mat[i].length
 * - 1 <= m, n <= 10^4
 * - 1 <= m * n <= 10^4
 * - -10^5 <= mat[i][j] <= 10^5
 */
public class E498_Medium_DiagonalTraverse {

    public static void test(Function<int[][], int[]> method) {
        assertArrayEquals(new int[]{1,2,4,7,5,3,6,8,9}, method.apply(new int[][]{{1,2,3}, {4,5,6}, {7,8,9}}));
        assertArrayEquals(new int[]{1,2,3,4}, method.apply(new int[][]{{1,2}, {3,4}}));
        assertArrayEquals(new int[]{1,2,4,5,3,6}, method.apply(new int[][]{{1,2,3}, {4,5,6}}));
    }

    /**
     * 模拟法
     *
     * LeetCode 耗时：2 ms - 90.03%
     *          内存消耗：40.3 MB - 69.62%
     */
    public int[] findDiagonalOrder(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] result = new int[m * n];
        // di、dj 表示 i、j 方向的增量
        for (int cnt = 0, i = 0, j = 0, di = -1, dj = 1; cnt < result.length; cnt++) {
            result[cnt] = mat[i][j];
            i += di;
            j += dj;
            // 当遇到边界时，则需要改变方向了
            if (i < 0 || i >= m || j < 0 || j >= n) {
                // 如果之前是向右上的方向
                if (di == -1) {
                    // 先放到上一个元素右边试试看
                    i++;
                    if (j >= n) {
                        // 不行再放到下边
                        i++;
                        j--;
                    }
                } else {  // 否则之前是向左下的方向
                    // 先放到上一个元素下边试试看
                    j++;
                    if (i >= m) {
                        // 不行再放到右边
                        i--;
                        j++;
                    }
                }
                // 反转方向
                int tmp = di;
                di = dj;
                dj = tmp;
            }
        }

        return result;
    }

    @Test
    public void testFindDiagonalOrder() {
        test(this::findDiagonalOrder);
    }
}
