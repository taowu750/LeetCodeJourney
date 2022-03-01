package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 486. 预测赢家: https://leetcode-cn.com/problems/predict-the-winner/
 *
 * 给你一个整数数组 nums。玩家 1 和玩家 2 基于这个数组设计了一个游戏。
 *
 * 玩家 1 和玩家 2 轮流进行自己的回合，「玩家 1 先手」。开始时，两个玩家的初始分值都是 0 。
 * 每一回合，玩家从数组的任意一端取一个数字（即，nums[0] 或 nums[nums.length - 1]），
 * 取到的数字将会从数组中移除（数组长度减 1 ）。玩家选中的数字将会加到他的得分上。
 * 当数组中没有剩余数字可取时，游戏结束。
 *
 * 如果玩家 1 能成为赢家，返回 true。「如果两个玩家得分相等，同样认为玩家 1 是游戏的赢家，也返回 true」。
 * 你可以假设「每个玩家的玩法都会使他的分数最大化」。
 *
 * 例 1：
 * 输入：nums = [1,5,2]
 * 输出：false
 * 解释：一开始，玩家 1 可以从 1 和 2 中进行选择。
 * 如果他选择 2（或者 1 ），那么玩家 2 可以从 1（或者 2 ）和 5 中进行选择。如果玩家 2 选择了 5 ，那么玩家 1 则只剩下 1（或者 2 ）可选。
 * 所以，玩家 1 的最终分数为 1 + 2 = 3，而玩家 2 为 5 。
 * 因此，玩家 1 永远不会成为赢家，返回 false 。
 *
 * 例 2：
 * 输入：nums = [1,5,233,7]
 * 输出：true
 * 解释：玩家 1 一开始选择 1 。然后玩家 2 必须从 5 和 7 中进行选择。无论玩家 2 选择了哪个，玩家 1 都可以选择 233 。
 * 最终，玩家 1（234 分）比玩家 2（12 分）获得更多的分数，所以返回 true，表示玩家 1 可以成为赢家。
 *
 * 说明：
 * - 1 <= nums.length <= 20
 * - 0 <= nums[i] <= 10^7
 */
public class E486_Medium_PredictTheWinner {

    public static void test(Predicate<int[]> method) {
        assertFalse(method.test(new int[]{1,5,2}));
        assertTrue(method.test(new int[]{1,5,233,7}));
    }

    /**
     * LeetCode 耗时：1 ms - 46.65%
     *          内存消耗：38.7 MB - 26.64%
     */
    public boolean PredictTheWinner(int[] nums) {
        if (nums.length <= 2) {
            return true;
        }

        return dfs(nums, 0, nums.length - 1, true, 0);
    }

    public boolean dfs(int[] nums, int lo, int hi, boolean isPlayer1, int scoreDiff) {
        if (lo > hi) {
            return scoreDiff >= 0;
        }

        // 因为每个玩家的玩法都会使他的分数最大化，所以当玩家一旦在自己的回合（也就是先手权）赢了，那么他就会是赢家
        // 轮到玩家 1
        if (isPlayer1) {
            // 玩家1选了左边，并且在自己的回合赢了就返回 true
            if (dfs(nums, lo + 1, hi, false, scoreDiff + nums[lo])) {
                return true;
            }
            // 否则返回玩家1选右边的结果
            return dfs(nums, lo, hi - 1, false, scoreDiff + nums[hi]);
        } else {  // 轮到玩家 2
            // 玩家2选了左边，并且在自己的回合赢了就返回 false
            if (!dfs(nums, lo + 1, hi, true, scoreDiff - nums[lo])) {
                return false;
            }
            // 否则返回玩家2选右边的结果
            return dfs(nums, lo, hi - 1, true, scoreDiff - nums[hi]);
        }
    }

    @Test
    public void testPredictTheWinner() {
        test(this::PredictTheWinner);
    }


    /**
     *
     * LeetCode 耗时：1 ms - 46.65%
     *          内存消耗：38.7 MB - 20.50%
     */
    public boolean dpMethod(int[] nums) {
        final int n = nums.length;
        // dp[i][j][0] 表示 nums[i..j] 范围内玩家1先手能获得的最大分数差
        // dp[i][j][1] 表示 nums[i..j] 范围内玩家2先手能获得的最小分数差
        int[][][] dp = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            dp[i][i][0] = nums[i];
            dp[i][i][1] = -nums[i];
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j][0] = Math.max(nums[i] + dp[i + 1][j][1], nums[j] + dp[i][j - 1][1]);
                dp[i][j][1] = Math.min(dp[i + 1][j][0] - nums[i], dp[i][j - 1][0] - nums[j]);
            }
        }

        return dp[0][n - 1][0] >= 0;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 参见 https://leetcode-cn.com/problems/predict-the-winner/solution/yu-ce-ying-jia-by-leetcode-solution/
     *
     * LeetCode 耗时：0 ms - 100%
     *          内存消耗：38.8 MB - 16.75%
     */
    public boolean betterDpMethod(int[] nums) {
        final int n = nums.length;
        // dp[i][j] 表示 nums[i..j] 范围内当前玩家与另一个玩家的分数之差的最大值
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1] >= 0;
    }

    @Test
    public void testBetterDpMethod() {
        test(this::betterDpMethod);
    }
}
