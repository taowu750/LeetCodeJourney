package training.dynamicprogramming;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 10. 正则表达式匹配：https://leetcode-cn.com/problems/regular-expression-matching/
 *
 * 给你一个字符串 s 和一个字符规则 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 * - '.' 匹配任意单个字符
 * - '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖「整个」字符串 s 的，而不是部分字符串。
 * <p>
 * 例 1：
 * 输入：s = "aa" p = "a"
 * 输出：false
 * 解释："a" 无法匹配 "aa" 整个字符串。
 * <p>
 * 例 2：
 * 输入：s = "aa" p = "a*"
 * 输出：true
 * 解释：因为 '*' 代表可以匹配零个或多个前面的那一个元素, 在这里前面的元素就是 'a'。因此，
 * 字符串 "aa" 可被视为 'a' 重复了一次。
 * <p>
 * 例 3：
 * 输入：s = "ab" p = ".*"
 * 输出：true
 * 解释：".*" 表示可匹配零个或多个（'*'）任意字符（'.'）。
 * <p>
 * 例 4：
 * 输入：s = "aab" p = "c*a*b"
 * 输出：true
 * 解释：因为 '*' 表示零个或多个，这里 'c' 为 0 个, 'a' 被重复一次。因此可以匹配字符串 "aab"。
 * <p>
 * 例 5：
 * 输入：s = "mississippi" p = "mis*is*p*."
 * 输出：false
 * <p>
 * 约束：
 * - 0 <= s.length <= 20
 * - 0 <= p.length <= 30
 * - s 可能为空，且只包含从 a-z 的小写字母。
 * - p 可能为空，且只包含从 a-z 的小写字母，以及字符 . 和 *。
 * - 保证每次出现字符 * 时，前面都匹配到有效的字符
 */
public class E10_Hard_RegularExpressionMatching {

    static void test(BiPredicate<String, String> method) {
        assertFalse(method.test("aa", "a"));
        assertTrue(method.test("aa", "a*"));
        assertTrue(method.test("ab", ".*"));
        assertTrue(method.test("aab", "c*a*b"));
        assertFalse(method.test("mississippi", "mis*is*p*."));
        assertFalse(method.test("mississippi", ""));
        assertTrue(method.test("", ".*"));
        assertTrue(method.test("", "a*b*c*"));
        assertFalse(method.test("", "."));
        assertTrue(method.test("", ""));
        assertTrue(method.test("aaa", "a*a"));
        assertFalse(method.test("aaa", "a*ab"));
        assertTrue(method.test("aaabbbz", "a*ab*b."));
        assertTrue(method.test("aaa", "a*aaa"));
        assertFalse(method.test("aaa", "a*aaaa"));
        assertTrue(method.test("aaa", "ab*a*c*a"));
    }


    /**
     * 算法思想参见 https://mp.weixin.qq.com/s/TAiIIxoDXx67MNGXea6gfQ
     *
     * 经过测试，使用字符数组和使用字符串速度上无差别，而使用字符串内存消耗更小。
     *
     * LeetCode 耗时：3 ms - 89.35%
     *          内存消耗：38.4 MB - 40.82%
     */
    public boolean isMatch(String s, String p) {
        return isMatch(s, 0, p, 0, new HashMap<>());
    }

    private boolean isMatch(String s, int i, String p, int j,
                            Map<Pair<Integer, Integer>, Boolean> memory) {
        Pair<Integer, Integer> cur = new Pair<>(i, j);
        Boolean cache = memory.getOrDefault(cur, null);
        if (cache != null)
            return cache;

        // 如果模式 p 已到末尾，返回 s 是否到了末尾
        if (j >= p.length())
            return i >= s.length();
        // 如果 p 当前有 *
        if (j < p.length() - 1 && p.charAt(j + 1) == '*')
            /*
            如果后面的模式能够匹配，就算匹配成功；
            否则如果 s 还有内容且和 p 字符匹配成功，才算匹配成功。此时 s 移动一位，p 不移动。
             */
            cache = isMatch(s, i, p, j + 2, memory) ||
                    (i < s.length() && (p.charAt(j) == '.' || s.charAt(i) == p.charAt(j))
                            && isMatch(s, i + 1, p, j, memory));
        // 否则查看字符是否匹配
        else
            cache = i < s.length()
                    // 匹配双方都先后移动一位
                    && (p.charAt(j) == '.' || s.charAt(i) == p.charAt(j))
                    && isMatch(s, i + 1, p, j + 1, memory);
        memory.put(cur, cache);

        return cache;
    }

    @Test
    public void testIsMatch() {
        test(this::isMatch);
    }
}
