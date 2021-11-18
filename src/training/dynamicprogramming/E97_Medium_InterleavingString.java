package training.dynamicprogramming;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriPredicate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 97. 交错字符串: https://leetcode-cn.com/problems/interleaving-string/
 *
 * 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 「交错」组成的。
 *
 * 两个字符串 s 和 t 「交错」的定义与过程如下，其中每个字符串都会被分割成若「非空」子字符串：
 * - s = s1 + s2 + ... + sn
 * - t = t1 + t2 + ... + tm
 * - |n - m| <= 1
 * - 「交错」是 s1 + t1 + s2 + t2 + s3 + t3 + ... 或者 t1 + s1 + t2 + s2 + t3 + s3 + ...
 *
 * 例 1：
 * 输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
 * 输出：true
 *
 * 例 2：
 * 输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
 * 输出：false
 *
 * 例 3：
 * 输入：s1 = "", s2 = "", s3 = ""
 * 输出：true
 *
 * 说明：
 * - 0 <= s1.length, s2.length <= 100
 * - 0 <= s3.length <= 200
 * - s1、s2、和 s3 都由小写英文字母组成
 */
public class E97_Medium_InterleavingString {

    public static void test(TriPredicate<String, String, String> method) {
        assertTrue(method.test("aabcc", "dbbca", "aadbbcbcac"));
        assertFalse(method.test("aabcc", "dbbca", "aadbbbaccc"));
        assertTrue(method.test("", "", ""));
        assertTrue(method.test("aabcc", "dbbca", "aadbcbbcac"));
        assertFalse(method.test("a", "", "c"));
        assertFalse(method.test("db", "b", "cbb"));
    }

    /**
     * 记忆化搜索。
     *
     * LeetCode 耗时：10 ms - 5.57%
     *          内存消耗：39.3 MB - 5.02%
     */
    public boolean isInterleave(String s1, String s2, String s3) {
        return isInterleave(s1, s2, s3, 0, 0, 0, 0, new HashMap<>());
    }

    public static class Tuple {
        int i1, i2;

