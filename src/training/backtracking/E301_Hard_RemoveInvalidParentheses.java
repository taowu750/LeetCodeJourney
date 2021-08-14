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
 * <p>
 * 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效。
 * 返回所有可能的结果。答案可以按「任意顺序」返回。
 * <p>
 * 例 1：
 * 输入：s = "()())()"
 * 输出：["(())()","()()()"]
 * <p>
 * 例 2：
 * 输入：s = "(a)())()"
 * 输出：["(a())()","(a)()()"]
 * <p>
 * 例 3：
 * 输入：s = ")("
 * 输出：[""]
 * <p>
 * 约束：
 * - 1 <= s.length <= 25
 * - s 由小写英文字母以及括号 '(' 和 ')' 组成
 * - s 中至多含 20 个括号
 */
public class E301_Hard_RemoveInvalidParentheses {

    static void test(Function<String, List<String>> method) {
        equalsIgnoreOrder(asList("(a())()", "(a)()()"), method.apply("(a)())()"));
        equalsIgnoreOrder(asList("(())()", "()()()"), method.apply("()())()"));
        equalsIgnoreOrder(singletonList(""), method.apply(")("));
        equalsIgnoreOrder(singletonList(""), method.apply("(((("));
        equalsIgnoreOrder(singletonList("abcdef"), method.apply("abcdef"));
        equalsIgnoreOrder(asList("(())()", "()()()"), method.apply("()()))()"));
        equalsIgnoreOrder(singletonList("()"), method.apply(")()("));
    }

    /**
     * LeetCode 耗时：109 ms - 16.34%
     * 内存消耗：38.4 MB - 78.31%
     */
    public List<String> removeInvalidParentheses(String s) {
        // 计算需要删除的左括号，右括号数量
        int leftRemove = 0, rightRemove = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                leftRemove++;
            } else if (ch == ')') {
                // 前面有左括号，则右括号可以配对
                if (leftRemove > 0) {
                    leftRemove--;
                } else {
                    rightRemove++;
                }
            }
        }

        StringBuilder sb = new StringBuilder(s.length());
        Set<String> result = new HashSet<>();
        dfs(s, 0, result, sb, leftRemove, rightRemove, 0);

        return new ArrayList<>(result);
    }

    private void dfs(String s, int idx, Set<String> result, StringBuilder sb,
                     int leftRemove, int rightRemove, int lrDiff) {
        if (idx >= s.length()) {
            if (lrDiff == 0) {
                result.add(sb.toString());
            }
            return;
        }

        char ch = s.charAt(idx);
        if (ch == '(') {
            sb.append('(');
            dfs(s, idx + 1, result, sb, leftRemove, rightRemove, lrDiff + 1);
            sb.deleteCharAt(sb.length() - 1);
            if (--leftRemove >= 0) {
                dfs(s, idx + 1, result, sb, leftRemove, rightRemove, lrDiff);
            }
        } else if (ch == ')') {
            // 必须保证 ( 数量大于等于 ) 才能构成合法序列
            if (lrDiff - 1 >= 0) {
                sb.append(')');
                dfs(s, idx + 1, result, sb, leftRemove, rightRemove, lrDiff - 1);
                sb.deleteCharAt(sb.length() - 1);
            }
            if (--rightRemove >= 0) {
                dfs(s, idx + 1, result, sb, leftRemove, rightRemove, lrDiff);
            }
        } else {
            sb.append(ch);
            dfs(s, idx + 1, result, sb, leftRemove, rightRemove, lrDiff);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Test
    public void testRemoveInvalidParentheses() {
        test(this::removeInvalidParentheses);
    }


    /**
     * 更好的 dfs 方法。
     *
     * LeetCode 耗时：2 ms - 97.06%
     *          内存消耗：38.4 MB - 73.99%
     */
    public List<String> betterDfs(String s) {
        // 计算需要删除的左括号，右括号数量
        int leftRemove = 0, rightRemove = 0, leftCnt = 0, rightCnt = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                leftRemove++;
                leftCnt++;
            } else if (ch == ')') {
                // 前面有左括号，则右括号可以配对
                if (leftRemove > 0) {
                    leftRemove--;
                } else {
                    rightRemove++;
                }
                rightCnt++;
            }
        }

        StringBuilder sb = new StringBuilder(s.length());
        Set<String> result = new HashSet<>();
        betterDfs(s, 0, result, sb, 0, 0, leftRemove, rightRemove, leftCnt, rightCnt);

        return new ArrayList<>(result);
    }

    /**
     * cnt 表示已添加的括号数量，remove 表示待删除的括号数量，remain 表示 [idx, len) 中剩余的括号数量。
     */
    private void betterDfs(String s, int idx, Set<String> result, StringBuilder sb,
                           int leftCnt, int rightCnt, int leftRemove, int rightRemove, int leftRemain, int rightRemain) {
        if (idx == s.length()) {
            result.add(sb.toString());
            return;
        }

        char ch = s.charAt(idx);
        switch (ch) {
            case '(':
                // 剪枝操作
                // 保证剩下的括号数量要足够减，才能添加括号到路径中
                if (leftRemain - 1 >= leftRemove) {
                    sb.append(ch);
                    betterDfs(s, idx + 1, result, sb, leftCnt + 1, rightCnt, leftRemove, rightRemove,
                            leftRemain - 1, rightRemain);
                    sb.deleteCharAt(sb.length() - 1);
                }
                if (leftRemove >= 1) {
                    betterDfs(s, idx + 1, result, sb, leftCnt, rightCnt, leftRemove - 1, rightRemove,
                            leftRemain - 1, rightRemain);
                }
                break;

            case ')':
                // 右括号除了保证剩下的括号数量要足够减，还要保证当前右括号数量小于左括号数量
                if (rightRemain - 1 >= rightRemove && leftCnt >= rightCnt + 1) {
                    sb.append(ch);
                    betterDfs(s, idx + 1, result, sb, leftCnt, rightCnt + 1, leftRemove, rightRemove,
                            leftRemain, rightRemain - 1);
                    sb.deleteCharAt(sb.length() - 1);
                }
                if (rightRemove >= 1) {
                    betterDfs(s, idx + 1, result, sb, leftCnt, rightCnt, leftRemove, rightRemove - 1,
                            leftRemain, rightRemain - 1);
                }
                break;

            default:
                sb.append(ch);
                betterDfs(s, idx + 1, result, sb, leftCnt, rightCnt, leftRemove, rightRemove, leftRemain, rightRemain);
                sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Test
    public void testBetterDfs() {
        test(this::betterDfs);
    }
}
