package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 16. 最接近的三数之和: https://leetcode-cn.com/problems/3sum-closest/
 *
 * 给定一个包括 n 个整数的数组 nums 和一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。
 * 返回这三个数的和。假定每组输入只存在唯一答案。
 *
 * 例 1：
 * 输入：nums = [-1,2,1,-4], target = 1
 * 输出：2
 * 解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。
 *
 * 约束：
 * - 3 <= nums.length <= 10^3
 * - -10^3 <= nums[i] <= 10^3
 * - -10^4 <= target <= 10^4
 */
public class E16_Medium_3SumClosest {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(2, method.applyAsInt(new int[]{-1,2,1,-4}, 1));
        assertEquals(-101, method.applyAsInt(new int[]{-100,-98,-2,-1}, -101));
    }

    /**
     * 加入了很多剪枝操作，大大减少了耗时
     *
     * LeetCode 耗时：6 ms - 99.40%
     *          内存消耗：41.3 MB - 76.71%
     */
    public int threeSumClosest(int[] nums, int target) {
        final int n = nums.length;
        // 其实还可以用计数排序进一步加快速度
        Arrays.sort(nums);

        int result = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < n - 2; i++) {
            // 如果最小和都大于等于 target，那后面就无需遍历了
            int minSum = nums[i] + nums[i + 1] + nums[i + 2];
            if (minSum >= target) {
                if (minSum - target < Math.abs(result - target)) {
                    result = minSum;
                }
                break;
            }

            for (int l = i + 1, r = nums.length - 1; l < r;) {
                minSum = nums[i] + nums[l] + nums[l + 1];
                if (minSum >= target) {
                    if (minSum - target < Math.abs(result - target)) {
                        result = minSum;
                    }
                    break;
                }
                // 如果最大和都小于等于 target，那后面就无需遍历了
                int maxSum = nums[i] + nums[r - 1] + nums[r];
                if (maxSum <= target) {
                    if (target - maxSum < Math.abs(result - target)) {
                        result = maxSum;
                    }
                    break;
                }

                int num = nums[i] + nums[l] + nums[r];
                if (Math.abs(num - target) < Math.abs(result - target)) {
                    result = num;
                }
                if (num == target) {
                    return target;
                } else if (num > target) {
                    r--;
                } else {
                    l++;
                }
            }
        }

        return result;
    }

    @Test
    public void testThreeSumClosest() {
        test(this::threeSumClosest);
    }
}
