package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 73. 矩阵置零: https://leetcode-cn.com/problems/set-matrix-zeroes/
 *
 * 给定一个 m x n 的矩阵，如果一个元素为 0 ，则将其所在行和列的所有元素都设为 0 。请使用「原地」算法。
 *
 * 一个直观的解决方案是使用 O(mn) 的额外空间，但这并不是一个好的解决方案。
 * 一个简单的改进方案是使用 O(m+n) 的额外空间，但这仍然不是最好的解决方案。
 * 你能想出一个仅使用常量空间的解决方案吗？
 *
 * 例 1：
 * 输入：matrix = [[1,1,1],
 *                [1,0,1],
 *                [1,1,1]]
 * 输出：[[1,0,1],
 *       [0,0,0],
 *       [1,0,1]]
 *
 * 例 2：
 * 输入：matrix = [[0,1,2,0],
 *                [3,4,5,2],
 *                [1,3,1,5]]
 * 输出：[[0,0,0,0],
 *       [0,4,5,0],
 *       [0,3,1,0]]
 *
 * 提示：
 * - m == matrix.length
 * - n == matrix[0].length
 * - 1 <= m, n <= 200
 * - -2^31 <= matrix[i][j] <= 2^31 - 1
 */
public class E73_Medium_SetMatrixZeroes {

    static void test(Consumer<int[][]> method) {
        int[][] matrix = {{1,1,1},{1,0,1},{1,1,1}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{{1,0,1},{0,0,0},{1,0,1}}, matrix);

        matrix = new int[][]{{0,1,2,0},{3,4,5,2},{1,3,1,5}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{{0,0,0,0},{0,4,5,0},{0,3,1,0}}, matrix);

        matrix = new int[][]{
                {1,2,3,4},
                {5,0,7,8},
                {0,10,11,12},
                {13,14,15,0}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{{0,0,3,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}}, matrix);
    }

    /**
     * trick 方法，将某个数选做占位符，用来和其他数和 0 区分。
     *
     * LeetCode 耗时：1 ms - 98.62%
     *          内存消耗：40 MB - 35.61%
     */
    public void setZeroes(int[][] matrix) {
        int placeholder = Integer.MIN_VALUE + 1, m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    for (int k = 0; k < n; k++) {
                        if (matrix[i][k] != 0) {
                            matrix[i][k] = placeholder;
                        }
                    }
                    for (int k = 0; k < m; k++) {
                        if (matrix[k][j] != 0) {
                            matrix[k][j] = placeholder;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == placeholder) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    @Test
    public void testSetZeroes() {
        test(this::setZeroes);
    }


    /**
     * LeetCode 耗时：1 ms - 98.62%
     *          内存消耗：39.6 MB - 90.34%
     */
    public void o1Method(int[][] matrix) {
        // 我们可以使用第一个遇到的 0 的行和列作为标志位，记录需要置 0 的行和列
        int flagI = -1, flagJ = -1;
        int m = matrix.length, n = matrix[0].length;

        // matrix[flagI][..] 所在行的元素用来表示对应列是否需要被置为 0；
        // matrix[..][flagJ] 所在列的元素用来表示对应行是否需要被置为 0；
        // 当标志位为 0 时，所在行/列需要置为 0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    flagI = i;
                    flagJ = j;
                }
            }
        }

        if (flagI == -1) {
            return;
        }

        // 遍历矩阵，记录需要置为 0 的行/列
        for (int i = 0; i < m; i++) {
            if (i == flagI) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (j == flagJ) {
                    continue;
                }
                if (matrix[i][j] == 0) {
                    matrix[flagI][j] = 0;
                    matrix[i][flagJ] = 0;
                }
            }
        }

        // 将行置为 0，注意标志位所在行列暂时不要动
        for (int k = 0; k < m; k++) {
            if (matrix[k][flagJ] == 0 && k != flagI) {
                for (int j = 0; j < n; j++) {
                    matrix[k][j] = 0;
                }
            }
        }
        // 将列置为 0
        for (int k = 0; k < n; k++) {
            if (matrix[flagI][k] == 0 && k != flagJ) {
                for (int i = 0; i < m; i++) {
                    matrix[i][k] = 0;
                }
            }
        }

        // 将标志所在行列置 0
        for (int k = 0; k < m; k++) {
            matrix[k][flagJ] = 0;
        }
        for (int k = 0; k < n; k++) {
            matrix[flagI][k] = 0;
        }
    }

    @Test
    public void testO1Method() {
        test(this::o1Method);
    }
}
