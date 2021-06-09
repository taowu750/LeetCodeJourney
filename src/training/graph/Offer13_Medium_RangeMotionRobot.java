package training.graph;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 13. 机器人的运动范围: https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/
 *
 * 地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。
 *
 * 一个机器人从坐标 [0, 0] 的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），
 * 也不能进入行坐标和列坐标的「数位之和」大于k的格子。
 *
 * 例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，因为3+5+3+8=19。
 *
 * 请问该机器人能够到达多少个格子？
 *
 * 例 1：
 * 输入：m = 2, n = 3, k = 1
 * 输出：3
 *
 * 例 2：
 * 输入：m = 3, n = 1, k = 0
 * 输出：1
 *
 * 约束：
 * - 1 <= n,m <= 100
 * - 0 <= k <= 20
 */
public class Offer13_Medium_RangeMotionRobot {

    static void test(TriFunction<Integer, Integer, Integer, Integer> method) {
        assertEquals(3, method.apply(2, 3, 1));
        assertEquals(1, method.apply(3, 1, 0));
        assertEquals(1484, method.apply(40, 40, 18));
    }

    /**
     * LeetCode 耗时：7 ms - 12.88%
     *          内存消耗：35.7 MB - 35.31%
     */
    public int movingCount(int m, int n, int k) {
        boolean[][] table = new boolean[m][n];
        table[0][0] = true;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, 0});
        int count = 1;

        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] pos = queue.remove();
            for (int[] dir : dirs) {
                int row = pos[0] + dir[0], col = pos[1] + dir[1];
                if (row >= 0 && row < m && col >= 0 && col < n
                        && !table[row][col]
                        && (sumOfDigits(row) + sumOfDigits(col) <= k)) {
                    table[row][col] = true;
                    queue.add(new int[]{row, col});
                    count++;
                }
            }
        }

        return count;
    }

    private int sumOfDigits(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }

        return sum;
    }

    @Test
    public void testMovingCount() {
        test(this::movingCount);
    }
}
