package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToDoubleBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 实现 pow(x，n)，计算 x 的 n 次幂。
 *
 * 例 1：
 * Input: x = 2.00000, n = 10
 * Output: 1024.00000
 *
 * 例 2：
 * Input: x = 2.10000, n = 3
 * Output: 9.26100
 *
 * 例 3：
 * Input: x = 2.00000, n = -2
 * Output: 0.25000
 *
 * 约束：
 * - -100.0 < x < 100.0
 * - -2**31 <= n <= 2**31-1
 * - -10**4 <= x**n <= 10**4
 */
public class E50_Medium_Powxn {

    static void test(ToDoubleBiFunction<Double, Integer> method) {
        assertEquals(method.applyAsDouble(2.0, 10), 1024.0);

        assertEquals(method.applyAsDouble(2.1, 3), 9.261, 0.00001);

        assertEquals(method.applyAsDouble(2.0, -2), 0.25, 0.00001);

        assertEquals(method.applyAsDouble(1.13, 0), 1, 0.00001);

        assertEquals(method.applyAsDouble(1.13, 1), 1.13, 0.00001);

        assertEquals(method.applyAsDouble(0.00001, 2147483647), 0., 0.00001);

        assertEquals(method.applyAsDouble(1.0, Integer.MIN_VALUE), 1., 0.00001);

        assertEquals(method.applyAsDouble(-1.0, Integer.MIN_VALUE), 1., 0.00001);

        assertEquals(method.applyAsDouble(1.1, Integer.MIN_VALUE), 0., 0.00001);

        assertEquals(method.applyAsDouble(2.2, -Integer.MAX_VALUE), 0., 0.00001);
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public double myPow(double x, int n) {
        if (x == 0 || x == 1.0)
            return x;
        if (x == -1.0)
            return n % 2 == 0 ? 1.0 : -1.0;
        if (n == Integer.MIN_VALUE)
            // |x| 此时不会小于 1，否则结果会是无穷大，不符合约束条件。于是结果只能为 0。
            return 0;

        int sign = n > 0 ? 1 : -1;
        if (sign == -1)
            n = -n;

        double result = 1.0;
        while (n > 0) {
            double power = x;
            int pow2 = 2;
            while (n >= pow2) {
                power *= power;
                pow2 *= 2;
                // pow2 可能溢出，当 n 大于等于 10_73741_823，就会这样
                if (pow2 <= 0)
                    break;
            }
            result *= power;
            if (pow2 > 0)
                n -= pow2 >> 1;
            // 溢出了要特别处理
            else
                n -= 10_73741_823;
        }

        return sign == 1 ? result : 1.0 / result;
    }

    @Test
    public void testMyPow() {
        test(this::myPow);
    }


    /**
     * 递归方法。
     */
    public double recursiveMethod(double x, int n) {
        if (n == 0)
            return 1;
        else if (n < 0) {
            if (n == Integer.MIN_VALUE) {
                n += 2;
                x *= x;
            }
            n = -n;
            x = 1/x;
        }
        return (n % 2 == 0)
                ? recursiveMethod(x * x, n / 2)
                : x * recursiveMethod(x * x, n / 2);
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
