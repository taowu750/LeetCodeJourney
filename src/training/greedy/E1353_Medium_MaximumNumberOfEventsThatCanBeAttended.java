package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1353. 最多可以参加的会议数目: https://leetcode-cn.com/problems/maximum-number-of-events-that-can-be-attended/
 *
 * 给你一个数组 events，其中 events[i] = [startDayi, endDayi]，表示会议 i 开始于 startDayi，结束于 endDayi。
 * 你可以在满足 startDayi <= d <= endDayi 中的任意一天 d 参加会议 i。注意，一天只能参加一个会议。
 * 请你返回你可以参加的最大会议数目。
 *
 * 例 1：
 * 输入：events = [[1,2],[2,3],[3,4]]
 * 输出：3
 * 解释：你可以参加所有的三个会议。
 * 安排会议的一种方案如上图。
 * 第 1 天参加第一个会议。
 * 第 2 天参加第二个会议。
 * 第 3 天参加第三个会议。
 *
 * 例 2：
 * 输入：events= [[1,2],[2,3],[3,4],[1,2]]
 * 输出：4
 *
 * 说明：
 * - 1 <= events.length <= 10^5
 * - events[i].length == 2
 * - 1 <= startDayi <= endDayi <= 10^5
 */
public class E1353_Medium_MaximumNumberOfEventsThatCanBeAttended {

    public static void test(ToIntFunction<int[][]> method) {
        assertEquals(3, method.applyAsInt(new int[][]{{1,2}, {2,3}, {3,4}}));
        assertEquals(4, method.applyAsInt(new int[][]{{1,2}, {2,3}, {3,4}, {1,2}}));
        assertEquals(4, method.applyAsInt(new int[][]{{1,4}, {4,4}, {2,2}, {3,4}, {1,1}}));
        assertEquals(5, method.applyAsInt(new int[][]{{1,2}, {1,2}, {3,3}, {1,5}, {1,5}}));
    }

    /**
     * 参见：https://leetcode-cn.com/problems/maximum-number-of-events-that-can-be-attended/solution/sao-miao-suan-fa-tan-xin-by-lucifer1004/
     * 和 https://leetcode-cn.com/problems/maximum-number-of-events-that-can-be-attended/solution/zui-duo-ke-yi-can-jia-de-hui-yi-shu-mu-by-leetcode/ 评论区
     *
     * 这是一道典型的扫描+贪心算法题。由于每个时间点最多参加一个会议，我们可以从1开始遍历所有时间。
     *
     * 对于每一个时间点，所有在当前时间及之前时间开始，并且在当前时间还未结束的会议都是可参加的。
     * 显然，在所有可参加的会议中，选择结束时间最早的会议是最优的，因为其他会议还有更多的机会可以去参加。
     *
     * 怎样动态获得当前结束时间最早的会议呢？我们可以使用一个小根堆记录所有当前可参加会议的结束时间。
     * 在每一个时间点，我们首先将当前时间点开始的会议加入小根堆，再把当前已经结束的会议移除出小根堆（因为已经无法参加了），
     * 然后从剩下的会议中选择一个结束时间最早的去参加。
     *
     * 为了快速获得当前时间点开始的会议，对会议按照开始时间升序排列，排序的目的是为了可以方便的一起选择开始时间相同的会议。
     *
     * LeetCode 耗时：66 ms - 65.22%
     *          内存消耗：86.3 MB - 36.06%
     */
    public int maxEvents(int[][] events) {
        final int n = events.length;
        Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));
        // 小顶堆：用于高效的维护最小的 endDay
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int result = 0;
        for (int i = 0, curDay = 1; i < n || !pq.isEmpty();) {
            // 将所有开始时间等于 curDay 的会议的结束时间放到小顶堆
            while (i < n && events[i][0] == curDay) {
                pq.add(events[i][1]);
                i++;
            }

            // 将已经结束的会议弹出堆中
            while (!pq.isEmpty() && pq.peek() < curDay) {
                pq.remove();
            }

            // 贪心的选择结束时间最小的会议参加
            if (!pq.isEmpty()) {
                // 参加的会议，就从堆中删除
                pq.remove();
                result++;
            }

            // 当前的天往前走一天，开始看下下一天能不能参加会议
            curDay++;
        }

        return result;
    }

    @Test
    public void testMaxEvents() {
        test(this::maxEvents);
    }
}
