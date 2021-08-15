package training.binarysearch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * 1044. 最长重复子串: https://leetcode-cn.com/problems/longest-duplicate-substring/
 *
 * 给出一个字符串 S，考虑其所有重复子串（S 的连续子串，出现两次或多次，可能会有重叠）。
 * 返回「任何」具有最长可能长度的重复子串。（如果 S 不含重复子串，那么答案为 ""。）
 *
 * 例 1：
 * 输入："banana"
 * 输出："ana"
 *
 * 例 2：
 * 输入："abcd"
 * 输出：""
 *
 * 约束：
 * - 2 <= S.length <= 10^5
 * - S 由小写英文字母组成。
 */
public class E1044_Hard_LongestDuplicateSubstring {

    static void test(UnaryOperator<String> method) {
        Assertions.assertEquals("ana", method.apply("banana"));
        Assertions.assertEquals("", method.apply("abcd"));
        Assertions.assertEquals("a", method.apply("aa"));
    }

    public String longestDupSubstring(String s) {
        int n = s.length();
        // dp[i][j] 表示以 i 开头和以 j 开头的最长重复子串的长度。
        // 其中 i > j，因为不能让它们有一样的位置
        int[][] dp = new int[n + 1][n];

        int start = 0, maxLen = 0;
        // 扫描下三角
        for (int i = n - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (s.charAt(i) == s.charAt(j))
                    dp[i][j] = dp[i + 1][j + 1] + 1;
                if (dp[i][j] > maxLen) {
                    maxLen = dp[i][j];
                    start = j;
                }
            }
        }

        return maxLen > 0 ? s.substring(start, start + maxLen) : "";
    }

    @Test
    public void testLongestDupSubstring() {
        test(this::longestDupSubstring);
    }


    /**
     * 超时。s 长度最大有 10**5。
     */
    public String compressMethod(String s) {
        int n = s.length();
        int[] dp = new int[n];

        int start = 0, maxLen = 0;
        for (int i = n - 1; i > 0; i--) {
            // 左边的状态依赖右边的状态，所以需要从左到右遍历
            for (int j = 0; j <= i - 1; j++) {
                if (s.charAt(i) == s.charAt(j))
                    dp[j] = dp[j + 1] + 1;
                else
                    // 注意重置为 0
                    dp[j] = 0;
                if (dp[j] > maxLen) {
                    maxLen = dp[j];
                    start = j;
                }
            }
        }

        return maxLen > 0 ? s.substring(start, start + maxLen) : "";
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }


    /**
     * 二分查找结合 Rabin-Karp 算法，参见：
     * https://leetcode-cn.com/problems/longest-duplicate-substring/solution/zui-chang-zhong-fu-zi-chuan-by-leetcode/
     *
     * LeetCode 耗时：374 ms - 16.04%
     *          内存消耗：46.6 MB - 52.94%
     */
    public String binaryAndRKMethod(String s) {
        // s 只包含小写字符，将其转化为 0-25 的数字
        int n = s.length();
        int[] sequence = new int[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = s.charAt(i) - 'a';
        }

        long modulus = (long) Integer.MAX_VALUE + 1;
        int lo = 1, hi = n - 1, start = -1, maxLen = 0;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int st = rabinKarpSearch(sequence, mid, 26, modulus);
            if (st != -1) {
                lo = mid + 1;
                if (mid > maxLen) {
                    start = st;
                    maxLen = mid;
                }
            } else
                hi = mid - 1;
        }

        return start != -1 ? s.substring(start, start + maxLen) : "";
    }

    /**
     * Radin-Karp 算法。
     *
     * @param sequence 序列
     * @param searchLen 要查找的子序列长度
     * @param radix 序列的基数
     * @param modulus 模数，限定计算的哈希值的范围，防止其过大溢出
     * @return 重复字符串的开始下标
     */
    private int rabinKarpSearch(int[] sequence, int searchLen, int radix, long modulus) {
        // 先计算出前面 searchLen 的子序列哈希值
        long h = 0;
        for (int i = 0; i < searchLen; i++) {
            h = (h * radix + sequence[i]) % modulus;
        }

        // 计算 radix 的 searchLen 次方
        long radixL = 1;
        for (int i = 0; i < searchLen; i++) {
            radixL = (radixL * radix) % modulus;
        }

        // 保存访问过的哈希值和下标
        Map<Long, Integer> visited = new HashMap<>();
        visited.put(h, 0);
        int n = sequence.length;
        for (int i = 1; i < n - searchLen + 1; i++) {
            h = (h * radix - sequence[i - 1] * radixL % modulus + modulus) % modulus;
            h = (h + sequence[i + searchLen - 1]) % modulus;
            int idx = visited.getOrDefault(h, -1);
            // 哈希值相同还要计算子序列是否相等，因为有哈希碰撞的可能性
            if (idx != -1 && eq(sequence, idx, i, searchLen))
                return i;
            visited.put(h, i);
        }
        return -1;
    }

    private boolean eq(int[] sequence, int i, int j, int len) {
        while (len-- > 0) {
            if (sequence[i++] != sequence[j++])
                return false;
        }

        return true;
    }

    @Test
    public void testBinaryAndRKMethod() {
        test(this::binaryAndRKMethod);
    }
}
