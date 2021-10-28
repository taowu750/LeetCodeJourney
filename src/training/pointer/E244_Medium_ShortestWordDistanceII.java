package training.pointer;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 244. 最短单词距离 II: https://leetcode-cn.com/problems/shortest-word-distance-ii/
 *
 * 请设计一个类，使该类的构造函数能够接收一个单词列表。然后再实现一个方法，该方法能够分别接收两个单词
 * word1 和 word2，并返回列表中这两个单词之间的最短距离。您的方法将被以不同的参数调用多次。
 *
 * 注意，单词列表中的单词可能重复。你可以假设 word1 不等于 word2, 并且 word1 和 word2 都在列表里。
 *
 * 例 1：
 * 假设 words = ["practice", "makes", "perfect", "coding", "makes"]
 *
 * 输入: word1 = “coding”, word2 = “practice”
 * 输出: 3
 *
 * 输入: word1 = "makes", word2 = "coding"
 * 输出: 1
 */
public class E244_Medium_ShortestWordDistanceII {

    public static void test(Function<String[], IWordDistance> factory) {
        IWordDistance wordDistance = factory.apply(new String[]{"practice", "makes", "perfect", "coding", "makes"});
        assertEquals(3, wordDistance.shortest("coding", "practice"));
        assertEquals(1, wordDistance.shortest("makes", "coding"));
    }

    @Test
    public void testWordDistance() {
        test(WordDistance::new);
    }
}

interface IWordDistance {

    int shortest(String word1, String word2);
}

/**
 * LeetCode 耗时：27 ms - 88.16%
 *          内存消耗：44.8 MB - 77.30%
 */
class WordDistance implements IWordDistance {

    private Map<String, List<Integer>> s2idx;

    public WordDistance(String[] wordsDict) {
        s2idx = new HashMap<>();
        for (int i = 0; i < wordsDict.length; i++) {
            s2idx.computeIfAbsent(wordsDict[i], k -> new ArrayList<>(4)).add(i);
        }
    }

    public int shortest(String word1, String word2) {
        List<Integer> wl1 = s2idx.get(word1), wl2 = s2idx.get(word2);
        int result = Integer.MAX_VALUE;
        for (int i = 0, j = 0; i < wl1.size() && j < wl2.size();) {
            result = Math.min(result, Math.abs(wl1.get(i) - wl2.get(j)));
            if (wl1.get(i) < wl2.get(j)) {
                i++;
            } else {
                j++;
            }
        }

        return result;
    }
}