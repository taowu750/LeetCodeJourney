package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给你一个字符串 s ，每一次操作你都可以在字符串的任意位置「插入任意字符」。
 * 请你返回让 s 成为回文串的「最少操作次数」。
 *
 * 「回文串」是正读和反读都相同的字符串。
 *
 * 例 1：
 * 输入：s = "zzazz"
 * 输出：0
 * 解释：字符串 "zzazz" 已经是回文串了，所以不需要做任何插入操作。
 *
 * 例 2：
 * 输入：s = "mbadm"
 * 输出：2
 * 解释：字符串可变为 "mbdadbm" 或者 "mdbabdm" 。
 *
 * 例 3：
 * 输入：s = "leetcode"
 * 输出：5
 * 解释：插入 5 个字符后字符串变为 "leetcodocteel"。
 *
 * 例 4：
 * 输入：s = "g"
 * 输出：0
 *
 * 例 5：
 * 输入：s = "no"
 * 输出：1
 *
 * 约束：
 * - 1 <= s.length <= 500
 * - s 中所有字符都是小写字母。
 */
public class E1312_Hard_MinimumInsertionStepsToMakeStringPalindrome {

    static void test(ToIntFunction<String> method) {
        assertEquals(method.applyAsInt("zzazz"), 0);
        assertEquals(method.applyAsInt("leetcode"), 5);
        assertEquals(method.applyAsInt("mbadm"), 2);
        assertEquals(method.applyAsInt("g"), 0);
        assertEquals(method.applyAsInt("no"), 1);
    }

    public int minInsertions(String s) {
        /*
        i 从头开始，j 从尾开始。
        s[i]==s[j]: i++, j--
        s[i]!=s[j]:
            1. 两个都插：
                - i 前面插 s[j], j 前面插 s[i]
                - i 后面插 s[j], j 后面插 s[i]
                其实这两个是一样的
            2. 只插一个：
                - i 前面插 s[j]
                - j 后面插 s[i]
         */
        return minInsertions(s, 0, s.length() - 1);
    }

    private int minInsertions(String s, int i, int j) {
        if (i >= j)
            return 0;

        if (s.charAt(i) == s.charAt(j))
            return minInsertions(s, i + 1, j - 1);
        else
            return Math.min(
                    2 + minInsertions(s, i + 1, j - 1),     // 两个都插
                    1 + Math.min(minInsertions(s, i, j - 1),  // i 前面插 s[j]
                            minInsertions(s, i + 1, j)));     // j 后面插 s[i]
    }

    @Test
    public void testMinInsertions() {
        test(this::minInsertions);
    }


    /**
     * LeetCode 耗时：25 ms - 14.89%
     *          内存消耗：39.7 MB - 49.59%
     */
    public int dpMethod(String s) {
        final int n = s.length();
        final int[][] dp = new int[n][n];

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j))
                    dp[i][j] = dp[i + 1][j - 1];
                else
                    dp[i][j] = Math.min(
                            2 + dp[i + 1][j - 1],
                            1 + Math.min(dp[i][j - 1], dp[i + 1][j]));
            }
        }

        return dp[0][n - 1];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：18 ms - 71.98%
     *          内存消耗：36.6 MB - 87.11%
     */
    public int compressMethod(String s) {
        final int n = s.length();
        final int[] dp = new int[n];

        for (int i = n - 2; i >= 0; i--) {
            int pre = 0;
            for (int j = i + 1; j < n; j++) {
                int tmp = dp[j];
                if (s.charAt(i) == s.charAt(j))
                    dp[j] = pre;
                else
                    dp[j] = Math.min(
                            2 + dp[j - 1],
                            1 + Math.min(dp[j - 1], dp[j]));
                pre = tmp;
            }
        }

        return dp[n - 1];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
