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

    public void test(Consumer<int[][]> method) {
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
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 79.22%
     */
    public void betterMethod(int[][] matrix) {
        int n = matrix.length;
        // 由外向内顺时针旋转，总共需要旋转 n/2 次
        for (int times = 0; times < (n >>> 1); times++) {
            int len = n - (times << 1);
            for (int i = 0; i < len - 1; i++) {
                // 保存右
                int temp = matrix[times + i][n - times - 1];
                // 上移到右
                matrix[times + i][n - times - 1] = matrix[times][times + i];
                // 左移到上
                matrix[times][times + i] = matrix[times + len - i - 1][times];
                // 下移到左
                matrix[times + len - i - 1][times] = matrix[times + len - 1][times + len - i - 1];
                // 右移到下
                matrix[times + len - 1][times + len - i - 1] = temp;
            }
        }
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
