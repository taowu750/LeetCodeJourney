package training.queue;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1438. 绝对差不超过限制的最长连续子数组: https://leetcode-cn.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 *
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，请你返回最长连续子数组的长度，
 * 该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。
 *
 * 如果不存在满足条件的子数组，则返回 0 。
 *
 * 例 1：
 * 输入：nums = [8,2,4,7], limit = 4
 * 输出：2
 * 解释：所有子数组如下：
 * [8] 最大绝对差 |8-8| = 0 <= 4.
 * [8,2] 最大绝对差 |8-2| = 6 > 4.
 * [8,2,4] 最大绝对差 |8-2| = 6 > 4.
 * [8,2,4,7] 最大绝对差 |8-2| = 6 > 4.
 * [2] 最大绝对差 |2-2| = 0 <= 4.
 * [2,4] 最大绝对差 |2-4| = 2 <= 4.
 * [2,4,7] 最大绝对差 |2-7| = 5 > 4.
 * [4] 最大绝对差 |4-4| = 0 <= 4.
 * [4,7] 最大绝对差 |4-7| = 3 <= 4.
 * [7] 最大绝对差 |7-7| = 0 <= 4.
 * 因此，满足题意的最长子数组的长度为 2 。
 *
 * 例 2：
 * 输入：nums = [10,1,2,4,7,2], limit = 5
 * 输出：4
 * 解释：满足题意的最长子数组是 [2,4,7,2]，其最大绝对差 |2-7| = 5 <= 5 。
 *
 * 例 3：
 * 输入：nums = [4,2,2,2,4,4,2,2], limit = 0
 * 输出：3
 *
 * 说明：
 * - 1 <= nums.length <= 10^5
 * - 1 <= nums[i] <= 10^9
 * - 0 <= limit <= 10^9
 */
public class E1438_Medium_LongestSubarrayWithDiffLessEqualLimit {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(2, method.applyAsInt(new int[]{8,2,4,7}, 4));
        assertEquals(4, method.applyAsInt(new int[]{10,1,2,4,7,2}, 5));
        assertEquals(3, method.applyAsInt(new int[]{4,2,2,2,4,4,2,2}, 0));
        assertEquals(5, method.applyAsInt(new int[]{9,10,1,7,9,3,9,9}, 7));
    }

    /**
     * LeetCode 耗时：47 ms - 45.79%
     *          内存消耗：50.4 MB - 56.75%
     */
    public int longestSubarray(int[] nums, int limit) {
        // TreeMap 可以同时记录最大最小值
        TreeMap<Integer, Integer> num2cnt = new TreeMap<>();
        int left = 0, right = 0, result = 1;
        while (right < nums.length) {
            int num = nums[right++];
            if (num2cnt.isEmpty()) {
                num2cnt.put(num, 1);
            } else {
                int min = num2cnt.firstKey(), max = num2cnt.lastKey();
                Map<Integer, Integer> cutoff = null;
                // 如果 max - num > limit，需要收缩滑动窗口，使得新的最大值 max' - num <= limit
                if (num < min && max - num > limit) {
                    cutoff = num2cnt.tailMap(num + limit + 1, true);
                }
                // 如果 num - min > limit，需要收缩滑动窗口，使得新的最小值 num - min' <= limit
                else if (num > max && num - min > limit) {
                    cutoff = num2cnt.headMap(num - limit - 1, true);
                }
                if (cutoff != null) {
                    // 收缩滑动窗口
                    for (; !cutoff.isEmpty(); left++) {
                        num2cnt.merge(nums[left], -1, (old, delta) -> old + delta > 0 ? old + delta : null);
                    }
                }
                num2cnt.merge(num, 1, Integer::sum);
            }
            result = Math.max(result, right - left);
        }

        return result;
    }

    @Test
    public void testLongestSubarray() {
        test(this::longestSubarray);
    }


    /**
     * LeetCode 耗时：41 ms - 46.90%
     *          内存消耗：49.6 MB - 63.91%
     */
    public int betterTreeMapMethod(int[] nums, int limit) {
        TreeMap<Integer, Integer> num2cnt = new TreeMap<>();
        int left = 0, right = 0, result = 1;
        while (right < nums.length) {
            num2cnt.merge(nums[right++], 1, Integer::sum);
            while (num2cnt.lastKey() - num2cnt.firstKey() > limit) {
                num2cnt.merge(nums[left++], -1, (old, delta) -> old + delta > 0 ? old + delta : null);
            }
            result = Math.max(result, right - left);
        }

        return result;
    }

    @Test
    public void testBetterTreeMapMethod() {
        test(this::betterTreeMapMethod);
    }


    /**
     * 单调队列解法。
     *
     * LeetCode 耗时：27 ms - 93.48%
     *          内存消耗：53 MB - 46.10%
     */
    public int monoQueueMethod(int[] nums, int limit) {
        Deque<Integer> maxQueue = new LinkedList<>();
        Deque<Integer> minQueue = new LinkedList<>();

        int left = 0, right = 0, result = 1;
        while (right < nums.length) {
            int num = nums[right++];
            while (!maxQueue.isEmpty() && maxQueue.getLast() < num) {
                maxQueue.removeLast();
            }
            while (!minQueue.isEmpty() && minQueue.getLast() > num) {
                minQueue.removeLast();
            }
            maxQueue.add(num);
            minQueue.add(num);

            while (maxQueue.getFirst() - minQueue.getFirst() > limit) {
                int removed = nums[left++];
                if (removed == maxQueue.getFirst()) {
                    maxQueue.removeFirst();
                } else if (removed == minQueue.getFirst()) {
                    minQueue.removeFirst();
                }
            }

            result = Math.max(result, right - left);
        }

        return result;
    }

    @Test
    public void testMonoQueueMethod() {
        test(this::monoQueueMethod);
    }
}
