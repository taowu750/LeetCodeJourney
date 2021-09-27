package training.trie;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 212. 单词搜索 II: https://leetcode-cn.com/problems/word-search-ii/
 *
 * 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，找出所有同时在二维网格和字典中出现的单词。
 * 单词必须按照字母顺序，通过「相邻的单元格」内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母在一个单词中不允许被重复使用。
 *
 * 例 1：
 * 输入：board = [["o","a","a","n"],
 *               ["e","t","a","e"],
 *               ["i","h","k","r"],
 *               ["i","f","l","v"]],
 *      words = ["oath","pea","eat","rain"]
 * 输出：["eat","oath"]
 *
 * 例 2：
 * 输入：board = [["a","b"],["c","d"]], words = ["abcb"]
 * 输出：[]
 *
 * 约束：
 * - m == board.length
 * - n == board[i].length
 * - 1 <= m, n <= 12
 * - board[i][j] 是一个小写英文字母
 * - 1 <= words.length <= 3 * 10^4
 * - 1 <= words[i].length <= 10
 * - words[i] 由小写英文字母组成
 * - words 中的所有字符串互不相同
 */
public class E212_Hard_WordSearchII {

    static void test(BiFunction<char[][], String[], List<String>> method) {
        equalsIgnoreOrder(asList("eat","oath"), method.apply(new char[][]{
                {'o','a','a','n'},
                {'e','t','a','e'},
                {'i','h','k','r'},
                {'i','f','l','v'}},
                new String[]{"oath","pea","eat","rain"}));

        equalsIgnoreOrder(emptyList(), method.apply(new char[][]{
                        {'a','b'},
                        {'c','d'}},
                new String[]{"abcb"}));

        equalsIgnoreOrder(singletonList("a"), method.apply(new char[][]{{'a'}}, new String[]{"a"}));
    }

