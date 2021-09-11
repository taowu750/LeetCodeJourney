package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 322. 零钱兑换：https://leetcode-cn.com/problems/coin-change/
 *
 * 给定一个整数数组 coins，表示不同面额的硬币，以及一个整数 amount，表示总金额。
 * 编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，
 * 返回 -1。
 * <p>
 * 你可以认为每种硬币的数量是无限的。
 * <p>
 * 例 1：
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 * <p>
 * 例 2：
 * Input: coins = [2], amount = 3
 * Output: -1
 * <p>
 * 例 3：
 * Input: coins = [1], amount = 0
 * Output: 0
 * <p>
 * 例 4：
 * Input: coins = [1], amount = 1
 * Output: 1
 * <p>
 * 例 5：
 * Input: coins = [1], amount = 2
 * Output: 2
 * <p>
 * 约束：
 * - 1 <= coins.length <= 12
 * - 1 <= coins[i] <= 2**31 - 1
 * - 0 <= amount <= 10**4
 */
public class E322_Medium_CoinChange {

    static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(method.applyAsInt(new int[]{1, 2, 5}, 11), 3);

        assertEquals(method.applyAsInt(new int[]{2}, 3), -1);

        assertEquals(method.applyAsInt(new int[]{1}, 0), 0);

        assertEquals(method.applyAsInt(new int[]{1}, 1), 1);

        assertEquals(method.applyAsInt(new int[]{1}, 2), 2);

        assertEquals(method.applyAsInt(new int[]{1, 2, 5}, 100), 20);

