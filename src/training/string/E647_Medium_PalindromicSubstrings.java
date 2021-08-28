package training.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

/**
 * 647. 回文子串: https://leetcode-cn.com/problems/palindromic-substrings/
 *
 * 给定一个字符串，你的任务是计算这个字符串中有多少个回文子串。
 * 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被视作不同的子串。
 *
 * 例 1：
 * 输入："abc"
 * 输出：3
 * 解释：三个回文子串: "a", "b", "c"
 *
 * 例 2：
 * 输入："aaa"
 * 输出：6
 * 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
 *
 * 约束：
 * - 输入的字符串长度不会超过 1000 。
 */
public class E647_Medium_PalindromicSubstrings {

    static void test(ToIntFunction<String> method) {
        Assertions.assertEquals(3, method.applyAsInt("abc"));
        Assertions.assertEquals(6, method.applyAsInt("aaa"));
        Assertions.assertEquals(15, method.applyAsInt("aaaaa"));
        Assertions.assertEquals(4, method.applyAsInt("aba"));
    }

    /**
     * LeetCode 耗时：8 ms - 49.31%
     *          内存消耗：38.4 MB - 41.02%
     */
    public int countSubstrings(String s) {
        int n = s.length();
        // dp[i][j] 表示 s[i..j] 是不是回文子串
        boolean[][] dp = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        int result = n;
        for (int i = n - 2; i >= 0; i--) {
            dp[i][i + 1] = s.charAt(i) == s.charAt(i + 1);
            if (dp[i][i + 1]) {
                result++;
            }
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = dp[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
                if (dp[i][j]) {
                    result++;
                }
            }
        }

        return result;
    }

    @Test
    public void testCountSubstrings() {
        test(this::countSubstrings);
    }


    /**
     * LeetCode 耗时：5 ms - 57.39%
     *          内存消耗：36.6 MB - 69.62%
     */
    public int compressMethod(String s) {
        int n = s.length();
        boolean[] dp = new boolean[n];

        int result = n;
        for (int i = n - 2; i >= 0; i--) {
            // 初始化
            dp[i] = true;
            // 以为 dp[i][j] 依赖左下角，所以需要倒着遍历
            for (int j = n - 1; j >= i + 2; j--) {
                dp[j] = dp[j - 1] && s.charAt(i) == s.charAt(j);
                if (dp[j]) {
                    result++;
                }
            }
            // 注意，这里也需要倒过来放在下面
            dp[i + 1] = s.charAt(i) == s.charAt(i + 1);
            if (dp[i + 1]) {
                result++;
            }
        }

        return result;
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }


    /**
     * 中心扩散法。参见：
     * https://leetcode-cn.com/problems/palindromic-substrings/solution/liang-dao-hui-wen-zi-chuan-de-jie-fa-xiang-jie-zho/
     *
     * LeetCode 耗时：4 ms - 68.59%
     *          内存消耗：36.4 MB - 84.90%
     */
    public int centerSpreadMethod(String s) {
        int result = 0;
        // 中心点有 2 * len - 1 个，分别是 len 个单字符和 len - 1 个双字符
        for (int center = 0; center < 2 * s.length() + 1; center++) {
            int left = center / 2, right = left + center % 2;

            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                result++;
                left--;
                right++;
            }
        }

        return result;
    }

    @Test
    public void testCenterSpreadMethod() {
        test(this::centerSpreadMethod);
    }
}