    /**
     * LeetCode 耗时：1633 ms - 10.14%
     *          内存消耗：36.8 MB - 91.52%
     */
    public List<String> findWords(char[][] board, String[] words) {
        int m = board.length, n = board[0].length, size = m * n;
        boolean[][] visited = new boolean[m][n];
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (word.length() > size) {
                continue;
            }
            OUTER: for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (board[i][j] == word.charAt(0) && dfs(board, word, visited, 1, i, j)) {
                        result.add(word);
                        break OUTER;
                    }
                }
            }
        }

        return result;
    }

    private static final int[][] DIRS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private boolean dfs(char[][] board, String word, boolean[][] visited, int nextIdx, int i, int j) {
        if (nextIdx == word.length()) {
            return true;
        }

        boolean result = false;
        visited[i][j] = true;
        for (int[] dir : DIRS) {
            int r = i + dir[0], c = j + dir[1];
            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length
                    && word.charAt(nextIdx) == board[r][c] && !visited[r][c]
                    && dfs(board, word, visited, nextIdx + 1, r, c)) {
                result = true;
                break;
            }
        }
        visited[i][j] = false;

        return result;
    }

    @Test
    public void testFindWords() {
        test(this::findWords);
    }


    /**
     * 枚举法，枚举所有可能的单词，判断 words 中的单词在不在里面。
     *
     * LeetCode 耗时：513 ms - 30.30%
     *          内存消耗：79.4 MB - 5.00%
     */
    public List<String> enumMethod(char[][] board, String[] words) {
        int m = board.length, n = board[0].length, size = m * n;
        Set<Integer> lens = new HashSet<>();
        Set<Character> heads = new HashSet<>();
        int maxLen = 0;
        for (String word : words) {
            if (word.length() > size) {
                continue;
            }
            lens.add(word.length());
            if (maxLen < word.length()) {
                maxLen = word.length();
            }
            heads.add(word.charAt(0));
        }

        Set<String> allWords = new HashSet<>();
        boolean[][] visited = new boolean[m][n];
        StringBuilder word = new StringBuilder(maxLen);
        for (int len : lens) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    // 一个优化，只从首字母开始枚举
                    if (!heads.contains(board[i][j])) {
                        continue;
                    }
                    enumWord(board, visited, allWords, word, len, i, j);
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (String s : words) {
            if (allWords.contains(s)) {
                result.add(s);
            }
        }

        return result;
    }

    private void enumWord(char[][] board, boolean[][] visited, Set<String> allWords, StringBuilder word,
                          int len, int i, int j) {
        visited[i][j] = true;
        word.append(board[i][j]);
        if (len == 1) {
            allWords.add(word.toString());
        } else {
            for (int[] dir : DIRS) {
                int r = i + dir[0], c = j + dir[1];
                if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && !visited[r][c]) {
                    enumWord(board, visited, allWords, word, len - 1, r, c);
                }
            }
        }
        visited[i][j] = false;
        word.deleteCharAt(word.length() - 1);
    }

    @Test
    public void testEnumMethod() {
        test(this::enumMethod);
    }


    /**
     * 和上面的枚举法相反。
     *
     * LeetCode 耗时：519 ms - 29.96%
     *          内存消耗：39.2 MB - 15.13%
     */
    public List<String> reverseEnumMethod(char[][] board, String[] words) {
        int m = board.length, n = board[0].length, size = m * n;
        Set<Integer> lens = new HashSet<>();
        Set<Character> heads = new HashSet<>();
        Set<String> wordSet = new HashSet<>();
        int maxLen = 0;
        for (String word : words) {
            if (word.length() > size) {
                continue;
            }
            lens.add(word.length());
            if (maxLen < word.length()) {
                maxLen = word.length();
            }
            heads.add(word.charAt(0));
            wordSet.add(word);
        }

        boolean[][] visited = new boolean[m][n];
        StringBuilder word = new StringBuilder(maxLen);
        Set<String> result = new HashSet<>();
        for (int len : lens) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    // 一个优化，只从首字母开始枚举
                    if (!heads.contains(board[i][j])) {
                        continue;
                    }
                    reverseEnumWord(board, visited, wordSet, result, word, len, i, j);
                }
            }
        }

        return new ArrayList<>(result);
    }

    private void reverseEnumWord(char[][] board, boolean[][] visited, Set<String> wordSet, Set<String> result,
                                 StringBuilder word, int len, int i, int j) {
        visited[i][j] = true;
        word.append(board[i][j]);
        if (len == 1) {
            String s = word.toString();
            if (wordSet.contains(s)) {
                result.add(s);
            }
        } else {
            for (int[] dir : DIRS) {
                int r = i + dir[0], c = j + dir[1];
                if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && !visited[r][c]) {
                    reverseEnumWord(board, visited, wordSet, result, word, len - 1, r, c);
                }
            }
        }
        visited[i][j] = false;
        word.deleteCharAt(word.length() - 1);
    }

    @Test
    public void testReverseEnumWord() {
        test(this::reverseEnumMethod);
    }


    /**
     * 回溯+前缀树。
     *
     * 用了下面两个优化技巧后，时间从 250 ms 缩减到了 12 ms。
     *
     * LeetCode 耗时：12 ms - 96.33%
     *          内存消耗：38.3 MB - 42.62%
     */
    public List<String> trieMethod(char[][] board, String[] words) {
        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        List<String> result = new ArrayList<>();
        Trie root = new Trie(Arrays.asList(words));

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                trieDfs(result, board, root, visited, i, j);
            }
        }

        return result;
    }

    private void trieDfs(List<String> result, char[][] board, Trie root,
                         boolean[][] visited, int i, int j) {
        Trie cur = root.children.get(board[i][j]);
        if (cur == null) {
            return;
        }

        if (cur.word != null) {
            result.add(cur.word);
            // 优化 1：将匹配到的单词从前缀树中移除，来避免重复寻找相同的单词。
            // 因为这种方法可以保证每个单词只能被匹配一次；所以我们也不需要再对结果集去重了
            cur.word = null;
        }

        // 优化 2：将没有单词的前缀也移除，缩减后续的查找范围
        if (!cur.children.isEmpty()) {
            visited[i][j] = true;
            for (int[] dir : DIRS) {
                int r = i + dir[0], c = j + dir[1];
                if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && !visited[r][c]) {
                    trieDfs(result, board, cur, visited, r, c);
                }
            }
            visited[i][j] = false;
        } else {
            root.children.remove(board[i][j]);
        }
    }

    public static class Trie {

        public String word;
        public Map<Character, Trie> children;

        Trie() {
            children = new HashMap<>();
        }

        Trie(Collection<String> words) {
            children = new HashMap<>();
            for (String word : words) {
                insert(word);
            }
        }

        public void insert(String word) {
            Trie cur = this;
            for (int i = 0; i < word.length(); i++) {
                cur = cur.children.computeIfAbsent(word.charAt(i), ch -> new Trie());
            }
            cur.word = word;
        }

        public Trie longestPrefix(CharSequence word) {
            Trie cur = this, parent = cur;
            for (int i = 0; i < word.length() && cur != null; i++) {
                char c = word.charAt(i);
                parent = cur;
                cur = children.get(c);
            }

            return parent;
        }
    }

    @Test
    public void testTrie() {
        test(this::trieMethod);
    }
}
