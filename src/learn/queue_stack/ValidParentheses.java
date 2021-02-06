package learn.queue_stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个仅包含字符 '('，')'，'{'，'}'，'[' 和 ']' 的字符串 s，请确定输入字符串是否有效。
 *
 * 输入有效当且仅当满足以下条件
 * 1. 开括号必须用相同类型的括号封闭。
 * 2. 开括号必须以正确的顺序关闭。
 *
 * 例 1：
 * Input: s = "()"
 * Output: true
 *
 * 例 2：
 * Input: s = "()[]{}"
 * Output: true
 *
 * 例 3：
 * Input: s = "(]"
 * Output: false
 *
 * 例 4：
 * Input: s = "([)]"
 * Output: false
 *
 * 例 5：
 * Input: s = "{[]}"
 * Output: true
 *
 * 约束
 * - 1 <= s.length <= 10**4
 */
public class ValidParentheses {

    static void test(Predicate<String> method) {
        assertTrue(method.test("()"));
        assertTrue(method.test("()[]{}"));
        assertFalse(method.test("(]"));
        assertFalse(method.test("([)]"));
        assertTrue(method.test("{[]}"));
        assertTrue(method.test("{{[]([])}[]()(({}))}"));
        assertFalse(method.test("{{[]([])}[]()(({}))"));
        assertFalse(method.test("{"));
        assertFalse(method.test("["));
        assertFalse(method.test("("));
        assertFalse(method.test("}"));
        assertFalse(method.test("]"));
        assertFalse(method.test(")"));
    }

    public boolean isValid(String s) {
        int len = s.length();
        // 更快的做法是自己维护一个栈
        Deque<Character> stack = new ArrayDeque<>(len);

        for (int i = 0; i < len; i++) {
            char bracket = s.charAt(i);
            char openBracket;
            if (bracket == '(' || bracket == '[' || bracket == '{')
                stack.push(bracket);
            // 通过查看 ascii 表得知，'(' 比 ')' 小 1，'{'、'[' 分别比 '}'、']' 小 2
            else if (stack.isEmpty() || (((openBracket = stack.pop()) != bracket - 1)
                    && openBracket != bracket - 2))
                return false;
        }
        return stack.isEmpty();
    }

    @Test
    public void testIsValid() {
        test(this::isValid);
    }
}
