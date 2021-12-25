package training.stack;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1190. 反转每对括号间的子串: https://leetcode-cn.com/problems/reverse-substrings-between-each-pair-of-parentheses/
 *
 * 给出一个字符串 s（仅含有小写英文字母和括号）。
 * 请你按照从括号内到外的顺序，逐层反转每对匹配括号中的字符串，并返回最终的结果。
 * 注意，您的结果中「不应」包含任何括号。
 *
 * 例 1：
 * 输入：s = "(abcd)"
 * 输出："dcba"
 *
 * 例 2：
 * 输入：s = "(u(love)i)"
 * 输出："iloveu"
 * 解释：先反转子字符串 "love" ，然后反转整个字符串。
 *
 * 例 3：
 * 输入：s = "(ed(et(oc))el)"
 * 输出："leetcode"
 * 解释：先反转子字符串 "oc" ，接着反转 "etco" ，然后反转整个字符串。
 *
 * 例 4：
 * 输入：s = "a(bcdefghijkl(mno)p)q"
 * 输出："apmnolkjihgfedcbq"
 *
 * 约束：
 * - 0 <= s.length <= 2000
 * - s 中只有小写英文字母和括号
 * - 题目测试用例确保所有括号都是成对出现的
 */
public class E1190_Medium_ReverseSubstringsBetweenEachPairOfParentheses {

    public static void test(UnaryOperator<String> method) {
        assertEquals("dcba", method.apply("(abcd)"));
        assertEquals("iloveu", method.apply("(u(love)i)"));
        assertEquals("leetcode", method.apply("(ed(et(oc))el)"));
        assertEquals("apmnolkjihgfedcbq", method.apply("a(bcdefghijkl(mno)p)q"));
        assertEquals("", method.apply(""));
        assertEquals("", method.apply("((()))"));
        assertEquals("abc", method.apply("((abc))"));
    }

    /**
     * LeetCode 耗时：1 ms - 98.17%
     *          内存消耗：36.4 MB - 71.34%
     */
    public String reverseParentheses(String s) {
        char[] result = new char[s.length()];
        int end = 0;
        // stack 记录每个左括号包围字符序列在 result 中的起始下标
        Deque<Integer> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(end);
            } else if (c == ')') {
                // 反转括号区域
                for (int leftIdx = stack.pop(), rightIdx = end - 1; leftIdx < rightIdx; leftIdx++, rightIdx--) {
                    char tmp = result[leftIdx];
                    result[leftIdx] = result[rightIdx];
                    result[rightIdx] = tmp;
                }
            } else {
                result[end++] = c;
            }
        }

        return new String(result, 0, end);
    }

    @Test
    public void testReverseParentheses() {
        test(this::reverseParentheses);
    }
}
