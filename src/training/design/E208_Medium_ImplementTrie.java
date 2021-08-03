package training.design;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 208. 实现 Trie (前缀树): https://leetcode-cn.com/problems/implement-trie-prefix-tree/
 *
 * Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。
 * 这一数据结构有相当多的应用情景，例如自动补完和拼写检查。
 *
 * 请你实现 Trie 类：
 * - Trie() 初始化前缀树对象。
 * - void insert(String word) 向前缀树中插入字符串 word 。
 * - boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
 * - boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；
 *                                     否则，返回 false 。
 *
 * 例 1：
 * 输入
 * ["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
 * [[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
 * 输出
 * [null, null, true, false, true, null, true]
 * 解释
 * Trie trie = new Trie();
 * trie.insert("apple");
 * trie.search("apple");   // 返回 True
 * trie.search("app");     // 返回 False
 * trie.startsWith("app"); // 返回 True
 * trie.insert("app");
 * trie.search("app");     // 返回 True
 *
 * 约束：
 * - 1 <= word.length, prefix.length <= 2000
 * - word 和 prefix 仅由小写英文字母组成
 * - insert、search 和 startsWith 调用次数 总计 不超过 3 * 10^4 次
 */
public class E208_Medium_ImplementTrie {

    static void test(Supplier<ITrie> factory) {
        ITrie trie = factory.get();
        trie.insert("apple");
        assertTrue(trie.search("apple"));   // 返回 True
        assertFalse(trie.search("app"));     // 返回 False
        assertTrue(trie.startsWith("app")); // 返回 True
        trie.insert("app");
        assertTrue(trie.search("app"));     // 返回 True
    }

    @Test
    public void testTrie() {
        test(Trie::new);
    }
}

interface ITrie {

    void insert(String word);

    boolean search(String word);

    boolean startsWith(String prefix);
}

/**
 * LeetCode 耗时：42 ms - 48%
 *          内存消耗：47.6 MB - 57%
 */
class Trie implements ITrie {

    private static class Node {
        private Map<Character, Node> branch = new HashMap<>(8);
        private boolean isWord;
    }

    private Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String word) {
        Node p = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            Node child = p.branch.get(ch);
            if (child == null) {
                child = new Node();
                p.branch.put(ch, child);
            }
            p = child;
        }
        p.isWord = true;
    }

    public boolean search(String word) {
        Node p = root;
        for (int i = 0; i < word.length(); i++) {
            Node child = p.branch.get(word.charAt(i));
            if (child == null) {
                return false;
            }
            p = child;
        }

        return p.isWord;
    }

    public boolean startsWith(String prefix) {
        Node p = root;
        for (int i = 0; i < prefix.length(); i++) {
            Node child = p.branch.get(prefix.charAt(i));
            if (child == null) {
                return false;
            }
            p = child;
        }

        return true;
    }
}
