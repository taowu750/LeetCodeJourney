package training.stack;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 给定一个循环数组（最后一个元素的下一个元素是数组的第一个元素），输出每个元素的下一个更大元素。
 * 数字 x 的下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，
 * 这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1。
 *
 * 例 1：
 * 输入: [1,2,1]
 * 输出: [2,-1,2]
 * 解释: 第一个 1 的下一个更大的数是 2；
 * 数字 2 找不到下一个更大的数；
 * 第二个 1 的下一个最大的数需要循环搜索，结果也是 2。
 *
 * 约束：
 * - 输入数组的长度不会超过 10000。
 */
public class E503_Medium_NextGreaterElementII {

    static void test(UnaryOperator<int[]> method) {
        assertArrayEquals(new int[]{2,-1,2}, method.apply(new int[]{1,2,1}));
        assertArrayEquals(new int[]{5,-1,-1,4,2,3,4}, method.apply(new int[]{4,5,5,3,1,2,3}));
    }

    /**
     * 递增栈，类似于 {@link E496_Easy_NextGreaterElementI}。
     *
     * LeetCode 耗时：2 ms - 99.82%
     *          内存消耗：41 MB - 5.06%
     */
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length, top = -1;
        int[] stack = new int[n], result = new int[n];
        // 使用递增栈计算出每个元素的下一个数字（非循环数组的情况）
        for (int i = 0; i < n; i++) {
            while (top > -1 && nums[i] > nums[stack[top]])
                result[stack[top--]] = nums[i];
            stack[++top] = i;
        }

        // 如果递增栈中还存有元素，那么这些元素一定是降序排列（不是严格降序，可能有重复元素）。
        // 这些元素的下一个元素可能在 nums[0]..nums[stack[0]] 中（stack[0] 是最大的元素）
        if (top > -1) {
            // stack[0] 是最大的元素，它不可能有下一个元素
            result[stack[0]] = -1;
            // 逆序遍历 stack，并且在 lo..hi 范围内找到它的下一个元素
            for (int i = top, lo = 0, hi = stack[0]; i >= 1; i--) {
                while (lo <= hi) {
                    if (nums[lo] > nums[stack[i]]) {
                        result[stack[i]] = nums[lo];
                        break;
                    }
                    lo++;
                }
                // 如果 lo..hi 范围内没有下一个元素，说明 result[stack[i]] 没有下一个元素
                if (lo > hi)
                    result[stack[i]] = -1;
            }
        }

        return result;
    }

    @Test
    void testNextGreaterElements() {
        test(this::nextGreaterElements);
    }


    /**
     * LeetCode 耗时：4 ms - 96.96%
     *          内存消耗：40.7 MB - 5.06%
     */
    public int[] betterMethod(int[] nums) {
        int n = nums.length, top = -1;
        int[] stack = new int[n], result = new int[n];

        // 对于环形数组，常用套路就是将数组长度翻倍。我们可以假装数组长度翻倍，无需实际的扩容。
        // 逆向遍历，栈中保存从最大值开始的递减序列，这样也能达到一样的效果
        for (int i = 2 * n - 1; i >= 0; i--) {
            int idx = i % n;
            while (top > -1 && nums[idx] >= stack[top])
                top--;
            result[idx] = top > -1 ? stack[top] : -1;
            stack[++top] = nums[idx];
        }

        return result;
    }

    @Test
    void testBetterMethod() {
        test(this::betterMethod);
    }
}
