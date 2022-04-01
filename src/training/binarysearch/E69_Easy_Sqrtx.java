package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 69. x 的平方根：https://leetcode-cn.com/problems/sqrtx/
 *
 * 给定一个非负整数 x，计算并返回 x 的平方根。
 * 由于返回类型是整数，因此十进制数字将被截断，并且仅返回结果的整数部分。
 *
 * 例 1：
 * Input: x = 4
 * Output: 2
 *
 * 例 2：
 * Input: x = 8
 * Output: 2
 *
 * 约束：
 * - 0 <= x <= 2**31 - 1
 */
public class E69_Easy_Sqrtx {

    public static void test(IntUnaryOperator method) {
        int limit = 1000000;
        for (int i = 0; i <= limit; i++) {
            assertEquals(method.applyAsInt(i), (int) Math.sqrt(i));
        }
    }

    /**
     * LeetCode 耗时：1ms - 99.98%。
     */
    public int mySqrt(int x) {
        if (x == 0 || x == 1)
            return x;

        int lo = 0, hi = x;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int sqrt = x / mid;
            if (mid < sqrt)
                lo = mid + 1;
            else if (mid > sqrt)
                hi = mid - 1;
            else
                return mid;
        }

        return hi;
    }

    @Test
    public void testMySqrt() {
        test(this::mySqrt);
    }
}
