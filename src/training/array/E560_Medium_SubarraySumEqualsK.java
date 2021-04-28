package training.array;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 560. 和为K的子数组: https://leetcode-cn.com/problems/subarray-sum-equals-k/
 *
 * 给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
 *
 * 例 1：
 * 输入: nums = [1,1,1], k = 2
 * 输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
 *
 * 约束：
 * - 数组的长度为 [1, 20,000]。
 * - 数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
 */
public class E560_Medium_SubarraySumEqualsK {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(2, method.applyAsInt(new int[]{1,1,1}, 2));
    }

    /**
     * 前缀数组技巧，参见：
     * https://labuladong.gitee.io/algo/算法思维系列/前缀和技巧/
     *
     * LeetCode 耗时：21 ms - 97.54%
     *          内存消耗：40.7 MB - 81.82%
     */
    public int subarraySum(int[] nums, int k) {
        // 将前缀数组转化为计数哈希表，消除了内循环
        Map<Integer, Integer> prefixSumCnt = new HashMap<>(nums.length);
        // base case
        prefixSumCnt.put(0, 1);

        int prefixSum = 0, result = 0;
        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];
            result += prefixSumCnt.getOrDefault(prefixSum - k, 0);
            prefixSumCnt.merge(prefixSum, 1, Integer::sum);
        }

        return result;
    }

    @Test
    public void testSubarraySum() {
        test(this::subarraySum);
    }
}
