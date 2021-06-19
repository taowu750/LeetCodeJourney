package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 79. 单词搜索: https://leetcode-cn.com/problems/word-search/
 *
 * 给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。如果 word 存在于网格中，
 * 返回 true ；否则，返回 false 。
 *
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母不允许被重复使用。
 *
 * 例 1：
 * 输入：
 * board = [["A","B","C","E"],
 *          ["S","F","C","S"],
 *          ["A","D","E","E"]],
 * word = "ABCCED"
 * 输出：true
 *
 * 例 2：
 * 输入：
 * board = [["A","B","C","E"],
 *          ["S","F","C","S"],
 *          ["A","D","E","E"]],
 * word = "SEE"
 * 输出：true
 *
 * 例 3：
 * 输入：
 * board = [["A","B","C","E"],
 *          ["S","F","C","S"],
 *          ["A","D","E","E"]],
 * word = "ABCB"
 * 输出：false
 *
 * 约束：
 * - m == board.length
 * - n = board[i].length
 * - 1 <= m, n <= 6
 * - 1 <= word.length <= 15
 * - board 和 word 仅由大小写英文字母组成
 */
public class E79_Medium_WordSearch {

    static void test(BiPredicate<char[][], String> method) {
        assertTrue(method.test(new char[][]{
                        {'A', 'B', 'C', 'E'},
                        {'S', 'F', 'C', 'S'},
                        {'A', 'D', 'E', 'E'}},
                "ABCCED"));
        assertTrue(method.test(new char[][]{
                        {'A', 'B', 'C', 'E'},
                        {'S', 'F', 'C', 'S'},
                        {'A', 'D', 'E', 'E'}},
                "SEE"));
        assertFalse(method.test(new char[][]{
                        {'A', 'B', 'C', 'E'},
                        {'S', 'F', 'C', 'S'},
                        {'A', 'D', 'E', 'E'}},
                "ABCB"));
        assertTrue(method.test(new char[][]{{'a'}}, "a"));
    }

    /**
     * 回溯算法+预处理。其中预处理效果惊人，不加预处理需要 118 ms。
     *
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：36.6 MB - 54.62%
     */
    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;

        // 统计 word 中每个字符出现的次数。
        int[] countMap1 = new int[58];
        for (int i = 0; i < word.length(); i++) {
            countMap1[word.charAt(i) - 'A']++;
        }
        // 统计 board 中每个字符出现的次数。
        int[] countMap2 = new int[58];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                countMap2[board[i][j] - 'A']++;
            }
        }
        // 预处理判断
        for (int i = 0; i < 58; i++) {
            if (countMap1[i] > countMap2[i]) {
                return false;
            }
        }


        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(board, visited, i, j, word, 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private boolean dfs(char[][] board, boolean[][] visited, int r, int c,
                        String word, int i) {
        if (word.charAt(i) != board[r][c]) {
            return false;
        }
        if (i == word.length() - 1) {
            return true;
        }

        visited[r][c] = true;
        for (int[] dir : DIRS) {
            int nr = r + dir[0], nc = c + dir[1];
            if (nr >= 0 && nr < board.length && nc >= 0 && nc < board[0].length
                    && !visited[nr][nc]
                    && dfs(board, visited, nr, nc, word, i + 1)) {
                return true;
            }
        }
        visited[r][c] = false;

        return false;
    }

    @Test
    public void testExist() {
        test(this::exist);
    }
}
