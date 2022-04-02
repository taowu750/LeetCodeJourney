package training.graph;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1293. 网格中的最短路径: https://leetcode-cn.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/
 *
 * 给你一个 m * n 的网格，其中每个单元格不是 0（空）就是 1（障碍物）。每一步，您都可以在空白单元格中上、下、左、右移动。
 *
 * 如果您「最多」可以消除 k 个障碍物，请找出从左上角 (0, 0) 到右下角 (m-1, n-1) 的最短路径，并返回通过该路径所需的步数。
 * 如果找不到这样的路径，则返回 -1。
 *
 * 例 1：
 * 输入： grid = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]], k = 1
 * 输出：6
 * 解释：
 * 不消除任何障碍的最短路径是 10。
 * 消除位置 (3,2) 处的障碍后，最短路径是 6 。该路径是 (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2) -> (3,2) -> (4,2).
 *
 * 例 2：
 * 输入：grid = [[0,1,1],[1,1,1],[1,0,0]], k = 1
 * 输出：-1
 * 解释：我们至少需要消除两个障碍才能找到这样的路径。
 *
 * 说明：
 * - grid.length == m
 * - grid[0].length == n
 * - 1 <= m, n <= 40
 * - 1 <= k <= m*n
 * - grid[i][j] 是 0 或 1
 * - grid[0][0] == grid[m-1][n-1] == 0
 */
public class E1293_Hard_ShortestPathInGridWithObstaclesElimination {

    public static void test(ToIntBiFunction<int[][], Integer> method) {
        assertEquals(6, method.applyAsInt(new int[][]{
                {0,0,0},
                {1,1,0},
                {0,0,0},
                {0,1,1},
                {0,0,0}}, 1));
        assertEquals(-1, method.applyAsInt(new int[][]{
                {0,1,1},
                {1,1,1},
                {1,0,0}}, 1));
        assertEquals(13, method.applyAsInt(new int[][]{
                {0,1,0,0,0,1,0,0},
                {0,1,0,1,0,1,0,1},
                {0,0,0,1,0,0,1,0}}, 1));
        assertEquals(20, method.applyAsInt(new int[][]{
                {0,0,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,0,0,0,0,0,0,0,0},
                {0,1,0,1,1,1,1,1,1,1},
                {0,1,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,1,1,1,1,0},
                {0,1,0,0,0,0,0,0,0,0},
                {0,1,0,1,1,1,1,1,1,1},
                {0,1,0,1,1,1,1,0,0,0},
                {0,1,0,0,0,0,0,0,1,0},
                {0,1,1,1,1,1,1,0,1,0},
                {0,0,0,0,0,0,0,0,1,0}}, 1));
    }

    /**
     * LeetCode 耗时：3 ms - 97.41%
     *          内存消耗：40.8 MB - 64.94%
     */
    public int shortestPath(int[][] grid, int k) {
        final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        final int m = grid.length, n = grid[0].length;
        if ( k >= m + n - 3){
            return m + n - 2;
        }

        Queue<int[]> queue = new LinkedList<>();
        // 多出一个维度，记录还剩多少 k 时到达了 i,j
        boolean[][][] visited = new boolean[m][n][k + 1];
        queue.add(new int[]{0, 0, k});
        visited[0][0][k] = true;
        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int l = 0; l < size; l++) {
                int[] pos = queue.remove();
                int i = pos[0], j = pos[1], kk = pos[2];
                // [1]
                for (int[] dir : dirs) {
                    int ni = dir[0] + i, nj = dir[1] + j;
                    if (ni >= 0 && ni < m && nj >= 0 && nj < n) {
                        int nk = grid[ni][nj] == 0 ? kk : kk - 1;
                        if (nk < 0) {
                            continue;
                        }
                        // 特别注意，在内循环处判断比在 [1] 处判断时间大大缩短，推测是少添加了很多无用状态
                        if (ni == m - 1 && nj == n - 1) {
                            return level + 1;
                        }
                        if (!visited[ni][nj][nk]) {
                            queue.add(new int[]{ni, nj, nk});
                            visited[ni][nj][nk] = true;
                        }
                    }
                }
            }
            level++;
        }

        return -1;
    }

    @Test
    public void testShortestPath() {
        test(this::shortestPath);
    }
}
