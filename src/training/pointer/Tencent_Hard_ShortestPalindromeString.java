package training.pointer;

import org.junit.jupiter.api.Test;
import util.Util;

import java.util.function.BinaryOperator;

/**
 * 腾讯笔试题：最短回文串。
 *
 * 给定一个字符串，要求插入字符将其变为回文串，最后返回这个回文串。
 * 除了字符串参数 str 外，我们还会提供一个参数 strips，它是 str 的最长回文子序列。
 * 请使用 strips 以得到比一般方法更小的时间复杂度。
 */
public class Tencent_Hard_ShortestPalindromeString {

    public static void test(BinaryOperator<String> method) {
        Util.assertIn(method.apply("A1B21C", "121"), "AC1B2B1CA", "CA1B2B1AC");
    }

    /**
     * 动态规划法，参见 {@link training.dynamicprogramming.E1312_Hard_MinimumInsertionStepsToMakeStringPalindrome}。
     */
    public String sps(String s, String sips) {
        int n = s.length();
        StringBuilder empty = new StringBuilder();
        StringBuilder[][] dp = new StringBuilder[n][n];
        dp[0][0] = new StringBuilder(1).append(s.charAt(0));
        for (int i = 1; i < n; i++) {
            dp[i][i] = new StringBuilder(1).append(s.charAt(i));
            dp[i][i - 1] = empty;
        }

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = new StringBuilder(dp[i + 1][j - 1].length() + 2);
                    dp[i][j].append(s.charAt(i)).append(dp[i + 1][j - 1]).append(s.charAt(i));
                } else {
                    int len = Math.min(dp[i + 1][j - 1].length() + 4,
                            Math.min(dp[i + 1][j].length(), dp[i][j - 1].length()) + 2);
                    dp[i][j] = new StringBuilder(len);
                    if (len == dp[i + 1][j - 1].length() + 4) {
                        dp[i][j].append(s.charAt(i)).append(s.charAt(j))
                                .append(dp[i + 1][j - 1])
                                .append(s.charAt(j)).append(s.charAt(i));
                    } else if (len == dp[i + 1][j].length() + 2) {
                        dp[i][j].append(s.charAt(i)).append(dp[i + 1][j]).append(s.charAt(i));
                    } else {
                        dp[i][j].append(s.charAt(j)).append(dp[i][j - 1]).append(s.charAt(j));
                    }
                }
            }
        }

        return dp[0][n - 1].toString();
    }

    @Test
    public void testSps() {
        test(this::sps);
    }


    public String compressedMethod(String s, String sips) {
        int n = s.length();
        StringBuilder empty = new StringBuilder(1);
        StringBuilder[] dp = new StringBuilder[n];
        dp[n - 2] = empty;
        dp[n - 1] = new StringBuilder(1).append(s.charAt(n - 1));

        for (int i = n - 2; i >= 0; i--) {
            StringBuilder prev = empty;
            dp[i] = new StringBuilder().append(s.charAt(i));
            for (int j = i + 1; j < n; j++) {
                StringBuilder tmp = dp[j];
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = new StringBuilder(prev.length() + 2);
                    dp[j].append(s.charAt(i)).append(prev).append(s.charAt(i));
                } else {
                    int len = Math.min(prev.length() + 4,
                            Math.min(dp[j].length(),
                                    dp[j - 1].length()) + 2);
                    dp[j] = new StringBuilder(len);
                    if (len == prev.length() + 4) {
                        dp[j].append(s.charAt(i)).append(s.charAt(j))
                                .append(prev)
                                .append(s.charAt(j)).append(s.charAt(i));
                    }
                    // 一定要注意这里是 tmp，而不是 dp[j]！！！
                    else if (len == tmp.length() + 2) {
                        dp[j].append(s.charAt(i)).append(tmp).append(s.charAt(i));
                    } else {
                        dp[j].append(s.charAt(j)).append(dp[j - 1]).append(s.charAt(j));
                    }
                }
                prev = tmp;
            }
        }

        return dp[n - 1].toString();
    }

    @Test
    public void testCompressedMethod() {
        test(this::compressedMethod);
    }


    /**
     * 在 s 中，除了最长回文子序列之外，其余字符都需要再插入一个进行配对。
     * 因此，结果回文字符串 result 的长度 = sips 长度 + (s 长度 - sips 长度) * 2。
     *
     * 我们可以使用三组双指针，分别指向 s 的头尾、sips 的头尾和 result 的头尾。
     * 当 s[left] != sips[left] 时，需要在 result 头尾添加 s[left]；
     * 否则当 s[right] != sips[right] 时，需要在 result 头尾添加 s[right]；
     * 否则，找到了当前 s 和 sips 的回文配对字符，需要在 result 头尾添加。
     */
    public String twoPointerMethod(String s, String sips) {
        int resultLen = sips.length() + (s.length() - sips.length()) * 2;
        StringBuilder result = new StringBuilder(resultLen);
        for (int i = 0; i < resultLen; i++) {
            result.append(' ');
        }

        int resultLeft = 0, resultRight = resultLen - 1;
        int sLeft = 0, sRight = s.length() - 1;
        int sipsLeft = 0, sipsRight = sips.length() - 1;
        while (resultLeft <= resultRight) {
            if (s.charAt(sLeft) != sips.charAt(sipsLeft)) {
                result.setCharAt(resultLeft++, s.charAt(sLeft));
                result.setCharAt(resultRight--, s.charAt(sLeft));
                sLeft++;
            } else if (s.charAt(sRight) != sips.charAt(sipsRight)) {
                result.setCharAt(resultLeft++, s.charAt(sRight));
                result.setCharAt(resultRight--, s.charAt(sRight));
                sRight--;
            } else {
                result.setCharAt(resultLeft++, s.charAt(sLeft));
                result.setCharAt(resultRight--, s.charAt(sLeft));
                sLeft++;
                sRight--;
                sipsLeft++;
                sipsRight--;
            }
        }

        return result.toString();
    }

    @Test
    public void testTwoPointerMethod() {
        test(this::twoPointerMethod);
    }
}
