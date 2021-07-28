package training.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static util.CollectionUtil.deepEqualsIgnoreOutOrder;

/**
 * 417. 太平洋大西洋水流问题: https://leetcode-cn.com/problems/pacific-atlantic-water-flow/
 *
 * 给定一个 m x n 的非负整数矩阵来表示一片大陆上各个单元格的高度。“太平洋”处于大陆的左边界和上边界，
 * 而“大西洋”处于大陆的右边界和下边界。
 *
 * 规定水流只能按照上、下、左、右四个方向流动，且只能从高到低或者在同等高度上流动。
 * 请找出那些水流既可以流动到“太平洋”，又能流动到“大西洋”的陆地单元的坐标。
 *
 * 例 1：
 * 给定下面的 5x5 矩阵:
 *
 *   太平洋 ~   ~   ~   ~   ~
 *        ~  1   2   2   3  (5) *
 *        ~  3   2   3  (4) (4) *
 *        ~  2   4  (5)  3   1  *
 *        ~ (6) (7)  1   4   5  *
 *        ~ (5)  1   1   2   4  *
 *           *   *   *   *   * 大西洋
 *
 * 返回:
 * [[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]] (上图中带括号的单元).
 *
 * 约束：
 * - 输出坐标的顺序不重要
 * - m 和 n 都小于150
 */
public class E417_Medium_PacificAtlanticWaterFlow {

    static void test(Function<int[][], List<List<Integer>>> method) {
        deepEqualsIgnoreOutOrder(Arrays.asList(Arrays.asList(0, 4), Arrays.asList(1, 3), Arrays.asList(1, 4),
                Arrays.asList(2, 2), Arrays.asList(3, 0), Arrays.asList(3, 1), Arrays.asList(4, 0)),
                method.apply(new int[][]{
                        {1, 2, 3, 4, 5},
                        {3, 2, 3, 4, 4},
                        {2, 4, 5, 3, 1},
                        {6, 7, 1, 4, 5},
                        {5, 1, 1, 2, 4},
                }));

        deepEqualsIgnoreOutOrder(Arrays.asList(Arrays.asList(0, 0), Arrays.asList(0, 1), Arrays.asList(1, 0),
                Arrays.asList(1, 1), Arrays.asList(2, 0), Arrays.asList(2, 1)),
                method.apply(new int[][]{
                        {1, 1},
                        {1, 1},
                        {1, 1},
                }));
    }

    private boolean flowPacific, flowAtlantic;

    /**
     * 顺流而下。
     *
     * LeetCode 耗时：58 ms - 13%
     *          内存消耗：39.8 MB - 36%
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        if (heights.length == 0 || heights[0].length == 0) {
            return Collections.emptyList();
        } else if (heights.length == 1 && heights[0].length == 1) {
            return Collections.singletonList(Arrays.asList(0, 0));
        }

        int m = heights.length, n = heights[0].length;
        List<List<Integer>> result = new ArrayList<>();
        // 反斜对角的坐标一定可以
        result.add(Arrays.asList(m - 1, 0));
        result.add(Arrays.asList(0, n - 1));

        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if ((i == m - 1 && j == 0) || (i == 0 && j == n - 1)) {
                    continue;
                }
                flowPacific = flowAtlantic = false;
                dfs(heights, visited, i, j);
                if (flowPacific && flowAtlantic) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }

        return result;
    }

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private void dfs(int[][] heights, boolean[][] visited, int row, int col) {
        if (flowPacific && flowAtlantic) {
            return;
        }

        int m = heights.length, n = heights[0].length;
        if (row < 0 || col < 0) {
            flowPacific = true;
            return;
        }
        if (row >= m || col >= n) {
            flowAtlantic = true;
            return;
        }

        visited[row][col] = true;
        for (int[] dir: DIRS) {
            int r = row + dir[0], c = col + dir[1];
            if (r < 0 || r >= m || c < 0 || c >= n || (!visited[r][c] && heights[row][col] >= heights[r][c])) {
                dfs(heights, visited, r, c);
            }
        }
        visited[row][col] = false;
    }

    @Test
    public void testPacificAtlantic() {
        test(this::pacificAtlantic);
    }


    /**
     * 逆向思维，逆流而上。
     *
     * LeetCode 耗时：4 ms - 98%
     *          内存消耗：39.8 MB - 38%
     */
    public List<List<Integer>> reverseMethod(int[][] heights) {
        if (heights.length == 0 || heights[0].length == 0) {
            return Collections.emptyList();
        } else if (heights.length == 1 && heights[0].length == 1) {
            return Collections.singletonList(Arrays.asList(0, 0));
        }

        int m = heights.length, n = heights[0].length;
        boolean[][] pacificArea = new boolean[m][n];
        boolean[][] atlanticArea = new boolean[m][n];

        for (int i = 0; i < m; i++) {
            newDfs(heights, pacificArea, i, 0);
            newDfs(heights, atlanticArea, i, n - 1);
        }
        for (int j = 0; j < n; j++) {
            newDfs(heights, pacificArea, 0, j);
            newDfs(heights, atlanticArea, m - 1, j);
        }

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacificArea[i][j] && atlanticArea[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }

        return result;
    }

    private void newDfs(int[][] heights, boolean[][] area, int row, int col) {
        int m = heights.length, n = heights[0].length;
        area[row][col] = true;
        for (int[] dir: DIRS) {
            int r = row + dir[0], c = col + dir[1];
            if (r >= 0 && r < m && c >= 0 && c < n && (!area[r][c] && heights[row][col] <= heights[r][c])) {
                newDfs(heights, area, r, c);
            }
        }
    }

    @Test
    public void testReverseMethod() {
        test(this::reverseMethod);
    }
}
