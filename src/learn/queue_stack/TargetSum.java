package learn.queue_stack;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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
public class TargetSum {

    interface TargetSumMethod {
        int findTargetSumWays(int[] nums, int S);
    }

    static void test(TargetSumMethod method) {
        assertEquals(method.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3), 5);

        assertEquals(method.findTargetSumWays(new int[]{0, 0, 0}, 3), 0);

        assertEquals(method.findTargetSumWays(new int[]{1, 1}, 0), 2);

        assertEquals(method.findTargetSumWays(new int[]{0, 1, 3}, -2), 2);
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
}
