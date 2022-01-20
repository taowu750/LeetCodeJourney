package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 922. 按奇偶排序数组 II: https://leetcode-cn.com/problems/sort-array-by-parity-ii/
 *
 * 给定一个非负整数数组 A，A 中一半整数是奇数，一半整数是偶数。
 * 对数组进行排序，以便当 A[i] 为奇数时，i 也是奇数；当 A[i] 为偶数时， i 也是偶数。
 * 你可以返回任何满足上述条件的数组作为答案。
 *
 * 例 1：
 * 输入：[4,2,5,7]
 * 输出：[4,5,2,7]
 * 解释：[4,7,2,5]，[2,5,4,7]，[2,7,4,5] 也会被接受。
 *
 * 说明：
 * - 2 <= A.length <= 20000
 * - A.length % 2 == 0
 * - 0 <= A[i] <= 1000
 */
public class E922_Easy_SortArrayByParityII {

    public static boolean isValid(int[] arr) {
        boolean even = true;
        for (int i = 0; i < arr.length; i++, even = !even) {
            if ((even && arr[i] % 2 != 0) || (!even && arr[i] % 2 != 1)) {
                return false;
            }
        }

        return true;
    }

    public static void test(UnaryOperator<int[]> method) {
        assertTrue(isValid(method.apply(new int[]{4,2,5,7})));
    }

    /**
     * LeetCode 耗时：2 ms - 98.37%
     *          内存消耗：39.5 MB - 86.22%
     */
    public int[] sortArrayByParityII(int[] nums) {
        // 将偶数放到前一半，奇数放到后一半
        for (int i = 0, j = nums.length - 1;;) {
            while (nums[i] % 2 == 0) {
                i++;
            }
            while (nums[j] % 2 == 1) {
                j--;
            }
            if (i < j) {
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
            } else {
                break;
            }
        }

        // 再进行排序
        for (int i = 1, j = nums.length - 2; i < j; i += 2, j -= 2) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }

        return nums;
    }

    @Test
    public void testSortArrayByParityII() {
        test(this::sortArrayByParityII);
    }


    /**
     * LeetCode 耗时：2 ms - 98.37%
     *          内存消耗：39.5 MB - 86.22%
     */
    public int[] betterMethod(int[] nums) {
        // 让 i 遍历偶数位置，j 遍历奇数位置
        for (int i = 0, j = 1;;) {
            while (i < nums.length - 1 && nums[i] % 2 == 0) {
                i += 2;
            }
            while (j < nums.length && nums[j] % 2 == 1) {
                j += 2;
            }

            if (i >= nums.length - 1 || j >= nums.length) {
                break;
            }
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }

        return nums;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
