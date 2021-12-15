package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 12. 整数转罗马数字: https://leetcode-cn.com/problems/integer-to-roman/
 *
 * 罗马数字包含以下七种字符：I，V，X，L，C，D 和 M。
 *
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 *
 * 例如， 罗马数字 2 写做 II，即为两个并列的 1。12 写做 XII ，即为 X+II。27 写做 XXVII, 即为 XX+V+II。
 *
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。
 * 数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。
 * 这个特殊的规则只适用于以下六种情况：
 * - I 可以放在 V(5) 和 X(10) 的左边，来表示 4 和 9。
 * - X 可以放在 L(50) 和 C(100) 的左边，来表示 40 和 90。
 * - C 可以放在 D(500) 和 M(1000) 的左边，来表示 400 和 900。
 *
 * 给你一个整数，将其转为罗马数字。
 *
 * 例 1：
 * 输入: num = 3
 * 输出: "III"
 *
 * 例 2：
 * 输入: num = 4
 * 输出: "IV"
 *
 * 例 3：
 * 输入: num = 9
 * 输出: "IX"
 *
 * 例 4：
 * 输入: num = 58
 * 输出: "LVIII"
 * 解释: L = 50, V = 5, III = 3.
 *
 * 例 5：
 * 输入: num = 1994
 * 输出: "MCMXCIV"
 * 解释: M = 1000, CM = 900, XC = 90, IV = 4.
 *
 * 说明：
 * - 1 <= num <= 3999
 */
public class E12_Medium_IntegerToRoman {

    public static void test(IntFunction<String> method) {
        assertEquals("III", method.apply(3));
        assertEquals("IV", method.apply(4));
        assertEquals("IX", method.apply(9));
        assertEquals("LVIII", method.apply(58));
        assertEquals("MCMXCIV", method.apply(1994));
        assertEquals("LXXXVI", method.apply(86));
    }

    /**
     * LeetCode 耗时：4 ms - 86.74%
     *          内存消耗：38.2 MB - 35.12%
     */
    public String intToRoman(int num) {
        StringBuilder result = new StringBuilder();
        int[] divs = {1000, 100, 10, 1};
        // 等于 9、4 时的特殊规则
        String[][] special = {{"", ""}, {"CM", "CD"}, {"XC", "XL"}, {"IX", "IV"}};
        /*
        其他一般情况，使用 1 和 5 的基数。令数字为 n，分为三种 case：
        - n ∈ [1,3]: n*1
        - n = 5:     1*5
        - n ∈ [6,8]: 5+(n-5)*1
         */
        char[][] normal = {{'M', '\0'}, {'C', 'D'}, {'X', 'L'}, {'I', 'V'}};
        for (int i = 0; i < divs.length; i++) {
            int div = divs[i];
            int quotient = num / div;
            if (quotient == 0) {
                continue;
            }
            if (quotient == 9) {
                result.append(special[i][0]);
            } else if (quotient == 4) {
                result.append(special[i][1]);
            } else {
                if (quotient < 4) {
                    for (int j = 0; j < quotient; j++) {
                        result.append(normal[i][0]);
                    }
                } else if (quotient == 5) {
                    result.append(normal[i][1]);
                } else {
                    result.append(normal[i][1]);
                    for (int j = 0; j < quotient - 5; j++) {
                        result.append(normal[i][0]);
                    }
                }
            }
            num = num % div;
        }

        return result.toString();
    }

    @Test
    public void testIntToRoman() {
        test(this::intToRoman);
    }


    public static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    public static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     * 划分所有可能情况的区间，类似于 Integer 中求取整数长度的代码。参见：
     * https://leetcode-cn.com/problems/integer-to-roman/solution/zheng-shu-zhuan-luo-ma-shu-zi-by-leetcod-75rs/
     *
     * LeetCode 耗时：4 ms - 86.74%
     *          内存消耗：38.2 MB - 35.12%
     */
    public String betterMethod(int num) {
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < VALUES.length; ++i) {
            int value = VALUES[i];
            String symbol = SYMBOLS[i];
            while (num >= value) {
                num -= value;
                roman.append(symbol);
            }
            if (num == 0) {
                break;
            }
        }

        return roman.toString();
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
