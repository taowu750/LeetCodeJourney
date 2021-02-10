package learn.queue_stack;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个编码字符串，返回其解码字符串。编码规则是：k[encoded_string]，
 * 其中方括号内的 encode_string 正好重复k次。注意，保证 k 为正整数。
 *
 * 您可以假设输入字符串始终有效；没有多余的空格，方括号格式正确等。
 * 此外，您可以假定原始数据不包含任何数字，并且这些数字仅用于那些重复数字 k。例如，
 * 不会有 3a 或 2[4]。
 *
 * 例 1：
 * Input: s = "3[a]2[bc]"
 * Output: "aaabcbc"
 *
 * 例 2：
 * Input: s = "3[a2[c]]"
 * Output: "accaccacc"
 *
 * 例 3：
 * Input: s = "2[abc]3[cd]ef"
 * Output: "abcabccdcdcdef"
 *
 * 例 4：
 * Input: s = "abc3[cd]xyz"
 * Output: "abccdcdcdxyz"
 *
 * 约束：
 * - 1 <= s.length <= 30
 * - s 由小写英文字母，数字和方括号“[]”组成。
 * - s中的所有整数都在 [1，300] 范围内。
 */
public class DecodeString {

    static void test(Function<String, String> method) {
        assertEquals(method.apply("3[a]2[bc]"), "aaabcbc");

        assertEquals(method.apply("3[a2[c]]"), "accaccacc");

        assertEquals(method.apply("2[abc]3[cd]ef"), "abcabccdcdcdef");

        assertEquals(method.apply("abc3[cd]xyz"), "abccdcdcdxyz");

        assertEquals(method.apply("abcxyz"), "abcxyz");

        assertEquals(method.apply("ab10[c]3[de2[f]gh]ijk"), "abccccccccccdeffghdeffghdeffghijk");
    }

    /**
     * 递归方式
     */
    public String decodeString(String s) {
        StringBuilder result = new StringBuilder(s.length() << 1);
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i]))
                i = decodeString(chars, i, result);
            else
                result.append(chars[i]);
        }

        return result.toString();
    }

    private int decodeString(char[] chars, int idx, StringBuilder sb) {
        int repeat = chars[idx] - '0';
        while (Character.isDigit(chars[++idx]))
            repeat = 10 * repeat + chars[idx] - '0';

        StringBuilder sub = new StringBuilder(chars.length - idx);
        for(++idx; chars[idx] != ']'; idx++) {
            if (Character.isDigit(chars[idx]))
                idx = decodeString(chars, idx, sub);
            else
                sub.append(chars[idx]);
        }
        for (int i = 0; i < repeat; i++)
            sb.append(sub);

        return idx;
    }

    @Test
    public void testDecodeString() {
        test(this::decodeString);
    }


    /**
     * 迭代方式
     */
    public String iterateMethod(String s) {
        Deque<Integer> repeatStack = new LinkedList<>();
        Deque<StringBuilder> sbStack = new LinkedList<>();
        char[] chars = s.toCharArray();

        repeatStack.push(1);
        sbStack.push(new StringBuilder(chars.length << 1));
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                int repeat = chars[i] - '0';
                while (Character.isDigit(chars[++i]))
                    repeat = 10 * repeat + chars[i] - '0';
                repeatStack.push(repeat);
                sbStack.push(new StringBuilder(chars.length - i));
            } else if (chars[i] == ']') {
                int repeat = repeatStack.pop();
                StringBuilder sb = sbStack.pop();
                for (int j = 0; j < repeat; j++)
                    sbStack.peek().append(sb);
            } else {
                sbStack.peek().append(chars[i]);
            }
        }

        return sbStack.peek().toString();
    }

    @Test
    public void testIterateMethod() {
        test(this::iterateMethod);
    }
}
