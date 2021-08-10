package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 301. 删除无效的括号: https://leetcode-cn.com/problems/remove-invalid-parentheses/
 *
 * 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效。
 * 返回所有可能的结果。答案可以按「任意顺序」返回。
 *
 * 例 1：
 * 输入：s = "()())()"
 * 输出：["(())()","()()()"]
 *
 * 例 2：
 * 输入：s = "(a)())()"
 * 输出：["(a())()","(a)()()"]
 *
 * 例 3：
 * 输入：s = ")("
 * 输出：[""]
 *
 * 约束：
 * - 1 <= s.length <= 25
 * - s 由小写英文字母以及括号 '(' 和 ')' 组成
 * - s 中至多含 20 个括号
 */
public class E301_Hard_RemoveInvalidParentheses {

    static void test(Function<String, List<String>> method) {
        equalsIgnoreOrder(asList("(a())()","(a)()()"), method.apply("(a)())()"));
        equalsIgnoreOrder(asList("(())()","()()()"), method.apply("()())()"));
        equalsIgnoreOrder(singletonList(""), method.apply(")("));
        equalsIgnoreOrder(singletonList(""), method.apply("(((("));
        equalsIgnoreOrder(singletonList("abcdef"), method.apply("abcdef"));
        equalsIgnoreOrder(asList("(())()","()()()"), method.apply("()()))()"));
    }

    /**
     * LeetCode 耗时：117 ms - 11.47%
     *          内存消耗：38.4 MB - 78.31%
     */
    public List<String> removeInvalidParentheses(String s) {
        // 去掉小写字母
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(' || ch == ')') {
                sb.append(ch);
            }
        }
        // 计算合法括号序列的长度，算法参见 E32_Hard_LongestValidParentheses
        int validLen = 0;
        String parentheses = sb.toString();
        int[] dp = new int[parentheses.length()];
        for (int i = 1; i < parentheses.length(); i++) {
            char c = parentheses.charAt(i);
            if (c == ')') {
                if (parentheses.charAt(i - 1) == '(') {
                    dp[i] = 2 + (i >= 2 ? dp[i - 2] : 0);
                    validLen += 2;
                } else if (dp[i - 1] > 0 && dp[i - 1] <= i - 1 && parentheses.charAt(i - 1 - dp[i - 1]) == '(') {
                    int prev = i - 1 - dp[i - 1] - 1;
                    dp[i] = 2 + dp[i - 1] + (prev >= 0 ? dp[prev] : 0);
                    validLen += 2;
                }
            }
        }
        // 需要删除的括号的数量
        int needRemove = dp.length - validLen;

        sb.setLength(0);
        Set<String> result = new HashSet<>();
        dfs(s, 0, result, sb, needRemove, 0);

        return new ArrayList<>(result);
    }

    private void dfs(String s, int idx, Set<String> result, StringBuilder sb, int needRemove, int lrDiff) {
        if (idx >= s.length()) {
            if (lrDiff == 0) {
                result.add(sb.toString());
            }
            return;
        }

        char ch = s.charAt(idx);
        if (ch == '(') {
            sb.append('(');
            dfs(s, idx + 1, result, sb, needRemove, lrDiff + 1);
            sb.deleteCharAt(sb.length() - 1);
            if (--needRemove >= 0) {
                dfs(s, idx + 1, result, sb, needRemove, lrDiff);
            }
        } else if (ch == ')') {
            // 必须保证 ( 数量大于等于 ) 才能构成合法序列
            if (lrDiff - 1 >= 0) {
                sb.append(')');
                dfs(s, idx + 1, result, sb, needRemove, lrDiff - 1);
                sb.deleteCharAt(sb.length() - 1);
            }
            if (--needRemove >= 0) {
                dfs(s, idx + 1, result, sb, needRemove, lrDiff);
            }
        } else {
            sb.append(ch);
            dfs(s, idx + 1, result, sb, needRemove, lrDiff);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Test
    public void testRemoveInvalidParentheses() {
        test(this::removeInvalidParentheses);
    }
}
