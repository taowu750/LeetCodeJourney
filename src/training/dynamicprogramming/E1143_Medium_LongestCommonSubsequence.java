package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
 *
 * 一个字符串的「子序列」是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符
 * （也可以不删除任何字符）后组成的新字符串。
 *
 * 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。两个字符串的「公共子序列」是这两个字符串所共同拥有的子序列。
 *
 * 若这两个字符串没有公共子序列，则返回 0。
 *
 * 例 1：
 * Input：text1 = "abcde", text2 = "ace"
 * Output：3
 * Explanation：最长公共子序列是 "ace"，它的长度为 3。
 *
 * 例 2：
 * Input：text1 = "abc", text2 = "abc"
 * Output：3
 * Explanation：最长公共子序列是 "abc"，它的长度为 3。
 *
 * 例 3：
 * Input：text1 = "abc", text2 = "def"
 * Output：0
 * Explanation：两个字符串没有公共子序列，返回 0。
 *
 * 约束：
 * - 1 <= text1.length <= 1000
 * - 1 <= text2.length <= 1000
 * - 输入的字符串只含有小写英文字符。
 */
public class E1143_Medium_LongestCommonSubsequence {

    static void test(ToIntBiFunction<String, String> method) {
        assertEquals(method.applyAsInt("abcde", "ace"), 3);
        assertEquals(method.applyAsInt("abc", "abc"), 3);
        assertEquals(method.applyAsInt("abc", "def"), 0);
    }

    public int longestCommonSubsequence(String text1, String text2) {
        return dp(text1, text1.length() - 1, text2, text2.length() - 1);
    }

    private int dp(String text1, int i, String text2, int j) {
        // 其中一个是空串
        if (i == -1 || j == -1)
            return 0;
        if (text1.charAt(i) == text2.charAt(j))
            // 找到一个 lcs 的元素，继续往前找
            return dp(text1, i - 1, text2, j - 1) + 1;
        else
            // 从两个里面选最长的
            return Math.max(dp(text1, i - 1, text2, j), dp(text1, i, text2, j - 1));
    }

    @Test
    public void testLongestCommonSubsequence() {
        test(this::longestCommonSubsequence);
    }


    /**
     * LeetCode 耗时：10ms - 61.60%
     *          内存消耗：42.8 MB - 62.61%
     */
    public int dpTableMethod(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        final int[][] dp = new int[m][n];

        // 注意需要正斜着扫描。因为遍历的过程中，所需的状态必须是已经计算出来的。
        for (int size = 1; size <= m + n - 1; size++) {
            int iMax = Math.min(size, m);
            for (int i = (n >= size ? 0 : size - n); i < iMax; i++) {
                int j = size - i - 1;
                if (text1.charAt(i) == text2.charAt(j))
                    dp[i][j] = (i - 1 >= 0 && j - 1 >= 0 ? dp[i - 1][j - 1] : 0) + 1;
                else
                    dp[i][j] = Math.max(i - 1 >= 0 ? dp[i - 1][j] : 0,
                            j - 1 >= 0 ? dp[i][j - 1] : 0);
            }
        }

        return dp[m - 1][n - 1];
    }

    @Test
    public void testDpTableMethod() {
        test(this::dpTableMethod);
    }


    /**
     * 结果没有任何改进，应该是判断已经被优化掉了。
     */
    public int failedTrickDpMethod(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        // 相较于 dpTableMethod，避免了在循环中对下标进行判断
        final int[][] dp = new int[m + 1][n + 1];

        // 注意需要正斜着扫描。因为遍历的过程中，所需的状态必须是已经计算出来的。
        for (int size = 1; size <= m + n - 1; size++) {
            int iMax = Math.min(size, m);
            for (int i = (n >= size ? 1 : size - n + 1); i <= iMax; i++) {
                int j = size - i + 1;
                if (text1.charAt(i - 1) == text2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        return dp[m][n];
    }

    @Test
    public void testTrickDpMethod() {
        test(this::failedTrickDpMethod);
    }
}
