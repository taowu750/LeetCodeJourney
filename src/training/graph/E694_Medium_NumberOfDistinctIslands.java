package training.graph;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 694. 不同岛屿的数量: https://leetcode-cn.com/problems/number-of-distinct-islands/
 *
 * 给定一个非空 01 二维数组表示的网格，一个岛屿由四连通（上、下、左、右四个方向）的 1 组成，你可以认为网格的四周被海水包围。
 *
 * 请你计算这个网格中共有多少个形状不同的岛屿。两个岛屿被认为是相同的，当且仅当一个岛屿可以通过平移变换（不可以旋转、翻转）和另一个岛屿重合。
 *
 * 例 1：
 * 11000
 * 11000
 * 00011
 * 00011
 * 给定上图，返回结果 1 。
 *
 * 例 2：
 * 11011
 * 10000
 * 00001
 * 11011
 * 给定上图，返回结果 3 。
 * 注意：
 *
 * 11
 * 1
 * 和
 *
 *  1
 * 11
 * 是不同的岛屿，因为我们不考虑旋转、翻转操作。
 *
 * 说明：
 * - 二维数组每维的大小都不会超过 50 。
 */
public class E694_Medium_NumberOfDistinctIslands {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(1, method.applyAsInt(new int[][]{
                {1,1,0,0,0},
                {1,1,0,0,0},
                {0,0,0,1,1},
                {0,0,0,1,1}}));
        assertEquals(3, method.applyAsInt(new int[][]{
                {1,1,0,1,1},
                {1,0,0,0,0},
                {0,0,0,0,1},
                {1,1,0,1,1}}));
        assertEquals(15, method.applyAsInt(new int[][]{
                {0,0,1,0,1,0,1,1,1,0,0,0,0,1,0,0,1,0,0,1,1,1,0,1,1,1,0,0,0,1,1,0,1,1,0,1,0,1,0,1,0,0,0,0,0,1,1,1,1,0},
                {0,0,1,0,0,1,1,1,0,0,1,0,1,0,0,1,1,0,0,1,0,0,0,1,0,1,1,1,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,1,1,0,1,0,0,0},
                {0,1,0,1,0,1,1,1,0,0,1,1,0,0,0,0,1,0,1,0,1,1,1,0,1,1,1,0,0,0,1,0,1,0,1,0,0,0,1,1,1,1,1,0,0,1,0,0,1,0},
                {1,0,1,0,0,1,0,1,0,0,1,0,0,1,1,1,0,1,0,0,0,0,1,0,1,0,0,1,0,1,1,1,0,1,0,0,0,1,1,1,0,0,0,0,1,1,1,1,1,1}}));
    }

    /**
     * LeetCode 耗时：6 ms - 59.01%
     *          内存消耗：41.4 MB - 51.84%
     */
    public int numDistinctIslands(int[][] grid) {
        // 遍历顺序和起点一致，因此形状相同的岛屿生成的字符串一致
        Set<String> distinctIslands = new HashSet<>();
        final int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (visited[i][j] || grid[i][j] == 0) {
                    continue;
                }
                StringBuilder island = new StringBuilder();
                // dfs 比 dfs1 快 1ms
                dfs(grid, visited, island, i, j, 0, 0);
//                dfs1(grid, visited, island, i, j, 5);
                distinctIslands.add(island.toString());
            }
        }

        return distinctIslands.size();
    }

    public static int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private void dfs(int[][] grid, boolean[][] visited, StringBuilder island, int i, int j, int pi, int pj) {
        visited[i][j] = true;
        island.append(pi).append(',').append(pj).append(',');
        for (int[] dir : DIRS) {
            int ni = i + dir[0], nj = j + dir[1];
            int npi = pi + dir[0], npj = pj + dir[1];
            if (ni >= 0 && ni < grid.length && nj >= 0 && nj < grid[0].length
                    && !visited[ni][nj] && grid[ni][nj] == 1) {
                dfs(grid, visited, island, ni, nj, npi, npj);
            }
        }
    }

    private void dfs1(int[][] grid, boolean[][] visited, StringBuilder island, int i, int j, int dir) {
        visited[i][j] = true;
        island.append(dir).append(',');
        for (int d = 0; d < DIRS.length; d++) {
            int[] dd = DIRS[d];
            int ni = i + dd[0], nj = j + dd[1];
            if (ni >= 0 && ni < grid.length && nj >= 0 && nj < grid[0].length
                    && !visited[ni][nj] && grid[ni][nj] == 1) {
                dfs1(grid, visited, island, ni, nj, d + 1);
            }
        }
        /*
        还需要记录退出方向。不记录的话下面两个岛屿会被认为是一样的：
        11  11
        1    1
         */
        island.append(-dir).append(',');
    }

    @Test
    public void testNumDistinctIslands() {
        test(this::numDistinctIslands);
    }
}
