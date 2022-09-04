package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 719. 找出第 k 小的距离对: https://leetcode-cn.com/problems/find-k-th-smallest-pair-distance/
 *
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
public class E719_Hard_FindKthSmallestPairDistance {

    public static void test(BiFunction<int[], Integer, Integer> method) {
        assertEquals(0, method.apply(new int[]{1,3,1}, 1));
        assertEquals(58, method.apply(new int[]{62, 100, 4}, 2));
    }

    /**
     * LeetCode 耗时：9ms - 29.01%
     */
    public int smallestDistancePair(int[] nums, int k) {
        Arrays.sort(nums);

        int lo = 0, hi = nums[nums.length - 1] - nums[0];
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            // 计算差的绝对值(距离)小于等于 mid 的数量
            int cnt = 0;
            for (int i = 0; i < nums.length; i++)
                cnt += upperBound(nums, i, nums.length - 1, nums[i] + mid) - i - 1;
            // 如果计数小于 k，表示所找距离在 mid+1...hi 中；否则在 lo...mid 中
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

        int lo = 0, hi = nums[n - 1] - nums[0];
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            int cnt = 0, left = 0;
            // 此循环同样计算小于等于 mid 的距离数量。不过这里计算的是从 right 到 left 的数量，从右到左。
            // 而 smallestDistancePair 中则是从左到右。相比较而言，此实现内循环无需查找，仅仅调整 left 指针即可，
            // 因此此实现更快
            // 固定一个指针，计算另一个
            for (int right = 0; right < n; ++right) {
                // 不断循环直到差绝对值小于等于 mid
                while (nums[right] - nums[left] > mid) left++;
                cnt += right - left;
            }

            if (cnt < k) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
