package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 76. 最小覆盖子串: https://leetcode-cn.com/problems/minimum-window-substring/
 *
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。
 * 如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 *
 * 注意：t 中的重复字符数量必须小于等于涵盖子串。
 *
 * 注意：如果 s 中存在这样的子串，我们保证它是唯一的答案。
 *
 * 你能设计一个在 O(n) 时间内解决此问题的算法吗？
 *
 * 例 1：
 * Input：s = "ADOBECODEBANC", t = "ABC"
 * Output："BANC"
 *
 * 例 2：
 * Input：s = "a", t = "a"
 * Output："a"
 *
 * 约束：
 * - 1 <= s.length, t.length <= 10**5
 * - s 和 t 由英文字母组成
 */
public class E76_Hard_MinimumWindowSubstring {

    public static void test(BiFunction<String, String, String> method) {
        assertEquals(method.apply("ADOBECODEBANC", "ABC"), "BANC");
        assertEquals(method.apply("a", "a"), "a");
        assertEquals(method.apply("a", "aa"), "");
        assertEquals(method.apply("aa", "aa"), "aa");
        assertEquals(method.apply("aaa", "aa"), "aa");
    }

    /**
     * 滑动窗口。这个算法的框架如下：
     * void slidingWindow(string s, string t) {
     *     unordered_map<char, int> need, window;
     *     for (char c : t) need[c]++;
     *
     *     int left = 0, right = 0;
     *     int valid = 0;
     *     while (right < s.size()) {
     *         // c 是将移入窗口的字符
     *         char c = s[right];
     *         // 右移窗口
     *         right++;
     *         // 进行窗口内数据的一系列更新
     *         ...
     *
     *         // 判断左侧窗口是否要收缩
     *         while (window needs shrink) {
     *             // d 是将移出窗口的字符
     *             char d = s[left];
     *             // 左移窗口
     *             left++;
     *             // 进行窗口内数据的一系列更新
     *             ...
     *         }
     *     }
     * }
     *
     * LeetCode 耗时：3 ms - 91.45%
     *          内存消耗：38.5 MB - 77.57%
     */
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        } else if (t.length() == 1) {
            return s.indexOf(t.charAt(0)) >= 0 ? t : "";
        }

        // 使用 tcnts 记录 t 中字符出现的次数
        int[] tcnts = new int[128];
        // 使用 distinct 记录 tcnts 和下面的滑动窗口中字符的差异
        int distinct = 0;
        for (int i = 0; i < t.length(); i++) {
            if (tcnts[t.charAt(i)]++ == 0) {
                distinct++;
            }
        }

        int[] window = new int[128];
        int left = 0, right = 0, resultLeft = 0, resultRight = Integer.MAX_VALUE;
        while (right < s.length()) {
            char c = s.charAt(right++);
            if (++window[c] == tcnts[c]) {
                distinct--;
            }

            while (distinct == 0) {
                if (resultRight - resultLeft > right - left) {
                    resultLeft = left;
                    resultRight = right;
                }
                char lc = s.charAt(left++);
                if (--window[lc] < tcnts[lc]) {
                    distinct++;
                }
            }
        }

        return resultRight != Integer.MAX_VALUE ? s.substring(resultLeft, resultRight) : "";
    }

    @Test
    public void testMinWindow() {
        test(this::minWindow);
    }


    /**
     * 和上面的一样，不过在收缩窗口上有小改动。
     * 上面是循环收缩一个字符，直到字符不匹配为止；这里是一次收缩到刚好匹配为止
     *
     * LeetCode 耗时：2 ms - 96.79%
     *          内存消耗：41.6 MB - 83.02%
     */
    public String otherMethod(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        } else if (t.length() == 1) {
            return s.indexOf(t.charAt(0)) >= 0 ? t : "";
        }

        int charKind = 0;
        int[] tcnts = new int[126], window = new int[126];
        for (int i = 0; i < t.length(); i++) {
            if (tcnts[t.charAt(i)]++ == 0) {
                charKind++;
            }
        }
        int left = 0, right = 0, ansLeft = -1, ansRight = -1;
        while (right < s.length()) {
            char c = s.charAt(right++);
            if (++window[c] == tcnts[c]) {
                charKind--;
            }

            if (charKind == 0) {
                for (char leftChar = s.charAt(left); window[leftChar] - 1 >= tcnts[leftChar]; leftChar = s.charAt(++left)) {
                    window[leftChar]--;
                }
                if (ansLeft == -1 || ansRight - ansLeft > right - left) {
                    ansLeft = left;
                    ansRight = right;
                }
            }
        }

        return ansLeft == -1 ? "" : s.substring(ansLeft, ansRight);
    }

    @Test
    public void testOtherMethod() {
        test(this::otherMethod);
    }
}
