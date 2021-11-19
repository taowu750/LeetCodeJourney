package training.math;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 405. 数字转换为十六进制数: https://leetcode-cn.com/problems/convert-a-number-to-hexadecimal/
 *
 * 给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用补码运算方法。注意：
 * 1. 十六进制中所有字母(a-f)都必须是小写。
 * 2. 十六进制字符串中不能包含多余的前导零。如果要转化的数为0，那么以单个字符'0'来表示；
 *    对于其他情况，十六进制字符串中的第一个字符将不会是0字符。
 * 3. 给定的数确保在32位有符号整数范围内。
 * 4. 不能使用任何由库提供的将数字直接转换或格式化为十六进制的方法。
 *
 * 例 1：
 * 输入:
 * 26
 * 输出:
 * "1a"
 *
 * 例 2：
 * 输入:
 * -1
 * 输出:
 * "ffffffff"
 */
public class E405_Easy_ConvertNumberToHexadecimal {

    public static void test(IntFunction<String> method) {
        assertEquals("1a", method.apply(26));
        assertEquals("ffffffff", method.apply(-1));
        assertEquals("0", method.apply(0));
    }

    private static final char[] DIGIT_TO_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
    private static final int[] MOVE = {28, 24, 20, 16, 12, 8, 4, 0};

    /**
     * 表驱动法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：35.6 MB - 65.92%
     */
    public String toHex(int num) {
        StringBuilder result = new StringBuilder(8);
        boolean start = true;
        for (int move : MOVE) {
            char hex = DIGIT_TO_HEX[(num >>> move) & 0xf];
            if (start) {
                if (hex == '0') {
                    continue;
                }
            }
            start = false;
            result.append(hex);
        }

        return result.length() == 0 ? "0" : result.toString();
    }

    @Test
    public void testToHex() {
        test(this::toHex);
    }
}
