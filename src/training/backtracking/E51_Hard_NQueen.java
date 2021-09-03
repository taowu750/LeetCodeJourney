package training.backtracking;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.*;
import java.util.function.IntFunction;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 51. N 皇后: https://leetcode-cn.com/problems/n-queens/
 *
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 皇后可以攻击同一行、同一列、左上左下右上右下四个方向的任意单位。
 * <p>
 * 给你一个整数 n ，返回所有不同的 n 皇后问题的解决方案。
 * 每一种解法包含一个不同的 n 皇后问题的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
 * <p>
 * 例 1：
 * 输入：n = 4
 * 输出：[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
 * 解释：如上图所示，4 皇后问题存在两个不同的解法。
 * <p>
 * 例 2：
 * 输入：n = 1
 * 输出：[["Q"]]
 * <p>
 * 约束：
 * - 1 <= n <= 9
 * - 皇后彼此不能相互攻击，也就是说：任何两个皇后都不能处于同一条横行、纵行或斜线上。
 */
public class E51_Hard_NQueen {

    static void test(IntFunction<List<List<String>>> method) {
        assertTrue(CollectionUtil.deepEqualsIgnoreOutOrder(method.apply(4),
                Arrays.asList(Arrays.asList(".Q..", "...Q", "Q...", "..Q."),
                        Arrays.asList("..Q.", "Q...", "...Q", ".Q.."))));

        assertEquals(method.apply(1), singletonList(singletonList("Q")));
    }

    /**
     * LeetCode 耗时：2 ms - 92.96%
     *          内存消耗：38.7 MB - 63.84%
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] chess = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                chess[i][j] = '.';
            }
        }

        dfs(result, 0, chess);

        return result;
    }

    // 每行放一个皇后。这真是绝妙的点子，这样就不需要考虑很多繁杂的情况了
    private void dfs(List<List<String>> result, int row, char[][] chess) {
        final int n = chess.length;
        if (row == n) {
            String[] res = new String[n];
            for (int i = 0; i < n; i++)
                res[i] = new String(chess[i]);
            result.add(Arrays.asList(res));
            return;
        }

        for (int col = 0; col < n; col++) {
            if (isValid(chess, row, col)) {
                chess[row][col] = 'Q';
                dfs(result, row + 1, chess);
                chess[row][col] = '.';
            }
        }
    }

    private boolean isValid(char[][] chess, int row, int col) {
        for (int k = row - 1, lt = col - 1, rt = col + 1; k >= 0; k--, lt--, rt++) {
            // 确保同一列没有皇后
            if (chess[k][col] == 'Q') {
                return false;
            }
            // 确保左上方没有皇后
            if (lt >= 0 && chess[k][lt] == 'Q') {
                return false;
            }
            // 确保右上方没有皇后
            if (rt < chess.length && chess[k][rt] == 'Q') {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testSolveNQueens() {
        test(this::solveNQueens);
    }
}
