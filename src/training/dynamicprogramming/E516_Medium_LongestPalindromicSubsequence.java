package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个字符串 s ，找到其中最长的回文子序列，并返回该序列的长度。可以假设 s 的最大长度为 1000。
 *
 * 例 1：
 * Input: "bbbab"
 * Output: 4
 * Explanation: 一个可能的最长回文子序列为 "bbbb"。
 *
 * 例 2：
 * Input: "cbbd"
 * Output: 2
 * Explanation: 一个可能的最长回文子序列为 "bb"。
 *
 * 约束：
 * - 1 <= s.length <= 1000
 * - s 只包含小写英文字母
 */
public class E516_Medium_LongestPalindromicSubsequence {

    static void test(ToIntFunction<String> method) {
        assertEquals(method.applyAsInt("bbbab"), 4);
        assertEquals(method.applyAsInt("cbbd"), 2);
        assertEquals(method.applyAsInt("abcba"), 5);
        assertEquals(method.applyAsInt("abbac"), 4);
    }

    /**
     * LeetCode 耗时：55 ms - 35.27%
     *          内存消耗：37.4 MB - 97.73%
     */
    public int longestPalindromeSubseq(String s) {
        final int n = s.length();
        // 定义 dp[i][j] 是 s 中前 i 个字符和 rs（s 的相反串）中相等的最长子序列长度。
        // 这样问题就转化为了“最长公共子序列”问题。
        // 注意下面是压缩了行的 dp 数组
        final int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            // prevCol 保存当前行前一列的数据。
            int preCol = 0;
            for (int j = 1; j <= n; j++) {
                int cur;
                if (s.charAt(i - 1) == s.charAt(n - j))
                    cur = dp[j - 1] + 1;
                else
                    cur = Math.max(preCol, dp[j]);
                dp[j - 1] = preCol;
                preCol = cur;
            }
            dp[n] = preCol;
        }

        return dp[n];
    }

    @Test
    public void testLongestPalindromeSubseq() {
        test(this::longestPalindromeSubseq);
    }


    /**
     * 参见 README 的具体描述。此方法比上面的方法大概少一半数组扫描操作。
     *
     * LeetCode 耗时：33 ms - 77.70%
     *          内存消耗：49.2 MB - 49.60%
     */
    public int otherDpMethod(String s) {
        int n = s.length();
        // 在子串 s[i..j] 中，最长回文子序列的长度为 dp[i][j]
        int[][] dp = new int[n][n];
        // base case
        for (int i = 0; i < n; i++)
            dp[i][i] = 1;

        // 反着遍历保证正确的状态转移
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                // 状态转移方程
                if (s.charAt(i) == s.charAt(j))
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                else
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
            }
        }

        // 整个 s 的最长回文子串长度
        return dp[0][n - 1];
    }

    @Test
    public void testOtherDpMethod() {
        test(this::otherDpMethod);
    }
}
