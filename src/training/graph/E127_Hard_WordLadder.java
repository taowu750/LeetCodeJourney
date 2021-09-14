package training.graph;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 127. 单词接龙: https://leetcode-cn.com/problems/word-ladder/
 *
 * 字典 wordList 中从单词 beginWord 和 endWord 的「转换序列」是一个按下述规格形成的序列：
 * - 序列中第一个单词是 beginWord 。
 * - 序列中最后一个单词是 endWord 。
 * - 每次转换只能改变一个字母。
 * - 转换过程中的中间单词必须是字典 wordList 中的单词。
 *
 * 给你两个单词 beginWord 和 endWord 和一个字典 wordList ，找到从 beginWord 到 endWord
 * 的「最短转换序列」中的「单词数目」。如果不存在这样的转换序列，返回 0。
 *
 * 例 1：
 * 输入：beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 * 输出：5
 * 解释：一个最短转换序列是 "hit" -> "hot" -> "dot" -> "dog" -> "cog", 返回它的长度 5。
 *
 * 例 2：
 * 输入：beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
 * 输出：0
 * 解释：endWord "cog" 不在字典中，所以无法进行转换。
 *
 * 约束：
 * - 1 <= beginWord.length <= 10
 * - endWord.length == beginWord.length
 * - 1 <= wordList.length <= 5000
 * - wordList[i].length == beginWord.length
 * - beginWord、endWord 和 wordList[i] 由小写英文字母组成
 * - beginWord != endWord
 * - wordList 中的所有字符串「互不相同」
 */
public class E127_Hard_WordLadder {

    static void test(TriFunction<String, String, List<String>, Integer> method) {
        assertEquals(5, method.apply("hit", "cog", Arrays.asList("hot","dot","dog","lot","log","cog")));
        assertEquals(0, method.apply("hit", "cog", Arrays.asList("hot","dot","dog","lot","log")));
        assertEquals(2, method.apply("a", "c", Arrays.asList("a","b","c")));
        assertEquals(3, method.apply("hot", "dog", Arrays.asList("hot", "dog", "dot")));
    }

    /**
     * BFS 解法。
     *
     * LeetCode 耗时：553 ms - 29.17%
     *          内存消耗：38.5 MB - 92.75%
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) {
            return 0;
        }

        LinkedList<String> dict = new LinkedList<>(wordList);
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);
        int level = 1;
        while (!queue.isEmpty() && !dict.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String s = queue.remove();
                Iterator<String> iter = dict.iterator();
                while (iter.hasNext()) {
                    String neighbor = iter.next();
                    if (diff(s, neighbor) == 1) {
                        if (neighbor.equals(endWord)) {
                            return ++level;
                        }
                        queue.add(neighbor);
                        iter.remove();
                    }
                }
            }
            level++;
        }

        return 0;
    }

    private int diff(String word1, String word2) {
        int cnt = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                cnt++;
            }
        }

        return cnt;
    }

    @Test
    public void testLadderLength() {
        test(this::ladderLength);
    }


    /**
     * 双向 bfs。
     *
     * LeetCode 耗时：188 ms - 37.90%
     *          内存消耗：38.7 MB - 85.77%
     */
    public int bidirectionalBfsMethod(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) {
            return 0;
        }

        // 让 begin 和 dict 分别选择单词
        LinkedList<String> beginDict = new LinkedList<>(wordList);
        LinkedList<String> endDict = new LinkedList<>(beginDict);

        Set<String> tmp;
        Set<String> begin = new HashSet<>();
        Set<String> end = new HashSet<>();
        begin.add(beginWord);
        end.add(endWord);

        int level = 0;
        while (!begin.isEmpty() && !end.isEmpty()) {
            if (begin.size() > end.size()) {
                tmp = begin;
                begin = end;
                end = tmp;

                LinkedList<String> t = beginDict;
                beginDict = endDict;
                endDict = t;
            }
            tmp = new HashSet<>();
            for (String s : begin) {
                if (end.contains(s)) {
                    return level + 1;
                }
                /*
                一种更快的方法是对 s 每个单词更改为 25 个其他字母，然后判断是不是在 wordList 中。
                因为每个 s 长度不超过 10，因此循环次数不超过 260；而下面的方法因为 wordList 长度可能有 5000，所以复杂度更高

                参见：https://leetcode-cn.com/problems/word-ladder/solution/yan-du-you-xian-bian-li-shuang-xiang-yan-du-you-2/
                 */
                Iterator<String> iter = beginDict.iterator();
                while (iter.hasNext()) {
                    String neighbor = iter.next();
                    if (diff(s, neighbor) == 1) {
                        tmp.add(neighbor);
                        iter.remove();
                    }
                }
            }
            level++;
            begin = tmp;
        }

        return 0;
    }

    @Test
    public void testBidirectionalBfsMethod() {
        test(this::bidirectionalBfsMethod);
    }
}
