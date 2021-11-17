package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 31. 下一个排列: https://leetcode-cn.com/problems/next-permutation/
 *
 * 实现获取「下一个排列」的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 *
 * 必须「原地」修改，只允许使用额外常数空间。
 *
 * 例 1：
 * 输入：nums = [1,2,3]
 * 输出：[1,3,2]
 *
 * 例 2：
 * 输入：nums = [3,2,1]
 * 输出：[1,2,3]
 *
 * 例 3：
 * 输入：nums = [1,1,5]
 * 输出：[1,5,1]
 *
 * 例 4：
 * 输入：nums = [1]
 * 输出：[1]
 *
 * 约束：
 * - 1 <= nums.length <= 100
 * - 0 <= nums[i] <= 100
 */
public class E31_Medium_NextPermutation {

    static void test(Consumer<int[]> method) {
        int[] nums = {1,2,3};
        method.accept(nums);
        assertArrayEquals(new int[]{1,3,2}, nums);

        nums = new int[]{3,2,1};
        method.accept(nums);
        assertArrayEquals(new int[]{1,2,3}, nums);

        nums = new int[]{1,1,5};
        method.accept(nums);
        assertArrayEquals(new int[]{1,5,1}, nums);

        nums = new int[]{1};
        method.accept(nums);
        assertArrayEquals(new int[]{1}, nums);

        nums = new int[]{1, 2, 3, 2};
        method.accept(nums);
        assertArrayEquals(new int[]{1, 3, 2, 2}, nums);
    }

    /**
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：38.5 MB - 84.63%
     */
    public void nextPermutation(int[] nums) {
        int n = nums.length, i = n - 1;
        // 从右往左遍历，如果 [i-1] < [i]，则可以通过排序 [i..len]，
        // 然后选择排序后的数字中刚好大于 [i-1] 的数字与其交换，就能得到下一个排列
        for (; i > 0; i--) {
            if (nums[i - 1] < nums[i]) {
                // [i..len] 是逆序的，通过交换可以变为正序的
                int cnt = (n - i) >>> 1;
                for (int j = 0; j < cnt; j++) {
                    int tmp = nums[i + j];
                    nums[i + j] = nums[n - 1 - j];
                    nums[n - 1 - j] = tmp;
                }
                // 交换值，得到下一个最大排列
                int num = nums[i - 1];
                nums[i - 1] = nums[nums.length - 1];
                nums[nums.length - 1] = num;
                break;
            }
        }

        // 如果 nums 是降序排列的，则需要将其反过来
        if (i == 0) {
            int size = nums.length / 2;
            for (int j = 0; j < size; j++) {
                int tmp = nums[j];
                nums[j] = nums[nums.length - j - 1];
                nums[nums.length - j - 1] = tmp;
            }
        }
    }

    @Test
    public void testNextPermutation() {
        test(this::nextPermutation);
    }
}
