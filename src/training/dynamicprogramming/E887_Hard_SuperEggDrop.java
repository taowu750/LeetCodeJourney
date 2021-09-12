package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 887. 鸡蛋掉落: https://leetcode-cn.com/problems/super-egg-drop/
 *
 * 给定 k 枚相同的鸡蛋，和一栋 n 层楼的建筑。
 * 已知存在楼层 f ，满足 0<=f<=n ，任何从「高于 f」的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
 * <p>
 * 每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足 1 <= x <= n）。如果鸡蛋碎了，你就不能再次使用它。
 * 如果某枚鸡蛋扔下后没有摔碎，则可以在之后的操作中「重复使用」这枚鸡蛋。
 * <p>
 * 请你计算在「最坏情况」下，「最少」要扔几次鸡蛋，才能确定这个楼层 f 呢？
 * <p>
 * 例 1：
 * Input：k = 1, n = 2
 * Output：2
 * Explanation：
 * 鸡蛋从 1 楼掉落。如果它碎了，肯定能得出 f = 0 。
 * 否则，鸡蛋从 2 楼掉落。如果它碎了，肯定能得出 f = 1 。
 * 如果它没碎，那么肯定能得出 f = 2 。
 * 因此，在最坏的情况下我们需要扔 2 次以确定 f 是多少。
 * <p>
 * 例 2：
 * Input：k = 2, n = 6
 * Output：3
 * <p>
 * 例 3：
 * Input：k = 3, n = 14
 * Output：4
 * <p>
 * 约束：
 * - 1 <= k <= 100
 * - 1 <= n <= 10**4
 */
public class E887_Hard_SuperEggDrop {

    static void test(ToIntBiFunction<Integer, Integer> method) {
        assertEquals(method.applyAsInt(2, 6), 3);
        assertEquals(method.applyAsInt(3, 14), 4);
        assertEquals(method.applyAsInt(1, 2), 2);
        assertEquals(method.applyAsInt(2, 3), 2);
        assertEquals(method.applyAsInt(2, 2), 2);
        assertEquals(method.applyAsInt(2, 7), 4);
        assertEquals(method.applyAsInt(8, 1000), 10);
    }

    /**
     * 参见 README 高楼扔鸡蛋例子。
     */
    public int superEggDrop(int k, int n) {
        int[][] dp = new int[k + 1][n + 1];
        return dp(k, n, dp);
    }

    private int dp(int k, int n, int[][] dp) {
        if (dp[k][n] != 0)
            return dp[k][n];
        if (k == 1 || n == 0)
            return n;
        int res = n + 1;
        // 找出最坏情况下的最少扔鸡蛋次数
        for (int i = 1; i <= n; i++)
            res = Math.min(res,
                    // 状态转移
                    Math.max(dp(k, n - i, dp),         // 没碎
                            dp(k - 1, i - 1, dp))   // 碎了
                            + 1);                        // 在第 i 楼扔了一次
        dp[k][n] = res;
        return res;
    }

    @Test
    public void testSuperEggDrop() {
        test(this::superEggDrop);
    }


    /**
     * 超时。
     */
    public int iterateMethod(int k, int n) {
        if (k == 1 || n == 1)
            return n;

        int[][] dp = new int[k + 1][n + 1];
        // 当 k == 0 || n == 0 时，dp[k][n] 等于 0
        // 当只有一个鸡蛋时，有多少楼层就得扔多少次
        for (int floor = 1; floor <= n; floor++) {
            // 注意不要赋值为 n！！
            dp[1][floor] = floor;
        }
        // 其他情况设为 n + 1，
        for (int eggNum = 2; eggNum <= k; eggNum++) {
            for (int floor = 1; floor <= n; floor++) {
                dp[eggNum][floor] = n + 1;
            }
        }

        // 三重循环，算法复杂度为 O(k * n^2)

        // 状态1：鸡蛋数量
        for (int eggNum = 2; eggNum <= k; eggNum++) {
            // 状态2：楼层数量
            for (int floorNum = 1; floorNum <= n; floorNum++) {
                // 选择：在每个楼层试一下
                for (int choose = 1; choose <= floorNum; choose++) {
                    // 找出最坏情况下的最少扔鸡蛋次数
                    dp[eggNum][floorNum] = Math.min(
                            dp[eggNum][floorNum],
                            Math.max(dp[eggNum][floorNum - choose],     // 没碎
                                    dp[eggNum - 1][choose - 1])         // 碎了
                                    + 1);                               // 在第 choose 楼扔了一次
                }
            }
        }

        return dp[k][n];
    }

