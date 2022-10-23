package training.slidewindow;

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
 *
 * 说明：
 * - 1 <= nums.length <= 2 * 10^4
 * - -10 <= nums[i] <= 10
 * - nums 的任何前缀或后缀的乘积都保证是一个 32 位整数
 */
public class E152_Medium_MaximumProductSubarray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(6, method.applyAsInt(new int[]{2,3,-2,4}));
        assertEquals(0, method.applyAsInt(new int[]{-2,0,-1}));
        assertEquals(288, method.applyAsInt(new int[]{2,3,-2,4,-1,3,2,-3}));
        assertEquals(-2, method.applyAsInt(new int[]{-2}));
        assertEquals(3, method.applyAsInt(new int[]{2, 0, 3, -2}));
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


    /**
     * 滑动窗口方法。
     *
     * 0 与任何数的乘积都是 0，因此可以将数组看成被 0 分割的子数组，在各个子数组中查找乘积最大的值。
     * 在一个非 0 的数组中求最大乘积，需要分析正负性。
     * - 没有负数或者负数为偶数个，最大乘积就是整个数组的乘积
     * - 有奇数个负数，如果第 i 个元素为负数，则 [start,i-1]，[i+1,end] 这2个区间的乘积都是最大乘积的候选。
     *   通过下面 2 个指针交替移动算法可以计算所有 [start,i-1] 和 [i+1,end] 的乘积。
     *
     * 参见：
     * https://leetcode-cn.com/problems/maximum-product-subarray/solution/shuang-zhi-zhen-by-wanglongjiang-ier1/
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.4 MB - 5.18%
     */
    public int slideWindowMethod(int[] nums) {
        int left = 0, right = 0;
        int result = Integer.MIN_VALUE;

        int product = 1;
        // 遇到 0 左指针开始收敛
        while (right <= nums.length && left < nums.length) {
            //如果右指针到达最后一个位置，左指针开始滑动
            if (right == nums.length) {
                product /= nums[left++];
            } else {
                // 如果右指针所指元素不为 0，右滑，更新 product
                if (nums[right] != 0) {
                    product *= nums[right++];
                } else if (left < right) {  // 右指针为0，开始左滑，更新 product
                    product /= nums[left++];
                } else { // 左右指针都滑到了 0 的位置，更新 result，左右指针指向下一位置重新滑动
                    if (result < 0) {
                        result = 0;
                    }
                    left++;
                    right++;
                }
            }
            // 只要左右指针没有指向同一位置，更新 result（指向同一位置说明左滑到顶了，记录的 product 一定为1，
            // 但并不是乘积值）
            if (left != right && product > result) {
                result = product;
            }
        }

        return result;
    }

    @Test
    public void testSlideWindowMethod() {
        test(this::slideWindowMethod);
    }
}
