package training.greedy;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 44. 通配符匹配: https://leetcode-cn.com/problems/wildcard-matching/
 *
 * 给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
 * - '?' 可以匹配任何单个字符。
 * - '*' 可以匹配任意字符串（包括空字符串）。
 *
 * 两个字符串完全匹配才算匹配成功。
 *
 * 例 1：
 * 输入:
 * s = "aa"
 * p = "a"
 * 输出: false
 * 解释: "a" 无法匹配 "aa" 整个字符串。
 *
 * 例 2：
 * 输入:
 * s = "aa"
 * p = "*"
 * 输出: true
 * 解释: '*' 可以匹配任意字符串。
 *
 * 例 3：
 * 输入:
 * s = "cb"
 * p = "?a"
 * 输出: false
 * 解释: '?' 可以匹配 'c', 但第二个 'a' 无法匹配 'b'。
 *
 * 例 4：
 * 输入:
 * s = "adceb"
 * p = "*a*b"
 * 输出: true
 * 解释: 第一个 '*' 可以匹配空字符串, 第二个 '*' 可以匹配字符串 "dce".
 *
 * 例 5：
 * 输入:
 * s = "acdcb"
 * p = "a*c?b"
 * 输出: false
 *
 * 约束：
 * - s 可能为空，且只包含从 a-z 的小写字母。
 * - p 可能为空，且只包含从 a-z 的小写字母，以及字符 ? 和 *。
 */
public class E44_Hard_WildcardMatching {

    static void test(BiPredicate<String, String> method) {
        assertFalse(method.test("aa", "a"));
        assertTrue(method.test("aa", "*"));
        assertFalse(method.test("cb", "?a"));
        assertTrue(method.test("adceb", "*a*b"));
        assertFalse(method.test("acdcb", "a*c?b"));
    }

    static class Tuple {
        int i, j;

        public Tuple(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tuple)) return false;
            Tuple tuple = (Tuple) o;
            return i == tuple.i && j == tuple.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    /**
     * 参见 {@link training.dynamicprogramming.E10_Hard_RegularExpressionMatching}。
     *
     * LeetCode 耗时：591 ms - 5.01%
     *          内存消耗：96.6 MB - 5.01%
     */
    public boolean isMatch(String s, String p) {
        HashMap<Tuple, Boolean> cache = new HashMap<>();
        return match(s, p, 0, 0, cache);
    }

    private boolean match(String s, String p, int si, int pi, HashMap<Tuple, Boolean> cache) {
        Tuple tuple = new Tuple(si, pi);
        if (cache.containsKey(tuple)) {
            return cache.get(tuple);
        }

        if (pi == p.length()) {
            return si == s.length();
        }
        if (si == s.length()) {
            return p.charAt(pi) == '*' && match(s, p, si, pi + 1, cache);
        }

        boolean result;
        if (p.charAt(pi) == '?') {
            result = match(s, p, si + 1, pi + 1, cache);
        } else if (p.charAt(pi) == '*') {
            result = match(s, p, si, pi + 1, cache) || match(s, p, si + 1, pi, cache);
        } else {
            result = s.charAt(si) == p.charAt(pi) && match(s, p, si + 1, pi + 1, cache);
        }
        cache.put(tuple, result);

        return result;
    }

    @Test
    public void testIsMatch() {
        test(this::isMatch);
    }


    /**
     * 参见 https://leetcode-cn.com/problems/wildcard-matching/submissions/
     *
     * LeetCode 耗时：26 ms - 65.90%
     *          内存消耗：38.7 MB - 74.25%
     */
    public boolean dpMethod(String s, String p) {
        // dp[i][j] 表示 s 的前 i 个字符和 p 的前 j 个字符是否匹配
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        // 当 i 为 0 时，p 的前 j 个字符都是 * 则匹配
        for (int j = 1; j <= p.length(); j++) {
            dp[0][j] = dp[0][j - 1] && p.charAt(j - 1) == '*';
        }

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 1; j <= p.length(); j++) {
                char sc = s.charAt(i - 1), pc = p.charAt(j - 1);
                if (pc == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    // 当 pi 等于 * 时，则 dp[i][j] 等于不使用 pi 匹配 s 前 i 个字符的情况、
                    // 或使用 pi 匹配前 i - 1 的情况
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else {
                    dp[i][j] = sc == pc && dp[i - 1][j - 1];
                }
            }
        }

        return dp[s.length()][p.length()];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：22 ms - 74.72%
     *          内存消耗：38.6 MB - 85.55%
     */
    public boolean compressMethod(String s, String p) {
        boolean[] dp = new boolean[p.length() + 1];
        dp[0] = true;
        for (int j = 1; j <= p.length(); j++) {
            dp[j] = dp[j - 1] && p.charAt(j - 1) == '*';
        }

        for (int i = 1; i <= s.length(); i++) {
            boolean prev = dp[0];
            dp[0] = false;
            for (int j = 1; j <= p.length(); j++) {
                boolean tmp = dp[j];
                char sc = s.charAt(i - 1), pc = p.charAt(j - 1);
                if (pc == '?') {
                    dp[j] = prev;
                } else if (pc == '*') {
                    dp[j] = dp[j - 1] || dp[j];
                } else {
                    dp[j] = sc == pc && prev;
                }
                prev = tmp;
            }
        }

        return dp[p.length()];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/wildcard-matching/solution/tong-pei-fu-pi-pei-by-leetcode-solution
     *
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：38.5 MB - 90.76%
     */
    public boolean greedyMethod(String s, String p) {
        int sRight = s.length(), pRight = p.length();
        while (sRight > 0 && pRight > 0 && p.charAt(pRight - 1) != '*') {
            if (charMatch(s.charAt(sRight - 1), p.charAt(pRight - 1))) {
                --sRight;
                --pRight;
            } else {
                return false;
            }
        }

        if (pRight == 0) {
            return sRight == 0;
        }

        int sIndex = 0, pIndex = 0;
        int sRecord = -1, pRecord = -1;

        while (sIndex < sRight && pIndex < pRight) {
            if (p.charAt(pIndex) == '*') {
                ++pIndex;
                sRecord = sIndex;
                pRecord = pIndex;
            } else if (charMatch(s.charAt(sIndex), p.charAt(pIndex))) {
                ++sIndex;
                ++pIndex;
            } else if (sRecord != -1 && sRecord + 1 < sRight) {
                ++sRecord;
                sIndex = sRecord;
                pIndex = pRecord;
            } else {
                return false;
            }
        }

        return allStars(p, pIndex, pRight);
    }

    public boolean allStars(String str, int left, int right) {
        for (int i = left; i < right; ++i) {
            if (str.charAt(i) != '*') {
                return false;
            }
        }
        return true;
    }

    public boolean charMatch(char u, char v) {
        return u == v || v == '?';
    }

    @Test
    public void testGreedyMethod() {
        test(this::greedyMethod);
    }
}
