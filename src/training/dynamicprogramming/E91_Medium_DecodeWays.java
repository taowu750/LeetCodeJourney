package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 91. 解码方法: https://leetcode-cn.com/problems/decode-ways/
 *
 * 一条包含字母 A-Z 的消息通过以下映射进行了「编码」：
 * 'A' -> 1
 * 'B' -> 2
 * ...
 * 'Z' -> 26
 *
 * 要「解码」已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。例如，"11106" 可以映射为：
 * - "AAJF" ，将消息分组为 (1 1 10 6)
 * - "KJF" ，将消息分组为 (11 10 6)
 * 注意，消息不能分组为 (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
 *
 * 给你一个只含数字的「非空」字符串 s ，请计算并返回「解码」方法的总数。
 * 题目数据保证答案肯定是一个 32 位的整数。
 *
 * 例 1：
 * 输入：s = "12"
 * 输出：2
 * 解释：它可以解码为 "AB"（1 2）或者 "L"（12）。
 *
 * 例 2：
 * 输入：s = "226"
 * 输出：3
 * 解释：它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6) 。
 *
 * 例 3：
 * 输入：s = "0"
 * 输出：0
 * 解释：没有字符映射到以 0 开头的数字。
 * 含有 0 的有效映射是 'J' -> "10" 和 'T'-> "20" 。
 * 由于没有字符，因此没有有效的方法对此进行解码，因为所有数字都需要映射。
 *
 * 例 4：
 * 输入：s = "06"
 * 输出：0
 * 解释："06" 不能映射到 "F" ，因为字符串含有前导 0（"6" 和 "06" 在映射中并不等价）。
 *
 * 约束：
 * - 1 <= s.length <= 100
 * - s 只包含数字，并且可能包含前导零。
 */
public class E91_Medium_DecodeWays {

    static void test(ToIntFunction<String> method) {
        assertEquals(2, method.applyAsInt("12"));
        assertEquals(3, method.applyAsInt("226"));
        assertEquals(0, method.applyAsInt("0"));
        assertEquals(0, method.applyAsInt("06"));
        assertEquals(1, method.applyAsInt("1"));
        assertEquals(0, method.applyAsInt("10011"));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.6 MB - 65.48%
     */
    public int numDecodings(String s) {
        // dp[i] 表示前 i 的字符解码结果的总数
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        if (s.charAt(0) != '0') {
            dp[1] = 1;
        }

        for (int i = 2; i <= s.length(); i++) {
            char cur = s.charAt(i - 1), prev = s.charAt(i - 2);
            // 当前字符为 0，则它必须和前面一个字符结合
            if (cur == '0') {
                // 无法结合，则编码失败
                if (prev != '1' && prev != '2') {
                    break;
                }
                dp[i] = dp[i - 2];
            } else {
                dp[i] = dp[i - 1];
                if (prev != '0' && (prev - '0') * 10 + (cur - '0') <= 26) {
                    dp[i] += dp[i - 2];
                }
            }
        }

        return dp[s.length()];
    }

    @Test
    public void testNumDecodings() {
        test(this::numDecodings);
    }


    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.4 MB - 84.13%
     */
    public int compressedMethod(String s) {
        int dpi_2 = 1, dpi_1 = s.charAt(0) != '0' ? 1 : 0, dpi;

        for (int i = 2; i <= s.length(); i++) {
            char cur = s.charAt(i - 1), prev = s.charAt(i - 2);
            // 当前字符为 0，则它必须和前面一个字符结合
            if (cur == '0') {
                // 无法结合，则编码失败
                if (prev != '1' && prev != '2') {
                    // 这里不要忘了赋值
                    dpi_1 = 0;
                    break;
                }
                dpi = dpi_2;
            } else {
                dpi = dpi_1;
                if (prev != '0' && (prev - '0') * 10 + (cur - '0') <= 26) {
                    dpi += dpi_2;
                }
            }
            dpi_2 = dpi_1;
            dpi_1 = dpi;
        }

        return dpi_1;
    }

    @Test
    public void testCompressedMethod() {
        test(this::compressedMethod);
    }
}
