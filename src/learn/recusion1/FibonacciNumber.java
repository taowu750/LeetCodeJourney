package learn.recusion1;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Fibonacci 数，通常用 F(n) 表示，形成一个序列，称为 Fibonacci 序列，这样每个数都是前面两个数的和，
 * 从 0 和 1 开始。也就是说：
 *      F(0) = 0, F(1) = 1
 *      F(n) = F(n - 1) + F(n - 2), for n > 1.
 * 给定 n，计算 F(n)。
 *
 * 例 1：
 * Input: n = 2
 * Output: 1
 * Explanation: F(2) = F(1) + F(0) = 1 + 0 = 1.
 *
 * 例 2：
 * Input: n = 3
 * Output: 2
 * Explanation: F(3) = F(2) + F(1) = 1 + 1 = 2.
 *
 * 例 3：
 * Input: n = 4
 * Output: 3
 * Explanation: F(4) = F(3) + F(2) = 2 + 1 = 3.
 *
 * 约束：
 * - 0 <= n <= 30
 */
public class FibonacciNumber {

    static void test(IntUnaryOperator method) {
        assertEquals(method.applyAsInt(0), 0);
        assertEquals(method.applyAsInt(1), 1);
        assertEquals(method.applyAsInt(2), 1);
        assertEquals(method.applyAsInt(3), 2);
        assertEquals(method.applyAsInt(4), 3);
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public int fib(int n) {
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;

        int a = 0, b = 1, c = 0;
        for (n -= 1; n > 0; n--) {
            c = a + b;
            a = b;
            b = c;
        }

        return c;
    }

    @Test
    public void testFib() {
        test(this::fib);
    }
}
