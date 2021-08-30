package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 59. 螺旋矩阵 II: https://leetcode-cn.com/problems/spiral-matrix-ii/
 *
 * 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。
 *
 * 例 1：
 * 输入：n = 3
 * 输出：[[1,2,3],
 *       [8,9,4],
 *       [7,6,5]]
 *
 * 例 2：
 * 输入：n = 1
 * 输出：[[1]]
 *
 * 约束：
 * - 1 <= n <= 20
 */
public class E59_Medium_SpiralMatrixII {

    static void test(IntFunction<int[][]> method) {
        assertArrayEquals(new int[][]{{1,2,3}, {8,9,4}, {7,6,5}}, method.apply(3));
        assertArrayEquals(new int[][]{{1}}, method.apply(1));
        assertArrayEquals(new int[][]{{1,2,3,4}, {12,13,14,5}, {11,16,15,6}, {10,9,8,7}}, method.apply(4));
    }

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.2 MB - 87.51%
     */
    public int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int size = n * n, dirIdx = 0, nextI, nextJ;
        for (int num = 1, i = 0, j = 0; num <= size; num++, i = nextI, j = nextJ) {
            matrix[i][j] = num;
            nextI = i + DIRS[dirIdx][0];
            nextJ = j + DIRS[dirIdx][1];
            if (nextI < 0 || nextI >= n || nextJ < 0 || nextJ >= n || matrix[nextI][nextJ] != 0) {
                dirIdx = (dirIdx + 1) % DIRS.length;
                nextI = i + DIRS[dirIdx][0];
                nextJ = j + DIRS[dirIdx][1];
            }
        }

        return matrix;
    }

    @Test
    public void testGenerateMatrix() {
        test(this::generateMatrix);
    }
}
