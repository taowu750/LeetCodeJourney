package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(method.applyAsInt(new int[][]{{1,2}, {2,3}, {3,4}, {1,3}}), 1);
        assertEquals(method.applyAsInt(new int[][]{{1,2}, {1,2}, {1,2}}), 2);
        assertEquals(method.applyAsInt(new int[][]{{1,2}, {2,3}}), 0);
    }

    /**
     * 算法思想参见 https://labuladong.gitee.io/algo/动态规划系列/贪心算法之区间调度问题.html
     *
     * LeetCode 耗时：4 ms - 44.46%
     *          内存消耗：38.5 MB - 35.96%
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length < 2)
            return 0;

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
    public void testEraseOverlapIntervals() {
        test(this::eraseOverlapIntervals);
    }
}
