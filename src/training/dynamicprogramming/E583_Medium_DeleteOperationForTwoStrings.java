package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定两个字符串 word1 和 word2，返回使 word1 和 word2 相同所需的最小步骤数。
 * 在一步中，您可以在任意一个字符串中删除一个字符。
 *
 * 例 1：
 * Input: word1 = "sea", word2 = "eat"
 * Output: 2
 * Explanation: 需要删除 s 和 t
 *
 * 例 2：
 * Input: word1 = "leetcode", word2 = "etco"
 * Output: 4
 *
 * 约束：
 * - 1 <= word1.length, word2.length <= 500
 * - word1 和 word2 都只包含小写英文字母
 */
public class E583_Medium_DeleteOperationForTwoStrings {

    static void test(ToIntBiFunction<String, String> method) {
        assertEquals(method.applyAsInt("sea", "eat"), 2);
        assertEquals(method.applyAsInt("leetcode", "etco"), 4);
    }

    /**
     * 此题和{@link E1143_Medium_LongestCommonSubsequence}类似。
     */
    public int minDistance(String word1, String word2) {
        return dp(word1, word1.length() - 1, word2, word2.length() - 1);
    }

    private int dp(String word1, int i, String word2, int j) {
        // 都已经遍历完，返回 0
        if (i == -1 && j == -1)
            return 0;
        // 其中一个遍历完，剩下的字符需要删除
        if (i == -1)
            return j + 1;
        if (j == -1)
            return i + 1;
        // 相等，不需要删除
        if (word1.charAt(i) == word2.charAt(j))
            return dp(word1, i - 1, word2, j - 1);
        else
            // 做选择，从两种删除方式中找出删除次数最小的一种
            return Math.min(dp(word1, i - 1, word2, j),
                    dp(word1, i, word2, j - 1))
                    + 1; // 不管哪一种，都要删除一个字符，所以不要忘了加 1
    }

    @Test
    public void testMinDistance() {
        test(this::minDistance);
    }


    /**
     * DP table 迭代方法。
     *
     * LeetCode 耗时：7 ms - 84.20%
     *          内存消耗：39.5 MB - 75.47%
     */
    public int dpTableMethod(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        // 一定要注意 base case
        for (int k = 1; k <= m; k++)
            dp[k][0] = k;
        for (int k = 1; k <= n; k++)
            dp[0][k] = k;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + 1;
            }
        }

        return dp[m][n];
    }

    @Test
    public void testDpTableMethod() {
        test(this::dpTableMethod);
    }
}
