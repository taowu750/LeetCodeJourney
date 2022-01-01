package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 214. 最短回文串: https://leetcode-cn.com/problems/shortest-palindrome/
 *
 * 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串。
 *
 * 例 1：
 * 输入：s = "aacecaaa"
 * 输出："aaacecaaa"
 *
 * 例 2：
 * 输入：s = "abcd"
 * 输出："dcbabcd"
 *
 * 说明：
 * - 0 <= s.length <= 5 * 10^4
 * - s 仅由小写英文字母组成
 */
public class E214_Hard_ShortestPalindrome {

    public static void test(UnaryOperator<String> method) {
        assertEquals("aaacecaaa", method.apply("aacecaaa"));
        assertEquals("dcbabcd", method.apply("abcd"));
        assertEquals("", method.apply(""));
        assertEquals("a", method.apply("a"));
    }

    /**
     * LeetCode 耗时：655 ms - 5.10%
     *          内存消耗：38.1 MB - 97.25%
     */
    public String shortestPalindrome(String s) {
        // 找到 s 最长的前缀回文字符串
        int prefixPalindromeLength = s.length();
        while (!isPalindrome(s, prefixPalindromeLength)) {
            prefixPalindromeLength--;
        }

        StringBuilder result = new StringBuilder(prefixPalindromeLength + (s.length() - prefixPalindromeLength) * 2);
        // 将剩余的字符补齐
        for (int i = s.length() - 1; i >= prefixPalindromeLength; i--) {
            result.append(s.charAt(i));
        }
        result.append(s);

        return result.toString();
    }

    private boolean isPalindrome(String s, int len) {
        for (int i = 0, j = len - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testShortestPalindrome() {
        test(this::shortestPalindrome);
    }


    /**
     * Rabin-Karp 字符串哈希算法，参见：
     * https://leetcode-cn.com/problems/shortest-palindrome/solution/zui-duan-hui-wen-chuan-by-leetcode-solution/
     *
     * 在该方法中，我们将字符串看成一个 base 进制的数，它对应的十进制值就是哈希值。显然，两个字符串的哈希值相等，
     * 当且仅当这两个字符串本身相同。然而如果字符串本身很长，其对应的十进制值在大多数语言中无法使用内置的整数类型进行存储。
     * 因此，我们会将十进制值对一个大质数 mod 进行取模。此时：
     * - 如果两个字符串的哈希值在取模后不相等，那么这两个字符串本身一定不相同；
     * - 如果两个字符串的哈希值在取模后相等，并不能代表这两个字符串本身一定相同。例如两个字符串的哈希值分别为 2 和 15，
     *   模数为 13，虽然 2 = 15(mod 13)，但它们不相同。
     *
     * 然而，我们在编码中使用的 base 和 mod 对于测试数据本身是「黑盒」的，也就是说，并不存在一组测试数据，
     * 使得对于任意的 base 和 mod，都会产生哈希碰撞，导致错误的判断结果。因此，我们可以「投机取巧」地尝试不同的
     * base 和 mod，使之可以通过所有的测试数据。
     *
     * 一般来说，我们选取一个大于字符集大小（即字符串中可能出现的字符种类的数目）的质数作为 base，
     * 再选取一个在字符串长度平方级别左右的质数作为 mod，产生哈希碰撞的概率就会很低。
     *
     *
     * 一个字符串是回文串，当且仅当该字符串与它的反序相同。因此，我们仍然暴力地枚举 s_1 的结束位置，
     * 并计算 s_1 与 s_1 反序的哈希值。如果这两个哈希值相等，说明我们找到了一个 s 的前缀回文串。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.5 MB - 62.95%
     */
    public String rabinKarpMethod(String s) {
        // 使用 Rabin-Karp 算法找到 s 最长的前缀回文字符串
        int prefixLength = 0;
        long rkHash = 0, rrkHash = 0, mul = 1;
        final int base = 31, mod = 1000000007, n = s.length();

        for (int i = 0; i < n; i++) {
            rkHash = (rkHash * base + s.charAt(i)) % mod;
            rrkHash = (rrkHash + s.charAt(i) * mul) % mod;
            if (rkHash == rrkHash) {
                prefixLength = i + 1;
            }
            mul = mul * base % mod;
        }

        StringBuilder result = new StringBuilder(prefixLength + (s.length() - prefixLength) * 2);
        // 将剩余的字符补齐
        for (int i = s.length() - 1; i >= prefixLength; i--) {
            result.append(s.charAt(i));
        }
        result.append(s);

        return result.toString();
    }

    @Test
    public void testRabinKarpMethod() {
        test(this::rabinKarpMethod);
    }
}
