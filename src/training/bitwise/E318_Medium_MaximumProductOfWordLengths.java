package training.bitwise;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 318. 最大单词长度乘积: https://leetcode-cn.com/problems/maximum-product-of-word-lengths/
 *
 * 给你一个字符串数组 words ，找出并返回 length(words[i]) * length(words[j]) 的最大值，
 * 并且这两个单词不含有公共字母。如果不存在这样的两个单词，返回 0 。
 *
 * 例 1：
 * 输入：words = ["abcw","baz","foo","bar","xtfn","abcdef"]
 * 输出：16
 * 解释：这两个单词为 "abcw", "xtfn"。
 *
 * 例 2：
 * 输入：words = ["a","ab","abc","d","cd","bcd","abcd"]
 * 输出：4
 * 解释：这两个单词为 "ab", "cd"。
 *
 * 例 3：
 * 输入：words = ["a","aa","aaa","aaaa"]
 * 输出：0
 * 解释：不存在这样的两个单词。
 *
 * 说明：
 * - 2 <= words.length <= 1000
 * - 1 <= words[i].length <= 1000
 * - words[i] 仅包含小写字母
 */
public class E318_Medium_MaximumProductOfWordLengths {

    public static void test(ToIntFunction<String[]> method) {
        assertEquals(16, method.applyAsInt(new String[]{"abcw","baz","foo","bar","xtfn","abcdef"}));
        assertEquals(4, method.applyAsInt(new String[]{"a","ab","abc","d","cd","bcd","abcd"}));
        assertEquals(0, method.applyAsInt(new String[]{"a","aa","aaa","aaaa"}));
    }

    /**
     * LeetCode 耗时：23 ms - 35.12%
     *          内存消耗：41.6 MB - 16.78%
     */
    public int maxProduct(String[] words) {
        Map<Integer, Integer> mask2len = new HashMap<>();
        for (String word : words) {
            // 用二进制位表示字符是否存在
            int mask = 0;
            for (int j = 0; j < word.length(); j++) {
                mask |= 1 << word.charAt(j) - 'a';
            }
            // 相同的 mask，记录最大长度
            mask2len.merge(mask, word.length(), Integer::max);
        }
        int ans = 0;
        // 比较每一个单词
        for (Map.Entry<Integer, Integer> en : mask2len.entrySet()) {
            int mask = en.getKey(), len = en.getValue();
            for (int other : mask2len.keySet()) {
                if ((mask & other) == 0) {
                    ans = Math.max(ans, len * mask2len.get(other));
                }
            }
        }

        return ans;
    }

    @Test
    public void testMaxProduct() {
        test(this::maxProduct);
    }
}
