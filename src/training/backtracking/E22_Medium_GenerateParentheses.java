package training.backtracking;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntFunction;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 22. 括号生成: https://leetcode-cn.com/problems/generate-parentheses/
 *
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且「有效的」括号组合。
 *
 * 例 1：
 * 输入：n = 3
 * 输出：["((()))","(()())","(())()","()(())","()()()"]
 *
 * 例 2：
 * 输入：n = 1
 * 输出：["()"]
 *
 * 约束：
 * - 1 <= n <= 8
 */
public class E22_Medium_GenerateParentheses {

    static void test(IntFunction<List<String>> method) {
        List<String> result = method.apply(3);
        System.out.println(result);
        assertTrue(CollectionUtil.setEquals(result,
                Arrays.asList("((()))","(()())","(())()","()(())","()()()")));

        assertTrue(CollectionUtil.setEquals(method.apply(1),
                singletonList("()")));

        result = method.apply(4);
        System.out.println(result);
        assertTrue(CollectionUtil.setEquals(result,
                Arrays.asList("(())(())","(((())))","((()()))","((())())","((()))()","(()(()))","(()()())",
                        "(()())()","(())()()","()((()))","()(()())","()(())()","()()(())","()()()()")));
    }

    /**
     * 括号问题有两个性质：
     * 1. 一个「合法」括号组合的左括号数量一定等于右括号数量，这个显而易见。
     * 2. 对于一个「合法」的括号字符串组合p，必然对于任何 0 <= i < len(p) 都有：
     *    子串 p[0..i] 中左括号的数量都大于或等于右括号的数量。
     *    因为从左往右算的话，肯定是左括号多嘛，到最后左右括号数量相等，说明这个括号组合是合法的。
     *
     * 此问题可以修改成这样的形式：现在有 2n 个位置，每个位置可以放置字符(或者)，组成的所有括号组合中，有多少个是合法的？
     *
     * 那么我们先想想如何得到全部 2^(2n) 种组合，然后再根据我们刚才总结出的合法括号组合的性质筛选出合法的组合。
     * 我们可以记录当前剩余左括号数量 left，当前剩余右括号数量 right：
     * 1. 当 left > right，那就不合法。
     * 2. 当 left < 0 || right < 0，那也不行
     * 3. 当 left == 0 && right == 0，所有括号正好用完，这就是合法的。
     *
     * 在每一步，我们可以尝试添加左括号或右括号试试。
     *
     * LeetCode 耗时：1 ms - 96.47%
     *          内存消耗：38.3 MB - 96.64%
     */
    public List<String> generateParenthesis(int n) {
        List<String> result = new LinkedList<>();
        backtrack(result, new StringBuilder(2 * n), n, n);
        return result;
    }

    private void backtrack(List<String> result, StringBuilder sb, int left, int right) {
        if (left > right)
            return;
        if (left < 0)
            return;
        if (left == 0 && right == 0) {
            result.add(sb.toString());
            return;
        }

        sb.append("(");
        backtrack(result, sb, left - 1, right);
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");
        backtrack(result, sb, left, right - 1);
        sb.deleteCharAt(sb.length() - 1);
    }

    @Test
    public void testGenerateParenthesis() {
        test(this::generateParenthesis);
    }
}
