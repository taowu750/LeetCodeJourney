package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 974. 和可被 K 整除的子数组: https://leetcode.cn/problems/subarray-sums-divisible-by-k/
 *
 * 给定一个整数数组 nums 和一个整数 k ，返回其中元素之和可被 k 整除的（连续、非空）子数组的数目。
 *
 * 例 1：
 * 输入：nums = [4,5,0,-2,-3,1], k = 5
 * 输出：7
 * 解释：
 * 有 7 个子数组满足其元素之和可被 k = 5 整除：
 * [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
 *
 * 例 2：
 * 输入: nums = [5], k = 9
 * 输出: 0
 *
 * 说明：
 * - 1 <= nums.length <= 3 * 10^4
 * -10^4 <= nums[i] <= 10^4
 * 2 <= k <= 10^4
 */
public class E974_Medium_SubarraySumsDivisibleByK {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(7, method.applyAsInt(new int[]{4,5,0,-2,-3,1}, 5));
        assertEquals(0, method.applyAsInt(new int[]{5}, 9));
        assertEquals(2, method.applyAsInt(new int[]{-1,2,9}, 2));
        assertEquals(2, method.applyAsInt(new int[]{2,-2,2,-4}, 6));
        assertEquals(11, method.applyAsInt(new int[]{7,-5,5,-8,-6,6,-4,7,-8,-7}, 7));
    }

    /**
     * 和 {@link E523_Medium_ContinuousSubarraySum} 类似，但有所不同。
     *
     * LeetCode 耗时：23 ms - 33.22%
     *          内存消耗：45.6 MB - 65.12%
     */
    public int subarraysDivByK(int[] nums, int k) {
        Map<Integer, Integer> remainder2cnt = new HashMap<>();
        remainder2cnt.put(0, 1);
        int ans = 0;
        for (int i = 0, prefix = 0; i < nums.length; i++) {
            prefix += nums[i];
            int modulus = prefix % k;
            ans += remainder2cnt.getOrDefault(modulus, 0);
            /*
            注意 nums 中有负数，因此会有下列两种情况：
            - 余数 modulus 是正数，那么之前余数为 modulus - k 的前缀和和当前前缀和之间的子数组是 k 的倍数，
              因为 modulus - k 的前缀和至少要加 k 才能得到当前余数 modulus
            - 余数 modulus 是负数，那么之前余数为 modulus + k 的前缀和和当前前缀和之间的子数组是 k 的倍数
              因为 modulus + k 的前缀和至少要减 k 才能得到当前余数 modulus
             */
            if (modulus > 0) {
                ans += remainder2cnt.getOrDefault(modulus - k, 0);
            } else if (modulus < 0) {
                ans += remainder2cnt.getOrDefault(modulus + k, 0);
            }
            remainder2cnt.merge(modulus, 1, Integer::sum);
        }

        return ans;
    }

    @Test
    public void testSubarraysDivByK() {
        test(this::subarraysDivByK);
    }


    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：45 MB - 89.59%
     */
    public int betterMethod(int[] nums, int k) {
        int[] remainder2cnt = new int[k];
        remainder2cnt[0] = 1;
        int ans = 0;
        for (int i = 0, prefix = 0; i < nums.length; i++) {
            prefix += nums[i];
            /*
             统一正负数的模。
             int modulus = (sum % k + k) % k 这句话的涵义是保证把求余数都归到一边。
             prefix取模后得到的区间是[-(K-1),(K-1)]，如果加上一个K的话区间就变成了[1,2K-1]，
             它取模就将所有的下标都保证为正了。

             本来可以这样写：
                int modulus = sum % K;
                if (modulus < 0) modulus += k;
             这个写法就直接精简成一条代码了。
             */
            int modulus = (prefix % k + k) % k;
            ans += remainder2cnt[modulus];
            remainder2cnt[modulus]++;
        }

        return ans;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
