package training.graph;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 130. 被围绕的区域: https://leetcode-cn.com/problems/surrounded-regions/
 *
 * 给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' ，找到所有被 'X' 围绕的区域，
 * 并将这些区域里所有的 'O' 用 'X' 填充。
 *
 * 例 1：
 * 输入：board = [["X","X","X","X"],
 *               ["X","O","O","X"],
 *               ["X","X","O","X"],
 *               ["X","O","X","X"]]
 * 输出：[["X","X","X","X"],
 *       ["X","X","X","X"],
 *       ["X","X","X","X"],
 *       ["X","O","X","X"]]
 * 解释：被围绕的区间不会存在于边界上，换句话说，任何边界上的 'O' 都不会被填充为 'X'。
 * 任何不在边界上，或不与边界上的 'O' 相连的 'O' 最终都会被填充为 'X'。如果两个元素在水平或垂直方向相邻，
 * 则称它们是“相连”的。
 *
 * 例 2：
 * 输入：board = [["X"]]
 * 输出：[["X"]]
 *
 * 约束：
 * - m == board.length
 * - n == board[i].length
 * - 1 <= m, n <= 200
 * - board[i][j] 为 'X' 或 'O'
 */
public class E130_Medium_SurroundedRegions {

    static void test(Consumer<char[][]> method) {
        char[][] board = {
                {'X','X','X','X'},
                {'X','O','O','X'},
                {'X','X','O','X'},
                {'X','O','X','X'}};
        method.accept(board);
        assertArrayEquals(new char[][]{
                {'X','X','X','X'},
                {'X','X','X','X'},
                {'X','X','X','X'},
                {'X','O','X','X'}}, board);

        board = new char[][]{{'X'}};
        method.accept(board);
        assertArrayEquals(new char[][]{{'X'}}, board);

        board = new char[][]{
                {'X','X','X','X'},
                {'O','O','O','X'},
                {'X','X','O','X'},
                {'X','X','X','X'}};
        method.accept(board);
        assertArrayEquals(new char[][]{
                {'X','X','X','X'},
                {'O','O','O','X'},
                {'X','X','O','X'},
                {'X','X','X','X'}}, board);

        board = new char[][]{
                {'O','X','X','O'},
                {'X','O','X','O'},
                {'X','O','O','X'},
                {'X','O','X','O'},
                {'X','O','O','O'}};
        method.accept(board);
        System.out.println(Arrays.deepToString(board).replace("],", "],\n"));
        assertArrayEquals(new char[][]{
                {'O','X','X','O'},
                {'X','O','X','O'},
                {'X','O','O','X'},
                {'X','O','X','O'},
                {'X','O','O','O'}}, board);
    }

    private static final int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    /**
     * LeetCode 耗时：2 ms - 72.27%
     *          内存消耗：40.6 MB - 22.54%
     */
    public void solve(char[][] board) {
        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];

        int[] rows = {0, m - 1}, cols = {0, n - 1};
        for (int i : rows) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O' && !visited[i][j]) {
                    dfs(board, visited, i, j);
                }
            }
        }
        for (int j : cols) {
            for (int i = 1; i < m - 1; i++) {
                if (board[i][j] == 'O' && !visited[i][j]) {
                    dfs(board, visited, i, j);
                }
            }
        }

        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (board[i][j] == 'O' && !visited[i][j]) {
                    board[i][j] = 'X';
                }
            }
        }
    }

    private void dfs(char[][] board, boolean[][] visited, int i, int j) {
        visited[i][j] = true;
        for (int[] dir : dirs) {
            int r = i + dir[0], c = j + dir[1];
            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length
                    && !visited[r][c] && board[r][c] == 'O') {
                dfs(board, visited, r, c);
            }
        }
    }

    @Test
    public void testSolve() {
        test(this::solve);
    }
}
