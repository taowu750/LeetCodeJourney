package training.slidewindow;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 340. 至多包含 K 个不同字符的最长子串: https://leetcode-cn.com/problems/longest-substring-with-at-most-k-distinct-characters/
 *
 * 给定一个字符串 s ，找出「至多」包含 k 个不同字符的最长子串 T。
 *
 * 例 1：
 * 输入: s = "eceba", k = 2
 * 输出: 3
 * 解释: 则 T 为 "ece"，所以长度为 3。
 *
 * 例 2：
 * 输入: s = "aa", k = 1
 * 输出: 2
 * 解释: 则 T 为 "aa"，所以长度为 2。
 *
 * 提示：
 * - 1 <= s.length <= 5 * 10^4
 * - 0 <= k <= 50
 */
public class E340_Medium_LongestSubstringWithMostKDistinctCharacters {

    public static void test(ToIntBiFunction<String, Integer> method) {
        assertEquals(3, method.applyAsInt("eceba", 2));
        assertEquals(2, method.applyAsInt("aa", 1));
    }

    /**
     * LeetCode 耗时：1 ms - 100%
     *          内存消耗：38.1 MB - 98.53%
     */
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (k == 0) {
            return 0;
        }

        int[] window = new int[128];
        int distinct = 0, left = 0, right = 0, result = 0;
        while (right < s.length()) {
            char c = s.charAt(right++);
            if (window[c]++ == 0) {
                distinct++;
            }

            if (distinct > k) {
                do {
                    if (--window[s.charAt(left++)] == 0) {
                        distinct--;
                    }
                } while (distinct > k);
            }

            // 比 Math.max 快 1ms
            if (right - left > result) {
                result = right - left;
            }
        }

        return result;
    }

    @Test
    public void testLengthOfLongestSubstringKDistinct() {
        test(this::lengthOfLongestSubstringKDistinct);
    }
}
