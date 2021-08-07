package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 253. 会议室 II: https://leetcode-cn.com/problems/meeting-rooms-ii/
 *
 * 给你一个会议时间安排的数组 intervals ，每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi]，
 * 为避免会议冲突，同时要考虑充分利用会议室资源，请你计算至少需要多少间会议室，才能满足这些会议安排。
 *
 * 例 1：
 * 输入：intervals = [[0,30],[5,10],[15,20]]
 * 输出：2
 *
 * 例 2：
 * 输入：intervals = [[7,10],[2,4]]
 * 输出：1
 *
 * 约束：
 * - 1 <= intervals.length <= 10^4
 * - 0 <= starti < endi <= 10^6
 */
public class E253_Medium_MeetingRoomsII {

    static void test(ToIntFunction<int[][]> method) {
        assertEquals(2, method.applyAsInt(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        assertEquals(1, method.applyAsInt(new int[][]{{7, 10}, {2, 4}}));
        assertEquals(2, method.applyAsInt(new int[][]{{0, 5}, {3, 10}, {6, 15}}));
        assertEquals(2, method.applyAsInt(new int[][]{{2, 15}, {36, 45}, {9, 29}, {16, 23}, {4, 9}}));
        assertEquals(1, method.applyAsInt(new int[][]{{2, 7}}));
        assertEquals(2, method.applyAsInt(new int[][]{{1, 5}, {8, 9}, {8, 9}}));
    }

    /**
     * 排序+优先队列。
     *
     * LeetCode 耗时：7 ms - 74.62%
     *          内存消耗：38.2 MB - 72.55%
     */
    public int minMeetingRooms(int[][] intervals) {
        // 按照开始时间进行排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // 将结束时间添加到堆中
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(intervals[0][1]);

        int result = 1, remainRoom = 0;
        for (int i = 1; i < intervals.length; i++) {
            int start = intervals[i][0], end = intervals[i][1];
            // 将已经结束的事件弹出来，这样就有会议室空了出来
            while (!pq.isEmpty() && pq.peek() <= start) {
                pq.poll();
                remainRoom++;
            }
            pq.add(end);
            // 如果没有剩余会议室，则需要添加会议室；否则可以使用空着的会议室
            if (remainRoom == 0) {
                result++;
            } else {
                remainRoom--;
            }
        }

        return result;
    }

    @Test
    public void testMinMeetingRooms() {
        test(this::minMeetingRooms);
    }


    /**
     * 排序+双指针。参见：
     * https://leetcode-cn.com/problems/meeting-rooms-ii/solution/hui-yi-shi-ii-by-leetcode/
     *
     * LeetCode 耗时：6 ms - 84.31%
     *          内存消耗：38.1 MB - 85.18%
     */
    public int sortMethod(int[][] intervals) {
        int[] ends = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            ends[i] = intervals[i][1];
        }

        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(ends);

        int result = 1, remainRoom = 1;
        for (int startPtr = 0, endPtr = 0; startPtr < intervals.length;) {
            if (intervals[startPtr][0] < ends[endPtr]) {
                startPtr++;
                if (remainRoom == 0) {
                    result++;
                } else {
                    remainRoom--;
                }
            } else {
                endPtr++;
                remainRoom++;
            }
        }

        return result;
    }

    @Test
    public void testSortMethod() {
        test(this::sortMethod);
    }
}
