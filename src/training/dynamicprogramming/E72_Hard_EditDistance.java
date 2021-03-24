package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数 。
 * 你可以对一个单词进行如下三种操作：
 * - 插入一个字符
 * - 删除一个字符
 * - 替换一个字符
 * <p>
 * 例 1：
 * Input：word1 = "horse", word2 = "ros"
 * Output：3
 * Explanation：
 * horse -> rorse (将 'h' 替换为 'r')
 * rorse -> rose (删除 'r')
 * rose -> ros (删除 'e')
 * <p>
 * 例 2：
 * Input：word1 = "intention", word2 = "execution"
 * Output：5
 * Explanation：
 * intention -> inention (删除 't')
 * inention -> enention (将 'i' 替换为 'e')
 * enention -> exention (将 'n' 替换为 'x')
 * exention -> exection (将 'n' 替换为 'c')
 * exection -> execution (插入 'u')
 * <p>
 * 约束：
 * - 0 <= word1.length, word2.length <= 500
 * - word1 和 word2 由小写英文字母组成
 */
public class E72_Hard_EditDistance {

    static void test(ToIntBiFunction<String, String> method) {
        assertEquals(method.applyAsInt("horse", "ros"), 3);
        assertEquals(method.applyAsInt("intention", "execution"), 5);
    }

    /**
     * 此题思路参见{@link E1143_Medium_LongestCommonSubsequence}。
     * <p>
     * LeetCode 耗时：5 ms - 65.18%
     * 内存消耗：39.2 MB - 52.81%
     */
    public int minDistance(String word1, String word2) {
        if (word1.length() == 0)
            return word2.length();
        if (word2.length() == 0)
            return word1.length();

        final int m = word1.length(), n = word2.length();
        // dp[i][j] 表示要使得 word1[0..i] == word2[0..j]，所需要的最小操作次数。
        final int[][] dp = new int[m + 1][n + 1];

        // base case
        for (int k = 1; k <= m; k++)
            dp[k][0] = k;
        for (int k = 1; k <= n; k++)
            dp[0][k] = k;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];            // 相等就不需要操作
                else
                    dp[i][j] = Math.min(dp[i - 1][j - 1],   // 替换
                            Math.min(dp[i - 1][j],          // 删除
                                    dp[i][j - 1]))          // 插入
                            + 1;                            // 加上这一次操作
            }
        }

        return dp[m][n];
    }

    @Test
    public void testMinDistance() {
        test(this::minDistance);
    }
}
