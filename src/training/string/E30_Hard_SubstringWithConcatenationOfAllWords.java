package training.string;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 30. 串联所有单词的子串: https://leetcode-cn.com/problems/substring-with-concatenation-of-all-words/
 *
 * 给定一个字符串 s 和一些「长度相同」的单词 words 。找出 s 中恰好可以由 words 中「所有单词」串联形成的子串的起始位置。
 *
 * 注意子串要与 words 中的单词完全匹配，中间不能有其他字符 ，但不需要考虑 words 中单词串联的顺序。
 *
 * 例 1：
 * 输入：s = "barfoothefoobarman", words = ["foo","bar"]
 * 输出：[0,9]
 * 解释：
 * 从索引 0 和 9 开始的子串分别是 "barfoo" 和 "foobar" 。
 * 输出的顺序不重要, [9,0] 也是有效答案。
 *
 * 例 2：
 * 输入：s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"]
 * 输出：[]
 *
 * 例 3：
 * 输入：s = "barfoofoobarthefoobarman", words = ["bar","foo","the"]
 * 输出：[6,9,12]
 *
 * 说明：
 * - 1 <= s.length <= 10^4
 * - s 由小写英文字母组成
 * - 1 <= words.length <= 5000
 * - 1 <= words[i].length <= 30
 * - words[i] 由小写英文字母组成
 */
public class E30_Hard_SubstringWithConcatenationOfAllWords {

    public static void test(BiFunction<String, String[], List<Integer>> method) {
        equalsIgnoreOrder(asList(0, 9), method.apply("barfoothefoobarman", new String[]{"foo","bar"}));
        equalsIgnoreOrder(emptyList(), method.apply("wordgoodgoodgoodbestword", new String[]{"word","good","best","word"}));
        equalsIgnoreOrder(asList(6, 9, 12), method.apply("barfoofoobarthefoobarman", new String[]{"bar","foo","the"}));
        equalsIgnoreOrder(singletonList(0), method.apply("ababababab", new String[]{"ababa","babab"}));
        equalsIgnoreOrder(singletonList(1), method.apply("acbcaabacb", new String[]{"aab","cbc"}));
    }

    /**
     * 一般的思想是遍历 s，查找满足的序列；这里我们逆向思维，先找到 words 所有单词在 s 中的下标，然后查找满足的序列
     *
     * LeetCode 耗时：160 ms - 36.32%
     *          内存消耗：42.7 MB - 5.02%
     */
    public List<Integer> findSubstring(String s, String[] words) {
        Map<String, Integer> word2cnt = new HashMap<>();
        for (String word : words) {
            word2cnt.merge(word, 1, Integer::sum);
        }
        // 用 KMP 算法找出 words 所有单词在 s 中出现的所有下标
        List<Tuple> indices = new ArrayList<>();
        for (String word: word2cnt.keySet()) {
            KMP kmp = new KMP(word);
            kmp.findAll(s, indices);
        }
        // 将找到的下标进行排序
        Comparator<Tuple> cmp = (a, b) -> Integer.compare(a.idx, b.idx);
        indices.sort(cmp);

        List<Integer> result = new ArrayList<>();
        Map<String, Integer> window = new HashMap<>((int) (word2cnt.size() / 0.75) + 1);
        final int wordsLen = words[0].length() * words.length;
        // 从第一个找到的下标开始，看看它和后面的序列是否满足条件
        for (int start = 0; start <= indices.size() - words.length; start++) {
            // words 中的单词长度相等，因此判断 wordsLen 长度的序列是否满足条件
            int idx = indices.get(start).idx, end = idx + wordsLen;
            for (; idx < end; idx += words[0].length()) {
                int searchedIdx = Collections.binarySearch(indices, new Tuple(idx, null), cmp);
                if (searchedIdx >= 0) {
                    String word = indices.get(searchedIdx).word;
                    int cnt = window.merge(word, 1, Integer::sum);
                    if (cnt > word2cnt.get(word)) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (idx == end) {
                result.add(indices.get(start).idx);
            }
            // 重置 window
            for (Map.Entry<String, Integer> entry : window.entrySet()) {
                entry.setValue(0);
            }
        }

        return result;
    }

    private static class Tuple {
        public int idx;
        public String word;

        public Tuple(int idx, String word) {
            this.idx = idx;
            this.word = word;
        }

        @Override
        public String toString() {
            return "{" + idx + "," + word + '}';
        }
    }

    private static class KMP {
        private String pattern;
        private int[] next;

        public KMP(String pattern) {
            this.pattern = pattern;
            final int n = pattern.length();
            next = new int[n];
            for (int i = 1, now = 0; i < n;) {
                if (pattern.charAt(now) == pattern.charAt(i)) {
                    next[i++] = ++now;
                } else if (now > 0) {
                    now = next[now - 1];
                } else {
                    i++;
                }
            }
        }

        public void findAll(String s, List<Tuple> indices) {
            final int m = s.length(), n = pattern.length();
            for (int target = 0, pos = 0; target < m;) {
                if (s.charAt(target) == pattern.charAt(pos)) {
                    target++;
                    pos++;
                } else if (pos > 0) {
                    pos = next[pos - 1];
                } else {
                    target++;
                }

                if (pos == n) {
                    indices.add(new Tuple(target - n, pattern));
                    pos = next[pos - 1];
                }
            }
        }
    }

    @Test
    public void testFindSubstring() {
        test(this::findSubstring);
    }
}
