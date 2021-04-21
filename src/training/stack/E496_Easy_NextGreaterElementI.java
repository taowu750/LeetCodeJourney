package training.stack;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 496. 下一个更大元素 I: https://leetcode-cn.com/problems/next-greater-element-i/
 *
 * 给你两个「没有重复元素」的数组 nums1 和 nums2，其中 nums1 是 nums2 的子集。
 * 请你找出 nums1 中每个元素在 nums2 中的下一个比其大的值。
 *
 * nums1 中数字 x 的下一个更大元素是指 x 在 nums2 中「对应位置」的右边的第一个比 x 大的元素。
 * 如果不存在，对应位置输出 -1 。
 *
 * 例 1：
 * 输入: nums1 = [4,1,2], nums2 = [1,3,4,2].
 * 输出: [-1,3,-1]
 * 解释:
 *     对于 num1 中的数字 4 ，你无法在第二个数组中找到下一个更大的数字，因此输出 -1 。
 *     对于 num1 中的数字 1 ，第二个数组中数字1右边的下一个较大数字是 3 。
 *     对于 num1 中的数字 2 ，第二个数组中没有下一个更大的数字，因此输出 -1 。
 *
 * 例 2：
 * 输入: nums1 = [2,4], nums2 = [1,2,3,4].
 * 输出: [3,-1]
 * 解释:
 *     对于 num1 中的数字 2 ，第二个数组中的下一个较大数字是 3 。
 *     对于 num1 中的数字 4 ，第二个数组中没有下一个更大的数字，因此输出 -1 。
 *
 * 约束：
 * - 1 <= nums1.length <= nums2.length <= 1000
 * - 0 <= nums1[i], nums2[i] <= 10**4
 * - nums1 和 nums2 中所有整数互不相同
 * - nums1 中的所有整数同样出现在 nums2 中
 */
public class E496_Easy_NextGreaterElementI {

    static void test(BinaryOperator<int[]> method) {
        assertArrayEquals(new int[]{-1,3,-1}, method.apply(new int[]{4,1,2}, new int[]{1,3,4,2}));
        assertArrayEquals(new int[]{3,-1}, method.apply(new int[]{2,4}, new int[]{1,2,3,4}));
        assertArrayEquals(new int[]{6,4}, method.apply(new int[]{5,3}, new int[]{5,3,4,6}));
    }

    /**
     * 单调栈算法。
     * 此解法类似于 {@link learn.queue_stack.DailyTemperatures#stackMethod(int[])}。
     *
     * LeetCode 耗时：3 ms - 98.32%
     *          内存消耗：38.6 MB - 65.56%
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int n = nums2.length, top = -1;
        // 记录 nums2 中每个元素的下一个数字
        int[] stack = new int[n];
        Map<Integer, Integer> nextGreater = new HashMap<>((int) (n / 0.75) + 1);
        for (int i = 0; i < nums2.length; i++) {
            while (top >= 0 && nums2[i] > stack[top])
                nextGreater.put(stack[top--], nums2[i]);
            stack[++top] = nums2[i];
        }
        // 还在栈中的元素没有下一个数字
        while (top >= 0)
            nextGreater.put(stack[top--], -1);

        for (int i = 0; i < nums1.length; i++)
            nums1[i] = nextGreater.get(nums1[i]);

        return nums1;
    }

    @Test
    void testNextGreaterElement() {
        test(this::nextGreaterElement);
    }
}
