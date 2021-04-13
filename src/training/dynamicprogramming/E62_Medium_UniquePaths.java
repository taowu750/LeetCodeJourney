package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。
 * 机器人每次只能「向下」或者「向右」移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。
 * 问总共有多少条不同的路径？
 *
 * 例 1：
 * 输入：m = 3, n = 7
 * 输出：28
 *
 * 例 2：
 * 输入：m = 3, n = 2
 * 输出：3
 * 解释：
 * 从左上角开始，总共有 3 条路径可以到达右下角。
 * 1. 向右 -> 向下 -> 向下
 * 2. 向下 -> 向下 -> 向右
 * 3. 向下 -> 向右 -> 向下
 *
 * 例 3：
 * 输入：m = 7, n = 3
 * 输出：28
 *
 * 例 4：
 * 输入：m = 3, n = 3
 * 输出：6
 *
 * 约束：
 * - 1 <= m, n <= 100
 * - 题目数据保证答案小于等于 2 * 10**9
 */
public class E62_Medium_UniquePaths {

    static void test(ToIntBiFunction<Integer, Integer> method) {
        assertEquals(28, method.applyAsInt(3, 7));
        assertEquals(3, method.applyAsInt(3, 2));
        assertEquals(28, method.applyAsInt(7, 3));
        assertEquals(6, method.applyAsInt(3, 3));
        assertEquals(1, method.applyAsInt(1, 1));
        assertEquals(1, method.applyAsInt(1, 5));
        assertEquals(1, method.applyAsInt(5, 1));
    }

    /**
     * 此题类似于 {@link company.baidu.BD4_Hard_MushroomArray}，不过这个是此题的进阶版。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.1 MB - 76.72%
     */
    public int uniquePaths(int m, int n) {
        final int[] dp = new int[n];
        Arrays.fill(dp, 1);

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] += dp[j - 1];
            }
        }

        return dp[n - 1];
    }

    @Test
    public void testUniquePaths() {
        test(this::uniquePaths);
    }
}
