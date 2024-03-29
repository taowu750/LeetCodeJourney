package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 3. 无重复字符的最长子串：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 *
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
 * Explanation: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *              请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
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

    public static void test(ToIntFunction<String> method) {
        assertEquals(3, method.applyAsInt("abcabcbb"));
        assertEquals(1, method.applyAsInt("bbbbb"));
        assertEquals(3, method.applyAsInt("pwwkew"));
        assertEquals(0, method.applyAsInt(""));
        assertEquals(2, method.applyAsInt("au"));
        assertEquals(1, method.applyAsInt(" "));
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


    /**
     * 更好的滑动窗口方法。
     *
     * LeetCode 耗时：1 ms - 100%
     *          内存消耗：38.3 MB - 84.68%
     */
    public int betterMethod(String s) {
        int[] window = new int[128];
        Arrays.fill(window, -1);
        int l = 0, r = 0, ans = 0;
        while (r < s.length()) {
            char c = s.charAt(r++);
            // 如果 c 已不在窗口内
            if (window[c] < l) {
                ans = Math.max(ans, r - l);
            } else {
                // 快速收缩窗口
                l = window[c] + 1;
            }
            window[c] = r - 1;
        }

        return ans;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
