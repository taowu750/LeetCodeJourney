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
        final int m = mat.length, n = mat[0].length;
        int[] result = new int[m * n];
        // dirs[0] 往右上，dirs[1] 往左下
        int[][] dirs = {{-1, 1}, {1, -1}};
        for (int k = 0, i = 0, j = 0, nextI, nextJ, dirIdx = 0; k < result.length; k++, i = nextI, j = nextJ) {
            result[k] = mat[i][j];
            nextI = i + dirs[dirIdx][0];
            nextJ = j + dirs[dirIdx][1];
            // 如果越界了，表示需要找的新的位置，并且要变换方向
            if (nextI < 0 || nextI >= m || nextJ < 0 || nextJ >= n) {
//                nextI = i + dirs[(dirIdx + 1) % 2][0];
//                nextJ = j + dirs[(dirIdx + 1) % 2][1];
                // 如果是往右上时越界了
                if (dirIdx == 0) {
                    // 往右上越界有两种情况，一是突破了右边界
                    if (nextJ >= n) {
                        nextI = i + 1;
                        nextJ = j;
                    } else {  // 二是突破了上边界
                        nextI = i;
                    }
                } else {  //否则是往左下时越界了
                    // 往左下越界有两种情况，一是突破了下边界
                    if (nextI >= m) {
                        nextI = i;
                        nextJ = j + 1;
                    } else {  // 二是突破了左边界
                        nextJ = j;
                    }
                }
                // 改变方向
                dirIdx = (dirIdx + 1) % 2;
            }
        }

        return result;
    }

    @Test
    public void testFindDiagonalOrder() {
        test(this::findDiagonalOrder);
    }
}
