package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 45. 跳跃游戏 II: https://leetcode-cn.com/problems/jump-game-ii/
 *
 * 给定一个非负整数数组，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的「最大」长度。
 * 你的目标是使用「最少」的跳跃次数到达数组的最后一个位置。
 *
 * 假设你总是可以到达数组的最后一个位置。
 *
 * 例 1：
 * 输入: [2,3,1,1,4]
 * 输出: 2
 * 解释: 跳到最后一个位置的最小跳跃数是 2。
 *      从下标为 0 跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。
 */
public class E45_Hard_JumpGameII {

    public static void test(ToIntFunction<int[]> method) {
        assertEquals(method.applyAsInt(new int[]{2,3,1,1,4}), 2);
        assertEquals(method.applyAsInt(new int[]{2,3,0,1,4}), 2);
    }

    /**
     * 贪心解法，每一步计算所能跳的最远的距离。
     *
     * 贪心算法可以认为是动态规划算法的一个特例，相比动态规划，使用贪心算法需要满足更多的条件（贪心选择性质），
     * 但是效率比动态规划要高。
     *
     * 什么是贪心选择性质呢，简单说就是：每一步都做出一个局部最优的选择，最终的结果就是全局最优。
     * 注意哦，这是一种特殊性质，其实只有一部分问题拥有这个性质。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.8 MB - 88.23%
     */
    public int jump(int[] nums) {
        final int n = nums.length;
        if (n < 2)
            return 0;
        if (nums[0] >= n - 1)
            return 1;

        // last: 上次最远能到达的下标
        int last = nums[0], count = 1;
        for (int i = 0; i < n;) {
            int max = last;
            // 计算 last 能到达的下标中，哪个是跳的最远的
            for (int j = i + 1; j <= last; j++) {
                // 从 j 能到达的最远下标
                int cur = j + nums[j];
                // 如果直接能达到终点，则返回
                if (cur >= n - 1)
                    return ++count;
                // 否则记录跳的最远的
                else if (cur > max) {
                    max = cur;
                    i = j;
                }
            }
            count++;
            last = max;
        }

        return count;
    }

    @Test
    public void testJump() {
        test(this::jump);
    }


    /**
     * 更精简快速的贪心算法。
     *
     * 参见 https://labuladong.gitee.io/algo/高频面试系列/跳跃游戏.html
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.7 MB - 91.44%
     */
    public int betterMethod(int[] nums) {
        /*
        每次在上次能跳到的范围（end）内选择一个能跳的最远的位置（也就是能跳到 farthest 位置的点）作为下次的起跳点。
         */

        int n = nums.length;
        // end 表示上次跳跃可达范围右边界（下次的最右起跳点）
        int end = 0, farthest = 0, count = 0;
        for (int i = 0; i < n - 1; i++) {
            farthest = Math.max(i + nums[i], farthest);
            // 当 end == i 时，表示已经到达了上次跳跃的最远下标。
            // 此时可以记录 [i, end] 之间的最佳跳跃位置。
            if (end == i) {
                count++;
                end = farthest;
            }
        }

        return count;
    }

    @Test
    public void testConciseMethod() {
        test(this::betterMethod);
    }


    /**
     * 动态规划解法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.1 MB - 66.67%
     */
    public int dpMethod(int[] nums) {
        final int n = nums.length;
        // dp[i] 记录从 i 跳到终点所需的最小次数
        final int[] dp = new int[n];

        for (int i = n - 2; i >= 0; i--) {
            int jump = i + nums[i];
            if (jump >= n - 1)
                dp[i] = 1;
            else {
                dp[i] = Integer.MAX_VALUE;
                for (int j = i + 1; j <= jump; j++)
                    dp[i] = Math.min(dp[i], dp[j]);
                if (dp[i] != Integer.MAX_VALUE)
                    dp[i] += 1;
            }
        }

        return dp[0];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
