package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 13. 罗马数字转整数: https://leetcode-cn.com/problems/roman-to-integer/
 *
 * 罗马数字包含以下七种字符: I， V， X， L，C，D 和 M。
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 *
 * 例如， 罗马数字 2 写做 II，即为两个并列的 1 。12 写做 XII，即为 X+II。 27 写做 XXVII, 即为 XX+V+II。
 *
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。
 * 数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。
 * 这个特殊的规则只适用于以下六种情况：
 * - I 可以放在 V(5) 和 X(10) 的左边，来表示 4 和 9。
 * - X 可以放在 L(50) 和 C(100) 的左边，来表示 40 和 90。
 * - C 可以放在 D(500) 和 M(1000) 的左边，来表示 400 和 900。
 *
 * 给定一个罗马数字，将其转换成整数。
 *
 * 例 1：
 * 输入: s = "III"
 * 输出: 3
 *
 * 例 2：
 * 输入: s = "IV"
 * 输出: 4
 *
 * 例 3：
 * 输入: s = "IX"
 * 输出: 9
 *
 * 例 4：
 * 输入: s = "LVIII"
 * 输出: 58
 * 解释: L = 50, V= 5, III = 3.
 *
 * 例 5：
 * 输入: s = "MCMXCIV"
 * 输出: 1994
 * 解释: M = 1000, CM = 900, XC = 90, IV = 4.
 *
 * 说明：
 * - 1 <= s.length <= 15
 * - s 仅含字符 ('I', 'V', 'X', 'L', 'C', 'D', 'M')
 * - 题目数据保证 s 是一个有效的罗马数字，且表示整数在范围 [1, 3999] 内
 * - 题目所给测试用例皆符合罗马数字书写规则，不会出现跨位等情况。
 * - IL 和 IM 这样的例子并不符合题目要求，49 应该写作 XLIX，999 应该写作 CMXCIX 。
 */
public class E13_Easy_RomanToInteger {

    public static void test(ToIntFunction<String> method) {
        assertEquals(3, method.applyAsInt("III"));
        assertEquals(4, method.applyAsInt("IV"));
        assertEquals(9, method.applyAsInt("IX"));
        assertEquals(58, method.applyAsInt("LVIII"));
        assertEquals(1994, method.applyAsInt("MCMXCIV"));
        assertEquals(49, method.applyAsInt("XLIX"));
        assertEquals(999, method.applyAsInt("CMXCIX"));
    }

    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：41.6 MB - 32.34%
     */
    public int romanToInt(String s) {
        final int n = s.length();
        int result = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {
                case 'I':
                    if (i == n - 1 || (s.charAt(i + 1) != 'V' && s.charAt(i + 1) != 'X')) {
                        result++;
                    } else if (s.charAt(i + 1) == 'V') {
                        result += 4;
                        i++;
                    } else {
                        result += 9;
                        i++;
                    }
                    break;

                case 'V':
                    result += 5;
                    break;

                case 'X':
                    if (i == n - 1 || (s.charAt(i + 1) != 'L' && s.charAt(i + 1) != 'C')) {
                        result += 10;
                    } else if (s.charAt(i + 1) == 'L') {
                        result += 40;
                        i++;
                    } else {
                        result += 90;
                        i++;
                    }
                    break;

                case 'L':
                    result += 50;
                    break;

                case 'C':
                    if (i == n - 1 || (s.charAt(i + 1) != 'D' && s.charAt(i + 1) != 'M')) {
                        result += 100;
                    } else if (s.charAt(i + 1) == 'D') {
                        result += 400;
                        i++;
                    } else {
                        result += 900;
                        i++;
                    }
                    break;

                case 'D':
                    result += 500;
                    break;

                case 'M':
                    result += 1000;
                    break;
            }
        }

        return result;
    }

    @Test
    public void testRomanToInt() {
        test(this::romanToInt);
    }
}
