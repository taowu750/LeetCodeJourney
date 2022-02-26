package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 48. 旋转图像: https://leetcode-cn.com/problems/rotate-image/
 *
 * 给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。
 *
 * 你必须在「原地」旋转图像，这意味着你需要直接修改输入的二维矩阵。「请不要」使用另一个矩阵来旋转图像。
 *
 * 例 1：
 * 输入：matrix =
 *      [[1,2,3],
 *       [4,5,6],
 *       [7,8,9]]
 * 输出：[[7,4,1],
 *       [8,5,2],
 *       [9,6,3]]
 *
 * 例 2：
 * 输入：matrix =
 *      [[5,1,9,11],
 *       [2,4,8,10],
 *       [13,3,6,7],
 *       [15,14,12,16]]
 * 输出：[[15,13,2,5],
 *       [14,3,4,1],
 *       [12,6,8,9],
 *       [16,7,10,11]]
 *
 * 例 3：
 * 输入：matrix = [[1]]
 * 输出：[[1]]
 *
 * 例 4：
 * 输入：matrix =
 *      [[1,2],
 *       [3,4]]
 * 输出：[[3,1],
 *       [4,2]]
 *
 * 约束：
 * - matrix.length == n
 * - matrix[i].length == n
 * - 1 <= n <= 20
 * - -1000 <= matrix[i][j] <= 1000
 */
public class E48_Medium_RotateImage {

    public static void test(Consumer<int[][]> method) {
        int[][] matrix = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{
                {7, 4, 1},
                {8, 5, 2},
                {9, 6, 3}}, matrix);

        matrix = new int[][]{
                {5, 1, 9, 11},
                {2, 4, 8, 10},
                {13, 3, 6, 7},
                {15, 14, 12, 16}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{
                {15, 13, 2, 5},
                {14, 3, 4, 1},
                {12, 6, 8, 9},
                {16, 7, 10, 11}}, matrix);

        matrix = new int[][]{{1}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{{1}}, matrix);

        matrix = new int[][]{
                {1, 2},
                {3, 4}};
        method.accept(matrix);
        assertArrayEquals(new int[][]{
                {3, 1},
                {4, 2}}, matrix);
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.7 MB - 27.50%
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        // 最外层循环选择图像的每一圈进行旋转
        for (int size = n, startI = 0, startJ = 0; size > 1; size -= 2, startI += 1, startJ += 1) {
            // 旋转一圈，也就是把上移到右、右移到下、下移到左、左移到上。
            // 我们分别选择每个元素，移动四次，直到移完一行为止
            for (int j = startJ; j < startJ + size - 1; j++) {
                // 上移到右
                int stepI = startI, stepJ = j;
                int nextI = stepI, nextJ = stepJ + size - 1;
                if (nextJ > startJ + size - 1) {
                    nextI += nextJ - (startJ + size - 1);
                    nextJ = startJ + size - 1;
                }
                int lastNum = matrix[stepI][stepJ], nextNum = matrix[nextI][nextJ];
                matrix[nextI][nextJ] = lastNum;
                lastNum = nextNum;

                // 右移到下
                stepI = nextI;
                stepJ = nextJ;
                nextI = stepI + size - 1;
                if (nextI > startI + size - 1) {
                    nextJ -= nextI - (startI + size - 1);
                    nextI = startI + size - 1;
                }
                nextNum = matrix[nextI][nextJ];
                matrix[nextI][nextJ] = lastNum;
                lastNum = nextNum;

                // 下移到左
                stepI = nextI;
                stepJ = nextJ;
                nextJ = stepJ - size + 1;
                if (nextJ < startJ) {
                    nextI -= startJ - nextJ;
                    nextJ = startJ;
                }
                nextNum = matrix[nextI][nextJ];
                matrix[nextI][nextJ] = lastNum;
                lastNum = nextNum;

                // 左移到上
                stepI = nextI;
                stepJ = nextJ;
                nextI = stepI - size + 1;
                if (nextI < startI) {
                    nextJ += startI - nextI;
                    nextI = startI;
                }
                matrix[nextI][nextJ] = lastNum;
            }
        }
    }

    @Test
    public void testRotate() {
        test(this::rotate);
    }


    /**
     *      top
     *      ————————| right
     *      |       |
     *      |       |
     *      |       |
     * left |————————
     *         bottom
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 79.22%
     */
    public void betterMethod(int[][] matrix) {
        final int n = matrix.length;
        // 要移动 n/2 圈的元素
        // 每次旋转一圈都是从左上角旋转，也就是 (i,i)，也就是移动的下标会和次数相关
        for (int i = 0; i < n / 2; i++) {
            // 一圈要移动 n-2*i-1 次，每次移动上、下、左、右四个元素
            for (int j = 0; j < n - 2 * i - 1; j++) {
                // 先保存上
                int top = matrix[i][i + j];
                // 将左移到上
                matrix[i][i + j] = matrix[n - i - j - 1][i];
                // 将下移到左
                matrix[n - i - j - 1][i] = matrix[n - i - 1][n - i - j - 1];
                // 将右移到下
                matrix[n - i - 1][n - i - j - 1] = matrix[i + j][n - i - 1];
                // 将上移到右
                matrix[i + j][n - i - 1] = top;
            }
        }
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
