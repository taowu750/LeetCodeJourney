package training.graph;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 329. 矩阵中的最长递增路径: https://leetcode-cn.com/problems/longest-increasing-path-in-a-matrix/
 *
 * 给定一个 m x n 整数矩阵 matrix ，找出其中「最长递增路径」的长度。
 * 对于每个单元格，你可以往上，下，左，右四个方向移动。 你「不能」在「对角线」方向上移动或移动到边界外（即不允许环绕）。
 *
 * 例 1：
 * 输入：matrix = [[9,9,4],[6,6,8],[2,1,1]]
 * 输出：4
 * 解释：最长递增路径为 [1, 2, 6, 9]。
 *
 * 例 2：
 * 输入：matrix = [[3,4,5],[3,2,6],[2,2,1]]
 * 输出：4
 * 解释：最长递增路径是 [3, 4, 5, 6]。注意不允许在对角线方向上移动。
 *
 * 例 3：
 * 输入：matrix = [[1]]
 * 输出：1
 *
 * 约束：
 * - m == matrix.length
 * - n == matrix[i].length
 * - 1 <= m, n <= 200
 * - 0 <= matrix[i][j] <= 2^31 - 1
 */
public class E329_Hard_LongestIncreasingPathInMatrix {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(4, method.applyAsInt(new int[][]{
                {9,9,4},
                {6,6,8},
                {2,1,1}}));

        assertEquals(4, method.applyAsInt(new int[][]{
                {3,4,5},
                {3,2,6},
                {2,2,1}}));

        assertEquals(1, method.applyAsInt(new int[][]{{1}}));

        assertEquals(2, method.applyAsInt(new int[][]{{1, 2}}));

        assertEquals(6, method.applyAsInt(new int[][]{
                {7,8,9},
                {9,7,6},
                {7,2,3}}));

        assertEquals(5, method.applyAsInt(new int[][]{
                {1,4,7,9},
                {0,3,8,5},
                {3,6,0,6},
                {1,4,5,6}}));
    }

    private int result;

    /**
     * 记忆化搜索。
     *
     * LeetCode 耗时：9 ms - 71.14%
     *          内存消耗：38.5 MB - 83.33%
     */
    public int longestIncreasingPath(int[][] matrix) {
        int[][] memo = new int[matrix.length][matrix[0].length];
        result = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result = Math.max(result, dfs(matrix, memo, i, j));
            }
        }

        return result;
    }

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    /**
     * dfs 计算的是以 (i,j) 开始的最长递增序列长度，因此可以使用 memo 记录经过路径上每个格子的最长递增序列长度
     */
    private int dfs(int[][] matrix, int[][] memo, int i, int j) {
        if (memo[i][j] != 0) {
            return memo[i][j];
        }

        memo[i][j] = 1;
        for (int[] dir : DIRS) {
            int r = i + dir[0], c = j + dir[1];
            if (r >= 0 && r < matrix.length && c >= 0 && c < matrix[0].length && matrix[r][c] > matrix[i][j]) {
                memo[i][j] = Math.max(memo[i][j], dfs(matrix, memo, r, c) + 1);
            }
        }

        return memo[i][j];
    }

    @Test
    public void testLongestIncreasingPath() {
        test(this::longestIncreasingPath);
    }


    /**
     * 拓扑排序法。将 matrix 看成是有向图，较小数指向相邻的较大数。
     * 记录每个节点的出度，从出度为 0 的点开始，用类似拓扑排序的方法进行 bfs。
     *
     * LeetCode 耗时：15 ms - 18.54%
     *          内存消耗：38.7 MB - 36.60%
     */
    public int topicSortMethod(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int[][] outDegrees = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int[] dir : DIRS) {
                    int r = i + dir[0], c = j + dir[1];
                    if (r >= 0 && r < matrix.length && c >= 0 && c < matrix[0].length && matrix[r][c] > matrix[i][j]) {
                        outDegrees[i][j]++;
                    }
                }
                if (outDegrees[i][j] == 0) {
                    queue.add(new int[]{i, j});
                }
            }
        }

        int level = 0;
        while (!queue.isEmpty()) {
            level++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] pos = queue.remove();
                for (int[] dir : DIRS) {
                    int r = pos[0] + dir[0], c = pos[1] + dir[1];
                    if (r >= 0 && r < matrix.length && c >= 0 && c < matrix[0].length
                            && matrix[r][c] < matrix[pos[0]][pos[1]]) {
                        if (--outDegrees[r][c] == 0) {
                            queue.add(new int[]{r, c});
                        }
                    }
                }
            }
        }

        return level;
    }

    @Test
    public void testTopicSortMethod() {
        test(this::topicSortMethod);
    }
}
