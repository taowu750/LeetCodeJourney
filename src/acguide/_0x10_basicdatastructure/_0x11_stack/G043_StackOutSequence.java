package acguide._0x10_basicdatastructure._0x11_stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 进出栈序列问题：https://www.acwing.com/problem/content/132/
 *
 * 给定 1~N 这 N 个整数和一个无限大的栈，每个数都要进栈并出栈一次。如果进栈的顺序为 1,2,……,N,
 * 那么可能的出栈序列有多少种？
 *
 * 输入格式：
 * 输入一个整数 N。
 *
 * 输出格式：
 * 输出一个整数 S 表示出栈的可能排列方式数量。
 *
 * 数据范围：
 * 1 ≤ N ≤ 60000
 *
 *
 * 例 1：
 * 输入：
 * 3
 * 输出：
 * 5
 */
public class G043_StackOutSequence {

    public static void test(Function<Integer, String> method) {
        assertEquals("1", method.apply(1));
        assertEquals("2", method.apply(2));
        assertEquals("5", method.apply(3));
        assertEquals("14", method.apply(4));
        assertEquals("42", method.apply(5));
        assertEquals("132", method.apply(6));
    }

    /**
     * dfs 模拟法。注意，下面几种方法都只考虑了 long 所能容纳的范围
     */
    public String sequences(int n) {
        return Long.toString(dfs(1, n, 0));
    }

    long dfs(int i, int n, int stack) {
        /*
        除最后一个数字外，其他每个数字入栈的时候，可以选择：
        1. 不弹出自己
        2. 弹出自己
        3. 不但弹出自己，还弹出栈中已有的数字。假设有 m 个，则有 [1,m] 种选择

        如果是最后一个数字入栈，那就只有一种选择，就是弹出栈中所有数字
         */

        if (i == n) {
            return 1;
        }

        // 由于只需要知道结果数量，所以栈中存什么元素无所谓
        long ans = dfs(i + 1, n, ++stack);
        ans += dfs(i + 1, n, --stack);

        int size = stack;
        for (int j = 1; j <= size; j++) {
            stack -= j;
            ans += dfs(i + 1, n, stack);
            // 注意回溯
            stack += j;
        }

        return ans;
    }

    @Test
    public void testSequences() {
        test(this::sequences);
    }


    public String dpMethod(int n) {
        /*
        dp[i][stack] = dp[i+1][stack + 1] + dp[i+1][stack] + dp[i+1][[0..stack)]
         */
        long[][] dp = new long[n+1][n+1];
        Arrays.fill(dp[n], 1);

        for (int i = n - 1; i >= 1; i--) {
            for (int stack = i; stack >= 0; stack--) {
                for (int k = 0; k <= stack + 1; k++) {
                    dp[i][stack] += dp[i+1][k];
                }
            }
        }

        return Long.toString(dp[1][0]);
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    public String compressedDpMethod(int n) {
        long[] dp = new long[n+1];
        Arrays.fill(dp,1);

        for (int i = n - 1; i >= 1; i--) {
            long right = dp[i + 1];
            for (int stack = i; stack >= 0; stack--) {
                long sum = right;
                // 下面的求和可以改为前缀和，从而使时间复杂度降低为 O(N^2)
                for (int k = 0; k <= stack; k++) {
                    sum += dp[k];
                }
                right = dp[stack];
                dp[stack] = sum;
            }
        }

        return Long.toString(dp[0]);
    }

    @Test
    public void testCompressedDpMethod() {
        test(this::compressedDpMethod);
    }


    public String dpPrefixMethod(int n) {
        long[] dp = new long[n];
        Arrays.fill(dp,1);

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                dp[j] += dp[j + 1];
            }
        }

        return Long.toString(dp[0]);
    }

    @Test
    public void testDpPrefixMethod() {
        test(this::dpPrefixMethod);
    }


    public String iterMethod(int n) {
        long[] s = new long[n+1];
        s[0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int k = 1; k <= i; k++) {
                s[i] += s[k-1] * s[i-k];
            }
        }

        return Long.toString(s[n]);
    }

    @Test
    public void testIterMethod() {
        test(this::iterMethod);
    }


    /**
     * 参见 https://www.acwing.com/solution/content/15603/
     */
    public String catalanMethod(int n) {
        int[] primes = new int[(2 * n + 1) / 2];
        int[] factors = new int[primes.length];
        int cnt = 0;
        boolean[] isNotPrime = new boolean[2 * n + 1];

        // 对 catalan 数分解质因数
        for (int i = 2; i <= 2 * n; i++) {
            if (!isNotPrime[i]) {
                primes[cnt] = i;
                // 计算 (2n)! 对于质数 i 的因数
                for (int j = 2 * n / i; j > 0; j /= i) {
                    factors[cnt] += j;
                }
                // 减去 (n!)**2 对于质数 i 的因数
                for (int j = n / i; j > 0; j /= i) {
                    factors[cnt] -= j * 2;
                }
                // 减去 n+1 对于质数 i 的因数
                for (int j = n + 1; j % i == 0; j /= i) {
                    factors[cnt]--;
                }
                cnt++;
            }
            // 线性筛算法，参见 https://leetcode.cn/problems/count-primes/solution/ji-shu-zhi-shu-by-leetcode-solution/
            for (int j = 0; j < cnt && primes[j] <= 2 * n / i; j++) {
                isNotPrime[primes[j] * i] = true;
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }

        // 使用大数乘法乘质因数
        List<Long> res = new ArrayList<>();
        res.add(1L);
        for (int i = 0; i < cnt; i++) {
            if (factors[i] > 0) {
                multi(res, qmi(primes[i], factors[i]));
            }
        }

        StringBuilder ans = new StringBuilder(9 * res.size());
        ans.append(res.get(res.size() - 1));
        for (int i = res.size() - 2; i >= 0; i--) {
            print(res.get(i), DIGITS, ans);
        }

        return ans.toString();
    }

    /**
     * 快速幂
     */
    public int qmi(int a, int b) {
        int res = 1;
        for (; b > 0; b >>= 1, a *= a) {
            if ((b & 1) == 1) {
                res *= a;
            }
        }

        return res;
    }

    public static long BASE = (long) 1e9;
    public static int DIGITS = 9;

    /**
     * 大数乘法，其实就是 BASE 进制数的乘法
     */
    public void multi(List<Long> num, int b) {
        long carry = 0;
        for (int i = 0; i < num.size(); i++) {
            long a = num.get(i) * b + carry;
            num.set(i, a % BASE);
            carry = a / BASE;
        }
        while (carry > 0) {
            num.add(carry % BASE);
            carry /= BASE;
        }
    }

    /**
     * 快速生成 digit 位
     */
    public void print(long x, int digit, StringBuilder sb) {
        if (digit == 0) {
            return;
        }
        print(x / 10, digit - 1, sb);
        sb.append((char) (x % 10 + '0'));
    }

    @Test
    public void testCatalanMethod() {
        test(this::catalanMethod);
    }
}
