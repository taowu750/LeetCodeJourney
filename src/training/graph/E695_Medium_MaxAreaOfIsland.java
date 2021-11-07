package training.graph;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 695. 岛屿的最大面积: https://leetcode-cn.com/problems/max-area-of-island/
 *
 * 给你一个大小为 m x n 的二进制矩阵 grid 。
 *
 * 岛屿是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在「水平或者竖直的四个方向上」相邻。
 * 你可以假设 grid 的四个边缘都被 0（代表水）包围着。
 *
 * 岛屿的面积是岛上值为 1 的单元格的数目。
 *
 * 计算并返回 grid 中最大的岛屿面积。如果没有岛屿，则返回面积为 0 。
 *
 * 例 1：
 * 输入：grid = [[0,0,1,0,0,0,0,1,0,0,0,0,0],
 *              [0,0,0,0,0,0,0,1,1,1,0,0,0],
 *              [0,1,1,0,1,0,0,0,0,0,0,0,0],
 *              [0,1,0,0,1,1,0,0,1,0,1,0,0],
 *              [0,1,0,0,1,1,0,0,1,1,1,0,0],
 *              [0,0,0,0,0,0,0,0,0,0,1,0,0],
 *              [0,0,0,0,0,0,0,1,1,1,0,0,0],
 *              [0,0,0,0,0,0,0,1,1,0,0,0,0]]
 * 输出：6
 * 解释：答案不应该是 11 ，因为岛屿只能包含水平或垂直这四个方向上的 1 。
 *
 * 例 2：
 * 输入：grid = [[0,0,0,0,0,0,0,0]]
 * 输出：0
 *
 * 说明：
 * - m == grid.length
 * - n == grid[i].length
 * - 1 <= m, n <= 50
 * - grid[i][j] 为 0 或 1
 */
public class E695_Medium_MaxAreaOfIsland {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(6, method.applyAsInt(new int[][]{
                {0,0,1,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,1,1,0,1,0,0,0,0,0,0,0,0},
                {0,1,0,0,1,1,0,0,1,0,1,0,0},
                {0,1,0,0,1,1,0,0,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,0,0,0,0,0,0,1,1,0,0,0,0}}));
        assertEquals(0, method.applyAsInt(new int[][]{{0,0,0,0,0,0,0,0}}));
    }

    /**
     * LeetCode 耗时：3 ms - 50.41%
     *          内存消耗：38.6 MB - 87.50%
     */
    public int maxAreaOfIsland(int[][] grid) {
        cnt = result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    dfs(grid, i, j);
                    if (result < cnt) {
                        result = cnt;
                    }
                    cnt = 0;
                }
            }
        }

        return result;
    }

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private int cnt, result;

    private void dfs(int[][] grid, int i, int j) {
        grid[i][j] = 2;
        cnt++;

        for (int[] dir : DIRS) {
            int r = i + dir[0], c = j + dir[1];
            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length
                    && grid[r][c] == 1) {
                dfs(grid, r, c);
            }
        }
    }

    @Test
    public void testMaxAreaOfIsland() {
        test(this::maxAreaOfIsland);
    }
}
