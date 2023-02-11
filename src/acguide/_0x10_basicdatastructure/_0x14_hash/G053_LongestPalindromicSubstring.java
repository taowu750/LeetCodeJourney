package acguide._0x10_basicdatastructure._0x14_hash;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 回文子串的最大长度: https://www.acwing.com/problem/content/141/
 *
 * 如果一个字符串正着读和倒着读是一样的，则称它是回文的。
 * 给定一个长度为 N 的字符串 S，求他的最长回文子串的长度是多少。
 *
 * 输入格式:
 * - 输入将包含最多 30 个测试用例，每个测试用例占一行，以最多 1000000 个小写字符的形式给出。
 * - 输入以一个以字符串 END 开头的行表示输入终止。
 *
 * 输出格式:
 * - 对于输入中的每个测试用例，输出测试用例编号和最大回文子串的长度（参考样例格式）。
 * - 每个输出占一行。
 *
 *
 * 例 1：
 * 输入：
 * abcbabcbabcba
 * abacacbaaaab
 * END
 * 输出：
 * Case 1: 13
 * Case 2: 6
 */
public class G053_LongestPalindromicSubstring {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x10_basicdatastructure/_0x14_hash/data/G053_input.txt",
                "acguide/_0x10_basicdatastructure/_0x14_hash/data/G053_output.txt");
    }

    public static class HashStr {

        private final long[][] hashes;
        private final long[][] reverseHashes;
        private final long[][] basePow;

        public HashStr(char[] s, int[] primes) {
            final int n = s.length;
            hashes = new long[primes.length][n+1];
            reverseHashes = new long[primes.length][n+1];
            basePow = new long[primes.length][n+1];
            for (int i = 0; i < primes.length; i++) {
                int p = primes[i];
                basePow[i][0] = 1;
                for (int j = 0; j < n; j++) {
                    hashes[i][j+1] = hashes[i][j] * p + s[j] - 'a';
                    basePow[i][j+1] = basePow[i][j] * p;
                }
                // reverseHashes[i][j] 表示 s[j..n) 的反向哈希值
                for (int j = n - 1; j >= 0; j--) {
                    reverseHashes[i][j] = reverseHashes[i][j+1] * p + s[j] - 'a';
                }
            }
        }

        public int maxPalindromeSize() {
            // abacacbaaaab
            final int n = hashes[0].length - 1;
            int ans = 0;
            for (int i = 1; i <= n; i++) {
                // 二分法求出中心点为 i 时，最长的奇回文串长度
                int lo = 0, hi = Math.min(i - 1, n - i);
                while (lo < hi) {
                    int mi = (lo + hi + 1) >>> 1;
                    if (isOddPalindrome(i, mi)) {
                        lo = mi;
                    } else {
                        hi = mi - 1;
                    }
                }
                ans = Math.max(ans, 2 * lo + 1);

                // 二分法求出中心点为 i 时，最长的偶回文串长度
                lo = 0;
                hi = Math.min(i - 1, n - i + 1);
                while (lo < hi) {
                    int mi = (lo + hi + 1) >>> 1;
                    if (isEvenPalindrome(i, mi)) {
                        lo = mi;
                    } else {
                        hi = mi - 1;
                    }
                }
                ans = Math.max(ans, 2 * lo);
            }

            return ans;
        }

        private boolean isOddPalindrome(int center, int p) {
            for (int i = 0; i < hashes.length; i++) {
                if (hash(i, center - p, center) != reverseHash(i, center, center + p)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isEvenPalindrome(int center, int q) {
            for (int i = 0; i < hashes.length; i++) {
                if (hash(i, center - q, center - 1) != reverseHash(i, center, center + q - 1)) {
                    return false;
                }
            }
            return true;
        }

        private long hash(int i, int l, int r) {
            return hashes[i][r] - hashes[i][l-1] * basePow[i][r - l + 1];
        }

        private long reverseHash(int i, int l, int r) {
            return reverseHashes[i][l-1] - reverseHashes[i][r] * basePow[i][r - l + 1];
        }
    }

    public void longestPalindrome() {
        Scanner in = new Scanner(System.in);
        int[] primes = {31};
        int i = 1;
        for (String s = in.nextLine(); !s.equals("END"); s = in.nextLine()) {
            HashStr hash = new HashStr(s.toCharArray(), primes);
            System.out.println("Case " + i++ + ": " + hash.maxPalindromeSize());
        }
    }

    @Test
    public void testLongestPalindrome() {
        test(this::longestPalindrome);
    }
}
