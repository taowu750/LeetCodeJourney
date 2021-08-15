package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 1109. 航班预订统计: https://leetcode-cn.com/problems/corporate-flight-bookings/
 *
 * 这里有 n 个航班，它们分别从 1 到 n 进行编号。
 *
 * 有一份航班预订表 bookings ，表中第 i 条预订记录 bookings[i] = [firsti, lasti, seatsi]
 * 意味着在从 firsti 到 lasti （包含 firsti 和 lasti ）的「每个航班」上预订了 seatsi 个座位。
 *
 * 请你返回一个长度为 n 的数组 answer，其中 answer[i] 是航班 i 上预订的座位总数。
 *
 * 例 1：
 * 输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
 * 输出：[10,55,45,25,25]
 * 解释：
 * 航班编号        1   2   3   4   5
 * 预订记录 1 ：   10  10
 * 预订记录 2 ：       20  20
 * 预订记录 3 ：       25  25  25  25
 * 总座位数：      10  55  45  25  25
 * 因此，answer = [10,55,45,25,25]
 *
 * 例 2：
 * 输入：bookings = [[1,2,10],[2,2,15]], n = 2
 * 输出：[10,25]
 * 解释：
 * 航班编号        1   2
 * 预订记录 1 ：   10  10
 * 预订记录 2 ：       15
 * 总座位数：      10  25
 * 因此，answer = [10,25]
 *
 * 约束：
 * - 1 <= n <= 2 * 10**4
 * - 1 <= bookings.length <= 2 * 10**4
 * - bookings[i].length == 3
 * - 1 <= firsti <= lasti <= n
 * - 1 <= seatsi <= 10**4
 */
public class E1109_Medium_CorporateFlightBookings {

    static void test(BiFunction<int[][], Integer, int[]> method) {
        assertArrayEquals(new int[]{10,55,45,25,25},
                method.apply(new int[][]{{1,2,10}, {2,3,20}, {2,5,25}}, 5));

        assertArrayEquals(new int[]{10,25},
                method.apply(new int[][]{{1,2,10}, {2,2,15}}, 2));
    }

    /**
     * 差分数组算法。参见：
     * https://labuladong.gitee.io/algo/算法思维系列/差分技巧/
     * 和 {@link util.datastructure.DiffArray}。
     *
     * LeetCode 耗时：3 ms - 99.39%
     *          内存消耗：53.5 MB - 60.58%
     */
    public int[] corpFlightBookings(int[][] bookings, int n) {
        int[] diff = new int[n];
        for (int[] booking : bookings) {
            // 注意 booking 中的下标从 1 开始
            diff[booking[0] - 1] += booking[2];
            if (booking[1] < n)
                diff[booking[1]] -= booking[2];
        }

        for (int i = 1; i < n; i++) {
            diff[i] += diff[i - 1];
        }

        return diff;
    }

    @Test
    public void testCorpFlightBookings() {
        test(this::corpFlightBookings);
    }
}
