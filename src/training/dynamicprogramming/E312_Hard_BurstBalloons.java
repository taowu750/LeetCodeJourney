package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 312. 戳气球: https://leetcode-cn.com/problems/burst-balloons/
 *
 * 有 n 个气球，编号为 0 到 n - 1，每个气球上都标有一个数字，这些数字存在数组 nums 中。
 *
 * 现在要求你戳破所有的气球。戳破第 i 个气球，你可以获得 nums[i - 1] * nums[i] * nums[i + 1] 枚硬币。
 * 这里的 i - 1 和 i + 1 代表和 i 相邻的两个气球的序号。如果 i - 1或 i + 1 超出了数组的边界，
 * 那么就当它是一个数字为 1 的气球。
 *
 * 求所能获得硬币的最大数量。
 *
 * 例 1：
 * 输入：nums = [3,1,5,8]
 * 输出：167
 * 解释：
 * nums = [3,1,5,8] --> [3,5,8] --> [3,8] --> [8] --> []
 * coins =  3*1*5    +   3*5*8   +  1*3*8  + 1*8*1 = 167
 *
 * 例 2：
 * 输入：nums = [1,5]
 * 输出：10
 *
 * 约束：
 * - n == nums.length
 * - 1 <= n <= 500
 * - 0 <= nums[i] <= 100
 */
public class E312_Hard_BurstBalloons {

    static void test(ToIntFunction<int[]> method) {
        assertEquals(167, method.applyAsInt(new int[]{3,1,5,8}));
        assertEquals(10, method.applyAsInt(new int[]{1,5}));
        assertEquals(165, method.applyAsInt(new int[]{3,3,2,2,4,5}));
    }

    /**
     * 区间类型动态规划。
     * 参见：https://leetcode-cn.com/problems/burst-balloons/solution/zhe-ge-cai-pu-zi-ji-zai-jia-ye-neng-zuo-guan-jian-/
     *
     * LeetCode 耗时：83 ms - 71.54%
     *          内存消耗：38.2 MB - 19.93%
     */
    public int maxCoins(int[] nums) {
        int n = nums.length + 2;
        // 在 nums 的首尾添加 1
        int[] newNums = new int[n];
        newNums[0] = newNums[newNums.length - 1] = 1;
        System.arraycopy(nums, 0, newNums, 1, nums.length);

        // dp[i][j] 表示 (i,j) 区间内你能拿到的最多金币。
        // 状态转移方程：dp[i][j] = max(dp[i][k] + val[i] * val[k] * val[j] + dp[k][j])   k ∈ (i,j)
        int[][] dp = new int[n][n];

        // 保证 i..j 长度大于等于 3
        // 因为 dp[i][j] 需要同一行同一列的数据，所以从下到上、从左到右遍历
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + newNums[i] * newNums[k] * newNums[j] + dp[k][j]);
                }
            }
        }

        return dp[0][dp.length - 1];
    }

    @Test
    public void testMaxCoins() {
        test(this::maxCoins);
    }
}
