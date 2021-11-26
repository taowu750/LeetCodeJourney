package training.sort;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 692. 前K个高频单词: https://leetcode-cn.com/problems/top-k-frequent-words/
 *
 * 给一非空的单词列表，返回前 k 个出现次数最多的单词。
 * 返回的答案应该按单词出现频率由高到低排序。如果不同的单词有相同出现频率，按字母顺序排序。
 *
 * 尝试以 O(n log k) 时间复杂度和 O(n) 空间复杂度解决。
 *
 * 例 1：
 * 输入: ["i", "love", "leetcode", "i", "love", "coding"], k = 2
 * 输出: ["i", "love"]
 * 解析: "i" 和 "love" 为出现次数最多的两个单词，均为2次。
 *     注意，按字母顺序 "i" 在 "love" 之前。
 *
 * 例 2：
 * 输入: ["the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"], k = 4
 * 输出: ["the", "is", "sunny", "day"]
 * 解析: "the", "is", "sunny" 和 "day" 是出现次数最多的四个单词，
 *     出现次数依次为 4, 3, 2 和 1 次。
 *
 * 说明：
 * - 假定 k 总为有效值， 1 ≤ k ≤ 集合元素数。
 * - 输入的单词均由小写字母组成。
 */
public class E692_Medium_TopKFrequentWords {

    public static void test(BiFunction<String[], Integer, List<String>> method) {
        assertEquals(asList("i", "love"), method.apply(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2));
        assertEquals(asList("the", "is", "sunny", "day"), method.apply(new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"}, 4));
        assertEquals(singletonList("the"), method.apply(new String[]{"the", "the", "the", "the"}, 1));
        assertEquals(singletonList("i"), method.apply(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 1));
        assertEquals(asList("i", "love", "coding"), method.apply(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 3));
    }

    /**
     * 哈希表计算cnt+快速选择。参见 {@link E347_Medium_TopKFrequentElements}。
     *
     * 除此之外，还可以使用优先队列，它更适合于在线计算。
     *
     * LeetCode 耗时：6 ms - 88.40%
     *          内存消耗：38.5 MB - 68.55%
     */
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> s2cnt = new HashMap<>();
        for (String word : words) {
            s2cnt.merge(word, 1, Integer::sum);
        }

        Tuple[] tuples = new Tuple[s2cnt.size()];
        int p = 0;
        for (Map.Entry<String, Integer> entry : s2cnt.entrySet()) {
            tuples[p++] = new Tuple(entry.getKey(), entry.getValue());
        }

        int lo = 0, hi = tuples.length - 1;
        for (;;) {
            if (lo == hi) {
                break;
            }

            Tuple pivot = median3(tuples, lo, hi);
            int i = lo, j = hi;
            for (;;) {
                while (tuples[++i].compareTo(pivot) < 0);
                while (tuples[--j].compareTo(pivot) > 0);

                if (i >= j) {
                    break;
                }
                exch(tuples, i, j);
            }
            exch(tuples, lo, j);

            if (j == tuples.length - k) {
                break;
            } else if (j < tuples.length - k) {
                lo = j + 1;
            } else {
                hi = j - 1;
            }
        }

        Tuple[] topK = new Tuple[k];
        System.arraycopy(tuples, tuples.length - k, topK, 0, k);
        Arrays.sort(topK);

        List<String> result = new ArrayList<>(k);
        for (int i = k - 1; i >= 0; i--) {
            result.add(topK[i].word);
        }

        return result;
    }

    public static class Tuple implements Comparable<Tuple> {
        String word;
        int cnt;

        public Tuple(String word, int cnt) {
            this.word = word;
            this.cnt = cnt;
        }

        @Override
        public int compareTo(Tuple o) {
            int cmp = Integer.compare(cnt, o.cnt);
            return cmp != 0 ? cmp : -word.compareTo(o.word);
        }
    }

    private Tuple median3(Tuple[] tuples, int lo, int hi) {
        int mid = (lo + hi) >>> 1;
        if (tuples[lo].compareTo(tuples[mid]) > 0) {
            exch(tuples, lo, mid);
        }
        if (tuples[mid].compareTo(tuples[hi]) > 0) {
            exch(tuples, mid, hi);
        }
        if (tuples[lo].compareTo(tuples[hi]) < 0) {
            exch(tuples, lo, mid);
        }

        return tuples[lo];
    }

    private void exch(Tuple[] tuples, int i, int j) {
        Tuple temp = tuples[i];
        tuples[i] = tuples[j];
        tuples[j] = temp;
    }

    @Test
    public void testTopKFrequent() {
        test(this::topKFrequent);
    }
}
