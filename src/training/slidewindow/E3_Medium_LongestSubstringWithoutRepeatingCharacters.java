package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的「最长子串」的长度。
 *
 * 例 1：
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 *
 * 例 2：
 * Input: s = "bbbbb"
 * Output: 1
 * Explanation: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 *
 * 例 3：
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *              请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 * 例 4：
 * Input: s = ""
 * Output: 0
 *
 * 约束：
 * - 0 <= s.length <= 5 * 10**4
 * - s 由英文字母、数字、符号和空格组成
 */
public class E3_Medium_LongestSubstringWithoutRepeatingCharacters {

    static void test(ToIntFunction<String> method) {
        assertEquals(method.applyAsInt("abcabcbb"), 3);
        assertEquals(method.applyAsInt("bbbbb"), 1);
        assertEquals(method.applyAsInt("pwwkew"), 3);
        assertEquals(method.applyAsInt(""), 0);
        assertEquals(method.applyAsInt("au"), 2);
    }

    /**
     * 滑动窗口。此题和{@link E567_Medium_PermutationInString}类似。
     *
     * LeetCode 耗时：3 ms - 94.59%
     *          内存消耗：38.8 MB - 28.72%
     */
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        if (n < 2)
            return n;

        boolean[] window = new boolean[128];

        int left = 0, right = 0, maxLength = 0;
        while (right < n) {
            char c = s.charAt(right++);
            if (!window[c]) {
                window[c] = true;
                // 长度判断放在不重复这里做，防止字符串从头到尾不重复
                if (right - left > maxLength)
                    maxLength = right - left;
            } else {
                while (left < right - 1) {
                    char leftChar = s.charAt(left++);
                    if (leftChar != c)
                        window[leftChar] = false;
                    else
                        break;
                }
            }
        }

        return maxLength;
    }

    @Test
    public void testLengthOfLongestSubstring() {
        test(this::lengthOfLongestSubstring);
    }
}
