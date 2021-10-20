package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 378. 有序矩阵中第 K 小的元素: https://leetcode-cn.com/problems/kth-smallest-element-in-a-sorted-matrix/
 *
 * 给你一个 n x n 矩阵 matrix ，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。
 * 请注意，它是「排序后」的第 k 小元素，而不是第 k 个「不同」的元素。
 *
 * 例 1：
 * 输入：matrix = [[1,5,9],
 *                [10,11,13],
 *                [12,13,15]],
 *      k = 8
 * 输出：13
 * 解释：矩阵中的元素为 [1,5,9,10,11,12,13,13,15]，第 8 小元素是 13
 *
 * 例 2：
 * 输入：matrix = [[-5]], k = 1
 * 输出：-5
 *
 * 提示：
 * - n == matrix.length
 * - n == matrix[i].length
 * - 1 <= n <= 300
 * - -10^9 <= matrix[i][j] <= 10^9
 * - 题目数据保证 matrix 中的所有行和列都按「非递减顺序」排列
 * - 1 <= k <= n^2
 */
public class E378_Medium_KthSmallestElementInSortedMatrix {

    public static void test(ToIntBiFunction<int[][], Integer> method) {
        assertEquals(13, method.applyAsInt(new int[][]{
                {1,5,9},
                {10,11,13},
                {12,13,15}},8));

        assertEquals(-5, method.applyAsInt(new int[][]{{-5}}, 1));

        assertEquals(2, method.applyAsInt(new int[][]{
                {1,3,5},
                {2,4,6}},2));

        assertEquals(1, method.applyAsInt(new int[][]{
                {1,2},
                {1,3}},1));

        assertEquals(1, method.applyAsInt(new int[][]{
                {1,2},
                {3,3}},1));

        assertEquals(-5, method.applyAsInt(new int[][]{
                {-5,-4},
                {-5,-4}},2));
    }

    /**
     * 多路归并，类似于 {@link training.linkedlist.E23_Hard_MergeKSortedLists}。
     *
     * LeetCode 耗时：18 ms - 19.91%
     *          内存消耗：43.7 MB - 68.95%
     */
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int[] indices = new int[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 0; i < n; i++) {
            pq.add(new int[]{matrix[i][0], i});
        }

        int result = 0;
        while (k > 0) {
            int[] elem = pq.remove();
            result = elem[0];
            int idx = elem[1];
            if (indices[idx] < n - 1) {
                pq.add(new int[]{matrix[idx][++indices[idx]], idx});
            }
            k--;
        }

        return result;
    }

    @Test
    public void testKthSmallest() {
        test(this::kthSmallest);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/kth-smallest-element-in-a-sorted-matrix/solution/you-xu-ju-zhen-zhong-di-kxiao-de-yuan-su-by-leetco/
     *
     * LeetCode 耗时：1 ms - 61.74%
     *          内存消耗：44.1 MB - 12.98%
     */
    public int binarySearchMethod(int[][] matrix, int k) {
        int n = matrix.length, lo = matrix[0][0], hi = matrix[n - 1][n - 1];

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            /*
            计算小于等于 mid 的元素数量。因为是排序矩阵，所有小于等于 mid 的元素都在左上部分。
            下面的代码类似于 E240_Medium_Search2DMatrixII.betterMethod()。
             */
            int lessCnt = 0, i = n - 1, j = 0;
            while (i >= 0 && j < n) {
                if (matrix[i][j] <= mid) {
                    lessCnt += i + 1;
                    j++;
                } else {
                    i--;
                }
            }

            if (lessCnt >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }

    @Test
    public void testBinarySearchMethod() {
        test(this::binarySearchMethod);
    }
}
