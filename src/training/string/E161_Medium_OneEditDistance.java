package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 161. 相隔为 1 的编辑距离: https://leetcode-cn.com/problems/one-edit-distance/
 *
 * 给定两个字符串 s 和 t，判断他们的编辑距离是否为 1。注意，满足编辑距离等于 1 有三种可能的情形：
 * - 往 s 中插入一个字符得到 t
 * - 从 s 中删除一个字符得到 t
 * - 在 s 中替换一个字符得到 t
 *
 * 例 1：
 * 输入: s = "ab", t = "acb"
 * 输出: true
 * 解释: 可以将 'c' 插入字符串 s 来得到 t。
 *
 * 例 2：
 * 输入: s = "cab", t = "ad"
 * 输出: false
 * 解释: 无法通过 1 步操作使 s 变为 t。
 *
 * 例 3：
 * 输入: s = "1203", t = "1213"
 * 输出: true
 * 解释: 可以将字符串 s 中的 '0' 替换为 '1' 来得到 t。
 */
public class E161_Medium_OneEditDistance {

    public static void test(BiPredicate<String, String> method) {
        assertTrue(method.test("ab", "acb"));
        assertFalse(method.test("cab", "ad"));
        assertTrue(method.test("1203", "1213"));
        assertTrue(method.test("abcd", "abcde"));
        assertTrue(method.test("eabcd", "abcd"));
    }

    /**
     * LeetCode 耗时：1 ms - 82.98%
     *          内存消耗：37.1 MB - 54.78%
     */
    public boolean isOneEditDistance(String s, String t) {
        if (Math.abs(s.length() - t.length()) > 1) {
            return false;
        }

        if (s.length() == t.length()) {
            // 找到 s 和 t 不一样的一个字符
            int cnt = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != t.charAt(i)) {
                    if (++cnt > 1) {
                        return false;
                    }
                }
            }

            return cnt == 1;
        } else {
            // 找到 s 比 t 少的一个字符
            // 保证 s 比 t 短
            if (s.length() > t.length()) {
                String tmp = s;
                s = t;
                t = tmp;
            }

            for (int i = 0, j = 0, cnt = 0; i < s.length(); j++) {
                if (s.charAt(i) != t.charAt(j)) {
                    if (++cnt > 1) {
                        return false;
                    }
                } else {
                    i++;
                }
            }

            return true;
        }
    }

    @Test
    public void testIsOneEditDistance() {
        test(this::isOneEditDistance);
    }
}
