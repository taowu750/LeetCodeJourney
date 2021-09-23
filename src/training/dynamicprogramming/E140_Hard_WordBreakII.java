package training.dynamicprogramming;

import org.junit.jupiter.api.Test;
import training.backtracking.E139_Medium_WordBreak;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 140. 单词拆分 II: https://leetcode-cn.com/problems/word-break-ii/
 *
 * 给定一个「非空」字符串 s 和一个包含「非空」单词列表的字典 wordDict，在字符串中增加空格来构建一个句子，
 * 使得句子中所有的单词都在词典中。返回所有这些可能的句子。
 *
 * 例 1：
 * 输入:
 * s = "catsanddog"
 * wordDict = ["cat", "cats", "and", "sand", "dog"]
 * 输出:
 * [
 *   "cats and dog",
 *   "cat sand dog"
 * ]
 *
 * 例 2：
 * 输入:
 * s = "pineapplepenapple"
 * wordDict = ["apple", "pen", "applepen", "pine", "pineapple"]
 * 输出:
 * [
 *   "pine apple pen apple",
 *   "pineapple pen apple",
 *   "pine applepen apple"
 * ]
 * 解释: 注意你可以重复使用字典中的单词。
 *
 * 例 3：
 * 输入:
 * s = "catsandog"
 * wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出:
 * []
 *
 * 约束：
 * - 分隔时可以重复使用字典中的单词。
 * - 你可以假设字典中没有重复的单词。
 */
public class E140_Hard_WordBreakII {

    static void test(BiFunction<String, List<String>, List<String>> method) {
        equalsIgnoreOrder(asList("cats and dog", "cat sand dog"),
                method.apply("catsanddog", asList("cat", "cats", "and", "sand", "dog")));
        equalsIgnoreOrder(asList("pine apple pen apple", "pineapple pen apple", "pine applepen apple"),
                method.apply("pineapplepenapple", asList("apple", "pen", "applepen", "pine", "pineapple")));
        equalsIgnoreOrder(emptyList(), method.apply("catsandog", asList("cats", "dog", "sand", "and", "cat")));
    }

    /**
     * 参见 {@link E139_Medium_WordBreak}。
     *
     * LeetCode 耗时：1ms - 99%
     *          内存消耗：36.6MB - 83%
     */
    public List<String> wordBreak(String s, List<String> wordDict) {
        HashSet<String> set = new HashSet<>(wordDict);
        int maxLen = 0;
        for (String word: wordDict) {
            if (word.length() > maxLen) {
                maxLen = word.length();
            }
        }

        boolean[] dp = new boolean[s.length() + 1];
        @SuppressWarnings("unchecked")
        List<String>[] dpStr = (List<String>[]) new ArrayList[s.length() + 1];
        dp[0] = true;
        for (int size = 1; size <= s.length(); size++) {
            int limit = Math.max(0, size - maxLen);
            List<String> match = null;
            for (int i = size - 1; i >= limit; i--) {
                String sub = s.substring(i, size);
                if (dp[i] && set.contains(sub)) {
                    dp[size] = true;
                    if (match == null) {
                        match = new ArrayList<>(4);
                        dpStr[size] = match;
                    }
                    match.add(sub);
                }
            }
        }

        if (dp[s.length()]) {
            List<String> result = new ArrayList<>();
            Deque<String> stack = new LinkedList<>();
            addResult(s.length(), dpStr, s.length(), stack, result);

            return result;
        } else {
            return emptyList();
        }
    }

    private void addResult(int sLen, List<String>[] dpStr, int idx, Deque<String> stack, List<String> result) {
        if (idx <= 0) {
            int i = 0;
            StringBuilder sb = new StringBuilder(sLen + stack.size() - 1);
            for (String s: stack) {
                sb.append(s);
                if (++i != stack.size()) {
                    sb.append(' ');
                }
            }
            result.add(sb.toString());
            return;
        }

        for (String s: dpStr[idx]) {
            stack.push(s);
            addResult(sLen, dpStr, idx - s.length(), stack, result);
            stack.pop();
        }
    }

    @Test
    public void testWordBreak() {
        test(this::wordBreak);
    }
}
