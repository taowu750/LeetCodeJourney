package training.array;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 54. 螺旋矩阵：https://leetcode-cn.com/problems/spiral-matrix/
 *
 * 给你一个 m 行 n 列的矩阵 matrix ，请按「顺时针螺旋」顺序 ，返回矩阵中的所有元素。
 *
 * 例 1：
 * 输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出：[1,2,3,6,9,8,7,4,5]
 * 解释：图片参见链接
 *
 * 例 2：
 * 输入：matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * 输出：[1,2,3,4,8,12,11,10,9,5,6,7]
 *
 * 约束：
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 10
 * - -100 <= matrix[i][j] <= 100
 */
public class E54_Medium_SpiralMatrix {

    static void test(Function<int[][], List<Integer>> method) {
        assertEquals(Arrays.asList(1,2,3,6,9,8,7,4,5),
                method.apply(new int[][]{
                        {1,2,3},
                        {4,5,6},
                        {7,8,9}
                }));

        assertEquals(Arrays.asList(1,2,3,4,8,12,11,10,9,5,6,7),
                method.apply(new int[][]{
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12}
                }));

        assertEquals(Arrays.asList(1,2,3,6,9,12,11,10,7,4,5,8),
                method.apply(new int[][]{
                        {1,2,3},
                        {4,5,6},
                        {7,8,9},
                        {10,11,12}
                }));

        assertEquals(Arrays.asList(1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10),
                method.apply(new int[][]{
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {13,14,15,16}
                }));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.4 MB - 94.09%
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length, size = m * n;
        List<Integer> result = new ArrayList<>(size);
        int rowStart = 0, rowEnd = m - 1;
        int colStart = 0, colEnd = n - 1;
        for (int cnt = 0; cnt < size;) {
            // 从左到右
            for (int col = colStart; cnt < size && col <= colEnd; col++, cnt++)
                result.add(matrix[rowStart][col]);
            // 从上到下
            for (int row = rowStart + 1; cnt < size && row <= rowEnd; row++, cnt++)
                result.add(matrix[row][colEnd]);
            // 从右到左
            for (int col = colEnd - 1; cnt < size && col >= colStart; col--, cnt++)
                result.add(matrix[rowEnd][col]);
            // 从下到上
            for (int row = rowEnd - 1; cnt < size && row > rowStart; row--, cnt++)
                result.add(matrix[row][colStart]);
            // 缩小范围
            rowStart++;
            rowEnd--;
            colStart++;
            colEnd--;
        }

        return result;
    }

    @Test
    void testSpiralOrder() {
        test(this::spiralOrder);
    }
}
