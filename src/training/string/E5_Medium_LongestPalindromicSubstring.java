package training.string;

import org.junit.jupiter.api.Test;
import util.Util;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 *
 * 例 1：
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 *
 * 例 2：
 * 输入：s = "cbbd"
 * 输出："bb"
 *
 * 例 3：
 * 输入：s = "a"
 * 输出："a"
 *
 * 例 4：
 * 输入：s = "ac"
 * 输出："a" 或 "c"
 *
 * 约束：
 * - 1 <= s.length <= 1000
 * - s 仅由数字和英文字母（大写和/或小写）组成
 */
public class E5_Medium_LongestPalindromicSubstring {

    static void test(Function<String, String> method) {
        assertTrue(Util.in(method.apply("babad"), "bab", "aba"));
        assertEquals(method.apply("cbbd"), "bb");
        assertEquals(method.apply("a"), "a");
        assertTrue(Util.in(method.apply("ac"), "a", "c"));
    }

    /**
     * 类似于{@link training.dynamicprogramming.E516_Medium_LongestPalindromicSubsequence}。
     *
     * LeetCode 耗时：128 ms - 62.51%
     *          内存消耗：42.9 MB - 30.78%
     */
    public String longestPalindrome(String s) {
        int len = s.length();
        if(len < 2)
            return s;

        int begin = 0, max = 1;
        // 巧妙地使用 boolean 数组，而不是存储实际的下标
        boolean[][] dp = new boolean[len][len];
        char[] charArray = s.toCharArray();

        for(int j = 0; j < len; j++) {
            for(int i = 0; i <= j; i++) {
                if(charArray[i] == charArray[j]) {
                    if(j - i < 3)
                        dp[i][j] = true;
                    else
                        dp[i][j] = dp[i + 1][j - 1];
                }

                if(dp[i][j] && j - i + 1 > max) {
                    begin = i;
                    max = j - i + 1;
                }
            }
        }
        return s.substring(begin, begin + max);
    }

    @Test
    public void test() {
        test(this::longestPalindrome);
    }
}
