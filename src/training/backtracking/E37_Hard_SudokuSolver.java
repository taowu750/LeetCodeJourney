package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 37. 解数独：https://leetcode-cn.com/problems/sudoku-solver/
 *
 * 编写一个程序，通过填充空格来解决数独问题。一个数独的解法需遵循如下规则：
 * 1. 数字 1-9 在每一行只能出现一次。
 * 2. 数字 1-9在每一列只能出现一次。
 * 3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
 *
 * 空白格用 '.' 表示。
 *
 * 约束：
 * - 给定的数独序列只包含数字 1-9 和字符 '.' 。
 * - 你可以假设给定的数独只有唯一解。
 * - 给定数独永远是 9x9 形式的。
 */
public class E37_Hard_SudokuSolver {

    public static void test(Consumer<char[][]> method) {
        char[][] board = new char[][]{
                "53..7....".toCharArray(),
                "6..195...".toCharArray(),
                ".98....6.".toCharArray(),
                "8...6...3".toCharArray(),
                "4..8.3..1".toCharArray(),
                "7...2...6".toCharArray(),
                ".6....28.".toCharArray(),
                "...419..5".toCharArray(),
                "....8..79".toCharArray()};
        char[][] result = new char[][] {
                "534678912".toCharArray(),
                "672195348".toCharArray(),
                "198342567".toCharArray(),
                "859761423".toCharArray(),
                "426853791".toCharArray(),
                "713924856".toCharArray(),
                "961537284".toCharArray(),
                "287419635".toCharArray(),
                "345286179".toCharArray()};
        method.accept(board);
        assertArrayEquals(result, board);
    }

    /**
     * LeetCode 耗时：19 ms - 27.61%
     *          内存消耗：35.7 MB - 87.52%
     */
    public void solveSudoku(char[][] board) {
        dfs(board, 0, 0);
    }

    private boolean dfs(char[][] board, int i, int j) {
        int nextJ = j + 1 , nextI = i;
        if (nextJ >= 9) {
            nextJ = 0;
            nextI += 1;
        }

        if (board[i][j] == '.') {
            for (char num = '1'; num <= '9'; num++) {
                if (!isValid(board, i, j, num))
                    continue;

                board[i][j] = num;
                if (nextI >= 9)
                    return true;
                boolean completed = dfs(board, nextI, nextJ);
                if (completed)
                    return true;
                board[i][j] = '.';
            }
            return false;
        }

        if (nextI >= 9)
            return true;

        return dfs(board, nextI, nextJ);
    }

    private boolean isValid(char[][] board, int r, int c, char num) {
        for (int i = 0; i < 9; i++) {
            if (board[r][i] == num)
                return false;
            if (board[i][c] == num)
                return false;
            if (board[r / 3 * 3 + i / 3][c / 3 * 3 + i % 3] == num)
                return false;
        }
        return true;
    }

    @Test
    public void testSolveSudoku() {
        test(this::solveSudoku);
    }


    /**
     * 空间换时间。
     *
     * LeetCode 耗时：1 ms - 99.08%
     *          内存消耗：36 MB - 27.87%
     */
    public void betterMethod(char[][] board) {
        boolean[][] rows = new boolean[9][9];
        boolean[][] cols = new boolean[9][9];
        boolean[][][] cells = new boolean[3][3][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    continue;
                }
                int num = board[i][j] - '0' - 1;
                rows[i][num] = true;
                cols[j][num] = true;
                cells[i / 3][j / 3][num] = true;
            }
        }

        dfs(board, rows, cols, cells, 0, 0);
    }

    private boolean dfs(char[][] board, boolean[][] rows, boolean[][] cols, boolean[][][] cells,
                        int i, int j) {
        if (i >= 9) {
            return true;
        }
        while (j >= 9 || board[i][j] != '.') {
            if (++j >= 9) {
                j = 0;
                if (++i >= 9) {
                    return true;
                }
            }
        }

        for (int num = 0; num < 9; num++) {
            if (!rows[i][num] && !cols[j][num] && !cells[i / 3][j / 3][num]) {
                board[i][j] = (char) (num + '1');
                rows[i][num] = true;
                cols[j][num] = true;
                cells[i / 3][j / 3][num] = true;

                if (dfs(board, rows, cols, cells, i, j + 1)) {
                    return true;
                }

                board[i][j] = '.';
                rows[i][num] = false;
                cols[j][num] = false;
                cells[i / 3][j / 3][num] = false;
            }
        }

        return false;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
