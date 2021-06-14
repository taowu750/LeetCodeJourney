package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 剑指 Offer 60. n个骰子的点数: https://leetcode-cn.com/problems/nge-tou-zi-de-dian-shu-lcof/
 *
 * 把n个骰子(六面)扔在地上，所有骰子朝上一面的点数之和为s。输入n，打印出s的所有可能的值出现的概率。
 *
 * 你需要用一个浮点数数组返回答案，其中第 i 个元素代表这 n 个骰子所能掷出的点数集合中第 i 小的那个的概率。
 *
 * 例 1：
 * 输入: 1
 * 输出: [0.16667,0.16667,0.16667,0.16667,0.16667,0.16667]
 *
 * 例 2：
 * 输入: 2
 * 输出: [0.02778,0.05556,0.08333,0.11111,0.13889,0.16667,0.13889,0.11111,0.08333,0.05556,0.02778]
 *
 * 约束：
 * - 1 <= n <= 11
 */
public class Offer60_Medium_TheNumberOfNDice {

    static void test(IntFunction<double[]> method) {
        assertArrayEquals(new double[]{0.16667,0.16667,0.16667,0.16667,0.16667,0.16667},
                method.apply(1), 0.00001);

        assertArrayEquals(new double[]{0.02778,0.05556,0.08333,0.11111,0.13889,0.16667,0.13889,0.11111,0.08333,0.05556,0.02778},
                method.apply(2), 0.00001);

        assertArrayEquals(new double[]{0.00463, 0.01389, 0.02778, 0.04630, 0.06944, 0.09722, 0.11574, 0.125, 0.125, 0.11574, 0.09722, 0.06944, 0.0463, 0.02778, 0.01389, 0.00463},
                method.apply(3), 0.00001);
    }

    /**
     * 此算法是我第一次想出来的版本，作为和后面思路的对比。
     */
    public double[] dicesProbability(int n) {
        int[] counts = new int[5 * n + 1];
        recur(n, counts, n, 0);

        double sum = Math.pow(6, n);
        double[] result = new double[counts.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = counts[i] / sum;
        }

        return result;
    }

    /**
     * 这个递归类似于循环：
     * <pre>
     *     for (int i1 = 1; i1 <= 6; i1++) {
     *         for (int i2 = 1; i2 <= 6; i2++) {
     *             for {...}
     *         }
     *     }
     * </pre>
     *
     * 从骰子出发，计算所有掷出点数的次数。
     */
    private void recur(int n, int[] counts, int dice, int par) {
        if (dice == 0)
            return;
        if (dice == 1) {
            for (int i = 1; i <= 6; i++) {
                counts[i + par - n] += 1;
            }
        } else {
            for (int i = 1; i <= 6; i++) {
                recur(n, counts, dice - 1, i + par);
            }
        }
    }

    @Test
    public void testDicesProbability() {
        test(this::dicesProbability);
    }


    /**
     * 自顶向下方法。
     */
    public double[] topdownMethod(int n) {
        int[] counts = new int[5 * n + 1];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = getCount(n, i + n);
        }

        double sum = Math.pow(6, n);
        double[] result = new double[counts.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = counts[i] / sum;
        }

        return result;
    }

    /**
     * 计算投掷 n 个骰子，点数 k 的出现次数。
     *
     * 这个方法和第一个方法的区别在于：从掷出点数出发，计算计数。
     * 此方法专注于一点，所以会有重复子结构，方便使用动态规划。
     *
     * 模拟计算 getCount(2, 4) 和 getCount(2, 6)。它们的计算公式为：
     * - getCount(2,4) = getCount(1,1) + getCount(1,2) + getCount(1,3)
     * - getCount(2,6) = getCount(1,1) + getCount(1,2) + getCount(1,3) + getCount(1,4) + getCount(1,5)
     *
     * 可以看到，两个计算出现了重复的结构，这正是动态规划可以优化的地方。
     */
    private int getCount(int n, int k) {
        if (k <= 0)
            return 0;
        if (n == 1) {
            // 需要注意，当 n 等于 1，而 k 大于 6 时，则只能返回 0
            if (k <= 6)
                return 1;
            else
                return 0;
        }

        int count = 0;
        // 骰子从 1 到 6
        for (int i = 1; i <= 6; i++) {
            count += getCount(n - 1, k - i);
        }

        return count;
    }

    @Test
    public void testTopdownMethod() {
        test(this::topdownMethod);
    }


    /**
     * 动态规划方法。参见:
     * https://leetcode-cn.com/problems/nge-tou-zi-de-dian-shu-lcof/solution/nge-tou-zi-de-dian-shu-dong-tai-gui-hua-ji-qi-yo-3/
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.6 MB - 74.75%
     */
    public double[] dpMethod(int n) {
        // dp[i][j] 表示 i 个骰子可能掷出 j 的次数
        int[][] dp = new int[n + 1][6 * n + 1];
        for (int i = 1; i <= 6; i++) {
            dp[1][i] = 1;
        }

        for (int i = 2; i <= n; i++) {
            for (int j = i; j <= 6 * i; j++) {
                for (int k = 1; k <= 6 && k <= j; k++) {
                    dp[i][j] += dp[i - 1][j - k];
                }
            }
        }

        double sum = Math.pow(6, n);
        double[] result = new double[5 * n + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = dp[n][i + n] / sum;
        }

        return result;
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * 状态压缩方法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.6 MB - 74.75%
     */
    public double[] compressMethod(int n) {
        int[] dp = new int[6 * n + 1];
        for (int i = 1; i <= 6; i++) {
            dp[i] = 1;
        }

        for (int i = 2; i <= n; i++) {
            // 依赖上一行左边的数据，所以从右往左遍历
            for (int j = 6 * i; j >= i; j--) {
                dp[j] = 0;
                for (int k = 1; k <= 6; k++) {
                    // 需要注意，因为其余的 i - 1 个骰子每个至少掷出一点，
                    // 所以当 j - k 不能小于 i - 1，防止加了错误的数据
                    if (j - k < i - 1)
                        break;
                    dp[j] += dp[j - k];
                }
            }
        }

        double sum = Math.pow(6, n);
        double[] result = new double[5 * n + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = dp[i + n] / sum;
        }

        return result;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
