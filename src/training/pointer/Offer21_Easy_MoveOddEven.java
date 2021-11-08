package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 剑指 Offer 21. 调整数组顺序使奇数位于偶数前面: https://leetcode-cn.com/problems/diao-zheng-shu-zu-shun-xu-shi-qi-shu-wei-yu-ou-shu-qian-mian-lcof/
 *
 * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数在数组的前半部分，所有偶数在数组的后半部分。
 *
 * 例 1：
 * 输入：nums = [1,2,3,4]
 * 输出：[1,3,2,4]
 * 注：[3,1,2,4] 也是正确的答案之一。
 *
 * 说明：
 * - 0 <= nums.length <= 50000
 * - 0 <= nums[i] <= 10000
 */
public class Offer21_Easy_MoveOddEven {

    public static void test(UnaryOperator<int[]> method) {
        assertArrayEquals(new int[]{1,3,2,4}, method.apply(new int[]{1,2,3,4}));
        assertArrayEquals(new int[]{1,3,5}, method.apply(new int[]{1,3,5}));
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：46.5 MB - 20.83%
     */
    public int[] exchange(int[] nums) {
        for (int i = 0, j = nums.length - 1; i < j;) {
            while (i < nums.length && (nums[i] & 1) == 1) {
                i++;
            }
            while (j >= 0 && (nums[j] & 1) == 0) {
                j--;
            }
            if (i < j) {
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
            }
        }

        return nums;
    }

    @Test
    public void testExchange() {
        test(this::exchange);
    }
}
