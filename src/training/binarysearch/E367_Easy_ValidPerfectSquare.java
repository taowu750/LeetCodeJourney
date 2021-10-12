package training.binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 367. 有效的完全平方数: https://leetcode-cn.com/problems/valid-perfect-square/
 *
 * 给定正整数 num，编写一个函数，如果 num 是一个完美平方数，则返回 True，否则返回 False。
 * 不要使用内置的 sqrt 函数。
 *
 * 一个完美平方数等于 1 + 3 + 5 + 7 + ...。例如：
 * 1 = 1
 * 4 = 1 + 3
 * 9 = 1 + 3 + 5
 * 16 = 1 + 3 + 5 + 7
 * 25 = 1 + 3 + 5 + 7 + 9
 * 36 = 1 + 3 + 5 + 7 + 9 + 11
 * ...
 * 因为 1+3+...+(2n-1) = (2n-1 + 1)*n/2 = n**2
 *
 * 例 1：
 * Input: num = 16
 * Output: true
 * Explanation: 16 = 4 * 4
 *
 * 例 2：
 * Input: num = 14
 * Output: false
 *
 * 约束：
 * 1 <= num <= 2**31 - 1
 */
public class E367_Easy_ValidPerfectSquare {

    static void test(IntPredicate method) {
        assertTrue(method.test(16));

        assertFalse(method.test(14));

        assertTrue(method.test(25));

        assertTrue(method.test(36));

        assertTrue(method.test(121));

        assertFalse(method.test(17));

        assertFalse(method.test(18));

        assertFalse(method.test(80));

        assertFalse(method.test(101));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public boolean isPerfectSquare(int num) {
        int lo = 1, hi = num;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int quotient = num / mid;
            if (quotient == mid) {
                return quotient * mid == num;
            } else if (quotient > mid) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return false;
    }

    @Test
    public void testIsPerfectSquare() {
        test(this::isPerfectSquare);
    }


    /**
     * 牛顿迭代法求平方根
     */
    public boolean newtonMethod(int num) {
        long x = num;
        while (x * x > num) {
            x = (x + num / x) >> 1;
        }
        return x * x == num;
    }

    @Test
    public void testNewtonMethod() {
        test(this::newtonMethod);
    }
}
