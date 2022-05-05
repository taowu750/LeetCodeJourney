package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 862. 和至少为 K 的最短子数组: https://leetcode-cn.com/problems/shortest-subarray-with-sum-at-least-k/
 *
 * 给你一个整数数组 nums 和一个整数 k，找出 nums 中和至少为 k 的「最短非空子数组」，并返回该子数组的长度。
 * 如果不存在这样的子数组，返回 -1。
 *
 * 例 1：
 * 输入：nums = [1], k = 1
 * 输出：1
 *
 * 例 2：
 * 输入：nums = [1,2], k = 4
 * 输出：-1
 *
 * 例 3：
 * 输入：nums = [2,-1,2], k = 3
 * 输出：3
 *
 * 说明：
 * - 1 <= nums.length <= 50000
 * - -10 ^ 5 <= nums[i] <= 10 ^ 5
 * - 1 <= k <= 10 ^ 9
 */
public class E862_Hard_ShortestSubarrayWithSumAtLeastK {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(1, method.applyAsInt(new int[]{1}, 1));
        assertEquals(-1, method.applyAsInt(new int[]{1,2}, 4));
        assertEquals(3, method.applyAsInt(new int[]{2,-1,2}, 3));
        assertEquals(3, method.applyAsInt(new int[]{84,-37,32,40,95}, 167));
        assertEquals(2, method.applyAsInt(new int[]{56, -21, 56, 35, -9},61));
    }

    /**
     * 前缀和，超出时间限制。
     */
    public int shortestSubarray(int[] nums, int k) {
        int[] prefix = new int[nums.length + 1];
        int len = Integer.MAX_VALUE;
        for (int i = 0, sum = 0, prefixEnd = 0; i < nums.length; i++) {
            sum += nums[i];
            // 找到最右边小于等于 sum - k 的下标
            for (int j = prefixEnd; j >= 0; j--) {
                if (prefix[j] <= sum - k) {
                    if (len > i - j + 1) {
                        len = i - j + 1;
                    }
                    break;
                }
            }
            prefix[++prefixEnd] = sum;
        }

        return len != Integer.MAX_VALUE ? len : -1;
    }

    @Test
    public void testShortestSubarray() {
        test(this::shortestSubarray);
    }


    /**
     * 前缀和+单调栈+二分查找
     *
     * LeetCode 耗时：49 ms - 9.73%
     *          内存消耗：54.9 MB - 5.19%
     */
    public int prefixAndStackMethod(int[] nums, int k) {
        // 一个小优化，剪枝不可能的数组
        long posSum = 0;
        for (int num : nums) {
            if (num > 0) {
                posSum += num;
            }
        }
        if (posSum < k) {
            return -1;
        }

        // 单增栈，记录前缀和下标
        long[][] stack = new long[nums.length + 1][2];
        // 添加一个 0 的前缀
        stack[0][1] = -1;
        int top = 0, len = Integer.MAX_VALUE;
        // 需要用 long，防止溢出
        long prefix = 0;
        for (int i = 0; i < nums.length; i++) {
            prefix += nums[i];
            // 找到最右边小于等于 prefix - k 的下标
            int idx = bs(stack, top + 1, prefix - k);
            // 如果可以的话，更新长度
            if (idx >= 0 && len > i - stack[idx][1]) {
                len = (int) (i - stack[idx][1]);
            }
            // 维持栈的单增
            while (top >= 0 && stack[top][0] >= prefix) {
                top--;
            }
            stack[++top][0] = prefix;
            stack[top][1] = i;
        }

        return len != Integer.MAX_VALUE ? len : -1;
    }

    /**
     * 找到最右边小于等于 target 的下标
     */
    private int bs(long[][] a, int hi, long target) {
        int lo = 0;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid][0] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo - 1;
    }

    @Test
    public void testPrefixAndStackMethod() {
        test(this::prefixAndStackMethod);
    }


    /**
     * 前缀和+单调队列。参见：
     * https://leetcode-cn.com/problems/shortest-subarray-with-sum-at-least-k/solution/he-zhi-shao-wei-k-de-zui-duan-zi-shu-zu-by-leetcod/
     *
     * 我们用数组 P 表示数组 A 的前缀和，即 P[i] = A[0] + A[1] + ... + A[i - 1]。我们需要找到 x 和 y，
     * 使得 P[y] - P[x] >= K 且 y - x 最小。
     *
     * 我们用 opt(y) 表示对于固定的 y，最大的满足 P[x] <= P[y] - K 的 x，这样所有 y - opt(y) 中的最小值即为答案。
     *
     * 如果 opt(y1) 的值为 x，那么我们以后就不用再考虑 x 了。这是因为如果有 y2 > y1 且 opt(y2) 的值也为 x，
     * 但此时 y2 - x 显然大于 y1 - x，不会作为所有 y - opt(y) 中的最小值。
     *
     * LeetCode 耗时：31 ms - 97.85%
     *          内存消耗：48.6 MB - 75.48%
     */
    public int prefixAndQueueMethod(int[] nums, int k) {
        long[] prefix = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int len = Integer.MAX_VALUE;
        // 单增队列，存储前缀的下标
        Deque<Integer> queue = new LinkedList<>();
        for (int i = 0; i < prefix.length; i++) {
            // 维持单增队列的单增性
            while (!queue.isEmpty() && prefix[queue.getLast()] >= prefix[i]) {
                queue.removeLast();
            }
            while (!queue.isEmpty() && prefix[queue.getFirst()] + k <= prefix[i]) {
                len = Math.min(len, i - queue.removeFirst());
            }
            queue.addLast(i);
        }

        return len != Integer.MAX_VALUE ? len : -1;
    }

    @Test
    public void testPrefixAndQueueMethod() {
        test(this::prefixAndQueueMethod);
    }
}
