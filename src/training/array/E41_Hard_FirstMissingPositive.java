package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 41. 缺失的第一个正数: https://leetcode-cn.com/problems/first-missing-positive/
 *
 * 给你一个未排序的整数数组 nums ，请你找出其中没有出现的最小的正整数。
 * 请你实现时间复杂度为 O(n) 并且只使用常数级别额外空间的解决方案。
 *
 * 例 1：
 * 输入：nums = [1,2,0]
 * 输出：3
 *
 * 例 2：
 * 输入：nums = [3,4,-1,1]
 * 输出：2
 *
 * 例 3：
 * 输入：nums = [7,8,9,11,12]
 * 输出：1
 *
 * 约束：
 * - 1 <= nums.length <= 5 * 10**5
 * - -2**31 <= nums[i] <= 2**31 - 1
 */
public class E41_Hard_FirstMissingPositive {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(3, method.applyAsInt(new int[]{1,2,0}));
        assertEquals(2, method.applyAsInt(new int[]{3,4,-1,1}));
        assertEquals(1, method.applyAsInt(new int[]{7,8,9,11,12}));
        assertEquals(10, method.applyAsInt(new int[]{7,9,8,2,1,5,3,6,4,11}));
        assertEquals(10, method.applyAsInt(new int[]{3,1,4,5,2,9,6,8,7}));
        assertEquals(2, method.applyAsInt(new int[]{1,1}));
    }

    /**
     * LeetCode 耗时：2 ms - 28.32%
     *          内存消耗：84.3 MB - 23.06%
     */
    public int firstMissingPositive(int[] nums) {
        // 为了实现时间复杂度为 O(n) 并且只使用常数级别额外空间，我们需要利用输入的数组
        for (int i = 0; i < nums.length; i++) {
            // 如果数字的范围在 [1, nums.length]，并且它不等于 i + 1，就不断进行交换
            while (nums[i] >= 1 && nums[i] <= nums.length) {
                int idx = nums[i] - 1;
                // 如果两个数字相等，则没有交换的必要
                if (idx != i && nums[idx] != nums[i]) {
                    int temp = nums[i];
                    nums[i] = nums[idx];
                    nums[idx] = temp;
                } else {
                    break;
                }
            }
        }

        // 再次遍历，如果有数字不等于下标的，那么就是最小的正整数
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        // 如果都满足，那么最小的正整数等于 nums.length + 1
        return nums.length + 1;
    }

    @Test
    public void testFirstMissingPositive() {
        test(this::firstMissingPositive);
    }
}