    @Test
    public void testIterateMethod() {
        test(this::iterateMethod);
    }


    /**
     * LeetCode 耗时：179 ms - 9.86%
     *          内存消耗：39.9 MB - 31.66%
     */
    public int binarySearchMethod(int k, int n) {
        if (k == 1 || n == 1)
            return n;

        int[][] dp = new int[k + 1][n + 1];
        // 当 k == 0 || n == 0 时，dp[k][n] 等于 0
        // 当只有一个鸡蛋时，有多少楼层就得扔多少次
        for (int floor = 1; floor <= n; floor++) {
            // 注意不要赋值为 n！！
            dp[1][floor] = floor;
        }
        // 其他情况设为 n + 1，
        for (int eggNum = 2; eggNum <= k; eggNum++) {
            for (int floor = 1; floor <= n; floor++) {
                dp[eggNum][floor] = n + 1;
            }
        }

        // 三重循环，算法复杂度为 O(k * n * log(n))

        // 状态1：鸡蛋数量
        for (int eggNum = 2; eggNum <= k; eggNum++) {
            // 状态2：楼层数量
            for (int floorNum = 1; floorNum <= n; floorNum++) {
                // 选择：使用二分搜索找两个线性函数相交值
                int lo = 1, hi = floorNum;
                while (lo <= hi) {
                    int mid = (lo + hi) >>> 1;
                    int broken = dp[eggNum - 1][mid - 1];
                    int notBroken = dp[eggNum][floorNum - mid];
                    if (broken > notBroken) {
                        hi = mid - 1;
                        dp[eggNum][floorNum] = Math.min(dp[eggNum][floorNum], broken + 1);
                    } else {
                        lo = mid + 1;
                        dp[eggNum][floorNum] = Math.min(dp[eggNum][floorNum], notBroken + 1);
                    }
                }
            }
        }

        return dp[k][n];
    }

    @Test
    public void testBinarySearchMethod() {
        test(this::binarySearchMethod);
    }


    /**
     * LeetCode 耗时：3 ms - 81.49%
     *          内存消耗：39.3 MB - 65.92%
     */
    public int reverseMethod(int k, int n) {
        if (k == 1 || n == 1)
            return n;

        // 最坏情况下的最少扔鸡蛋次数
        int m = 0;
        // 这个 dp 数组的状态是 dp[鸡蛋数][最多允许的扔鸡蛋次数]，值是最多能确切测试多少层。
        // m 不会超过 n（线性扫描）
        int[][] dp = new int[k + 1][n + 1];
        // 时间复杂度：O(k * n)
        for (int i = 1; i <= k; i++) {
            for (m = 1; m <= n; m++) {
                dp[i][m] = dp[i][m - 1] + dp[i - 1][m - 1] + 1;
                if (dp[i][m] >= n)
                    break;
            }
        }

        return m;
    }

    @Test
    public void testReverseMethod() {
        test(this::reverseMethod);
    }


    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.7 MB - 97.40%
     */
    public int reverseCompressMethod(int k, int n) {
        if (k == 1 || n == 1)
            return n;

        // 状态压缩
        int[] dp = new int[k + 1];
        int m;
        for (m = 0; dp[k] < n; ++m)
            for (int i = k; i > 0; --i)
                dp[i] += dp[i - 1] + 1;
        return m;
    }

    @Test
    public void testReverseCompressMethod() {
        test(this::reverseCompressMethod);
    }
}
