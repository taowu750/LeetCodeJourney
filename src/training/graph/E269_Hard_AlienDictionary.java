package training.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 269. 火星词典: https://leetcode-cn.com/problems/alien-dictionary/
 *
 * 现有一种使用英语字母的火星语言，这门语言的字母顺序与英语顺序不同。
 *
 * 给你一个字符串列表 words，作为这门语言的词典，words 中的字符串已经「按这门新语言的字母顺序进行了排序」。
 *
 * 请你根据该词典还原出此语言中已知的字母顺序，并「按字母递增顺序」排列。若不存在合法字母顺序，返回 "" 。
 * 若存在多种可能的合法字母顺序，返回其中「任意一种」顺序即可。
 *
 * 字符串 s 「字典顺序小于」字符串 t 有两种情况：
 * - 在第一个不同字母处，如果 s 中的字母在这门外星语言的字母顺序中位于 t 中字母之前，那么 s 的字典顺序小于 t 。
 * - 如果前面 min(s.length, t.length) 字母都相同，那么 s.length < t.length 时，s 的字典顺序也小于 t 。
 *
 * 例 1：
 * 输入：words = ["wrt","wrf","er","ett","rftt"]
 * 输出："wertf"
 *
 * 例 2：
 * 输入：words = ["z","x"]
 * 输出："zx"
 *
 * 例 3：
 * 输入：words = ["z","x","z"]
 * 输出：""
 * 解释：不存在合法字母顺序，因此返回 "" 。
 *
 * 约束：
 * - 1 <= words.length <= 100
 * - 1 <= words[i].length <= 100
 * - words[i] 仅由小写英文字母组成
 */
public class E269_Hard_AlienDictionary {

    public static void test(Function<String[], String> method) {
        assertEquals("wertf", method.apply(new String[]{"wrt","wrf","er","ett","rftt"}));
        assertEquals("zx", method.apply(new String[]{"z","x"}));
        assertEquals("", method.apply(new String[]{"z","x","z"}));
        assertEquals("", method.apply(new String[]{"abc","ab"}));
    }

    /**
     * LeetCode 耗时：2 ms - 99.31%
     *          内存消耗：36.1 MB - 96.53%
     */
    public String alienOrder(String[] words) {
        //noinspection unchecked
        List<Integer>[] graph = new List[26];
        for (int i = 0; i < words[0].length(); i++) {
            int idx = words[0].charAt(i) - 'a';
            if (graph[idx] == null) {
                graph[idx] = new ArrayList<>();
            }
        }

        for (int i = 1; i < words.length; i++) {
            int j = 0;
            for (; j < words[i].length(); j++) {
                if (j >= words[i - 1].length()) {
                    int idx = words[i].charAt(j) - 'a';
                    if (graph[idx] == null) {
                        graph[idx] = new ArrayList<>();
                    }
                } else if (words[i].charAt(j) != words[i - 1].charAt(j)) {
                    break;
                }
            }
            if (j != words[i].length()) {
                int idx = words[i].charAt(j) - 'a';
                if (graph[idx] == null) {
                    graph[idx] = new ArrayList<>();
                }
                graph[words[i - 1].charAt(j) - 'a'].add(idx);

                for (j++; j < words[i].length(); j++) {
                    idx = words[i].charAt(j) - 'a';
                    if (graph[idx] == null) {
                        graph[idx] = new ArrayList<>();
                    }
                }
            } else if (words[i - 1].length() > words[i].length()) {
                return "";
            }
        }

        if (hasCycle(graph)) {
            return "";
        }

        return topicSort(graph);
    }

    private boolean hasCycle(List<Integer>[] graph) {
        boolean[] visited = new boolean[26], onStack = new boolean[26];
        for (int v = 0; v < graph.length; v++) {
            if (!visited[v] && hasCycle(graph, visited, onStack, v)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycle(List<Integer>[] graph, boolean[] visited, boolean[] onStack, int v) {
        if (graph[v] == null) {
            return false;
        }

        visited[v] = true;
        onStack[v] = true;
        for (int w: graph[v]) {
            if (!visited[w]) {
                if (hasCycle(graph, visited, onStack, w)) {
                    return true;
                }
            } else if (onStack[w]) {
                return true;
            }
        }
        onStack[v] = false;

        return false;
    }

    private String topicSort(List<Integer>[] graph) {
        boolean[] visited = new boolean[26];
        Deque<Integer> stack = new ArrayDeque<>(26);
        for (int v = 0; v < graph.length; v++) {
            if (!visited[v]) {
                topicSort(graph, visited, stack, v);
            }
        }

        StringBuilder result = new StringBuilder(26);
        for (int c : stack) {
            result.append((char) (c + 'a'));
        }

        return result.toString();
    }

    private void topicSort(List<Integer>[] graph, boolean[] visited, Deque<Integer> stack, int v) {
        if (graph[v] == null) {
            return;
        }

        visited[v] = true;
        for (int w : graph[v]) {
            if (!visited[w]) {
                topicSort(graph, visited, stack, w);
            }
        }
        stack.addFirst(v);
    }

    @Test
    public void testAlienOrder() {
        test(this::alienOrder);
    }
}
