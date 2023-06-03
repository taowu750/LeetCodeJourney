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

    public static void test(BiFunction<String, String, String> method) {
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

        int m = num1.length(), n = num2.length();
        char[] result = new char[m + n];
        Arrays.fill(result, '0');

        for (int i = n - 1; i >= 0; i--) {
            // 每次计算乘积，然后加回到 result 中，因此需要分别记录乘积的进位和和的进位
            int n2 = num2.charAt(i) - '0', multiCarry = 0, sumCarry = 0;
            for (int j = m - 1; j >= 0; j--) {
                int product = n2 * (num1.charAt(j) - '0') + multiCarry;
                multiCarry = product / 10;
                int digit = product % 10;

                int sum = result[i + j + 1] - '0' + digit + sumCarry;
                sumCarry = sum / 10;
                result[i + j + 1]  = (char) (sum % 10 + '0');
            }
            if (multiCarry > 0 || sumCarry > 0) {
                result[i] = (char) (multiCarry + sumCarry + '0');
            }
        }

        int start = 0;
        for (; start < result.length && result[start] == '0'; start++);

        return start == result.length ? "0" : new String(result, start, result.length - start);
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
        和上面的方法不同。把个位数和另一个数相乘的步骤也进行分解
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
                /*
                 除开索引为 0 ，后面的 result[i+j] 都可能大于 10，但 i+j 是最终结果的高位，i+j+1 是低位。
                 由于计算顺序就是从右往左、从低到高的，所以每一轮都不需要高位是否要进位，下一轮自然会去处理可以放到下一次处理；
                 但是对于索引为 0 处的值，无论两个数多大，都不可能大于 10，因此后面到最后一次即使相加也无需进行处理；
                 你可以自己按下计算机 9999 * 99999之类的看看
                 */
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
