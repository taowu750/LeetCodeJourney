package learn.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个整数数组，返回所有对中的第 k 个最小距离。一对（A，B）的距离定义为 A 和 B 之间差的绝对值。
 *
 * 例 1：
 * Input:
 * nums = [1,3,1], k = 1
 * Output: 0
 * Explanation:
 * Here are all the pairs:
 * (1,3) -> 2
 * (1,1) -> 0
 * (3,1) -> 2
 * 然后，第一最小距离对为（1,1），其距离为 0。
 *
 * 约束：
 * - 2 <= len(nums) <= 10000.
 * - 0 <= nums[i] < 1000000.
 * - 1 <= k <= len(nums) * (len(nums) - 1) / 2.
 */
public class FindKthSmallestPairDistance {

    static void test(BiFunction<int[], Integer, Integer> method) {
        assertEquals(method.apply(new int[]{1,3,1}, 1), 0);
    }

    /**
     * LeetCode 耗时：9ms - 29.01%
     */
    public int smallestDistancePair(int[] nums, int k) {
        Arrays.sort(nums);

        int lo = 0, hi = nums[nums.length - 1] - nums[0];
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            // 计算差的绝对值小于等于 mid 的数量
            int cnt = 0;
            for (int i = 0; i < nums.length; i++)
                cnt += upperBound(nums, i, nums.length - 1, nums[i] + mid) - i - 1;
            if (cnt < k)
                lo = mid + 1;
            else
                hi = mid;
        }

        return lo;
    }

    // 返回 a 中第一个大于 key 的元素的下标
    private int upperBound(int[] a, int lo, int hi, int key) {
        if (a[hi] <= key)
            return hi + 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= key)
                lo = mid + 1;
            else
                hi = mid;
        }
        return lo;
    }

    @Test
    public void testSmallestDistancePair() {
        test(this::smallestDistancePair);
    }


    /**
     * LeetCode 耗时：3ms - 100%
     */
    public int betterMethod(int[] nums, int k) {
        int n = nums.length;
        Arrays.sort(nums);

        int l = 0, r = nums[n - 1] - nums[0];
        while (l < r) {
            int mid = (l + r) >>> 1;
            int cnt = 0, left = 0;
            // 此循环同样计算小于等于 mid 的差绝对值数量。
            for (int right = 0; right < n; ++right) {
                // 不断循环直到差绝对值小于等于 mid
                while (nums[right] - nums[left] > mid) left++;
                cnt += right - left;
            }

            if (cnt < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
