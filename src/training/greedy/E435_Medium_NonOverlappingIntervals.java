package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 435. 无重叠区间: https://leetcode-cn.com/problems/non-overlapping-intervals/
 *
 * 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
 * 注意：
 * 1. 可以认为区间的终点总是大于它的起点。
 * 2. 区间 [1,2] 和 [2,3] 的边界相互“接触”，但没有相互重叠。
 *
 * 例 1：
 * 输入: [ [1,2], [2,3], [3,4], [1,3] ]
 * 输出: 1
 * 解释: 移除 [1,3] 后，剩下的区间没有重叠。
 *
 * 例 2：
 * 输入: [ [1,2], [1,2], [1,2] ]
 * 输出: 2
 * 解释: 你需要移除两个 [1,2] 来使剩下的区间没有重叠。
 *
 * 例 3：
 * 输入: [ [1,2], [2,3] ]
 * 输出: 0
 * 解释: 你不需要移除任何区间，因为它们已经是无重叠的了。
 */
public class E435_Medium_NonOverlappingIntervals {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(method.applyAsInt(new int[][]{{1,2}, {2,3}, {3,4}, {1,3}}), 1);
        assertEquals(method.applyAsInt(new int[][]{{1,2}, {1,2}, {1,2}}), 2);
        assertEquals(method.applyAsInt(new int[][]{{1,2}, {2,3}}), 0);
        assertEquals(method.applyAsInt(new int[][]{{1,2}}), 0);
        assertEquals(method.applyAsInt(new int[][]{{-52,31}, {-73,-26}, {82,97}, {-65,-11}, {-62,-49}, {95,99}, {58,95},
                {-31,49}, {66,98}, {-63,2}, {30,47}, {-40,-26}}), 7);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/non-overlapping-intervals/solution/wu-zhong-die-qu-jian-by-leetcode-solutio-cpsb/
     *
     * 题目的要求等价于「选出最多数量的区间，使得它们互不重叠」。由于选出的区间互不重叠，因此我们可以将它们按照端点从小到大的顺序进行排序，
     * 并且无论我们按照左端点还是右端点进行排序，得到的结果都是唯一的。
     *
     * 这样一来，我们可以先将所有的 n 个区间按照左端点（或者右端点）从小到大进行排序，随后使用动态规划的方法求出区间数量的最大值。
     * 设排完序后这 n 个区间的左右端点分别为 l_0,...,l_{n-1} 以及 r_0,...,r_{n-1}，
     * 那么我们令 f_i 表示「以区间 i 为最后一个区间，可以选出的区间数量的最大值」，状态转移方程即为：
     *      f_i = max{f_j} + 1， j < i && r_j <= l_i
     *
     * LeetCode 超时
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        final int n = intervals.length;
        int[] dp = new int[n];
        // 需要注意，无论怎样最少我们都可以选出一个不重叠的区间
        Arrays.fill(dp, 1);

        int result = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 下面的 if 都不满足，dp[i] 应该为 1
                if (intervals[j][1] <= intervals[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            result = Math.max(result, dp[i]);
        }

        return intervals.length - result;
    }

    @Test
    public void testEraseOverlapIntervals() {
        test(this::eraseOverlapIntervals);
    }


    /**
     * 在选择要保留区间时，区间的结尾十分重要：选择的区间结尾越小，余留给其它区间的空间就越大，
     * 就越能保留更多的区间。因此，我们采取的贪心策略为，优先保留结尾小且不相交的区间。
     *
     * 我们不妨想一想应该选择哪一个区间作为首个区间。
     *
     * 假设在某一种最优的选择方法中，[l_k, r_k] 是首个（即最左侧的）区间，那么它的左侧没有其它区间，右侧有若干个不重叠的区间。
     * 设想一下，如果此时存在一个区间 [l_j, r_j] ，使得 r_j < r_k，即区间 j 的右端点在区间 k 的左侧，那么我们将区间 k 替换为区间 j，
     * 其与剩余右侧被选择的区间仍然是不重叠的。而当我们将区间 k 替换为区间 j 后，就得到了另一种最优的选择方法。
     *
     * 我们可以不断地寻找右端点在首个区间右端点左侧的新区间，将首个区间替换成该区间。那么当我们无法替换时，
     * 首个区间就是所有可以选择的区间中右端点最小的那个区间。因此我们将所有区间按照右端点从小到大进行排序，
     * 那么排完序之后的首个区间，就是我们选择的首个区间。
     *
     * 如果有多个区间的右端点都同样最小怎么办？由于我们选择的是首个区间，因此在左侧不会有其它的区间，那么左端点在何处是不重要的，
     * 我们只要任意选择一个右端点最小的区间即可。
     *
     * LeetCode 耗时：4 ms - 44.46%
     *          内存消耗：38.5 MB - 35.96%
     */
    public int greedyMethod(int[][] intervals) {
        // 按照 end 进行排序。
        // 比较器可以使用 Comparator.comparingInt(a -> a[1])，但这会略微影响速度
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

        // 不相交区间数量，至少有一个
        int count = 1;
        int lastEnd = intervals[0][1];
        for (int[] interval : intervals) {
            int start = interval[0];
            // 如果 start 大于等于 lastEnd，说明它们不相交
            if (start >= lastEnd) {
                count++;
                // 更新 lastEnd
                lastEnd = interval[1];
            }
        }

        // 需要去除的重叠子区间数量等于相交区间数量
        return intervals.length - count;
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
