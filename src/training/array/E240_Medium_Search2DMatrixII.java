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

        assertTrue(method.test(new int[][]{
                {2, 5},
                {2, 8},
                {7, 9},
                {7, 11},
                {9, 11}}, 7));
    }

    /**
     * LeetCode 耗时：6 ms - 91.20%
     *          内存消耗：43.6 MB - 95.94%
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int row = 0, col = n;
         for (;;) {
             // 先在行上二分查找
             int lo = 0, hi = col - 1;
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
             if (col < 0)
                 return false;

             // 再在列上二分查找
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

             /*
             这个算法不能都选小于(大于) target 的，因为可能 [i][j] 在行列上都刚好小于(大于) target，
             这样会导致无限循环。

             在行上如果某个值大于 target，那么这个值及右方、下方的值都大于 target，也就没有必要看了。
             所以行上选择小于的下标。

             列上如果某个值小于 target，那么它的左边、上边就不看了。并且由于之前在行上的查找结果，右边的也不用看了。
             所以列上选择大于的下标。
              */
         }
    }

    @Test
    public void testSearchMatrix() {
        test(this::searchMatrix);
    }


    /**
     * 极其巧妙的方法，利用了矩阵从左到右升序、从上到下升序的性质。
     * 从矩阵的左下角开始，如果 target 大于当前数，则上升一行；如果小于，则向右一列。
     * 每次都能去掉一行或一列，因此时间复杂度为 O(m+n)。
     *
     * 因为行是从左到右排序的，所以我们知道当前值右侧的每个值都比它大。因此如果当前值已经大于 target，则当前行不可能会有 target。
     * 也可以对列进行类似的论证，因此这种搜索方式将始终在矩阵中找到目标（如果存在）。
     *
     * 这个二维数组就类似一棵排序二叉树，对于每一个数来说，上方的数都小于它，右边的数都大于它，所以把左下角作为根节点开始查找。
     *
     * 参见：https://leetcode-cn.com/problems/search-a-2d-matrix-ii/solution/sou-suo-er-wei-ju-zhen-ii-by-leetcode-2/
     *
     * LeetCode 耗时：5 ms - 96.24%
     *          内存消耗：44 MB - 35.53%
     */
    public boolean betterMethod(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = m - 1, j = 0; i >= 0 && j < n;) {
            if (matrix[i][j] == target) {
                return true;
            } else if (matrix[i][j] > target) {
                i--;
            } else {
                j++;
            }
        }

        return false;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
