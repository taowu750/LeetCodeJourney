package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个非负整数数组 nums ，你最初位于数组的「第一个下标」。
 * 数组中的每个元素代表你在该位置可以跳跃的「最大」长度。
 * 判断你是否能够到达最后一个下标。
 *
 * 例 1：
 * 输入：nums = [2,3,1,1,4]
 * 输出：true
 * 解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。
 *
 * 例 2：
 * 输入：nums = [3,2,1,0,4]
 * 输出：false
 * 解释：无论怎样，总会到达下标为 3 的位置。但该下标的最大跳跃长度是 0，所以永远不可能到达最后一个下标。
 *
 * 约束：
 * - 1 <= nums.length <= 3 * 10**4
 * - 0 <= nums[i] <= 10**5
 */
public class E55_Medium_JumpGame {

    static void test(Predicate<int[]> method) {
        assertTrue(method.test(new int[]{2,3,1,1,4}));
        assertFalse(method.test(new int[]{3,2,1,0,4}));
    }

    /**
     * 动态规划算法。
     */
    public boolean canJump(int[] nums) {
        final int n = nums.length;
        // dp[i] 表示根据 nums[0..i] 所能到达的最远下标
        final int[] dp = new int[n];
        dp[0] = nums[0];

        for (int i = 1; i < n; i++) {
            // 如果上次最远距离可以达到 i，那么就进行更新
            if (dp[i - 1] >= i)
                dp[i] = Math.max(i + nums[i], dp[i - 1]);
        }

        return dp[n - 1] != 0;
    }

    @Test
    public void testCanJump() {
        test(this::canJump);
    }


    /**
     * 状态压缩的动规方法。
     *
     * LeetCode 耗时：2 ms - 82.93%
     *          内存消耗：40.6 MB - 22.79%
     */
    public boolean compressedMethod(int[] nums) {
        int last = nums[0], cur = 0;

        for (int i = 1; i < nums.length; i++) {
            if (last >= i) {
                cur = Math.max(i + nums[i], last);
                last = cur;
            } else
                break;
        }

        return cur >= nums.length - 1;
    }

    @Test
    public void testCompressedMethod() {
        test(this::compressedMethod);
    }
}
