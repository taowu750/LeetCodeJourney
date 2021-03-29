package training.string;

import org.junit.jupiter.api.Test;
import util.datastructure.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个字符串 s 和一个非空字符串 p，找到 s 中所有是 p 的字母异位词的子串，返回这些子串的起始索引。
 * 字母异位词指字母相同，但排列不同的字符串。不考虑答案输出的顺序。
 *
 * 字符串只包含小写英文字母，并且字符串 s 和 p 的长度都不超过 20100。
 *
 * 例 1：
 * Input:
 * s: "cbaebabacd" p: "abc"
 * Output:
 * [0, 6]
 * Explanation:
 * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的字母异位词。
 * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的字母异位词。
 *
 * 例 2：
 * Input:
 * s: "abab" p: "ab"
 * Output:
 * [0, 1, 2]
 * Explanation:
 * 起始索引等于 0 的子串是 "ab", 它是 "ab" 的字母异位词。
 * 起始索引等于 1 的子串是 "ba", 它是 "ab" 的字母异位词。
 * 起始索引等于 2 的子串是 "ab", 它是 "ab" 的字母异位词。
 */
public class E438_Medium_FindAllAnagramsInString {

    static void test(BiFunction<String, String, List<Integer>> method) {
        assertTrue(CollectionUtil.setEquals(method.apply("cbaebabacd", "abc"),
                Arrays.asList(0, 6)));
        assertTrue(CollectionUtil.setEquals(method.apply("abab", "ab"),
                Arrays.asList(0, 1, 2)));
        assertTrue(CollectionUtil.setEquals(method.apply("accbd", "c"),
                Arrays.asList(1, 2)));
        assertTrue(method.apply("ab", "abc").isEmpty());
        assertTrue(method.apply("", "d").isEmpty());
        assertTrue(CollectionUtil.setEquals(method.apply("abaacbabc", "abc"),
                Arrays.asList(3,4,6)));
    }

    /**
     * 滑动窗口。此题和{@link E567_Medium_PermutationInString}几乎一致。
     *
     * LeetCode 耗时：6 ms - 88.16%
     *          内存消耗：39.1 MB - 98.49%
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int[] need = new int[128], window = new int[128];
        for (char c : p.toCharArray()) {
            need[c]++;
        }

        int left = 0, right = 0;
        while (right < s.length()) {
            char c = s.charAt(right++);
            if (need[c] == 0) {
                for (; left < right; left++)
                    window[s.charAt(left)] = 0;
                continue;
            }
            if (need[c] == window[c]) {
                while (left < right - 1) {
                    char leftChar = s.charAt(left++);
                    if (leftChar != c)
                        window[leftChar] = 0;
                    else
                        break;
                }
                continue;
            }
            window[c]++;
            if (right - left == p.length()) {
                result.add(left);
                window[s.charAt(left++)]--;
            }
        }

        return result;
    }

    @Test
    public void testFindAnagrams() {
        test(this::findAnagrams);
    }
}
