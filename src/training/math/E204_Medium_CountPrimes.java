package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 204. 计数质数: https://leetcode-cn.com/problems/count-primes/
 *
 * 统计所有小于非负整数 n 的质数的数量。
 *
 * 例 1：
 * 输入：n = 10
 * 输出：4
 * 解释：小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7 。
 *
 * 例 2：
 * 输入：n = 0
 * 输出：0
 *
 * 例 3：
 * 输入：n = 1
 * 输出：0
 *
 * 约束：
 * - 0 <= n <= 5 * 10^6
 */
public class E204_Medium_CountPrimes {

    static void test(IntUnaryOperator method) {
        assertEquals(4, method.applyAsInt(10));
        assertEquals(0, method.applyAsInt(0));
        assertEquals(0, method.applyAsInt(1));
        assertEquals(0, method.applyAsInt(2));
        assertEquals(1, method.applyAsInt(3));
        assertEquals(348513, method.applyAsInt(5000000));
    }

    /**
     * 超出时间限制。
     */
    public int countPrimes(int n) {
        if (n <= 2) {
            return 0;
        }

        int result = 1;
        for (int i = 2; i < n; i++) {
            // 跳过偶数
            if ((i & 1) == 0) {
                continue;
            }

            boolean isPrime = true;
            for (int j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                result++;
            }
        }

        return result;
    }

    @Test
    public void testCountPrimes() {
        test(this::countPrimes);
    }


    /**
     * 埃氏筛。参见：
     * https://leetcode-cn.com/problems/count-primes/solution/ji-shu-zhi-shu-by-leetcode-solution/
     *
     * 下面的结果应该是官方更新了测试用例的结果，同样的代码以前只要 36 ms。
     *
     * LeetCode 耗时：98 ms - 20.17%
     *          内存消耗：42.9 MB - 44.43%
     */
    public int betterMethod(int n) {
        if (n <= 2) {
            return 0;
        }

        boolean[] isCompositeNum = new boolean[n];
        for (int i = 2; i * i < n; i++) {
            if (!isCompositeNum[i]) {
                for (int j = i * i; j < n; j += i) {
                    isCompositeNum[j] = true;
                }
            }
        }

        int result = 0;
        for (int i = 2; i < n; i++) {
            if (!isCompositeNum[i]) {
                result++;
            }
        }

        return result;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
