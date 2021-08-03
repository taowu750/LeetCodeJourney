package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 152. 乘积最大子数组: https://leetcode-cn.com/problems/maximum-product-subarray/
 *
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
 *
 * 例 1：
 * 输入: [2,3,-2,4]
 * 输出: 6
 * 解释: 子数组 [2,3] 有最大乘积 6。
 *
 * 例 2：
 * 输入: [-2,0,-1]
 * 输出: 0
 * 解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
 */
public class E152_Medium_MaximumProductSubarray {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(6, method.applyAsInt(new int[]{2,3,-2,4}));
        assertEquals(0, method.applyAsInt(new int[]{-2,0,-1}));
        assertEquals(288, method.applyAsInt(new int[]{2,3,-2,4,-1,3,2,-3}));
        assertEquals(-2, method.applyAsInt(new int[]{-2}));
    }

    /**
     * LeetCode 耗时：91 ms - 7%
     *          内存消耗：38.5MB - 98.5%
     */
    public int maxProduct(int[] nums) {
        List<Integer> negIndices = new ArrayList<>(nums.length * 2 / 3 + 1);
        // dp[i] 表示以 i 结尾的元素的最大乘积
        int[] dp = new int[nums.length];
        // multi 表示连续一段正数的乘积。负数和 0 都是单个单个的。
        int[] multi = new int[nums.length];
        int result = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                dp[i] = i > 0 && dp[i - 1] > 0 ? dp[i - 1] * nums[i] : nums[i];
                multi[i] = i > 0 && multi[i - 1] > 0 ? multi[i - 1] * nums[i] : nums[i];
            } else if (nums[i] < 0) {
                negIndices.add(i);
                if (negIndices.size() > 1) {
                    dp[i] = 1;
                    for (int j = (negIndices.size() & 1) == 0 ? 0 : 1; j < negIndices.size(); j++) {
                        int idx = negIndices.get(j);
                        dp[i] *= (idx > 0 && multi[idx - 1] > 0 ? multi[idx - 1] : 1) * nums[idx];
                    }
                } else {
                    dp[i] = nums[i];
                }
                multi[i] = nums[i];
            } else {
                negIndices.clear();
            }
            result = Math.max(result, dp[i]);
        }

        return result;
    }

    @Test
    public void testMaxProduct() {
        test(this::maxProduct);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/maximum-product-subarray/solution/cheng-ji-zui-da-zi-shu-zu-by-leetcode-solution/
     *
     * LeetCode 耗时：2 ms - 87%
     *          内存消耗：38.4MB - 98.5%
     */
    public int dpMethod(int[] nums) {
        // 多种状态被归类成了两种：最大值、最小值。类似于《程序员的数学》中的余数一节
        int max = nums[0], min = nums[0], result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int mx = max, mn = min;
            max = Math.max(nums[i], Math.max(mx * nums[i], mn * nums[i]));
            min = Math.min(nums[i], Math.min(mx * nums[i], mn * nums[i]));
            result = Math.max(result, max);
        }

        return result;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
