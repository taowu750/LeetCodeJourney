package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 115. 不同的子序列: https://leetcode-cn.com/problems/distinct-subsequences/
 * <p>
 * 给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。
 * <p>
 * 字符串的一个子序列是指，通过删除一些（也可以不删除）字符且不干扰剩余字符相对位置所组成的新字符串。
 * （例如，"ACE" 是 "ABCDE" 的一个子序列，而 "AEC" 不是）
 * <p>
 * 题目数据保证答案符合 32 位带符号整数范围。
 * <p>
 * 例 1：
 * 输入：s = "rabbbit", t = "rabbit"
 * 输出：3
 * <p>
 * 例 2：
 * 输入：s = "babgbag", t = "bag"
 * 输出：5
 * <p>
 * 说明：
 * - 0 <= s.length, t.length <= 1000
 * - s 和 t 由英文字母组成
 */
public class E115_Hard_DistinctSubsequences {

    public static void test(ToIntBiFunction<String, String> method) {
        assertEquals(3, method.applyAsInt("rabbbit", "rabbit"));
        assertEquals(5, method.applyAsInt("babgbag", "bag"));
        assertEquals(0, method.applyAsInt("aaabbbbbaaaaa", "bbaab"));
        assertEquals(3, method.applyAsInt("ddd", "dd"));
        assertEquals(8556153,
                method.applyAsInt("daacaedaceacabbaabdccdaaeaebacddadcaeaacadbceaecddecdeedcebcdacdaebccdeebcbdeacc" +
                                "abcecbeeaadbccbaeccbbdaeadecabbbedceaddcdeabbcdaeadcddedddcececbeeabcbecaeadddeddccb" +
                                "dbcdcbceabcacddbbcedebbcaccac",
                        "ceadbaa"));
    }

    /**
     * 超时。
     */
    public int numDistinct(String s, String t) {
        if (s.length() < t.length()) {
            return 0;
        } else if (s.length() == t.length()) {
            return s.equals(t) ? 1 : 0;
        }

        // 保存 t 中每个字符的下标
        Set<Integer>[] tIndices = new Set[128];
        for (int i = 0; i < tIndices.length; i++) {
            tIndices[i] = new HashSet<>();
        }
        for (int i = 0; i < t.length(); i++) {
            tIndices[t.charAt(i)].add(i);
        }

        // dp[i] 表示 i 这个位置有多少匹配 t 前缀的子序列的长度
        // 注意，下面是压缩后的 dp，否则会内存溢出
        List<Integer> dp = new ArrayList<>();
        dp.add(0);
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            // 如果 s[i] 在 t 中存在
            if (!tIndices[s.charAt(i)].isEmpty()) {
                // 上一次匹配（dp[i-1]）依然保存，表示不使用 s[i] 匹配 t 的情况
                int size = dp.size();
                // 下面使用之前已匹配的长度，当 s[i] 可以接在已匹配的子序列后面时，添加到结果中
                for (int j = 0; j < size; j++) {
                    int len = dp.get(j);
                    if (tIndices[s.charAt(i)].contains(len)) {
                        dp.add(len + 1);
                        // 注意，要在 len 改变的时候判断长度，避免多次判断
                        if (len + 1 == t.length()) {
                            result++;
                        }
                    }
                }
            }
        }

        return result;
    }

    @Test
    public void testNumDistinct() {
        test(this::numDistinct);
    }


    /**
     * 使用 TreeMap 改进上面的方法。
     *
     * LeetCode 耗时：14 ms - 40.11%
     *          内存消耗：38.7 MB - 91.34%
     */
    public int improveMethod(String s, String t) {
        if (s.length() < t.length()) {
            return 0;
        } else if (s.length() == t.length()) {
            return s.equals(t) ? 1 : 0;
        }

        // 保存 t 中每个字符的下标
        Set<Integer>[] tIndices = new Set[128];
        for (int i = 0; i < tIndices.length; i++) {
            tIndices[i] = new HashSet<>();
        }
        for (int i = 0; i < t.length(); i++) {
            tIndices[t.charAt(i)].add(i);
        }

        // 使用 TreeMap 在程序运行期间动态删除不可能满足的长度
        NavigableMap<Integer, Integer> dp = new TreeMap<>();
        dp.put(0, 1);
        for (int i = 0; i < s.length(); i++) {
            // 如果 s[i] 在 t 中存在
            if (!tIndices[s.charAt(i)].isEmpty()) {
                /*
                s.length() - i 表示还有多少字符能用来匹配
                t.length() - (s.length() - i) 表示最少必须已匹配的字符个数
                */
                dp = dp.tailMap(t.length() - (s.length() - i), true);
                // 注意，一定要是 descendingMap，因为长度小的计数会加到大的上面，所有要先计算大的，避免重复计数
                List<Map.Entry<Integer, Integer>> matched = new ArrayList<>(dp.descendingMap().entrySet());
                for (Map.Entry<Integer, Integer> entry : matched) {
                    int len = entry.getKey();
                    if (tIndices[s.charAt(i)].contains(len)) {
                        dp.merge(len + 1, entry.getValue(), Integer::sum);
                    }
                }
            }
        }

        return dp.getOrDefault(t.length(), 0);
    }

    @Test
    public void testImproveMethod() {
        test(this::improveMethod);
    }


    /**
     * LeetCode 耗时：7 ms - 90.69%
     *          内存消耗：42.3 MB - 72.53%
     */
    public int dpMethod(String s, String t) {
        int m = s.length(), n = t.length();
        if (m < n) {
            return 0;
        } else if (m == n) {
            return s.equals(t) ? 1 : 0;
        }

        // dp[i][j] 表示 s[..i] 匹配 t[..j] 的子序列个数
        int[][] dp = new int[m][n];
        dp[0][0] = s.charAt(0) == t.charAt(0) ? 1 : 0;
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0];
            if (s.charAt(i) == t.charAt(0)) {
                dp[i][0] += 1;
            }
        }

        for (int i = 1; i < m; i++) {
            int size = Math.min(i + 1, n);
            for (int j = 1; j < size; j++) {
                dp[i][j] = dp[i - 1][j];
                if (s.charAt(i) == t.charAt(j)) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }

        return dp[m - 1][n - 1];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：5 ms - 96.83%
     *          内存消耗：36.5 MB - 94.64%
     */
    public int compressMethod(String s, String t) {
        int m = s.length(), n = t.length();
        if (m < n) {
            return 0;
        } else if (m == n) {
            return s.equals(t) ? 1 : 0;
        }

        int[] dp = new int[n];
        dp[0] = s.charAt(0) == t.charAt(0) ? 1 : 0;
        for (int i = 1; i < m; i++) {
            int size = Math.min(i + 1, n);
            for (int j = size - 1; j >= 1; j--) {
                if (s.charAt(i) == t.charAt(j)) {
                    dp[j] += dp[j - 1];
                }
            }
            // 注意，遍历顺序反了，初始化也需要反过来
            if (s.charAt(i) == t.charAt(0)) {
                dp[0] += 1;
            }
        }

        return dp[n - 1];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
