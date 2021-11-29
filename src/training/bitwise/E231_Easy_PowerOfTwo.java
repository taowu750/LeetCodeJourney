package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 231. 2 的幂: https://leetcode-cn.com/problems/power-of-two/
 *
 * 给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
 *
 * 如果存在一个整数 x 使得 n == 2x ，则认为 n 是 2 的幂次方。
 *
 * 例 1：
 * 输入：n = 1
 * 输出：true
 * 解释：2^0 = 1
 *
 * 例 2：
 * 输入：n = 16
 * 输出：true
 * 解释：24 = 16
 *
 * 例 3：
 * 输入：n = 3
 * 输出：false
 *
 * 例 4：
 * 输入：n = 4
 * 输出：true
 *
 * 例 5：
 * 输入：n = 5
 * 输出：false
 *
 * 说明：
 * - -2^31 <= n <= 2^31 - 1
 */
public class E231_Easy_PowerOfTwo {

    public static void test(IntPredicate method) {
        assertTrue(method.test(1));
        assertTrue(method.test(16));
        assertFalse(method.test(3));
        assertTrue(method.test(4));
        assertFalse(method.test(5));
        assertFalse(method.test(-2));
    }

    /**
     * 更好的方法参见：
     * https://leetcode-cn.com/problems/power-of-two/solution/2de-mi-by-leetcode-solution-rny3/
     *
     * 用到了 n & (n - 1) 和 n & -n 的技巧。
     *
     * LeetCode 耗时：1 ms - 88.39%
     *          内存消耗：35.6 MB - 10.65%
     */
    public boolean isPowerOfTwo(int n) {
        if (n <= 0) {
            return false;
        }

        int pow2 = 1;
        while ((n & pow2) == 0) {
            pow2 <<= 1;
        }

        return n == pow2;
    }

    @Test
    public void testIsPowerOfTwo() {
        test(this::isPowerOfTwo);
    }
}
