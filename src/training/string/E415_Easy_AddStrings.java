package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定两个字符串形式的「非负」整数 num1 和 num2 ，计算它们的和。
 *
 * 约束：
 * - num1 和 num2 的长度都小于 5100
 * - num1 和 num2 都只包含数字 0-9
 * - num1 和 num2 都不包含任何前导零
 * - 你不能使用任何內建 BigInteger 库，也不能直接将输入的字符串转换为整数形式
 */
public class E415_Easy_AddStrings {

    static void test(BiFunction<String, String, String> method) {
        assertEquals("100", method.apply("0", "100"));
        assertEquals("4377330", method.apply("4365792", "11538"));
        assertEquals("10", method.apply("1", "9"));
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.3 MB - 84.64%
     */
    public String addStrings(String num1, String num2) {
        int m = num1.length(), n = num2.length();
        char[] result = new char[Math.max(m, n) + 1];

        // result 倒着存放结果
        int k = 0, advance = 0;
        for (int i = m - 1, j = n - 1; i >= 0 || j >= 0;) {
            int num;
            if (i < 0)
                num = num2.charAt(j--) - '0' + advance;
            else if (j < 0)
                num = num1.charAt(i--) - '0' + advance;
            else
                num = num1.charAt(i--) - '0' + num2.charAt(j--) - '0' + advance;
            advance = num / 10;
            result[k++] = (char) ((num % 10) + '0');
        }
        // 不要忘了最后的进位
        if (advance > 0)
            result[k++] = (char) (advance + '0');

        // 将数字正过来
        for (int i = 0, j = k - 1; i < j; i++, j--) {
            char tmp = result[i];
            result[i] = result[j];
            result[j] = tmp;
        }

        return new String(result, 0, k);
    }

    @Test
    void testAddStrings() {
        test(this::addStrings);
    }
}
