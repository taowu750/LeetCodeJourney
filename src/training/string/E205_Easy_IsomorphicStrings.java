package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 205. 同构字符串: https://leetcode-cn.com/problems/isomorphic-strings/
 *
 * 给定两个字符串 s 和 t，判断它们是否是同构的。
 *
 * 如果 s 中的字符可以按某种映射关系替换得到 t，那么这两个字符串是同构的。
 *
 * 每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。「不同字符不能映射到同一个字符上」，
 * 相同字符只能映射到同一个字符上，字符可以映射到自己本身。
 *
 * 例 1：
 * 输入：s = "egg", t = "add"
 * 输出：true
 *
 * 例 2：
 * 输入：s = "foo", t = "bar"
 * 输出：false
 *
 * 例 3：
 * 输入：s = "paper", t = "title"
 * 输出：true
 *
 * 说明：
 * - 1 <= s.length <= 5 * 10^4
 * - t.length == s.length
 * - s 和 t 由任意有效的 ASCII 字符组成
 */
public class E205_Easy_IsomorphicStrings {

    public static void test(BiPredicate<String, String> method) {
        assertTrue(method.test("egg", "add"));
        assertFalse(method.test("foo", "bar"));
        assertTrue(method.test("paper", "title"));
        assertFalse(method.test("badc", "baba"));
    }

    /**
     * LeetCode 耗时：4 ms - 95.00%
     *          内存消耗：41.1 MB - 50.21%
     */
    public boolean isIsomorphic(String s, String t) {
        final char[] s2t = new char[128], t2s = new char[128];
        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (s2t[sc] == '\0' && t2s[tc] == '\0') {
                s2t[sc] = tc;
                t2s[tc] = sc;
            } else if (s2t[sc] != tc) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testIsomorphic() {
        test(this::isIsomorphic);
    }
}
