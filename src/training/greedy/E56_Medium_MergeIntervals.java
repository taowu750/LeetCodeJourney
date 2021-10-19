package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 56. 合并区间: https://leetcode-cn.com/problems/merge-intervals/
 *
 * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。
 * 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
 *
 * 例 1：
 * 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
 * 输出：[[1,6],[8,10],[15,18]]
 * 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
 *
 * 例 2：
 * 输入：intervals = [[1,4],[4,5]]
 * 输出：[[1,5]]
 * 解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。
 *
 * 约束：
 * - 1 <= intervals.length <= 10**4
 * - intervals[i].length == 2
 * - 0 <= starti <= endi <= 10**4
 */
public class E56_Medium_MergeIntervals {

    public static void test(UnaryOperator<int[][]> method) {
        assertArrayEquals(new int[][]{{1, 6}, {8, 10}, {15, 18}},
                method.apply(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}));

        assertArrayEquals(new int[][]{{1, 5}},
                method.apply(new int[][]{{1, 4}, {4, 5}}));

        assertArrayEquals(new int[][]{{1, 10}},
                method.apply(new int[][]{{2, 3}, {4, 5}, {6, 7}, {8, 9}, {1, 10}}));
    }

    /**
     * 此题和 {@link training.greedy.E435_Medium_NonOverlappingIntervals} 类似。
     *
     * LeetCode 耗时：7 ms - 74.95%
     *          内存消耗：41.2 MB - 43.62%
     */
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1)
            return intervals;

        // 和 435 不同的是，这里按照 starti 排序，这样合并的时候就能合并跨度大的了
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });

        List<int[]> intervalList = new ArrayList<>((int) (intervals.length * 2. / 3.) + 1);
        intervalList.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] last = intervalList.get(intervalList.size() - 1);
            if (last[1] >= intervals[i][0]) {
                last[1] = Math.max(last[1], intervals[i][1]);
            } else {
                intervalList.add(intervals[i]);
            }
        }

        return intervalList.toArray(new int[0][0]);
    }

    @Test
    public void testMerge() {
        test(this::merge);
    }
}
