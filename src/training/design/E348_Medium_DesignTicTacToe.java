package training.design;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 348. 设计井字棋: https://leetcode-cn.com/problems/design-tic-tac-toe/
 *
 * 请在 n × n 的棋盘上，实现一个判定井字棋（Tic-Tac-Toe）胜负的神器，判断每一次玩家落子后，是否有胜出的玩家。
 * 在这个井字棋游戏中，会有 2 名玩家，他们将轮流在棋盘上放置自己的棋子。
 * 在实现这个判定器的过程中，你可以假设以下这些规则一定成立：
 * 1. 每一步棋都是在棋盘内的，并且只能被放置在一个空的格子里；
 * 2. 一旦游戏中有一名玩家胜出的话，游戏将不能再继续；
 * 3. 一个玩家如果在同一行、同一列或者同一斜对角线上都放置了自己的棋子，那么他便获得胜利。
 *
 * 您有没有可能将每一步的 move() 操作优化到比 O(n^2) 更快吗?
 *
 * 例 1：
 * 给定棋盘边长 n = 3, 玩家 1 的棋子符号是 "X"，玩家 2 的棋子符号是 "O"。
 *
 * TicTacToe toe = new TicTacToe(3);
 *
 * toe.move(0, 0, 1); -> 函数返回 0 (此时，暂时没有玩家赢得这场对决)
 * |X| | |
 * | | | |    // 玩家 1 在 (0, 0) 落子。
 * | | | |
 *
 * toe.move(0, 2, 2); -> 函数返回 0 (暂时没有玩家赢得本场比赛)
 * |X| |O|
 * | | | |    // 玩家 2 在 (0, 2) 落子。
 * | | | |
 *
 * toe.move(2, 2, 1); -> 函数返回 0 (暂时没有玩家赢得比赛)
 * |X| |O|
 * | | | |    // 玩家 1 在 (2, 2) 落子。
 * | | |X|
 *
 * toe.move(1, 1, 2); -> 函数返回 0 (暂没有玩家赢得比赛)
 * |X| |O|
 * | |O| |    // 玩家 2 在 (1, 1) 落子。
 * | | |X|
 *
 * toe.move(2, 0, 1); -> 函数返回 0 (暂无玩家赢得比赛)
 * |X| |O|
 * | |O| |    // 玩家 1 在 (2, 0) 落子。
 * |X| |X|
 *
 * toe.move(1, 0, 2); -> 函数返回 0 (没有玩家赢得比赛)
 * |X| |O|
 * |O|O| |    // 玩家 2 在 (1, 0) 落子.
 * |X| |X|
 *
 * toe.move(2, 1, 1); -> 函数返回 1 (此时，玩家 1 赢得了该场比赛)
 * |X| |O|
 * |O|O| |    // 玩家 1 在 (2, 1) 落子。
 * |X|X|X|
 */
public class E348_Medium_DesignTicTacToe {

    public static void test(IntFunction<ITicTacToe> factory) {
        ITicTacToe toe = factory.apply(3);
        assertEquals(0, toe.move(0, 0, 1));
        assertEquals(0, toe.move(0, 2, 2));
        assertEquals(0, toe.move(2, 2, 1));
        assertEquals(0, toe.move(1, 1, 2));
        assertEquals(0, toe.move(2, 0, 1));
        assertEquals(0, toe.move(1, 0, 2));
        assertEquals(1, toe.move(2, 1, 1));

        toe = factory.apply(3);
        assertEquals(0, toe.move(1, 2, 2));
        assertEquals(0, toe.move(0, 2, 1));
        assertEquals(0, toe.move(0, 0, 2));
        assertEquals(0, toe.move(2, 0, 1));
        assertEquals(0, toe.move(0, 1, 2));
        assertEquals(1, toe.move(1, 1, 1));
    }

    @Test
    public void testTicTacToe() {
        test(TicTacToe::new);
    }

    @Test
    public void testHashMethod() {
        test(HashMethod::new);
    }
}

interface ITicTacToe {

    int move(int row, int col, int player);
}

/**
 * LeetCode 耗时：4 ms - 61.35%
 *          内存消耗：41.2 MB - 87.25%
 */
class TicTacToe implements ITicTacToe {

    private char[][] grid;
    private int winner;

    public TicTacToe(int n) {
        grid = new char[n][n];
    }

    public int move(int row, int col, int player) {
        if (winner != 0) {
            return winner;
        }

        char piece = player == 1 ? 'X' : 'O';
        grid[row][col] = piece;

        // 检查列
        int i = 0;
        for (; i < grid.length; i++) {
            if (grid[i][col] != piece) {
                break;
            }
        }
        if (i == grid.length) {
            winner = player;
            return winner;
        }

        // 检查行
        for (i = 0; i < grid.length; i++) {
            if (grid[row][i] != piece) {
                break;
            }
        }
        if (i == grid.length) {
            winner = player;
            return winner;
        }

        // 检查正对角线
        if (row == col) {
            for (i = 0; i < grid.length; i++) {
                if (grid[i][i] != piece) {
                    break;
                }
            }
            if (i == grid.length) {
                winner = player;
                return winner;
            }
        }
        // 检查逆对角线
        if (row + col == grid.length - 1) {
            for (i = 0; i < grid.length; i++) {
                if (grid[i][grid.length - i - 1] != piece) {
                    break;
                }
            }
            if (i == grid.length) {
                winner = player;
                return winner;
            }
        }

        return 0;
    }
}

/**
 * LeetCode 耗时：3 ms - 100.00%
 *          内存消耗：41.6 MB - 18.73%
 */
class HashMethod implements ITicTacToe {

    private static class Counter {

        private int n;
        private int[] rowCnts, colCnts;
        private int diagonalCnt, rDiagonalCnt;

        public Counter(int n) {
            this.n = n;
            rowCnts = new int[n];
            colCnts = new int[n];
        }

        boolean count(int row, int col) {
            if (++rowCnts[row] == n) {
                return true;
            }
            if (++colCnts[col] == n) {
                return true;
            }
            if (row == col) {
                if (++diagonalCnt == n) {
                    return true;
                }
            }
            if (row + col == n - 1) {
                return ++rDiagonalCnt == n;
            }

            return false;
        }
    }

    private Counter[] counters;

    public HashMethod(int n) {
        counters = new Counter[3];
        counters[1] = new Counter(n);
        counters[2] = new Counter(n);
    }

    public int move(int row, int col, int player) {
        // 去掉 if 判断，提升 1ms

        if (counters[player].count(row, col)) {
            return player;
        }

        return 0;
    }
}