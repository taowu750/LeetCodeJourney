package training.string;

import org.junit.jupiter.api.Test;
import util.Util;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 5. 最长回文子串: https://leetcode-cn.com/problems/longest-palindromic-substring/
 *
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
        assertTrue(Util.in(method.apply("aacabdkacaa"), "aca"));
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
    public void testLongestPalindrome() {
        test(this::longestPalindrome);
    }


    public String intArrayMethod(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        int start = 0, len = 1;
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                // 不能仅仅只看是否相等，还要看是否连在一起
                if (s.charAt(i) == s.charAt(j) && (j - i < 3 || dp[i + 1][j - 1] > 0)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                }
                if (dp[i][j] > len) {
                    start = i;
                    len = dp[i][j];
                }
            }
        }

        return s.substring(start, start + len);
    }

    @Test
    public void testIntArrayMethod() {
        test(this::intArrayMethod);
    }


    public String compressMethod(String s) {
        int n = s.length();
        boolean[] dp = new boolean[n];

        int start = 0, len = 1;
        for (int i = n - 2; i >= 0; i--) {
            dp[i + 1] = true;
            // 右边依赖左边，所以从右往左遍历
            for (int j = n - 1; j >= i + 1; j--) {
                if (s.charAt(i) == s.charAt(j)) {
                    if (j - i < 3)
                        dp[j] = true;
                    else
                        dp[j] = dp[j - 1];
                } else {
                    dp[j] = false;
                }
                if (dp[j] && j - i + 1 > len) {
                    start = i;
                    len = j - i + 1;
                }
            }
        }

        return s.substring(start, start + len);
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
