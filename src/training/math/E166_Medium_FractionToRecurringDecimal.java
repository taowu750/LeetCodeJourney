package training.math;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 166. 分数到小数: https://leetcode-cn.com/problems/fraction-to-recurring-decimal/
 *
 * 给定两个整数，分别表示分数的分子 numerator 和分母 denominator，以「字符串形式返回小数」。
 * 如果小数部分为循环小数，则将循环的部分括在括号内。
 * 如果存在多个答案，只需返回「任意一个」。
 * 对于所有给定的输入，保证「答案字符串的长度小于 10**4」。
 *
 * 例 1：
 * 输入：numerator = 1, denominator = 2
 * 输出："0.5"
 *
 * 例 2：
 * 输入：numerator = 2, denominator = 1
 * 输出："2"
 *
 * 例 3：
 * 输入：numerator = 2, denominator = 3
 * 输出："0.(6)"
 *
 * 例 4：
 * 输入：numerator = 4, denominator = 333
 * 输出："0.(012)"
 *
 * 例 5：
 * 输入：numerator = 1, denominator = 5
 * 输出："0.2"
 *
 * 约束：
 * - -2**31 <= numerator, denominator <= 2**31 - 1
 * - denominator != 0
 */
public class E166_Medium_FractionToRecurringDecimal {

    public static void test(BiFunction<Integer, Integer, String> method) {
        assertEquals("0.5", method.apply(1,2));
        assertEquals("2", method.apply(2,1));
        assertEquals("0.(6)", method.apply(2,3));
        assertEquals("0.(012)", method.apply(4,333));
        assertEquals("0.2", method.apply(1,5));
        assertEquals("-2.25", method.apply(-9,4));
        assertEquals("0.1(6)", method.apply(1, 6));
        assertEquals("0.(0588235294117647)", method.apply(1,17));
        assertEquals("0.0000000004656612873077392578125", method.apply(-1,-2147483648));
    }

    /**
     * LeetCode 耗时：1 ms - 100%
     *          内存消耗：35.8 MB - 68.94%
     */
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0 || denominator == 1)
            return Integer.toString(numerator);

        StringBuilder result = new StringBuilder();
        // 添加符号
        result.append((numerator > 0 ? 1 : -1) * (denominator > 0 ? 1 : -1) > 0 ? "" : '-');

        // 防止数字溢出
        long n = numerator, d = denominator;
        // 都转换为正数
        n = Math.abs(n);
        d = Math.abs(d);

        long quotient = n / d;
        long remainder = n % d;
        result.append(quotient);
        if (remainder == 0) {
            return result.toString();
        }

        // 记录余数，以及对应下标。当出现一样的余数时，表示出现了循环小数
        Map<Long, Integer> digit2idx = new HashMap<>();
        result.append('.');
        do {
            if (digit2idx.containsKey(remainder)) {
                result.insert(digit2idx.get(remainder), "(");
                result.append(")");
                break;
            }
            digit2idx.put(remainder, result.length());
            /*
            不断对余数补 0，再重新计算余数和除数的新余数。由于是对余数补 0，因此往后的过程完全取决于余数，
            所以当出现了重复的余数时，表示出现了循环小数。
             */
            remainder *= 10;
            result.append(remainder / d);
            remainder %= d;
        } while (remainder != 0);

        return result.toString();
    }

    @Test
    public void testFractionToDecimal() {
        test(this::fractionToDecimal);
    }
}
