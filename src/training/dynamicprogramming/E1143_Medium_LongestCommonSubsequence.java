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
        assertEquals(method.applyAsInt("abcba", "abcbcba"), 5);
        assertEquals(method.applyAsInt("bsbininm", "jmjkbkjkv"), 1);
        assertEquals(method.applyAsInt("bl", "yby"), 1);
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

        // 最开始理解错了，以为顺序扫描下所需状态没有被计算出来的，所以用了正斜着扫描。
        // 我还写了一个去掉循环中下标判断的方法，但是效果没有提升，应该是虚拟机做了优化。
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
     * LeetCode 耗时：9 ms - 83.27%
     *          内存消耗：42.8 MB - 51.93%
     */
    public int betterMethod(String text1, String text2) {
        final int m = text1.length(), n = text2.length();
        final int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        return dp[m][n];
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }


    /**
     * LeetCode 耗时：8 ms - 93.60%
     *          内存消耗：37 MB - 96.94%
     */
    public int compressedMethod(String text1, String text2) {
        // 我们只需要前一列和当前列的信息就可以更新当前行。
        // 结果实验，发现其实压缩行或压缩列都可以。

        // 保证列的长度大于等于行
        if (text1.length() < text2.length()) {
            String tmp = text1;
            text1 = text2;
            text2 = tmp;
        }

        final int m = text1.length(), n = text2.length();
        final int[] dp = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1, prevRow = 0, prevRowPrevCol; j <= n; j++) {
                // 因为可能用到 [i - 1][j - 1]、[i - 1][j]、[i][j - 1] 的值，为了不让值被覆盖，需要进行记录。
                // prevRow 保存 dp[i - 1][j] 的值；prevRowPrevCol 保存 dp[i - 1][j - 1] 的值

                prevRowPrevCol = prevRow;
                prevRow = dp[j];
                if (text1.charAt(i - 1) == text2.charAt(j - 1))
                    dp[j] = prevRowPrevCol + 1;
                else
                    dp[j] = Math.max(prevRow, dp[j - 1]);
            }
        }

        return dp[n];
    }

    @Test
    public void testCompressedMethod() {
        test(this::compressedMethod);
    }
}
