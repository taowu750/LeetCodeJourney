package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 139. 单词拆分：https://leetcode-cn.com/problems/word-break/
 *
 * 给定一个「非空」字符串 s 和一个包含「非空」单词的列表 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。
 *
 * 例 1：
 * 输入: s = "leetcode", wordDict = ["leet", "code"]
 * 输出: true
 * 解释: 返回 true 因为 "leetcode" 可以被拆分成 "leet code"。
 *
 * 例 2：
 * 输入: s = "applepenapple", wordDict = ["apple", "pen"]
 * 输出: true
 * 解释: 返回 true 因为 "applepenapple" 可以被拆分成 "apple pen apple"。
 *      注意你可以重复使用字典中的单词。
 *
 * 例 3：
 * 输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出: false
 *
 * 约束：
 * - 拆分时可以重复使用字典中的单词。
 * - 你可以假设字典中没有重复的单词。
 */
public class E139_Medium_WordBreak {

    static void test(BiPredicate<String, List<String>> method) {
        assertTrue(method.test("leetcode", Arrays.asList("leet", "code")));
        assertTrue(method.test("applepenapple", Arrays.asList("apple", "pen")));
        assertFalse(method.test("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));
        assertFalse(method.test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
                Arrays.asList("a","aa","aaa","aaaa","aaaaa")));
    }

    /**
     * 超时。
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        return dfs(s, wordDict, 0);
    }

    /**
     * dfs，枚举 wordDict 列表。
     */
    private boolean dfs(String s, List<String> wordDict, int start) {
        if (start >= s.length()) {
            return true;
        }

        for (String word : wordDict) {
            if (s.startsWith(word, start)) {
                if (dfs(s, wordDict, start + word.length())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Test
    public void testWordBreak() {
        test(this::wordBreak);
    }


    /**
     * 从 s 出发使用 dfs，能够用上 cache 和 hash 优化，并且最终可以导出动态规划方法。
     * 参见：https://leetcode-cn.com/problems/word-break/solution/shou-hui-tu-jie-san-chong-fang-fa-dfs-bfs-dong-tai/
     */
    public boolean betterDfs(String s, List<String> wordDict) {
        Set<String> words = new HashSet<>(wordDict);
        Map<Integer, Boolean> cache = new HashMap<>();
        return dfs(s, words, cache, 0);
    }

    private boolean dfs(String s, Set<String> words, Map<Integer, Boolean> cache, int start) {
        if (start >= s.length()) {
            return true;
        }

        if (cache.containsKey(start)) {
            return cache.get(start);
        }

        boolean result = false;
        for (int end = start + 1; end <= s.length(); end++) {
            if (words.contains(s.substring(start, end)) && dfs(s, words, cache, end)) {
                result = true;
                break;
            }
        }
        cache.put(start, result);

        return result;
    }

    @Test
    public void testBetterDfs() {
        test(this::betterDfs);
    }


    /**
     * 参见：https://leetcode-cn.com/problems/word-break/solution/dan-ci-chai-fen-by-leetcode-solution/
     *
     * LeetCode 耗时：1 ms - 100%
     *          内存消耗：36.4 MB - 98%
     */
    public boolean dpMethod(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        int maxWordLength = 0;
        for (String word : wordDict) {
            if (word.length() > maxWordLength) {
                maxWordLength = word.length();
            }
        }

        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int len = 1; len <= s.length(); len++) {
            // 剪枝
            int limit = Math.max(0, len - maxWordLength);
            for (int i = len - 1; i >= limit; i--) {
                if (dp[i] && set.contains(s.substring(i, len))) {
                    dp[len] = true;
                    break;
                }
            }
        }

        return dp[s.length()];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }
}
