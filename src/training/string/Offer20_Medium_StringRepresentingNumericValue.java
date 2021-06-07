package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 剑指 Offer 20. 表示数值的字符串: https://leetcode-cn.com/problems/biao-shi-shu-zhi-de-zi-fu-chuan-lcof/
 *
 * 请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。
 *
 * 数值（按顺序）可以分成以下几个部分：
 * 1. 若干空格
 * 2. 一个「小数」或者「整数」
 * 3. （可选）一个 'e' 或 'E' ，后面跟着一个「整数」
 * 4. 若干空格
 *
 * 小数（按顺序）可以分成以下几个部分：
 * 1. （可选）一个符号字符（'+' 或 '-'）
 * 2. 下述格式之一：
 *      1. 至少一位数字，后面跟着一个点 '.'
 *      2. 至少一位数字，后面跟着一个点 '.' ，后面再跟着至少一位数字
 *      3. 一个点 '.' ，后面跟着至少一位数字
 *
 * 整数（按顺序）可以分成以下几个部分：
 * 1. （可选）一个符号字符（'+' 或 '-'）
 * 2. 至少一位数字
 *
 * 部分数值列举如下：
 * - ["+100", "5e2", "-123", "3.1416", "-1E-16", "0123"]
 *
 * 部分非数值列举如下：
 * - ["12e", "1a3.14", "1.2.3", "+-5", "12e+5.4"]
 *
 * 例 1：
 * 输入：s = "0"
 * 输出：true
 *
 * 例 2：
 * 输入：s = "e"
 * 输出：false
 *
 * 例 3：
 * 输入：s = "."
 * 输出：false
 *
 * 例 4：
 * 输入：s = "    .1  "
 * 输出：true
 *
 * 约束：
 * - 1 <= s.length <= 20
 * - s 仅含英文字母（大写和小写），数字（0-9），加号 '+' ，减号 '-' ，空格 ' ' 或者点 '.' 。
 */
public class Offer20_Medium_StringRepresentingNumericValue {

    static void test(Predicate<String> method) {
        assertTrue(method.test("+100"));
        assertTrue(method.test("5e2"));
        assertTrue(method.test("-123"));
        assertTrue(method.test("3.1416"));
        assertTrue(method.test("-1.37E-16"));
        assertTrue(method.test("0123"));
        assertTrue(method.test("    .1  "));
        assertTrue(method.test(" 2."));
        assertFalse(method.test("1a3.14"));
        assertFalse(method.test("1.2.3"));
        assertFalse(method.test("+-5"));
        assertFalse(method.test("12e+5.4"));
        assertFalse(method.test("  1.37  5"));
        assertFalse(method.test(" "));
        assertFalse(method.test("0e "));
        assertFalse(method.test("92e1740e91"));
    }

    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：38.5 MB - 61.15%
     */
    public boolean isNumber(String s) {
        // 是先导空格，还是后导空格
        boolean prevSpace = true;
        int numberPart = 0;
        // 分别解析三个部分（前导后导空格、小数或整数、指数）
        for (int i = 0; i < s.length();) {
            char c = s.charAt(i);
            // 如果 c 等于空格
            if (c == ' ') {
                // 一直到不是空格的字符为止
                while (++i < s.length() && s.charAt(i) == ' ');
                // 如果是前导空格
                if (prevSpace) {
                    prevSpace = false;
                    // 如果已经到字符串末尾，则返回 false
                    if (i == s.length())
                        return false;
                } else {
                    // 否则是后导空格，则返回是否到字符串末尾
                    return i == s.length();
                }
            }
            // 如果是第一部分的数字
            else if (numberPart == 0) {
                // 不能再有先导空格
                prevSpace = false;
                numberPart++;
                // 解析开头的符号
                if (c == '+' || c == '-') {
                    if (i == s.length() - 1)
                        return false;
                    c = s.charAt(++i);
                }
                // 解析开头的小数点
                boolean dot = false;
                if (c == '.') {
                    dot = true;
                    if (i == s.length() - 1)
                        return false;
                    c = s.charAt(++i);
                }
                // 遇到数字，解析成小数或整数
                if (c >= '0' && c <= '9') {
                    while (++i < s.length() && (c = s.charAt(i)) >= '0' && c <= '9');
                    if (c == '.') {
                        // 前面已经有小数点，则返回 false
                        if (dot)
                            return false;
                        while (++i < s.length() && (c = s.charAt(i)) >= '0' && c <= '9');
                    }
                }
                // 遇到其他字符，返回 false
                else {
                    return false;
                }
            }
            // 如果是第二部分的数字，即指数
            else if (numberPart == 1) {
                // 必须以 e 或 E 开头
                if (c != 'e' && c != 'E')
                    return false;
                // 不能再有先导空格
                prevSpace = false;
                numberPart++;
                // 指数必须存在
                if (i == s.length() - 1)
                    return false;
                c = s.charAt(++i);
                // 解析符号
                if (c == '+' || c == '-') {
                    if (i == s.length() - 1)
                        return false;
                    c = s.charAt(++i);
                }
                // 如果后面没有数字，返回 false
                if (c < '0' || c > '9')
                    return false;
                while (++i < s.length() && (c = s.charAt(i)) >= '0' && c <= '9');
            }
            // 遇到其他字符，或其他情况返回 false
            else {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testIsNumber() {
        test(this::isNumber);
    }
}
