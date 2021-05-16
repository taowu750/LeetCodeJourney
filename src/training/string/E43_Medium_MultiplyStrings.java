package training.string;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 43. 字符串相乘: https://leetcode-cn.com/problems/multiply-strings/
 *
 * 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
 *
 * 例 1：
 * 输入: num1 = "2", num2 = "3"
 * 输出: "6"
 *
 * 例 2：
 * 输入: num1 = "123", num2 = "456"
 * 输出: "56088"
 *
 * 约束：
 * - num1 和 num2 的长度小于110。
 * - num1 和 num2 只包含数字 0-9。
 * - num1 和 num2 均不以零开头，除非是数字 0 本身。
 * - 不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
 */
public class E43_Medium_MultiplyStrings {

    static void test(BiFunction<String, String, String> method) {
        assertEquals("6", method.apply("2", "3"));
        assertEquals("56088", method.apply("123", "456"));
        assertEquals("0", method.apply("9133", "0"));
    }

    /**
     * LeetCode 耗时：7 ms - 51.44%
     *          内存消耗：38 MB - 99.24%
     */
    public String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0"))
            return "0";

        if (num1.length() < num2.length()) {
            String tmp = num1;
            num1 = num2;
            num2 = tmp;
        }

        int m = num1.length(), n = num2.length();
        char[] result = new char[m + n];
        Arrays.fill(result, '0');
        int[] multiTmp = new int[n + 1];

        int start = m;
        // 将 num1 的每一位乘以 num2，并和上一次的结果相加
        for (int i = m - 1, lastIdx = result.length - 1; i >= 0; i--, lastIdx--) {
            int k = multiTmp.length - 1, advance = 0;
            // 将 num1[i] 和 nums2 相乘，结果存在 multiTmp 中
            for (int j = n - 1; j >= 0; j--, k--) {
                int multi = (num1.charAt(i) - '0') * (num2.charAt(j) - '0') + advance;
                advance = multi / 10;
                multiTmp[k] = multi % 10;
            }
            if (advance != 0) {
                multiTmp[k--] = advance;
            }

            advance = 0;
            int j = lastIdx;
            // 将相乘和结果和 result 相加
            for (int o = multiTmp.length - 1; o > k; j--, o--) {
                int add = result[j] - '0' + multiTmp[o] + advance;
                advance = add / 10;
                result[j] = (char) (add % 10 + '0');
            }
            if (advance != 0) {
                result[j--] = (char) (advance + '0');
            }
            start = j + 1;
        }

        return new String(result, start, result.length - start);
    }

    @Test
    public void testMultiply() {
        test(this::multiply);
    }


    /**
     * LeetCode 耗时：4 ms - 82.93%
     *          内存消耗：38.2 MB - 96.55%
     */
    public String betterMethod(String num1, String num2) {
        int m = num1.length(), n = num2.length();
        int[] result = new int[m + n];
        /*
        和上面的方法不同。把个位数和另一个数相乘的步骤也进行分解，从而不需要 tmp 数组
            1 2 3
         *    4 5
         --------
              1 5
            1 0
            5
            1 2
            8
          4
         --------
          5 5 3 5
         */
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int multi = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j, p2 = i + j + 1;
                int sum = multi + result[p2];
                result[p2] = sum % 10;
                result[p1] += sum / 10;
            }
        }

        int i = 0;
        while (i < result.length && result[i] == 0) {
            i++;
        }
        if (i == result.length)
            return "0";

        StringBuilder sb = new StringBuilder(result.length - i);
        for (; i < result.length; i++) {
            sb.append((char) (result[i] + '0'));
        }

        return sb.toString();
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
