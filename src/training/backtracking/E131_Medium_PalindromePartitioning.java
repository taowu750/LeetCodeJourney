package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.deepEqualsIgnoreOutOrder;

/**
 * 131. 分割回文串: https://leetcode-cn.com/problems/palindrome-partitioning/
 *
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
 *
 * 例 1：
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 *
 * 例 2：
 * 输入：s = "a"
 * 输出：[["a"]]
 *
 * 约束：
 * - 1 <= s.length <= 16
 * - s 仅由小写英文字母组成
 */
public class E131_Medium_PalindromePartitioning {

    static void test(Function<String, List<List<String>>> method) {
        deepEqualsIgnoreOutOrder(asList(asList("a","a","b"), asList("aa","b")), method.apply("aab"));
        deepEqualsIgnoreOutOrder(singletonList(singletonList("a")), method.apply("a"));
        deepEqualsIgnoreOutOrder(asList(asList("b", "a", "b", "a", "d"), asList("b", "aba", "d"), asList("bab", "a", "d")),
                method.apply("babad"));
    }

    /**
     * 中心扩散法预处理+回溯。中心扩散法参见 {@link training.string.E647_Medium_PalindromicSubstrings}。
     *
     * LeetCode 耗时：11 ms - 27.61%
     *          内存消耗：51 MB - 90.94%
     */
    public List<List<String>> partition(String s) {
        // isPalindrome[i][j] 表示 s[i..j] 是不是回文串
        int n = s.length();
        boolean[][] isPalindrome = new boolean[n][n];
        // 中心扩散法
        for (int center = 0; center < 2 * n - 1; center++) {
            int left = center / 2, right = left + center % 2;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                isPalindrome[left][right] = true;
                left--;
                right++;
            }
        }

        List<List<String>> result = new ArrayList<>();
        dfs(s, 0, isPalindrome, result, new ArrayList<>(n));

        return result;
    }

    private void dfs(String s, int i, boolean[][] isPalindrome, List<List<String>> result, ArrayList<String> path) {
        if (i == s.length()) {
            //noinspection unchecked
            result.add((List<String>) path.clone());
            return;
        }


        for (int j = i; j < s.length(); j++) {
            if (isPalindrome[i][j]) {
                path.add(s.substring(i, j + 1));
                dfs(s, j + 1, isPalindrome, result, path);
                path.remove(path.size() - 1);
            }
        }
    }

    @Test
    public void testPartition() {
        test(this::partition);
    }
}
