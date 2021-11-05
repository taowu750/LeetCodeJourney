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
    }

    /**
     * LeetCode 耗时：5 ms - 90.08%
     *          内存消耗：38.1 MB - 61.25%
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);

        int result = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1, k = nums.length - 1; j < k;) {
                int num = nums[i] + nums[j] + nums[k];
                if (Math.abs(num - target) < Math.abs(result - target)) {
                    result = num;
                }
                if (num == target) {
                    return target;
                } else if (num > target) {
                    k--;
                } else {
                    j++;
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
