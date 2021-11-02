package training.dynamicprogramming;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 494. 目标和: https://leetcode-cn.com/problems/target-sum/
 *
 * 给定非负整数 a1，a2，...，an 列表和目标 S。现在您有 2 个符号 + 和 -。
 * 对于每个整数，应从 + 和 - 中选择一个作为它的正负号。
 * <p>
 * 找出几种分配符号的方法，以使整数和等于目标 S。
 * <p>
 * 例 1：
 * Input: nums is [1, 1, 1, 1, 1], S is 3.
 * Output: 5
 * Explanation:
 * -1+1+1+1+1 = 3
 * +1-1+1+1+1 = 3
 * +1+1-1+1+1 = 3
 * +1+1+1-1+1 = 3
 * +1+1+1+1-1 = 3
 * <p>
 * 约束：
 * - 给定整数个数不超过 20
 * - 给定整数之和不超过 1000
 * - 输出结果保证在 int 范围内
 */
public class E494_Medium_TargetSum {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(5, method.applyAsInt(new int[]{1, 1, 1, 1, 1}, 3));
        assertEquals(0, method.applyAsInt(new int[]{0, 0, 0}, 3));
        assertEquals(2, method.applyAsInt(new int[]{1, 1}, 0));
        assertEquals(2, method.applyAsInt(new int[]{0, 1, 3}, -2));
    }

    int[] suffix;
    int[] nums;
    int count, S;

    /**
     * LeetCode 耗时：269ms - 37.55%
     */
    public int findTargetSumWays(int[] nums, int S) {
        int len = nums.length;
        this.nums = nums;
        this.S = S;
        count = 0;
        suffix = new int[len];
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                suffix[i] += nums[j];
            }
        }

        findTargetSumWays(0, 1, 0);
        findTargetSumWays(0, -1, 0);

        return count;
    }

    private void findTargetSumWays(int idx, int sign, int sum) {
        int residual = S - sum;
        residual = residual >= 0 ? residual : -residual;
        if (idx == nums.length - 1) {
            if (sum + sign * nums[idx] == S)
                count++;
        } else if (suffix[idx] >= residual) {
            sum = sum + sign * nums[idx];
            findTargetSumWays(idx + 1, 1, sum);
            findTargetSumWays(idx + 1, -1, sum);
        }
    }

    @Test
    public void testFindTargetSumWays() {
        test(this::findTargetSumWays);
    }


    // 动态规划中，用来存储子问题的结果
    Map<Pair<Integer, Integer>, Integer> memory;

    /**
     * 动态规划方法。方法思想：
     * https://leetcode.com/explore/learn/card/queue-stack/232/practical-application-stack/1389/discuss/455024/DP-IS-EASY!-5-Steps-to-Think-Through-DP-Questions.
     *
     * LeetCode 耗时：88 ms - 49.84%
     */
    public int dpMethod(int[] nums, int S) {
        this.nums = nums;
        this.S = S;
        memory = new HashMap<>();

        return dpMethod(nums.length - 1, 0);
    }

    private int dpMethod(int idx, int sum) {
        Pair<Integer, Integer> state = new Pair<>(idx, sum);
        if (memory.containsKey(state))
            return memory.get(state);

        if (idx < 0)
            return sum == S ? 1 : 0;

        int positive = dpMethod(idx - 1, sum + nums[idx]);
        int negative = dpMethod(idx - 1, sum - nums[idx]);
        memory.put(state, positive + negative);

        return positive + negative;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 原始问题等价于：查找需要为正数的 nums 子集，其余的为负数，以使总和等于 target。
     * 设 P 为正数子集而 N 为负数子集。
     *
     * 例如，给定 nums = [1, 2, 3, 4, 5] 和 target = 3，一个解法是 +1-2+3-4+5 = 3。
     * 因此整数子集 P = [1, 3, 5]，负数子集 N = [2, 4]。
     *
     * 然后让我们看看如何将其转换为子集和问题：
     * (1) sum(P) - sum(N) = target
     * (2) sum(P) + sum(N) = sum(nums)
     * (1) + (2) => 2 * sum(P) = target + sum(nums)
     * 于是我们有 sum(P) = (target + sum(nums)) / 2。
     *
     * 请注意，以上公式表明 target+sum(nums) 必须为偶数。
     * 我们可以利用这一事实来快速确定没有解决方案的输入。
     */
    public int dp2dMethod(int[] nums, int S) {
        int sum = 0;
        for (int num : nums)
            sum += num;

        if (sum < S || ((sum + S) & 1) != 0)
            return 0;

        int ps = (sum + S) >>> 1;
        /*
        设 int[][] dp = new int[nums.length + 1][ps + 1]，
        其中 dp[i][j] 表示使用 nums 前 i 个元素得到和 j 的方法数量。
        则我们有 dp[i][j] = dp[i-1][j] + dp[i-1][j - nums[i - 1]]。
         */
        int[][] dp = new int[nums.length + 1][ps + 1];
        // 注意，nums 中可能有 0，因此 dp[i][0] 可能大于 1
        dp[0][0] = 1;
        for (int i = 1; i <= nums.length;  i++) {
            for (int j = 0; j <= ps;  j++) {
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[nums.length][ps];
    }

    @Test
    public void testDp2dMethod() {
        test(this::dp2dMethod);
    }

    public int betterDpMethod(int[] nums, int S) {
        int sum = 0;
        for (int num : nums)
            sum += num;

        if (sum < S || ((sum + S) & 1) != 0)
            return 0;

        /*
         2d 版本中的转换总是涉及到 2d 数组中的连续两行，所以可以对 dp 应用经典的状态压缩技巧，
         并将其简化为 dp[j] = dp[j] + dp[j - nums[i - 1]]，
         等于 dp[j] += dp[j - nums[i - 1]]。
         */
        int ps = (sum + S) >>> 1;
        int[] dp = new int[ps + 1];
        dp[0] = 1;
        for (int num : nums) {
            for (int i = ps; i >= num; i--)
                dp[i] += dp[i - num];
        }

        return dp[ps];
    }

    @Test
    public void testBetterDpMethod() {
        test(this::betterDpMethod);
    }
}
