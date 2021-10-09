package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 289. 生命游戏: https://leetcode-cn.com/problems/game-of-life/
 *
 * 生命游戏，简称为生命，是英国数学家约翰·何顿·康威在 1970 年发明的细胞自动机。
 *
 * 给定一个包含 m × n 个格子的面板，每一个格子都可以看成是一个细胞。每个细胞都具有一个初始状态：
 * 1 即为活细胞（live），或 0 即为死细胞（dead）。每个细胞与其八个相邻位置（水平，垂直，对角线）
 * 的细胞都遵循以下四条生存定律：
 * 1. 如果活细胞周围八个位置的活细胞数少于两个，则该位置活细胞死亡；
 * 2. 如果活细胞周围八个位置有两个或三个活细胞，则该位置活细胞仍然存活；
 * 3. 如果活细胞周围八个位置有超过三个活细胞，则该位置活细胞死亡；
 * 4. 如果死细胞周围正好有三个活细胞，则该位置死细胞复活；
 *
 * 下一个状态是通过将上述规则同时应用于当前状态下的每个细胞所形成的，其中细胞的出生和死亡是同时发生的。
 * 给你 m x n 网格面板 board 的当前状态，返回下一个状态。
 *
 * 你可以使用原地算法解决本题吗？请注意，面板上所有格子需要同时被更新：你不能先更新某些格子，
 * 然后使用它们的更新后的值再更新其他格子。
 *
 * 本题中，我们使用二维数组来表示面板。原则上，面板是无限的，但当活细胞侵占了面板边界时会造成问题。你将如何解决这些问题？
 *
 * 例 1：
 * 输入：board = [[0,1,0],
 *               [0,0,1],
 *               [1,1,1],
 *               [0,0,0]]
 * 输出：[[0,0,0],
 *       [1,0,1],
 *       [0,1,1],
 *       [0,1,0]]
 *
 * 例 2：
 * 输入：board = [[1,1],
 *               [1,0]]
 * 输出：[[1,1],
 *       [1,1]]
 *
 * 约束：
 * - m == board.length
 * - n == board[i].length
 * - 1 <= m, n <= 25
 * - board[i][j] 为 0 或 1
 */
public class E289_Medium_LifeGame {

    public static void test(Consumer<int[][]> method) {
        int[][] board = new int[][]{
                {0,1,0},
                {0,0,1},
                {1,1,1},
                {0,0,0}};
        method.accept(board);
        assertArrayEquals(new int[][]{
                {0,0,0},
                {1,0,1},
                {0,1,1},
                {0,1,0}}, board);

        board = new int[][]{
                {1,1},
                {1,0}};
        method.accept(board);
        assertArrayEquals(new int[][]{
                {1,1},
                {1,1}}, board);
    }

    private static final int[][] DIRS = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.2 MB - 99.18%
     */
    public void gameOfLife(int[][] board) {
        // 原地算法，使用 board[i][j] 的倒数第二个 bit 位作为下一个状态的值
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int cnt = 0;
                for (int[] dir : DIRS) {
                    int r = i + dir[0], c = j + dir[1];
                    if (r >= 0 && r < m && c >= 0 && c < n && (board[r][c] & 1) == 1) {
                        cnt++;
                    }
                }
                if ((board[i][j] & 1) == 1) {
                    if (cnt == 2 || cnt == 3) {
                        board[i][j] += 2;
                    }
                } else if (cnt == 3) {
                    board[i][j] += 2;
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = board[i][j] >>> 1;
            }
        }
    }

    @Test
    public void testGameOfLife() {
        test(this::gameOfLife);
    }
}
