package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 费解的开关: https://ac.nowcoder.com/acm/contest/998/D
 *
 * 在一个5*5的01矩阵中，点击任意一个位置，该位置以及它上、下、左、右四个相邻的位置中的数字都会变化(0变成1，1变成0)，
 * （原题）最少需要多少次点击才能把01矩阵变成全0矩阵。
 * （牛客网）判断是否可能在6步以内使所有数字变为1。
 *
 * 例 1：
 * 输入：
 * 00111
 * 01011
 * 10001
 * 11010
 * 11100
 * 输出：
 * 3
 *
 * 例 2：
 * 输入：
 * 11101
 * 11101
 * 11110
 * 11111
 * 11111
 * 输出：
 * 2
 *
 * 例 3：
 * 输入：
 * 01111
 * 11111
 * 11111
 * 11111
 * 11111
 * 输出：
 * -1
 *
 * 说明：
 * - 输出最少需要几步才能使所有数字变为1
 * - 不能在6步以内使所有数字变为1则输出-1
 */
public class G008_PuzzlingSwitch {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(3, method.applyAsInt(new int[][]{
                {0, 0, 1, 1, 1},
                {0, 1, 0, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 0, 1, 0},
                {1, 1, 1, 0, 0}}));
        assertEquals(2, method.applyAsInt(new int[][]{
                {1, 1, 1, 0, 1},
                {1, 1, 1, 0, 1},
                {1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}}));
        assertEquals(-1, method.applyAsInt(new int[][]{
                {0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}}));
    }

    public int play(int[][] grid) {
        int ans = Integer.MAX_VALUE;
        // 不能直接用 clone，因为是浅复制
        int[][] tmp = new int[5][5];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(grid[i], 0, tmp[i], 0, 5);
        }
        for (int k = 0, status; k < 32; k++) {
            status = k;
            int cnt = 0;
            // 根据二进制位点击第0行
            while (status != 0) {
                cnt++;
                flip(tmp, 0, H[(status & -status) % 37]);
                status &= status - 1;
            }
            // 根据上一行点击下一行
            for (int i = 1; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (tmp[i - 1][j] == 0) {
                        cnt++;
                        flip(tmp, i, j);
                    }
                }
            }
            // 判断是否全为1
            boolean finished = true;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (tmp[i][j] == 0) {
                        finished = false;
                        break;
                    }
                }
            }
            if (finished) {
                ans = Math.min(ans, cnt);
            }
            // 复位tmp
            for (int i = 0; i < 5; i++) {
                System.arraycopy(grid[i], 0, tmp[i], 0, 5);
            }
        }

        // 满足牛客网的需求
        return ans < 7 ? ans : -1;
    }

    private static final int[] H = new int[37];
    static {
        for (int i = 0; i < 36; i++) {
            H[(int) ((1L << i) % 37)] = i;
        }
    }

    private void flip(int[][] grid, int i, int j) {
        final int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        grid[i][j] ^= 1;
        for (int[] dir : dirs) {
            int ni = i + dir[0], nj = j + dir[1];
            if (ni >= 0 && ni < 5 && nj >= 0 && nj < 5) {
                grid[ni][nj] ^= 1;
            }
        }
    }

    @Test
    public void testPlay() {
        test(this::play);
    }
}
