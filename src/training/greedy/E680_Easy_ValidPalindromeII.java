package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 680. 验证回文字符串 Ⅱ: https://leetcode-cn.com/problems/valid-palindrome-ii/
 *
 * 给定一个非空字符串 s，最多删除一个字符。判断是否能成为回文字符串。
 *
 * 例 1：
 * 输入: s = "aba"
 * 输出: true
 *
 * 例 2：
 * 输入: s = "abca"
 * 输出: true
 * 解释: 你可以删除c字符。
 *
 * 例 3：
 * 输入: s = "abc"
 * 输出: false
 *
 * 说明：
 * - 1 <= s.length <= 10^5
 * - s 由小写英文字母组成
 */
public class E680_Easy_ValidPalindromeII {

    public static void test(Predicate<String> method) {
        assertTrue(method.test("aba"));
        assertTrue(method.test("abca"));
        assertFalse(method.test("abc"));
        assertFalse(method.test("eeccccbebaeeabebccceea"));
        assertTrue(method.test("ac"));
    }

    /**
     * 超出内存限制。
     */
    public boolean validPalindrome(String s) {
        int n = s.length();
        // dp[i][j] 表示 [i, j] 是否合法
        boolean[][] dp = new boolean[n][n];
        // chance[i][j] 表示当前是否能删除一个字符，false 表示可以
        boolean[][] chance = new boolean[n][n];
        dp[0][0] = true;
        for (int i = 1; i < n; i++) {
            dp[i][i] = dp[i][i - 1] = true;
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1];
                    chance[i][j] = chance[i + 1][j - 1];
                } else {
                    if ((dp[i + 1][j] && !chance[i + 1][j])
                            || (dp[i][j - 1] && !chance[i][j - 1])) {
                        dp[i][j] = true;
                        chance[i][j] = true;
                    }
                }
            }
        }

        return dp[0][n - 1];
    }

    @Test
    public void testValidPalindrome() {
        test(this::validPalindrome);
    }


    /**
     * 超出时间限制。
     */
    public boolean compressMethod(String s) {
        int n = s.length();
        boolean[] dp = new boolean[n];
        boolean[] chance = new boolean[n];
        dp[n - 1] = true;

        for (int i = n - 2; i >= 0; i--) {
            dp[i] = true;
            // 左下
            boolean prevDp = true, prevChance = chance[i];
            for (int j = i + 1; j < n; j++) {
                boolean tmpDp = dp[j], tmpChance = chance[j];
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = prevDp;
                    chance[j] = prevChance;
                } else {
                    if ((dp[j] && !chance[j])
                            || (dp[j - 1] && !chance[j - 1])) {
                        dp[j] = true;
                        chance[j] = true;
                    } else {
                        dp[j] = false;
                    }
                }
                prevDp = tmpDp;
                prevChance = tmpChance;
            }
        }

        return dp[n - 1];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }


    /**
     * 贪心法。先从两端开始判断，遇到不相等的字符，“删除”其中一个，如果两种情况下一种是回文串，则返回 true。
     * 参见：https://leetcode-cn.com/problems/valid-palindrome-ii/solution/yan-zheng-hui-wen-zi-fu-chuan-ii-by-leetcode-solut/
     *
     * LeetCode 耗时：6 ms - 75.28%
     *          内存消耗：39.1 MB - 20.84%
     */
    public boolean greedyMethod(String s) {
        for (int i = 0, j = s.length() - 1; i < j;) {
            if (s.charAt(i) == s.charAt(j)) {
                i++;
                j--;
            } else {
                return isPalindrome(s, i + 1, j) || isPalindrome(s, i, j - 1);
            }
        }

        return true;
    }

    private boolean isPalindrome(String s, int lo, int hi) {
        while (lo < hi) {
            if (s.charAt(lo) != s.charAt(hi)) {
                return false;
            }
            lo++;
            hi--;
        }
        return true;
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
