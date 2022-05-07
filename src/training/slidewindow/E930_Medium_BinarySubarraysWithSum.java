package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 930. 和相同的二元子数组: https://leetcode-cn.com/problems/binary-subarrays-with-sum/
 *
 * 给你一个二元数组 nums，和一个整数 goal，请你统计并返回有多少个和为 goal 的「非空」子数组。
 * 子数组是数组的一段连续部分。
 *
 * 例 1：
 * 输入：nums = [1,0,1,0,1], goal = 2
 * 输出：4
 * 解释：
 * 有 4 个满足题目要求的子数组：[1,0,1]、[1,0,1,0]、[0,1,0,1]、[1,0,1]
 *
 * 例 2：
 * 输入：nums = [0,0,0,0,0], goal = 0
 * 输出：15
 *
 * 说明：
 * - 1 <= nums.length <= 3 * 10^4
 * - nums[i] 不是 0 就是 1
 * - 0 <= goal <= nums.length
 */
public class E930_Medium_BinarySubarraysWithSum {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(4, method.applyAsInt(new int[]{1,0,1,0,1}, 2));
        assertEquals(15, method.applyAsInt(new int[]{0,0,0,0,0}, 0));
    }

    /**
     * LeetCode 耗时：17 ms - 68.40%
     *          内存消耗：46.1 MB - 33.48%
     */
    public int numSubarraysWithSum(int[] nums, int goal) {
        Map<Integer, Integer> prefix2cnt = new HashMap<>();
        prefix2cnt.put(0, 1);
        int prefix = 0, ans = 0;
        for (int num : nums) {
            prefix += num;
            ans += prefix2cnt.getOrDefault(prefix - goal, 0);
            prefix2cnt.merge(prefix, 1, Integer::sum);
        }

        return ans;
    }

    @Test
    public void testNumSubarraysWithSum() {
        test(this::numSubarraysWithSum);
    }


    /**
     * 参见 {@link training.slidewindow.E992_Hard_SubarraysWithKDifferentIntegers}。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：45.5 MB - 44.88%
     */
    public int slideWindowMethod(int[] nums, int goal) {
        return up2k(nums, goal) - up2k(nums, goal - 1);
    }

    /**
     * 计算和最多为 k 的子数组个数。
     */
    private int up2k(int[] nums, int k) {
        if (k < 0) {
            return 0;
        }

        int left = 0, right = 0, window = 0, cnt = 0;
        while (right < nums.length) {
            window += nums[right++];
            while (window > k) {
                window -= nums[left++];
            }
            cnt += right - left;
        }

        return cnt;
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }
}