        public Tuple(int i1, int i2) {
            this.i1 = i1;
            this.i2 = i2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tuple)) return false;
            Tuple tuple = (Tuple) o;
            return i1 == tuple.i1 && i2 == tuple.i2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i1, i2);
        }
    }

    /**
     * lastUseWhich 表示上一次使用 s1 还是 s2 匹配的 s3。
     * - 为 0 表示上一次没有进行匹配；
     * - 为 1 表示上一次使用 s1 进行的匹配；
     * - 为 2 表示上一次使用 s2 进行的匹配。
     */
    private boolean isInterleave(String s1, String s2, String s3, int i1, int i2, int i3, int lastUseWhich,
                                 Map<Tuple, Boolean> cache) {
        if (s3.length() - i3 != s1.length() - i1 + s2.length() - i2) {
            return false;
        } else if (i1 == s1.length()) {
            int size = matchSize(s2, i2, s3, i3);
            return size == s2.length() - i2 && size == s3.length() - i3;
        } else if (i2 == s2.length()) {
            int size = matchSize(s1, i1, s3, i3);
            return size == s1.length() - i1 && size == s3.length() - i3;
        } else if (i3 == s3.length()) {
            return false;
        }

        Tuple cur = new Tuple(i1, i2);
        if (cache.containsKey(cur)) {
            return cache.get(cur);
        }

        boolean result = false;
        switch (lastUseWhich) {
            case 0:
                // 需要匹配所有可能的长度
                for (int size = matchSize(s1, i1, s3, i3); size > 0; size--) {
                    if (isInterleave(s1, s2, s3, i1 + size, i2, i3 + size, 1, cache)) {
                        result = true;
                        break;
                    }
                }
                for (int size = matchSize(s2, i2, s3, i3); size > 0; size--) {
                    if (isInterleave(s1, s2, s3, i1, i2 + size, i3 + size, 2, cache)) {
                        result = true;
                        break;
                    }
                }
                break;

            case 1:
                for (int size = matchSize(s2, i2, s3, i3); size > 0; size--) {
                    if (isInterleave(s1, s2, s3, i1, i2 + size, i3 + size, 2, cache)) {
                        result = true;
                        break;
                    }
                }
                break;

            case 2:
                for (int size = matchSize(s1, i1, s3, i3); size > 0; size--) {
                    if (isInterleave(s1, s2, s3, i1 + size, i2, i3 + size, 1, cache)) {
                        result = true;
                        break;
                    }
                }
                break;
        }

        cache.put(cur, result);
        return result;
    }

    private int matchSize(String s, int i, String t, int j) {
        int size = 0;
        for (; i + size < s.length() && j + size < t.length(); size++) {
            if (s.charAt(i + size) != t.charAt(j + size)) {
                break;
            }
        }

        return size;
    }

    @Test
    public void testIsInterleave() {
        test(this::isInterleave);
    }


    /**
     * 参见：
     * https://leetcode-cn.com/problems/interleaving-string/solution/jiao-cuo-zi-fu-chuan-by-leetcode-solution/
     *
     * 首先如果 len(s1) + len(s2) != len(s3)，那 s3 必然不可能由 s1 和 s2 交错组成。
     * 我们定义 f(i,j) 表示 s1 的前 i 个元素和 s2 的前 j 个元素是否能交错组成 s3 的前 i+j 个元素。
     *
     * 如果s1的前i个字符和s2的前j个字符，能够交替拼出s3的前i+j个字符的话，那么s3的下一个字符随便从s1还是s2拿都是有可能的。
     * 不用担心这样会出现 |n - m| > 1 的情况，因为新添加的字符可以接在上一个交错子串的后面。
     *
     * 于是就有两种情况：
     * - 如果 s1 的第 i 个元素和 s3 的第 i+j 个元素相等，那么能否匹配成功取决于 s1 的前 i-1 个元素和
     *   s2 的前 j 个元素是否能交错组成 s3 的前 i+j-1 个元素，即此时 f(i,j) 取决于 f(i-1,j)。
     * - 如果 s2 的第 j 个元素和 s3 的第 i+j 个元素相等，那么能否匹配成功取决于 s2 的前 j-1 个元素和
     *   s1 的前 i 个元素是否能交错组成 s3 的前 i+j-1 个元素，即此时 f(i,j) 取决于 f(i,j-1)。
     *
     * LeetCode 耗时：8 ms - 15.48%
     *          内存消耗：36.8 MB - 31.40%
     */
    public boolean dpMethod(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) {
            return false;
        }

        // dp[i][j] 表示 s1 前 i 个字符和 s2 前 j 个字符是否和 s3 前 i + j 个字符相等
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 1; i <= m; i++) {
            dp[i][0] = dp[i - 1][0] && s1.charAt(i - 1) == s3.charAt(i - 1);
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = dp[0][j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int p = i + j - 1;
                dp[i][j] = (s1.charAt(i - 1) == s3.charAt(p) && dp[i - 1][j])
                        || (s2.charAt(j - 1) == s3.charAt(p) && dp[i][j - 1]);
            }
        }

        return dp[m][n];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：8 ms - 15.48%
     *          内存消耗：36.5 MB - 85.49%
     */
    public boolean compressMethod(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) {
            return false;
        }

        // dp[i][j] 表示 s1 前 i 个字符和 s2 前 j 个字符是否和 s3 前 i + j 个字符相等
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int j = 1; j <= n; j++) {
            dp[j] = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            // 别忘了初始化
            dp[0] = dp[0] && s1.charAt(i - 1) == s3.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                int p = i + j - 1;
                dp[j] = (s1.charAt(i - 1) == s3.charAt(p) && dp[j])
                        || (s2.charAt(j - 1) == s3.charAt(p) && dp[j - 1]);
            }
        }

        return dp[n];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
