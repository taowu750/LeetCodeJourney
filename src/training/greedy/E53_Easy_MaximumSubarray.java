package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个整数数组 nums，找到连续子序列（至少包含一个数字），该子阵列的总和最大，并返回其总和。
 * <p>
 * 如果您已经找到了 O(n) 解决方案，请尝试使用分而治之的方法编写另一个解决方案，这种方法更为微妙。
 * <p>
 * 例 1：
 * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
 * Output: 6
 * Explanation: [4,-1,2,1] 有最大的和 6.
 * <p>
 * 例 2：
 * Input: nums = [1]
 * Output: 1
 * <p>
 * 例 3：
 * Input: nums = [5,4,-1,7,8]
 * Output: 23
 * <p>
 * 约束：
 * - 1 <= nums.length <= 3 * 10**4
 * - -10**5 <= nums[i] <= 10**5
 */
public class E53_Easy_MaximumSubarray {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}), 6);
        assertEquals(method.applyAsInt(new int[]{1}), 1);
        assertEquals(method.applyAsInt(new int[]{5, 4, -1, 7, 8}), 23);
        assertEquals(method.applyAsInt(new int[]{-2, -1, -6, -3}), -1);
    }

    /**
     * 线性方法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.9 MB - 62.16%
     */
    public int maxSubArray(int[] nums) {
        int sum = 0, maxSum = 0, max = Integer.MIN_VALUE;
        for (int num : nums) {
            // max 记录数组最大值
            max = Math.max(num, max);
            // sum 进行求和
            sum += num;
            // 如果 sum > maxSum，更新最大和
            if (sum > maxSum)
                maxSum = sum;
            // 如果 sum <= 0，则当前子序列和可以舍弃。因为小于等于 0 的子序列和只会对后面的结果有负面影响
            if (sum <= 0)
                sum = 0;
        }
        // 如果一圈下来 maxSum 等于 0，表示数组中都是负数，此时返回其中的最大值
        return maxSum == 0 ? max : maxSum;
    }

    @Test
    public void testMaxSubArray() {
        test(this::maxSubArray);
    }


    /**
     * 分治算法。
     *
     * LeetCode 耗时：1 ms - 49.49%
     *          内存消耗：39.1 MB - 28.71%
     */
    public int partitionMethod(int[] nums) {
        return partition(nums, 0, nums.length - 1);
    }

    private int partition(int[] nums, int lo, int hi) {
        if (lo > hi)
            return Integer.MIN_VALUE;
        if (lo == hi)
            return nums[lo];

        int mid = (lo + hi) >>> 1;
        // 分成两半，分别递归地求左右两半的最大子序列和
        int leftSubSum = partition(nums, lo, mid);
        int rightSubSum = partition(nums, mid + 1, hi);

        // 求以 mid 结尾的最大子序列和
        int leftHalfSum = 0, max = nums[mid];
        for (int i = mid; i >= lo; i--) {
            leftHalfSum += nums[i];
            if (leftHalfSum > max)
                max = leftHalfSum;
        }
        leftHalfSum = max;

        // 求以 mid + 1 开头的最大子序列和
        int rightHalfSum = 0;
        max = nums[mid + 1];
        for (int i = mid + 1; i <= hi; i++) {
            rightHalfSum += nums[i];
            if (rightHalfSum > max)
                max = rightHalfSum;
        }
        rightHalfSum = max;

        // leftHalfSum + rightHalfSum 就是跨左右两半的最大子序列和

        // 最后在三个值里面选最大的
        return Math.max(leftHalfSum + rightHalfSum, Math.max(leftSubSum, rightSubSum));
    }

    @Test
    public void testPartitionMethod() {
        test(this::partitionMethod);
    }


    /**
     * 动态规划方法。子序列问题，类似于{@link training.dynamicprogramming.E300_Medium_LongestIncreasingSubsequence}。
     *
     * LeetCode 耗时：1 ms - 49.49%
     *          内存消耗：39.2 MB - 28.71%
     */
    public int dpMethod(int[] nums) {
        // dp[i] 表示以 nums[i] 结尾的最大子序列和
        int[] dp = new int[nums.length];
        int max = nums[0];

        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = nums[i];
            // 前面的子序列和大于 0 才有利用的价值
            if (dp[i - 1] > 0)
                dp[i] += dp[i - 1];
            // 直接在过程中计算最大值，避免再次遍历
            if (dp[i] > max)
                max = dp[i];
        }

        return max;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