        assertEquals(method.applyAsInt(new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24}, 9999), -1);
    }

    /**
     * BFS 解法。
     *
     * LeetCode 耗时：113ms - 7.84%
     *          内存消耗：39.7 MB - 23.58%
     */
    public int coinChange(int[] coins, int amount) {
        if (amount == 0)
            return 0;

        Arrays.sort(coins);
        Queue<Integer> queue = new LinkedList<>();
        // 使用 Set 防止保存重复状态
        Set<Integer> visited = new HashSet<>();
        queue.add(amount);
        int level = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int residual = queue.remove();
                int coinIdx = Arrays.binarySearch(coins, residual);
                if (coinIdx >= 0)
                    return level;
                else {
                    coinIdx = -coinIdx - 1;
                    for (int j = coinIdx - 1; j >= 0; j--) {
                        int newResidual = residual - coins[j];
                        if (Arrays.binarySearch(coins, newResidual) >= 0)
                            return level + 1;
                        if (!visited.contains(newResidual)) {
                            visited.add(newResidual);
                            queue.add(newResidual);
                        }
                    }
                }
            }
            level++;
        }

        return -1;
    }

    @Test
    public void testCoinChange() {
        test(this::coinChange);
    }


    /**
     * 要符合「最优子结构」，子问题间必须互相独立。凑零钱问题，为什么说它符合最优子结构呢？
     * 比如你想求 amount = 11 时的最少硬币数（原问题），如果你知道凑出 amount = 10 的
     * 最少硬币数（子问题），你只需要把子问题的答案加一（再选一枚面值为 1 的硬币）就是原问题的答案。
     * 因为硬币的数量是没有限制的，所以子问题之间没有相互制，是互相独立的。
     *
     * 列出正确的状态转移方程：
     * 1. 明确「base case」，这个很简单，显然目标金额 amount 为 0 时算法返回 0，因为不需要
     *    任何硬币就已经凑出目标金额了。
     * 2. 明确「状态」，也就是原问题和子问题中会变化的变量。由于硬币数量无限，硬币的面额也是题目给定的，
     *    只有目标金额会不断地向 base case 靠近，所以唯一的「状态」就是目标金额 amount。
     * 3. 明确「选择」，也就是导致「状态」产生变化的行为。目标金额为什么变化呢，因为你在选择硬币，
     *    你每选择一枚硬币，就相当于减少了目标金额。所以说所有硬币的面值，就是你的「选择」。
     * 4. 明确「dp 函数/数组的定义」。我们这里讲的是自顶向下的解法，所以会有一个递归的 dp 函数，
     *    一般来说函数的参数就是状态转移中会变化的量，也就是上面说到的「状态」；函数的返回值就是
     *    题目要求我们计算的量。就本题来说，状态只有一个，即「目标金额」，题目要求我们计算凑出目标金额
     *    所需的最少硬币数量。所以我们可以这样定义 dp 函数：
     *          dp(n) 的定义：输入一个目标金额 n，返回凑出目标金额 n 的最少硬币数量。
     *
     * 最后的状态转移方程为：
     *      dp(n) = | 0, n = 0
     *              | -1, n < 0
     *              | min{dp(n - coin) + 1 | coin in coins}, n > 0
     *
     * LeetCode 耗时：25ms - 31.24%
     *          内存消耗：39.2 MB - 27.23%
     */
    public int topDownDp(int[] coins, int amount) {
        // 状态是金额，值是凑成这个金额所需的硬币数
        int[] memory = new int[amount + 1];
        // 因为凑成 amount 金额的硬币数最多只可能等于 amount（全用 1 元面值的硬币），
        // 所以初始化为 amount + 1 就相当于初始化为正无穷，便于后续取最小值。
        Arrays.fill(memory, amount + 1);
        return dp(coins, amount, memory);
    }

    private int dp(int[] coins, int amount, int[] memory) {
        // 基准条件
        if (amount == 0)
            return 0;
        if (amount < 0)
            return -1;

        // 保存的子问题
        int exist = memory[amount];
        if (exist != memory.length)
            return exist;

        // 自顶向下求解
        int res = memory.length;
        for (int coin : coins) {
            int subSolution = dp(coins, amount - coin, memory);
            // 跳过没有解的子问题
            if (subSolution == -1)
                continue;
            // 不要忘了加 1
            res = Math.min(res, 1 + subSolution);
        }
        // 所有子问题都无解，则最终无解
        res = res != memory.length ? res : -1;
        // 保存子问题的解
        memory[amount] = res;

        return res;
    }

    @Test
    public void testTopDownDp() {
        test(this::topDownDp);
    }


    /**
     * 自底向上，迭代法 dp。
     *
     * LeetCode 耗时：11 ms - 87.94%
     *          内存占用: 38.5 MB - 53.70%
     */
    public int downTopDp(int[] coins, int amount) {
        if (amount == 0)
            return 0;
        // dp table 大小为 amount + 1，初始值为 amount + 1（等同于上面的 Integer.MAX_VALUE 效果）
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        // base case
        dp[0] = 0;
        // 外层 for 循环在遍历所有状态的所有取值
        for (int status = 0; status < dp.length; status++) {
            // 内层 for 循环在求所有选择的最小值
            for (int coin : coins) {
                // 子问题无解，跳过
                if (status - coin < 0)
                    continue;
                dp[status] = Math.min(dp[status], 1 + dp[status - coin]);
            }
        }

        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    @Test
    public void testDownTopDp() {
        test(this::downTopDp);
    }


    public int dpMethod(int[] coins, int amount) {
        final int n = coins.length;
        // dp[i][j] 表示用前 i 枚硬币凑出金额 j 所需的最小硬币数
        final int[][] dp = new int[n + 1][amount + 1];

        // 注意初始化，amount + 1 表示不能凑出金额 j。
        // 当金额为 0 时，只需要 0 枚硬币就可以凑出
        for (int i = 0; i <= n; i++) {
            for (int j = 1; j <= amount; j++) {
                dp[i][j] = amount + 1;
            }
        }

        for (int i = 1; i <= n; i++) {
            int coin = coins[i - 1];
            for (int j = 1; j <= amount; j++) {
                if (coin > j)
                    // coin 大于金额 j，只能不使用这枚硬币
                    dp[i][j] = dp[i - 1][j];
                else
                    /*
                    否则在使用这枚硬币和不使用这枚硬币的选择中找最小值
                    需要注意的是，是 dp[i][j - coin] + 1，而不是 dp[i - 1][j - coin] + 1，
                    因为硬币数目无限，coin 可以重复使用
                     */
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - coin] + 1);
            }
        }

        return dp[n][amount] != amount + 1 ? dp[n][amount] : -1;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    public int compressMethod(int[] coins, int amount) {
        final int n = coins.length;
        // dp[i][j] 表示用前 i 枚硬币凑出金额 j 所需的最小硬币数
        final int[] dp = new int[amount + 1];

        for (int j = 1; j <= amount; j++) {
            dp[j] = amount + 1;
        }

        for (int coin : coins) {
            /*
            这里状态压缩后，遍历顺序还是和未压缩时一样，这看起来有些奇怪？
            看看之前的公式：dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - coin] + 1)，
            注意 dp[i - 1][j] 和它在同一列，不用管；而 dp[i][j - coin] + 1 和它在同一行，所以遍历还是从左到右。
             */
            for (int j = 1; j <= amount; j++) {
                if (coin <= j)
                    dp[j] = Math.min(dp[j], dp[j - coin] + 1);
            }
        }

        return dp[amount] != amount + 1 ? dp[amount] : -1;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
