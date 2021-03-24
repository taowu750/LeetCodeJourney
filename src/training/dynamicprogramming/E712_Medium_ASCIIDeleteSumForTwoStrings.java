package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定两个字符串 s1、s2，求删除字符的最小 ASCII 和，使两个字符串相等。
 *
 * 例 1：
 * Input: s1 = "sea", s2 = "eat"
 * Output: 231
 * Explanation:
 * 从“sea”删除“s”将ASCII值“s”（115）添加到总和。
 * 从“eat”删除“t”将向总和添加116。
 * 最后，两个字符串都相等，115+116=231是实现这一目标的最小和。
 *
 * 例 2：
 * Input: s1 = "delete", s2 = "leet"
 * Output: 403
 * Explanation:
 * 从“delete”中删除“dee”，将字符串变成“let”，加上100[d]+101[e]+101[e]。
 * 从“leet”中删除“e”将使总和增加101[e]。
 * 最后，两个字符串都等于“let”，答案是100+101+101+101=403。
 * 相反，如果我们把两个字符串都转换成“lee”或“eet”，我们将得到433或417的答案，它们更高。
 *
 * 注意：
 * - 0 < s1.length, s2.length <= 1000.
 * - 字符串中的字符 ASCII 码范围为 [97, 122].
 */
public class E712_Medium_ASCIIDeleteSumForTwoStrings {

    static void test(ToIntBiFunction<String, String> method) {
        assertEquals(method.applyAsInt("sea", "eat"), 231);
        assertEquals(method.applyAsInt("delete", "leet"), 403);
    }

    /**
     * 此题思路与{@link E1143_Medium_LongestCommonSubsequence}相同。
     *
     * LeetCode 耗时：18 ms - 74.60%
     *          内存消耗：39.9 MB - 33.91%
     */
    public int minimumDeleteSum(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        // dp[i][j] 表示 s1[0..i] 和 s2[0..j] 的删除字符最小 ASCII 和。
        // 同时 s1[0..i] 和 s2[0..j] 也需要是一个公共序列。
        final int[][] dp = new int[m + 1][n + 1];

        // base case
        for (int k = 1; k <= m; k++)
            dp[k][0] = dp[k - 1][0] + s1.charAt(k - 1);
        for (int k = 1; k <= n; k++)
            dp[0][k] = dp[0][k - 1] + s2.charAt(k - 1);

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 当 s1[i-1] == s2[j-1] 时，不需要删除其中任何字符就能保证 s1[0..i-1] == s2[0..j-1]
                if (s1.charAt(i - 1) == s2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = Math.min(dp[i - 1][j] + s1.charAt(i - 1),
                            dp[i][j - 1] + s2.charAt(j - 1));
            }
        }

        return dp[m][n];
    }

    @Test
    public void testMinimumDeleteSum() {
        test(this::minimumDeleteSum);
    }


    /**
     * 压缩思路和{@link E72_Hard_EditDistance}一样。
     *
     * LeetCode 耗时：15 ms - 87.45%
     *          内存消耗：37.3 MB - 98.56%
     */
    public int compressMethod(String s1, String s2) {
        final int m = s1.length(), n = s2.length();
        final int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++)
            dp[i] = dp[i - 1] + s2.charAt(i - 1);

        int s1Sum = 0;
        for (int i = 1; i <= m; i++) {
            s1Sum += s1.charAt(i - 1);
            int prevCol = s1Sum;
            for (int j = 1; j <= n; j++) {
                int cur;
                if (s1.charAt(i - 1) == s2.charAt(j - 1))
                    cur = dp[j - 1];
                else
                    cur = Math.min(dp[j] + s1.charAt(i - 1),
                            prevCol + s2.charAt(j - 1));
                dp[j - 1] = prevCol;
                prevCol = cur;
            }
            dp[n] = prevCol;
        }

        return dp[n];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
