package training.array;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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

    static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}), 6);
        assertEquals(method.applyAsInt(new int[]{4,2,0,3,2,5}), 9);
        assertEquals(method.applyAsInt(new int[]{}), 0);
        assertEquals(method.applyAsInt(new int[]{0}), 0);
        assertEquals(method.applyAsInt(new int[]{4,2,3}), 1);
        assertEquals(method.applyAsInt(new int[]{5,4,1,2}), 1);
        assertEquals(method.applyAsInt(new int[]{0,5,6,4,6,1,0,0,2,7}), 23);
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：37.7 MB - 97.17%
     */
    @SuppressWarnings("DuplicatedCode")
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
}
