package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 8. 字符串转换整数 (atoi)：https://leetcode-cn.com/problems/string-to-integer-atoi/
 *
 * 请你来实现一个 myAtoi(string s) 函数，使其能将字符串转换成一个 32 位有符号整数（类似 C/C++ 中的 atoi 函数）。
 *
 * 函数 myAtoi(string s) 的算法如下：
 * 1. 读入字符串并丢弃无用的前导空格
 * 2. 检查下一个字符（假设还未到字符末尾）为正还是负号，读取该字符（如果有）。确定最终结果是负数还是正数。
 * 如果两者都不存在，则假定结果为正。
 * 3. 读入下一个字符，直到到达下一个非数字字符或到达输入的结尾。字符串的其余部分将被忽略。
 * 4. 将前面步骤读入的这些数字转换为整数（即，"123" -> 123， "0032" -> 32）。如果没有读入数字，则整数为 0。
 * 必要时更改符号（从步骤 2 开始）。
 * 5. 如果整数数超过 32 位有符号整数范围 [−2**31,2**31−1]，需要截断这个整数，使其保持在这个范围内。
 * 具体来说，小于 −2**31 的整数应该被固定为 −2**31 ，大于 2**31−1 的整数应该被固定为 2**31−1。
 * 6. 返回整数作为最终结果。
 *
 * 注意：
 * - 本题中的空白字符只包括空格字符 ' ' 。
 * - 除前导空格或数字后的其余字符串外，请勿忽略「任何其他字符」。
 *
 * 例 1：
 * 输入：s = "42"
 * 输出：42
 * 解释：插入符号是当前读取的字符。
 * 第 1 步："42"（当前没有读入字符，因为没有前导空格）
 *          ^
 * 第 2 步："42"（当前没有读入字符，因为这里不存在 '-' 或者 '+'）
 *          ^
 * 第 3 步："42"（读入 "42"）
 *            ^
 * 解析得到整数 42 。
 * 由于 "42" 在范围 [-2**31, 2**31 - 1] 内，最终结果为 42 。
 *
 * 例 2：
 * 输入：s = "   -42"
 * 输出：-42
 * 解释：
 * 第 1 步："   -42"（读入前导空格，但忽视掉）
 *             ^
 * 第 2 步："   -42"（读入 '-' 字符，所以结果应该是负数）
 *              ^
 * 第 3 步："   -42"（读入 "42"）
 *                ^
 * 解析得到整数 -42 。
 * 由于 "-42" 在范围 [-2**31, 2**31 - 1] 内，最终结果为 -42 。
 *
 * 例 3：
 * 输入：s = "4193 with words"
 * 输出：4193
 * 解释：
 * 第 1 步："4193 with words"（当前没有读入字符，因为没有前导空格）
 *          ^
 * 第 2 步："4193 with words"（当前没有读入字符，因为这里不存在 '-' 或者 '+'）
 *          ^
 * 第 3 步："4193 with words"（读入 "4193"；由于下一个字符不是一个数字，所以读入停止）
 *              ^
 * 解析得到整数 4193 。
 * 由于 "4193" 在范围 [-2**31, 2**31 - 1] 内，最终结果为 4193 。
 *
 * 例 4：
 * 输入：s = "words and 987"
 * 输出：0
 * 解释：
 * 第 1 步："words and 987"（当前没有读入字符，因为没有前导空格）
 *          ^
 * 第 2 步："words and 987"（当前没有读入字符，因为这里不存在 '-' 或者 '+'）
 *          ^
 * 第 3 步："words and 987"（由于当前字符 'w' 不是一个数字，所以读入停止）
 *          ^
 * 解析得到整数 0 ，因为没有读入任何数字。
 * 由于 0 在范围 [-2**31, 2**31 - 1] 内，最终结果为 0 。
 *
 * 例 5：
 * 输入：s = "-91283472332"
 * 输出：-2147483648
 * 解释：
 * 第 1 步："-91283472332"（当前没有读入字符，因为没有前导空格）
 *          ^
 * 第 2 步："-91283472332"（读入 '-' 字符，所以结果应该是负数）
 *           ^
 * 第 3 步："-91283472332"（读入 "91283472332"）
 *                      ^
 * 解析得到整数 -91283472332 。
 * 由于 -91283472332 小于范围 [-2**31, 2**31 - 1] 的下界，最终结果被截断为 -2**31 = -2147483648 。
 */
public class E8_Medium_StringToInteger {

    public static void test(ToIntFunction<String> method) {
        assertEquals(42, method.applyAsInt("42"));
        assertEquals(-42, method.applyAsInt("   -42"));
        assertEquals(4193, method.applyAsInt("4193 with words"));
        assertEquals(0, method.applyAsInt("words and 987"));
        assertEquals(Integer.MIN_VALUE, method.applyAsInt("-91283472332"));
        assertEquals(32, method.applyAsInt("0032"));
        assertEquals(Integer.MAX_VALUE, method.applyAsInt("2147483648"));
        assertEquals(Integer.MIN_VALUE, method.applyAsInt("-2147483649"));
        assertEquals(467, method.applyAsInt("   +000467"));
        assertEquals(0, method.applyAsInt("+-12"));
        assertEquals(0, method.applyAsInt("00000-42a1234"));
        assertEquals(0, method.applyAsInt("   +0 123"));
        assertEquals(0, method.applyAsInt("  +  413"));
    }

    /**
     * LeetCode 耗时：2 ms - 99.59%
     *          内存消耗：38.5 MB - 62.48%
     */
    public int myAtoi(String s) {
        if (s.length() == 0)
            return 0;

        char[] numbers = new char[10];
        boolean overflow = false, hasSign = false, hasLeadingZero = false;
        int n = s.length(), j = 0, sign = 1;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            // 跳过前导空格。并且要防止空格跟在 0、符号 的后面
            if (c == ' ' && j == 0 && !hasLeadingZero && !hasSign)
                continue;
            // 确定符号。并且要防止重复符号
            if ((c == '-' || c == '+') && j == 0 && !hasSign) {
                sign = c == '-' ? -1 : 1;
                hasSign = true;
                continue;
            }
            // 遇到其他字符则跳出循环
            if (c < '0' || c > '9')
                break;
            // 遇到了数字，已经确定了符号。这里是为了防止符号跟在 0 后面
            hasSign = true;
            // 跳过前导 0
            if (c == '0' && j == 0) {
                hasLeadingZero = true;
                continue;
            }
            // 发生数字溢出则跳出循环
            if (j == numbers.length) {
                overflow = true;
                break;
            }
            numbers[j++] = c;
        }

        // 溢出则截断
        if (overflow)
            return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        // 计算结果和符号
        long result = 0;
        for (int i = 0; i < j; i++)
            result = result * 10 + numbers[i] - '0';
        result *= sign;

        if (sign == 1)
            return result <= Integer.MAX_VALUE ? (int) result : Integer.MAX_VALUE;
        else
            return result >= Integer.MIN_VALUE ? (int) result : Integer.MIN_VALUE;
    }

    @Test
    void testMyAtoi() {
        test(this::myAtoi);
    }
}
