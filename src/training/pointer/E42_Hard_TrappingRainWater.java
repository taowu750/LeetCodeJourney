package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 42. 接雨水: https://leetcode-cn.com/problems/trapping-rain-water/
 *
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 * 示例 1：
 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出：6
 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * 图片参见 https://leetcode-cn.com/problems/trapping-rain-water/
 *
 * 例 2：
 * 输入：height = [4,2,0,3,2,5]
 * 输出：9
 *
 * 约束：
 * - n == height.length
 * - 0 <= n <= 3 * 10**4
 * - 0 <= height[i] <= 10**5
 */
public class E42_Hard_TrappingRainWater {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}), 6);
        assertEquals(method.applyAsInt(new int[]{4,2,0,3,2,5}), 9);
        assertEquals(method.applyAsInt(new int[]{}), 0);
        assertEquals(method.applyAsInt(new int[]{0}), 0);
        assertEquals(method.applyAsInt(new int[]{4,2,3}), 1);
        assertEquals(method.applyAsInt(new int[]{5,4,1,2}), 1);
        assertEquals(method.applyAsInt(new int[]{0,5,6,4,6,1,0,0,2,7}), 23);
    }

    /**
     * 双指针法。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：37.7 MB - 97.17%
     */
    public int trap(int[] height) {
        int water = 0;
        // 要想接雨水，肯定要先降序后升序，形成凹槽
        for (int i = 0, j = height.length - 1; j - i > 1;) {
            // 跳过头尾的升序高度
            while (j - i > 1 && height[i] <= height[i + 1])
                i++;
            while (j - i > 1 && height[j] <= height[j - 1])
                j--;
            if (j - i <= 1)
                break;
            int maybe = 0;
            for (int k = i + 1; k <= j; k++) {
                if (height[k] >= height[i]) {
                    i = k;
                    break;
                }
                maybe += height[i] - height[k];
                // 如果 height[i] 比 height[j] 还大，那么就应该从 j 往 i 进行计算
                if (k == j)
                    maybe = 0;
            }
            water += maybe;
            maybe = 0;
            for (int k = j - 1; k >= i; k--) {
                if (height[k] >= height[j]) {
                    j = k;
                    break;
                }
                // i 可能已经变动，所以这里也需要检查
                maybe += height[j] - height[k];
                if (k == i)
                    maybe = 0;
            }
            water += maybe;
        }

        return water;
    }

    @Test
    public void testTrap() {
        test(this::trap);
    }


    /**
     * 动态规划算法。参见：
     * https://leetcode-cn.com/problems/trapping-rain-water/solution/jie-yu-shui-by-leetcode-solution-tuvc/
     *
     * 对于下标 i，下雨后水能到达的最大高度等于下标 i 两边的最大高度的最小值，下标 i 处能接的雨水量等于下标 i
     * 处的水能到达的最大高度减去 height[i]。
     *
     * LeetCode 耗时：1 ms - 82.24%
     *          内存消耗：38 MB - 80.64%
     */
    public int dpMethod(int[] height) {
        if (height.length <= 2) {
            return 0;
        }

        int n = height.length, result = 0;
        // leftMax 表示 i 左边最高的高度
        int[] leftMax = new int[n];
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(height[i], leftMax[i - 1]);
        }
        // rightMax 表示 i 右边最高的高度
        int rightMax = height[n - 1];
        for (int j = n - 2; j > 0; j--) {
            rightMax = Math.max(height[j], rightMax);
            result += Math.min(leftMax[j], rightMax) - height[j];
        }

        return result;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 单调栈方法。参见：
     * https://leetcode-cn.com/problems/trapping-rain-water/solution/jie-yu-shui-by-leetcode-solution-tuvc/
     *
     * 从左到右扫描，维护一个单调栈，单调栈存储的是下标，满足从栈底到栈顶的下标对应的数组 height 中的元素递减或相等。
     * 也就是这是个递减栈，其中栈底存储的是 i 的左边最大元素。
     *
     * 从左到右遍历数组，遍历到下标 i 时，如果栈内至少有两个元素，记栈顶元素为 top，top 的下面一个元素是 left，
     * 则一定有 height[left]≥height[top]。如果 height[i]>height[top]，则得到一个可以接雨水的区域，
     * 该区域的宽度是 i−left−1，高度是 min(height[left],height[i])−height[top]，
     * 根据宽度和高度即可计算得到该区域能接的雨水量。
     *
     * LeetCode 耗时：2 ms - 38.05%
     *          内存消耗：38.4 MB - 9.99%
     */
    public int stackMethod(int[] height) {
        LinkedList<Integer> stack = new LinkedList<>();
        int result = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int top = stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                int left = stack.peek();
                int currWidth = i - left - 1;
                int currHeight = Math.min(height[left], height[i]) - height[top];
                result += currWidth * currHeight;
            }
            stack.push(i);
        }

        return result;
    }

    @Test
    public void testStackMethod() {
        test(this::stackMethod);
    }


    /**
     * 双指针法。参见：
     * https://leetcode-cn.com/problems/trapping-rain-water/solution/jie-yu-shui-by-leetcode-solution-tuvc/
     *
     * 维护两个指针 left 和 right，以及两个变量leftMax 和 rightMax，初始时 left=0,right=n−1,leftMax=0,rightMax=0。
     * 指针 left 只会向右移动，指针 right 只会向左移动，在移动指针的过程中维护两个变量 leftMax 和 rightMax 的值。
     *
     * 当两个指针没有相遇时，进行如下操作：
     * - 使用 height[left] 和 height[right] 的值更新 leftMax 和 rightMax 的值；
     * - 如果 leftMax< rightMax，下标 left 处能接的雨水量等于 leftMax−height[left]，
     *   将下标 left 处能接的雨水量加到能接的雨水总量，然后将 left 加 1（即向右移动一位）；
     * - 如果 leftMax≥rightMax，下标 right 处能接的雨水量等于 rightMax−height[right]，
     *   将下标 right 处能接的雨水量加到能接的雨水总量，然后将 right 减 1（即向左移动一位）。
     *
     * 因为 i 处的接水量只能是 leftMax 和 rightMax 中的最小值，因此不需要完全找到最大的那个就可以判断。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.9 MB - 88.21%
     */
    public int betterTwoPointMethod(int[] height) {
        int result = 0, leftMax = 0, rightMax = 0;
        for (int left = 0, right = height.length - 1; left < right;) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);

            if (leftMax < rightMax) {
                result += leftMax - height[left];
                left++;
            } else {
                result += rightMax - height[right];
                right--;
            }
        }

        return result;
    }

    @Test
    public void testBetterTwoPointMethod() {
        test(this::betterTwoPointMethod);
    }
}
