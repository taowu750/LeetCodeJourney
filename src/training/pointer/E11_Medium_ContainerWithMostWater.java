package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 11. 盛最多水的容器: https://leetcode-cn.com/problems/container-with-most-water/
 *
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，
 * 垂直线 i 的两个端点分别为 (i,ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 * 例 1：
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 *
 * 例 2：
 * 输入：height = [1,1]
 * 输出：1
 *
 * 例 3：
 * 输入：height = [4,3,2,1,4]
 * 输出：16
 *
 * 例 4：
 * 输入：height = [1,2,1]
 * 输出：2
 *
 * 约束：
 * - n = height.length
 * - 2 <= n <= 3 * 10^4
 * - 0 <= height[i] <= 3 * 10^4
 */
public class E11_Medium_ContainerWithMostWater {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(49, method.applyAsInt(new int[]{1,8,6,2,5,4,8,3,7}));
        assertEquals(1, method.applyAsInt(new int[]{1,1}));
        assertEquals(16, method.applyAsInt(new int[]{4,3,2,1,4}));
        assertEquals(2, method.applyAsInt(new int[]{1,2,1}));
    }

    /**
     * 双指针法。
     *
     * 我们注意到，当固定一边 i 时，它和另一边 j 组成的容器的最大高度不会超过 height[i]，
     * 那么它们之间的容量就取决于 i、j 的距离。所以可以从左右向内遍历，每次移到高度小的那个，
     * 高度相等则可以移动任意一个。
     *
     * LeetCode 耗时：4ms - 84%
     *          内存消耗：53.2MB - 47%
     */
    public int maxArea(int[] height) {
        int max = 0;
        for (int i = 0, j = height.length - 1; i < j;) {
            max = Math.max(max, Math.min(height[i], height[j]) * (j - i));
            if (height[i] > height[j]) {
                j--;
            } else if (height[i] < height[j]) {
                i++;
            } else {
                i++;
                j--;
            }
        }

        return max;
    }

    @Test
    public void testMaxArea() {
        test(this::maxArea);
    }
}
