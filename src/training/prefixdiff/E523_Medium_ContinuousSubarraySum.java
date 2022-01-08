package training.prefixdiff;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 523. 连续的子数组和: https://leetcode-cn.com/problems/continuous-subarray-sum/
 *
 * 给你一个整数数组 nums 和一个整数 k，编写一个函数来判断该数组是否含有同时满足下述条件的连续子数组：
 * - 子数组大小「至少为 2」；
 * - 子数组元素总和为 k 的倍数。
 *
 * 如果存在，返回 true ；否则，返回 false 。
 *
 * 如果存在一个整数 n ，令整数 x 符合 x = n * k ，则称 x 是 k 的一个倍数。「0 始终视为 k 的一个倍数」。
 *
 * 例 1：
 * 输入：nums = [23,2,4,6,7], k = 6
 * 输出：true
 * 解释：[2,4] 是一个大小为 2 的子数组，并且和为 6 。
 *
 * 例 2：
 * 输入：nums = [23,2,6,4,7], k = 6
 * 输出：true
 * 解释：[23, 2, 6, 4, 7] 是大小为 5 的子数组，并且和为 42 。
 * 42 是 6 的倍数，因为 42 = 7 * 6 且 7 是一个整数。
 *
 * 例 3：
 * 输入：nums = [23,2,6,4,7], k = 13
 * 输出：false
 *
 * 说明：
 * - 1 <= nums.length <= 10^5
 * - 0 <= nums[i] <= 10^9
 * - 0 <= sum(nums[i]) <= 2^31 - 1
 * - 1 <= k <= 2^31 - 1
 */
public class E523_Medium_ContinuousSubarraySum {

    public void test(BiPredicate<int[], Integer> method) {
        assertTrue(method.test(new int[]{23,2,4,6,7}, 6));
        assertTrue(method.test(new int[]{23,2,6,4,7}, 6));
        assertFalse(method.test(new int[]{23,2,6,4,7}, 13));
        assertTrue(method.test(new int[]{1,2,0,0,2}, 6));
    }

    /**
     * 同余定理，参见：
     * https://leetcode-cn.com/problems/continuous-subarray-sum/solution/lian-xu-de-zi-shu-zu-he-by-leetcode-solu-rdzi/
     *
     * 当 prefixSums[q]−prefixSums[p] 为 k 的倍数时，prefixSums[p] 和 prefixSums[q] 除以 k 的余数相同。
     * 因此只需要计算每个下标对应的前缀和除以 k 的余数即可，使用哈希表存储每个余数第一次出现的下标。
     *
     * LeetCode 耗时：19 ms - 12.53%
     *          内存消耗：53.3 MB - 64.47%
     */
    public boolean checkSubarraySum(int[] nums, int k) {
        final int n = nums.length;
        Map<Integer, Integer> remainder2idx = new HashMap<>();
        remainder2idx.put(0, 0);
        for (int i = 1, prefix = 0; i <= n; i++) {
            prefix += nums[i - 1];
            int remainder = prefix % k, prevIdx = remainder2idx.getOrDefault(remainder, n);
            if (i - prevIdx >= 2) {
                return true;
            }
            if (prevIdx == n) {
                remainder2idx.put(remainder, i);
            }
        }

        return false;
    }

    @Test
    public void testCheckSubarraySum() {
        test(this::checkSubarraySum);
    }
}
