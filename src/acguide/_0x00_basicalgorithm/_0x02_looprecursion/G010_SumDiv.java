package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Sumdiv: https://ac.nowcoder.com/acm/contest/998/F
 *
 * 求 A^B 的所有因数之和 mod 9901 (1 ≤ A,B ≤ 5*10^7)。
 *
 * 例 1：
 * 输入：
 * 2 3
 * 输出：
 * 15
 * 解释：
 * 2^3=8.
 * 8 的自然因数是：1,2,4,8。它们的总和是 15。15 模 9901 是 15（应该输出）。
 */
public class G010_SumDiv {

    public static void test(IntBinaryOperator method) {
        assertEquals(15, method.applyAsInt(2, 3));
        assertEquals(4119, method.applyAsInt(37, 81));
        assertEquals(6651, method.applyAsInt(60, 127));
        assertEquals(9350, method.applyAsInt(48923657, 46579981));
        assertEquals(1, method.applyAsInt(1, 1));
        assertEquals(1, method.applyAsInt(1, 0));
        assertEquals(0, method.applyAsInt(0, 0));
    }

    private static final int MOD = 9901;

    public int sumdiv(int a, int b) {
        // 一定不能忘了边界条件！！
        if (a == 0) {
            return 0;
        }
        if (a == 1 || b == 0) {
            return 1;
        }

        // a 是质数，那么直接计算 a^b 的 sumdiv 即可
        if (isPrime(a)) {
            return geometricSum(a, b);
        }
        // 否则需要进行质因数分解
        List<int[]> primes = decomposeFactor(a);
        int ans = 1;
        for (int[] prime : primes) {
            ans = ans * geometricSum(prime[0], prime[1] * b) % MOD;
        }

        return ans;
    }

    private boolean isPrime(int n) {
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 找出小于 n 并且是 n 的因数的所有质数，使用埃式筛算法。
     * 参见 https://leetcode-cn.com/problems/count-primes/solution/ji-shu-zhi-shu-by-leetcode-solution/
     */
    private List<Integer> findPrimes(int n) {
        // 是否是合数
        boolean[] isCompose = new boolean[(int) Math.sqrt(n) + 1];
        List<Integer> result = new ArrayList<>();
        for (int i = 2; i * i <= n; i++) {
            if (!isCompose[i]) {
                if (n % i == 0) {
                    // 将 i 和 n/i 都判断一下，这样 isCompose 只需要 sqrt(n) 长度就可以了
                    result.add(i);
                    if (isPrime(n / i)) {
                        result.add(n / i);
                    }
                }
                for (int j = i * i; j < isCompose.length; j += i) {
                    isCompose[j] = true;
                }
            }
            // 可能存在 偶数*奇数 == n 的情况
            else if (n % i == 0 && isPrime(n / i)) {
                result.add(n / i);
            }
        }

        return result;
    }

    /**
     * 分解质因数，每个数组表示 {质因数，指数}。
     */
    private List<int[]> decomposeFactor(int n) {
        List<Integer> primes = findPrimes(n);
        List<int[]> result = new ArrayList<>();
        for (int prime : primes) {
            int[] pc = {prime, 0};
            while (n % prime == 0) {
                pc[1]++;
                n /= prime;
            }
            result.add(pc);
        }

        return result;
    }

    /**
     * 等比数列求和 mod 9901：
     * (1 + p + p^2 + ··· + p^c) % 9901
     */
    private int geometricSum(int p, int c) {
        if (c == 0) {
            return 1;
        }
        if ((c & 1) == 1) {
            return (1 + aPowB(p, (c + 1) / 2)) * geometricSum(p, (c - 1) / 2) % MOD;
        } else {
            return ((1 + aPowB(p, c / 2)) * geometricSum(p, c / 2 - 1) + aPowB(p, c)) % MOD;
        }
    }

    /**
     * 快速幂法计算 a^b
     */
    private int aPowB(int a, int b) {
        long aPowB = 1;
        for (long aPow = a; b > 0; b >>= 1, aPow = aPow * aPow % MOD) {
            if ((b & 1) == 1) {
                aPowB = aPowB * aPow % MOD;
            }
        }

        return (int) aPowB;
    }

    @Test
    public void testSumdiv() {
        test(this::sumdiv);
    }
}
