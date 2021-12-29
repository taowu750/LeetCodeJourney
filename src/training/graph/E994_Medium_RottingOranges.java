package training.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 994. 腐烂的橘子: https://leetcode-cn.com/problems/rotting-oranges/
 *
 * 在给定的网格中，每个单元格可以有以下三个值之一：
 * - 值 0 代表空单元格；
 * - 值 1 代表新鲜橘子；
 * - 值 2 代表腐烂的橘子。
 * 每分钟，任何与腐烂的橘子（在 4 个正方向上）相邻的新鲜橘子都会腐烂。
 *
 * 返回直到单元格中没有新鲜橘子为止所必须经过的最小分钟数。如果不可能，返回 -1。
 *
 * 例 1：
 * 输入：[[2,1,1],[1,1,0],[0,1,1]]
 * 输出：4
 *
 * 例 2：
 * 输入：[[2,1,1],[0,1,1],[1,0,1]]
 * 输出：-1
 * 解释：左下角的橘子（第 2 行， 第 0 列）永远不会腐烂，因为腐烂只会发生在 4 个正向上。
 *
 * 例 3：
 * 输入：[[0,2]]
 * 输出：0
 * 解释：因为 0 分钟时已经没有新鲜橘子了，所以答案就是 0 。
 *
 * 说明：
 * - 1 <= grid.length <= 10
 * - 1 <= grid[0].length <= 10
 * - grid[i][j] 仅为 0、1 或 2
 */
public class E994_Medium_RottingOranges {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(4, method.applyAsInt(new int[][]{
                {2,1,1},
                {1,1,0},
                {0,1,1}}));
        assertEquals(-1, method.applyAsInt(new int[][]{
                {2,1,1},
                {0,1,1},
                {1,0,1}}));
        assertEquals(0, method.applyAsInt(new int[][]{{0, 2}}));
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：37.8 MB - 62.66%
     */
    public int orangesRotting(int[][] grid) {
        final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int m = grid.length, n = grid[0].length, freshCnt = 0;
        Deque<int[]> rottens = new ArrayDeque<>(m * n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    freshCnt++;
                } else if (grid[i][j] == 2) {
                    rottens.add(new int[]{i, j});
                }
            }
        }
        if (freshCnt == 0) {
            return 0;
        } else if (rottens.isEmpty()) {
            return -1;
        }

        int minutes = 0;
        while (!rottens.isEmpty() && freshCnt != 0) {
            int size = rottens.size();
            for (int i = 0; i < size; i++) {
                int[] pos = rottens.remove();
                for (int[] dir : dirs) {
                    int r = dir[0] + pos[0], c = dir[1] + pos[1];
                    if (r >= 0 && r < m && c >= 0 && c < n && grid[r][c] == 1) {
                        grid[r][c] = 2;
                        rottens.add(new int[]{r, c});
                        freshCnt--;
                    }
                }
            }
            minutes++;
        }

        return freshCnt == 0 ? minutes : -1;
    }

    @Test
    public void testOrangesRotting() {
        test(this::orangesRotting);
    }
}
