package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 372. 超级次方：https://leetcode-cn.com/problems/super-pow/
 *
 * 你的任务是计算 a**b 对 1337 取模，a 是一个正整数，b 是一个非常大的正整数且会以数组形式给出。
 *
 * 例 1：
 * 输入：a = 2, b = [3]
 * 输出：8
 *
 * 例 2：
 * 输入：a = 2, b = [1,0]
 * 输出：1024
 *
 * 例 3：
 * 输入：a = 1, b = [4,3,3,8,5,2]
 * 输出：1
 *
 * 例 4：
 * 输入：a = 2147483647, b = [2,0,0]
 * 输出：1198
 *
 * 约束：
 * - 1 <= a <= 2**31 - 1
 * - 1 <= b.length <= 2000
 * - 0 <= b[i] <= 9
 * - b 不含前导 0
 */
public class E372_Medium_SuperPow {

    static void test(ToIntBiFunction<Integer, int[]> method) {
        assertEquals(8, method.applyAsInt(2, new int[]{3}));
        assertEquals(1024, method.applyAsInt(2, new int[]{1,0}));
        assertEquals(1, method.applyAsInt(1, new int[]{4,3,3,8,5,2}));
        assertEquals(1198, method.applyAsInt(2147483647, new int[]{2,0,0}));
    }

    static final int MOD = 1337;

    /**
     * LeetCode 耗时：4 ms - 89.87%
     *          内存消耗：38.7 MB - 64.68%
     */
    public int superPow(int a, int[] b) {
        return superPow(a, b, b.length - 1);
    }

    /**
     * a**(b[0]..b[end])
     * = a**b[end] * a**((b[0]..b[end-1]) * 10)
     */
    private int superPow(int a, int[] b, int end) {
        if (end == 0) {
            return myPow(a, b[0]);
        }

        int powEnd = myPow(a, b[end]);
        int powOther = myPow(superPow(a, b, end - 1), 10);

        return (powEnd * powOther) % MOD;
    }

    /**
     * a * b % N = (a % N) * (b % N) % N
     *
     * 因为：
     * a = m * N + A
     * b = n * N + B
     * 则 a * b % N = (mn * N**2 + m * N * B + n * N * B + AB) % N = AB % N
     *
     * 而 (a % N) * (b % N) % N = AB % N
     *
     * 所以两者相等。利用这个特性就可以计算连乘之后再取模
     */
    private int myPow(int a, int k) {
//        a %= MOD;
//        int result = 1;
//        for (int i = 0; i < k; i++) {
//            result *= a;
//            result %= MOD;
//        }
//
//        return result;

        if (k == 0)
            return 1;
        if (k == 1)
            return a % MOD;

        int halfPow = myPow(a, k >>> 1);
        int pow2 = halfPow * halfPow % MOD;
        if ((k & 1) == 0) {
            return pow2;
        } else {
            return pow2 * (a % MOD) % MOD;
        }
    }

    @Test
    public void testSuperPow() {
        test(this::superPow);
    }
}
