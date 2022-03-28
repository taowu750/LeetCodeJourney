package training.number_code;

import org.junit.jupiter.api.Test;

import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 738. 单调递增的数字: https://leetcode-cn.com/problems/monotone-increasing-digits/
 *
 * 当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的。
 * 给定一个整数 n ，返回「小于或等于 n」的最大数字，且数字呈单调递增。
 *
 * 例 1：
 * 输入: n = 10
 * 输出: 9
 *
 * 例 2：
 * 输入: n = 1234
 * 输出: 1234
 *
 * 例 3：
 * 输入: n = 332
 * 输出: 299
 *
 * 说明：
 * - 0 <= n <= 10^9
 */
public class E738_Medium_MonotoneIncreasingDigits {

    public static void test(IntUnaryOperator method) {
        assertEquals(9, method.applyAsInt(10));
        assertEquals(1234, method.applyAsInt(1234));
        assertEquals(299, method.applyAsInt(332));
        assertEquals(1229, method.applyAsInt(1230));
    }

    /**
     * LeetCode 耗时：6 ms - 13.16%
     *          内存消耗：38.1 MB - 73.14%
     */
    public int monotoneIncreasingDigits(int n) {
        if (n <= 9) {
            return n;
        }

        int[] digits = Integer.toString(n).chars().map(c -> c - '0').toArray();
        /*
        如果 x <= y 那么就继续往后检查；
        否则 x 要减 1，y 及 y 之后的数变成 9，然后往前检查
         */
        int i = 0;
        // 找到第一个 digits[i] > digits[i+1] 的数字
        for (; i < digits.length - 1 && digits[i] <= digits[i + 1]; i++) {
        }
        // 都是升序，则返回 n
        if (i == digits.length - 1) {
            return n;
        }

        // 当 digits[i] > digits[i + 1] 时，将 digits[i] 减一，并将 [i + 1, end) 的数字变成 9
        int end = digits.length;
        do {
            digits[i]--;
            for (int j = i + 1; j < end; j++) {
                digits[j] = 9;
            }
            end = i + 1;
            i--;
        } while (i >= 0 && digits[i] > digits[i + 1]);

        // 跳过开头的 0
        for (i = 0; i < digits.length && digits[i] == 0; i++) {
        }
        // 组合后面的数字
        int result = 0;
        for (; i < digits.length; i++) {
            result = result * 10 + digits[i];
        }

        return result;
    }

    @Test
    public void testMonotoneIncreasingDigits() {
        test(this::monotoneIncreasingDigits);
    }


    /**
     * 和上面的思路类似，只是更简洁快速。
     *
     * LeetCode 耗时：1 ms - 92.19%
     *          内存消耗：38.6 MB - 23.97%
     */
    public int betterMethod(int n) {
        if (n < 10)
            return n;

        char[] chars = String.valueOf(n).toCharArray();

        // 从左往右遍历各位数字，找到第一个 chars[i] > chars[i+1]
        int i = 0;
        while (i+1 < chars.length && chars[i] <= chars[i+1]) {
            i++;
        }
        if (i == chars.length-1)
            return n; // 整个数字符合要求，直接返回

        // 需要再往前再看下，是否还有等于当前[i]的数字，找到最前面那个
        while (i-1 >= 0 && chars[i-1] == chars[i]) {
            i--;
        }
        // 将此时的[i]--，并将[i+1 ...]各位数字全部置为9
        chars[i] = (char) (chars[i] - 1);
        i++;
        while (i < chars.length) {
            chars[i] = '9';
            i++;
        }

        return Integer.parseInt(String.valueOf(chars));
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
