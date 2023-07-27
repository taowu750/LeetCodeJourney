package training.string;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 459. 重复的子字符串: https://leetcode-cn.com/problems/repeated-substring-pattern/
 *
 * 给定一个非空的字符串，判断它是否可以由它的一个子串重复多次构成。给定的字符串只含有小写英文字母，并且长度不超过10000。
 *
 * 例 1：
 * 输入: "abab"
 * 输出: True
 * 解释: 可由子字符串 "ab" 重复两次构成。
 *
 * 例 2：
 * 输入: "aba"
 * 输出: False
 *
 * 例 3：
 * 输入: "abcabcabcabc"
 * 输出: True
 * 解释: 可由子字符串 "abc" 重复四次构成。 (或者子字符串 "abcabc" 重复两次构成。)
 */
public class E459_Easy_RepeatedSubstringPattern {

    public static void test(Predicate<String> method) {
        assertTrue(method.test("abab"));
        assertFalse(method.test("aba"));
        assertTrue(method.test("abcabcabcabc"));
        assertTrue(method.test("abcabc"));
        assertFalse(method.test("abcabcabcabcab"));
        assertTrue(method.test("aa"));
        assertFalse(method.test("ab"));
        assertFalse(method.test("a"));
        assertTrue(method.test("zzz"));
        assertTrue(method.test("ababab"));
    }

    /**
     * 暴力法。
     *
     * LeetCode 耗时：10 ms - 61.81%
     *          内存消耗：38.9 MB - 59.82%
     */
    public boolean repeatedSubstringPattern(String s) {
        if (s.length() < 2) {
            return false;
        }

        for (int subEnd = 0, half = s.length() / 2; subEnd < half; subEnd++) {
            // 如果子串长度不是字符串长度的因子，则跳过
            if (s.length() % (subEnd + 1) != 0) {
                continue;
            }
            boolean match = true;
            OUTER: for (int i = subEnd + 1; i < s.length(); i += subEnd + 1) {
                for (int cnt = 0; cnt <= subEnd; cnt++) {
                    if (s.charAt(cnt) != s.charAt(i + cnt)) {
                        match = false;
                        break OUTER;
                    }
                }
            }
            if (match) {
                return true;
            }
        }

        return false;
    }

    @Test
    public void testRepeatedSubstringPattern() {
        test(this::repeatedSubstringPattern);
    }


    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.8 MB - 63.00%
     */
    public boolean betterMethod(String s) {
        // 从 2 开始，之后取子串 len/2、len/3，保证子串从最大长度开始进行比较
        int part = 2, noRepeatLen = s.length();
        while (noRepeatLen > 1) {
            if (noRepeatLen % part == 0) {
                int subLen = s.length() / part;
                // 错位比较
                if (Objects.equals(s.substring(0, s.length() - subLen), s.substring(subLen))) {
                    return true;
                }
                /*
                去除重复的整除情况，在除数上进行操作。
                例如长度 4 不能匹配，那么长度 2 肯定也不能匹配。因为加入长度 2 可以匹配，那么长度 4 也应该可以匹配
                 */
                noRepeatLen /= part;
                while (noRepeatLen % part == 0) {
                    noRepeatLen /= part;
                }
            }
            part++;
        }

        return false;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }


    /**
     * 双倍字符串解法。参见：
     * https://leetcode-cn.com/problems/repeated-substring-pattern/solution/jian-dan-ming-liao-guan-yu-javaliang-xing-dai-ma-s/
     *
     * 假设判断字符串s（n个字符构成）是不是由重复子串构成的，我们先将两个字串拼成一个大的字符串：S = s + s（S 有2n个字符）；
     *
     * 字符串拼接的最底层思想应该是使用字符串移位来判断字符串是否由重复子串（循环节）构成，因为S这个大的字符串
     * 其实包含了字符串s的所有移位字符串。令字符串字符索引从0开始，[m,n]表示S中索引为m到索引为n的这一段字符，索引为闭区间，
     * 则[0,n-1]即S的前一半字符表示原始字符串s；[1,n]表示s右移1位的状态；[2,n+1]表示s右移2位的状态；······；
     * [n,2n-1]即S的后半拉字符串可以理解为s移n位的状态，此时s已经移回了原始的状态了；
     *
     * 此外我们不难知道如果一个没有循环节的字符串在移位时必须要右移n次他才能移回它的原始状态，而有循环节的字符串最多只需要n/2次（只有两个循环节）；
     * 所以当S砍去首尾字符时，对于没有循环节的字符串s，相当于砍去了[0,n-1]的原始状态和[n,2n-1]这个移位n次的又回归原始的状态，
     * 我们在[1,2n-2]范围内只能找到s移位1，2，···，n-1位时的状态，所以在[1,2n-2]内是不存在无循环节的s的；
     * 但是对于有循环节的s来说，n/2 <= n-1，所以一定存在至少一个移位状态为s，即最少存在一个s（其实对于有循环节的s来说，
     * 不考虑移位状态我们也能明白[1,2n-2]内一定至少有一个s，例如对于有两个循环节的s：组成S的前一个s的后一半字符 + 组成S的后一个s的前一半的字符 == s；
     * 三个循环节的s更不用说了)；
     *
     * 可以使用 KMP 加快字符串匹配，参见 {@link util.algorithm.KMP}。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.8 MB - 63.00%
     */
    public boolean conciseMethod(String s) {
        String str = s + s;
        return str.substring(1, str.length() - 1).contains(s);
    }

    @Test
    public void testConciseMethod() {
        test(this::conciseMethod);
    }
}
