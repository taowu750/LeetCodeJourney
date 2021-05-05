package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 240. 搜索二维矩阵 II: https://leetcode-cn.com/problems/search-a-2d-matrix-ii/
 *
 * 编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target 。该矩阵具有以下特性：
 * - 每行的元素从左到右升序排列。
 * - 每列的元素从上到下升序排列。
 *
 * 例 1：
 * 输入：matrix =
 *      [[1,4,7,11,15],
 *       [2,5,8,12,19],
 *       [3,6,9,16,22],
 *       [10,13,14,17,24],
 *       [18,21,23,26,30]], target = 5
 * 输出：true
 *
 * 例 2：
 * 输入：matrix =
 *      [[1,4,7,11,15],
 *       [2,5,8,12,19],
 *       [3,6,9,16,22],
 *       [10,13,14,17,24],
 *       [18,21,23,26,30]], target = 20
 * 输出：false
 *
 * 约束：
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= n, m <= 300
 * - -10**9 <= matix[i][j] <= 10**9
 * - 每行的所有元素从左到右升序排列
 * - 每列的所有元素从上到下升序排列
 * - -10**9 <= target <= 10**9
 */
public class E240_Medium_Search2DMatrixII {

    static void test(BiPredicate<int[][], Integer> method) {
        assertTrue(method.test(new int[][]{
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}}, 5));

        assertFalse(method.test(new int[][]{
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}}, 20));
    }

    /**
     * LeetCode 耗时：6 ms - 91.20%
     *          内存消耗：43.6 MB - 95.94%
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int row = 0, col = 0;
         for (;;) {
             // 先在横向上二分查找
             int lo = 0, hi = n - 1;
             while (lo <= hi) {
                 int mid = (lo + hi) >>> 1;
                 if (matrix[row][mid] == target)
                     return true;
                 else if (matrix[row][mid] < target)
                     lo = mid + 1;
                 else
                     hi = mid - 1;
             }
             // 没有找到，从小于 target 的那一列再开始
             col = hi;
             // 如果都大于 target，则不存在
             if (col < row)
                 return false;

             // 再在纵向上二分查找
             lo = row + 1;
             hi = m - 1;
             while (lo <= hi) {
                 int mid = (lo + hi) >>> 1;
                 if (matrix[mid][col] == target)
                     return true;
                 else if (matrix[mid][col] < target)
                     lo = mid + 1;
                 else
                     hi = mid - 1;
             }
             // 没有找到，从大于 target 的那一行再开始
             row = lo;
             // 如果都小于 target，则不存在
             if (row >= m)
                 return false;

             // 这个算法不能都选小于 target 的，因为可能 [i][j] 在行列上都刚好小于 target，这样会导致无限循环。
             // 而横向上查找不能选大于 target 的，因为可能一行都小于 target 或者
         }
    }

    @Test
    public void testSearchMatrix() {
        test(this::searchMatrix);
    }
}
