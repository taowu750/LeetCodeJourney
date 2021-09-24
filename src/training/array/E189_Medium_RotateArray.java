package training.array;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 189. 旋转数组: https://leetcode-cn.com/problems/rotate-array/
 *
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 * 你可以使用空间复杂度为 O(1) 的原地算法解决这个问题吗？
 *
 * 例 1：
 * 输入: nums = [1,2,3,4,5,6,7], k = 3
 * 输出: [5,6,7,1,2,3,4]
 * 解释:
 * 向右旋转 1 步: [7,1,2,3,4,5,6]
 * 向右旋转 2 步: [6,7,1,2,3,4,5]
 * 向右旋转 3 步: [5,6,7,1,2,3,4]
 *
 * 例 2：
 * 输入：nums = [-1,-100,3,99], k = 2
 * 输出：[3,99,-1,-100]
 * 解释:
 * 向右旋转 1 步: [99,-1,-100,3]
 * 向右旋转 2 步: [3,99,-1,-100]
 *
 * 约束：
 * - 1 <= nums.length <= 2 * 10^4
 * - -2^31 <= nums[i] <= 2^31 - 1
 * - 0 <= k <= 10^5
 */
public class E189_Medium_RotateArray {

    static void test(BiConsumer<int[], Integer> method) {
        int[] nums = {1,2,3,4,5,6,7};
        method.accept(nums, 3);
        assertArrayEquals(new int[]{5,6,7,1,2,3,4}, nums);

        nums = new int[]{-1,-100,3,99};
        method.accept(nums, 2);
        assertArrayEquals(new int[]{3,99,-1,-100}, nums);

        nums = new int[]{1,2,3,4,5,6,7};
        method.accept(nums, 5);
        assertArrayEquals(new int[]{3,4,5,6,7,1,2}, nums);

        nums = new int[]{1,2,3,4,5,6,7};
        method.accept(nums, 7);
        assertArrayEquals(new int[]{1,2,3,4,5,6,7}, nums);
    }

    /**
     * 普通方法。
     *
     * LeetCode 耗时：1 ms - 62.09%
     *          内存消耗：55.4 MB - 23.68%
     */
    public void rotate(int[] nums, int k) {
        k = k % nums.length;
        if (k == 0) {
            return;
        }

        if (k <= nums.length - k) {
            int[] back = Arrays.copyOfRange(nums, nums.length - k, nums.length);
            for (int i = nums.length - k - 1, to = nums.length - 1; i >= 0; i--, to--) {
                nums[to] = nums[i];
            }
            System.arraycopy(back, 0, nums, 0, back.length);
        } else {
            int[] front = Arrays.copyOfRange(nums, 0, nums.length - k);
            for (int i = nums.length - k, to = 0; i < nums.length; i++, to++) {
                nums[to] = nums[i];
            }
            System.arraycopy(front, 0, nums, k, front.length);
        }
    }

    @Test
    public void testRotate() {
        test(this::rotate);
    }


    /**
     * 类似递归的方法，不断改变数组右端和 k。
     *
     * LeetCode 耗时：2 ms - 16.36%
     *          内存消耗：55.5 MB - 12.50%
     */
    public void o1Method(int[] nums, int k) {
        k = k % nums.length;
        if (k == 0) {
            return;
        }

        int hi = nums.length;
        while (k > 0) {
            if (k < hi - k) {
                for (int i = 1; i <= k; i++) {
                    int tmp = nums[hi - i];
                    nums[hi - i] = nums[hi - k - i];
                    nums[hi - k - i] = tmp;
                }
                hi = hi - k;
            } else {
                int len = hi - k;
                for (int i = 0; i < len; i++) {
                    int tmp = nums[i];
                    nums[i] = nums[hi - len + i];
                    nums[hi - len + i] = tmp;
                }
                k = k - len;
                hi = hi - len;
            }
        }
    }

    @Test
    public void testO1Method() {
        test(this::o1Method);
    }


    /**
     * 三级翻转。
     *
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：55.4 MB - 19.45%
     */
    public void betterO1Method(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int lo, int hi) {
        if (lo < hi) {
            int len = (hi - lo + 1) / 2;
            for (int i = 0; i < len; i++) {
                int tmp = nums[lo + i];
                nums[lo + i] = nums[hi - i];
                nums[hi - i] = tmp;
            }
        }
    }

    @Test
    public void testBetterO1Method() {
        test(this::betterO1Method);
    }
}
