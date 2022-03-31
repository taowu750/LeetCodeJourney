package training.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.UnaryOperator;

import static util.Util.assertIn;

/**
 * 451. 根据字符出现频率排序: https://leetcode-cn.com/problems/sort-characters-by-frequency/
 *
 * 给定一个字符串 s ，根据字符出现的「频率」对其进行「降序排序」。一个字符出现的频率是它出现在字符串中的次数。
 *
 * 返回已排序的字符串。如果有多个答案，返回其中任何一个。
 *
 * 例 1：
 * 输入: s = "tree"
 * 输出: "eert"
 * 解释: 'e'出现两次，'r'和't'都只出现一次。
 * 因此'e'必须出现在'r'和't'之前。此外，"eetr"也是一个有效的答案。
 *
 * 例 2：
 * 输入: s = "cccaaa"
 * 输出: "cccaaa"
 * 解释: 'c'和'a'都出现三次。此外，"aaaccc"也是有效的答案。
 * 注意"cacaca"是不正确的，因为相同的字母必须放在一起。
 *
 * 例 3：
 * 输入: s = "Aabb"
 * 输出: "bbAa"
 * 解释: 此外，"bbaA"也是一个有效的答案，但"Aabb"是不正确的。
 * 注意'A'和'a'被认为是两种不同的字符。
 *
 * 说明：
 * - 1 <= s.length <= 5 * 10^5
 * - s 由大小写英文字母和数字组成
 */
public class E451_Medium_SortCharactersByFrequency {

    public static void test(UnaryOperator<String> method) {
        assertIn(method.apply("tree"), "eert", "eetr");
        assertIn(method.apply("cccaaa"), "cccaaa", "aaaccc");
        assertIn(method.apply("Aabb"), "bbAa", "bbaA");
    }

    /**
     * LeetCode 耗时：5 ms - 93.12%
     *          内存消耗：41.2 MB - 71.31%
     */
    public String frequencySort(String s) {
        final int[][] freq = new int[128][2];
        for (int i = 0; i < freq.length; i++) {
            freq[i][0] = i;
        }
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i)][1]++;
        }
        Arrays.sort(freq, (a, b) -> b[1] - a[1]);

        StringBuilder result = new StringBuilder(s.length());
        for (int[] ch2cnt : freq) {
            for (int j = 0; j < ch2cnt[1]; j++) {
                result.append((char) ch2cnt[0]);
            }
        }

        return result.toString();
    }

    @Test
    public void testFrequencySort() {
        test(this::frequencySort);
    }
}
