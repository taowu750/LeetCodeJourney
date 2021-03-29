package training.string;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
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

    static void test(BiFunction<String, String, String> method) {
        assertEquals(method.apply("ADOBECODEBANC", "ABC"), "BANC");
        assertEquals(method.apply("a", "a"), "a");
        assertEquals(method.apply("a", "aa"), "");
        assertEquals(method.apply("aa", "aa"), "aa");
        assertEquals(method.apply("aaa", "aa"), "aa");
    }

    /**
     * 滑动窗口。
     *
     * LeetCode 耗时：4ms - 88.49%
     *          内存消耗：38.7 MB - 74.36%
     */
    public String minWindow(String s, String t) {
        // t 只有一个字符，直接找就完事了
        if (t.length() == 1)
            return s.indexOf(t.charAt(0)) >= 0 ? t : "";

        int[] need = new int[128];
        int[] window = new int[128];
        for (char c : t.toCharArray())
            need[c] += 1;

        // 滑动窗口：[left, right)。left 和 right 是 s 中的下标。
        // left 是滑动窗口中第一个在 t 中的字符的位置；right 是滑动窗口右边界。
        // cnt 记录了当前滑动窗口内包含了多少个 t 中的字符。
        int left = 0, right = 0, cnt = 0, bestLeft = 0, bestRight = s.length();
        // covered 表示有没有找到过覆盖子串
        boolean covered = false;
        while (right < s.length()) {
            // 将 right 位置的字符加入滑动窗口
            char c = s.charAt(right++);
            // 如果 c 不是 t 中的字符，则跳过
            if (need[c] == 0) {
                // 如果还没有找到过任何字符，则收缩左边界
                if (cnt == 0)
                    left++;
                continue;
            }
            // 此时 c 是 t 中的字符

            // 进行记录
            window[c]++;
            // 如果 window 中记录的数量小于等于 t 中对应的数量，则增加计数
            if (window[c] <= need[c])
                cnt++;
            // 如果涵盖子串已找到，则收缩左侧窗口
            while (cnt == t.length()) {
                // 标记找到了涵盖子串
                covered = true;
                // 更新最小涵盖子串
                if (right - left < bestRight - bestLeft) {
                    bestLeft = left;
                    bestRight = right;
                }
                // 取消记录 left 的字符
                char leftChar = s.charAt(left);
                window[leftChar]--;
                // 如果 window 中包含的 leftChar 个数少于 t 中的，则减少计数
                if (window[leftChar] < need[leftChar])
                    cnt--;
                // 收缩左边界，直到下一个存在于 t 中的字符。注意别忘了边界检查
                while (++left < right && window[s.charAt(left)] == 0);
            }
        }

        return covered ? s.substring(bestLeft, bestRight) : "";
    }

    @Test
    public void testMinWindow() {
        test(this::minWindow);
    }
}
