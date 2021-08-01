package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的排列。
 *
 * 换句话说，第一个字符串的排列之一是第二个字符串的「子串」。
 *
 * 例 1：
 * Input: s1 = "ab" s2 = "eidbaooo"
 * Output: True
 * Explanation: s2 包含 s1 的排列之一 ("ba").
 *
 * 例 2：
 * Input: s1= "ab" s2 = "eidboaoo"
 * Output: False
 *
 * 约束：
 * - 输入的字符串只包含小写字母
 * - 两个字符串的长度都在 [1, 10,000] 之间
 */
public class E567_Medium_PermutationInString {

    static void test(BiPredicate<String, String> method) {
        assertTrue(method.test("ab", "eidbaooo"));
        assertTrue(method.test("ab", "eidbbaooo"));
        assertFalse(method.test("ab", "eidboaoo"));
        assertTrue(method.test("abcd", "eidacbcoo"));
        assertFalse(method.test("abcd", "eihabcoo"));
        assertTrue(method.test("adc", "dcda"));
        assertFalse(method.test("hello", "ooolleoooleh"));
    }

    /**
     * 滑动窗口。此题所匹配的子串大小是固定的。
     * 模板说明参见{@link E76_Hard_MinimumWindowSubstring}。
     *
     * LeetCode 耗时：7 ms - 47.80%
     *          内存消耗：38.7 MB - 26.05%
     */
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;
        if (s1.length() == 1)
            return s2.indexOf(s1.charAt(0)) >= 0;

        int[] need = new int[128], window = new int[128];
        for (char c : s1.toCharArray())
            need[c]++;

        int left = 0, right = 0;
        while (right < s2.length()) {
            char c = s2.charAt(right++);
            // 如果 c 不在 s1 中，那么 [left, right) 的字符都是无效的
            if (need[c] == 0) {
                // 取消记录。滑动窗口重新开始
                for (;left < right; left++)
                    window[s2.charAt(left)] = 0;
                continue;
            }
            // 如果 c 在 s1 中，但是滑动窗口中 c 的数量大于所需的数量
            if (need[c] == window[c]) {
                // 取消，一直到第一个重复的 c 的位置后
                while (left < right - 1) {
                    char leftChar = s2.charAt(left++);
                    if (leftChar != c)
                        window[leftChar] = 0;
                    else
                        break;
                }
                continue;
            }
            // 记录字符
            window[c]++;
            // 如果滑动窗口大小等于 s1 的长度，则找到一个排列
            if (right - left == s1.length())
                return true;
        }

        return false;
    }

    @Test
    public void testCheckInclusion() {
        test(this::checkInclusion);
    }
}
