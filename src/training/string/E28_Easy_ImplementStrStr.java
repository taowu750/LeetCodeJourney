package training.string;

import org.junit.jupiter.api.Test;
import util.algorithm.KMP;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 28. 实现 strStr(): https://leetcode-cn.com/problems/implement-strstr/
 *
 * 实现 strStr() 函数。
 * 给你两个字符串 haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串出现的第一个位置（下标从 0 开始）。如果不存在，则返回  -1 。
 *
 * 说明：
 * 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
 * 对于本题而言，当 needle 是空字符串时我们应当返回 0 。这与 C 语言的 strstr() 以及 Java 的 indexOf() 定义相符。
 *
 * 例 1：
 * 输入：haystack = "hello", needle = "ll"
 * 输出：2
 *
 * 例 2：
 * 输入：haystack = "aaaaa", needle = "bba"
 * 输出：-1
 *
 * 例 3：
 * 输入：haystack = "", needle = ""
 * 输出：0
 *
 * 说明：
 */
public class E28_Easy_ImplementStrStr {

    public static void test(ToIntBiFunction<String, String> method) {
        assertEquals(2, method.applyAsInt("hello", "ll"));
        assertEquals(-1, method.applyAsInt("aaaaa", "bba"));
        assertEquals(0, method.applyAsInt("", ""));
    }

    /**
     * KMP 算法。
     *
     * LeetCode 耗时：4ms - 94%
     *          内存消耗：38.3MB - 80%
     */
    public int strStr(String haystack, String needle) {
        if (haystack.length() < needle.length()) {
            return -1;
        } else if (haystack.length() == needle.length()) {
            return haystack.equals(needle) ? 0 : -1;
        }

        if (needle.length() == 0) {
            return 0;
        }

        KMP kmp = new KMP(needle);

        return kmp.search(haystack);
    }

    @Test
    public void testStrStr() {
        test(this::strStr);
    }
}
