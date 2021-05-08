package training.graph;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个 m×n 二维的由 “1”（土地）和 “0”（水）组成的网格图，返回岛的数量。
 * <p>
 * 一个岛屿被水包围，通过水平或垂直的邻接陆地组成。您可以假定网格的所有四条边都被水包围。
 * <p>
 * 例 1：
 * Input: grid = [
 * ["1","1","1","1","0"],
 * ["1","1","0","1","0"],
 * ["1","1","0","0","0"],
 * ["0","0","0","0","0"]
 * ]
 * Output: 1
 * <p>
 * 例 2：
 * Input: grid = [
 * ["1","1","0","0","0"],
 * ["1","1","0","0","0"],
 * ["0","0","1","0","0"],
 * ["0","0","0","1","1"]
 * ]
 * Output: 3
 * <p>
 * 约束：
 * - m == grid.length
 * - n == grid[i].length
 * - 1 <= m, n <= 300
 * - grid[i][j] 是 '0' 或 '1'.
 */
public class Review_E200_Medium_NumberOfIslands {

    static void test(ToIntFunction<char[][]> method) {
        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'},
        };
        assertEquals(method.applyAsInt(grid), 1);

        grid = new char[][]{
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'},
        };
        assertEquals(method.applyAsInt(grid), 3);
    }

    private int m, n;
    private char[][] grid;

    /**
     * 使用 DFS（深度优先遍历）。这和图算法中求连通分量非常类似。
     */
    public int numIslands(char[][] grid) {
        this.grid = grid;
        m = grid.length;
        n = grid[0].length;

        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    dfs(i, j);
                    count++;
                }
            }
        }

        return count;
    }

    private void dfs(int i, int j) {
        if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == '0')
            return;
        // 通过修改原数组的方式避免使用 visited 数组
        grid[i][j] = '0';
        dfs(i + 1, j);
        dfs(i - 1, j);
        dfs(i, j + 1);
        dfs(i, j - 1);
    }

    @Test
    public void testNumIslands() {
        test(this::numIslands);
    }


    /**
     * BFS（宽度优先遍历）算法。
     */
    public int bfsMethod(char[][] grid) {
        this.grid = grid;
        m = grid.length;
        n = grid[0].length;

        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    bfs(i, j);
                    count++;
                }
            }
        }

        return count;
    }

    private void bfs(int i, int j) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{i, j});
        grid[i][j] = '0';
        while (!queue.isEmpty()) {
            int[] pos = queue.remove();
            j = pos[1];
            if ((i = pos[0] + 1) < m && grid[i][j] == '1') {
                queue.add(new int[]{i, j});
                grid[i][j] = '0';
            }
            if ((i = pos[0] - 1) >= 0 && grid[i][j] == '1') {
                queue.add(new int[]{i, j});
                grid[i][j] = '0';
            }
            i = pos[0];
            if ((j = pos[1] + 1) < n && grid[i][j] == '1') {
                queue.add(new int[]{i, j});
                grid[i][j] = '0';
            }
            if ((j = pos[1] - 1) >=0 && grid[i][j] == '1') {
                queue.add(new int[]{i, j});
                grid[i][j] = '0';
            }
        }
    }

    @Test
    public void testBfsMethod() {
        test(this::bfsMethod);
    }
}
