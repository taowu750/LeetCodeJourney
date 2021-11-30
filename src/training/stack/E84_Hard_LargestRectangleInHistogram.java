package training.stack;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 84. 柱状图中最大的矩形: https://leetcode-cn.com/problems/largest-rectangle-in-histogram/
 *
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1。
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 *
 * 例 1：
 * 输入：heights = [2,1,5,6,2,3]
 * 输出：10
 * 解释：最大的矩形为图中红色区域，面积为 10
 *
 * 例 2：
 * 输入： heights = [2,4]
 * 输出： 4
 *
 * 约束：
 * - 1 <= heights.length <=10^5
 * - 0 <= heights[i] <= 10^4
 */
public class E84_Hard_LargestRectangleInHistogram {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(10, method.applyAsInt(new int[]{2,1,5,6,2,3}));
        assertEquals(4, method.applyAsInt(new int[]{2,4}));
        assertEquals(2, method.applyAsInt(new int[]{2,0,2}));
        assertEquals(20, method.applyAsInt(new int[]{3,6,5,7,4,8,1,0}));
        assertEquals(3, method.applyAsInt(new int[]{1,1,1}));
        assertEquals(6, method.applyAsInt(new int[]{2,3,4}));
    }

    /**
     * 超时。
     */
    public int largestRectangleArea(int[] heights) {
        int max = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] == 0) {
                continue;
            }

            int height = heights[i];
            // 计算 i 左边相连的大于等于 heights[i] 的高度
            for (int j = i - 1; j >= 0; j--) {
                if (heights[j] >= heights[i]) {
                    height += heights[i];
                } else {
                    break;
                }
            }
            // 计算 i 右边相连的大于等于 heights[i] 的高度
            for (int j = i + 1; j < heights.length; j++) {
                if (heights[j] >= heights[i]) {
                    height += heights[i];
                } else {
                    break;
                }
            }

            if (height > max) {
                max = height;
            }
        }

        return max;
    }

    @Test
    public void testLargestRectangleArea() {
        test(this::largestRectangleArea);
    }


    /**
     * 对每个柱子，我们需要找到它的左右边界，也就是左边和右边最近的比它小的柱子。
     *
     * 这题考的基础模型其实就是：在一维数组中对每一个数找到第一个比自己小的元素。
     * 这类“在一维数组中找第一个满足某种条件的数”的场景就是典型的单调栈应用场景。
     *
     * LeetCode 耗时：18ms - 91%
     *          内存消耗：47.1MB - 94%
     */
    public int stackMethod(int[] heights) {
        // 单增栈。在单调栈中的柱子都确定了它的左边界
        Deque<Integer> stack = new LinkedList<>();

        int max = 0;
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.getLast()] >= heights[i]) {
                int idx = stack.removeLast();
                // 每弹出一个柱子，也就确定了这个柱子的右边界，因此就可以求出它的面积
                if (stack.isEmpty()) {
                    max = Math.max(max, i * heights[idx]);
                } else {
                    max = Math.max(max, (i - stack.getLast() - 1) * heights[idx]);
                }
            }
            stack.addLast(i);
        }

        // 对还在栈中的柱子，需要确定它的面积
        int lastIdx = -1;
        while (!stack.isEmpty()) {
            int idx = stack.removeFirst();
            max = Math.max(max, (heights.length - lastIdx - 1) * heights[idx]);
            lastIdx = idx;
        }

        return max;
    }

    @Test
    public void test() {
        test(this::stackMethod);
    }
}
