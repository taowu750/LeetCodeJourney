package training.stack;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 316. 去除重复字母: https://leetcode-cn.com/problems/remove-duplicate-letters/
 *
 * 给你一个字符串 s ，请你去除字符串中重复的字母，使得每个字母只出现一次。
 * 需「保证返回结果的字典序最小」（要求不能打乱其他字符的相对位置）。
 *
 * 例 1：
 * 输入：s = "bcabc"
 * 输出："abc"
 *
 * 例 2：
 * 输入：s = "cbacdcbc"
 * 输出："acdb"
 *
 * 约束：
 * - 1 <= s.length <= 10**4
 * - s 由小写英文字母组成
 */
public class E316_Medium_RemoveDuplicateLetters {

    public static void test(UnaryOperator<String> method) {
        assertEquals("abc", method.apply("bcabc"));
        assertEquals("acdb", method.apply("cbacdcbc"));
        assertEquals("adbc", method.apply("cdadabcc"));
        assertEquals("abc", method.apply("abacb"));
        assertEquals("bcd", method.apply("bddbccd"));
        assertEquals("bac", method.apply("bcbac"));
        assertEquals("ilrhjfyzmnstwkboxuc", method.apply("mitnlruhznjfyzmtmfnstsxwktxlboxutbic"));
    }

    /**
     * 有关递增栈参见 {@link training.stack.E496_Easy_NextGreaterElementI}。
     *
     * 这个题目要求有三点：
     * 1. 要去重。
     * 2. 去重字符串中的字符顺序不能打乱 s 中字符出现的相对顺序。
     * 3. 在所有符合上一条要求的去重字符串中，字典序最小的作为最终结果。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：36.9 MB - 96.17%
     */
    public String removeDuplicateLetters(String s) {
        int n = s.length();
        if (n <= 1)
            return s;

        // 记录字符计数
        int[] count = new int[26];
        for (int i = 0; i < n; i++)
            count[s.charAt(i) - 'a']++;

        // 使用递增栈来保存递增顺序，使得满足「字典序最小的作为最终结果」
        char[] stack = new char[26];
        int top = -1;
        // 判断字符是否在栈中
        boolean[] inStack = new boolean[26];
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int idx = c - 'a';
            // 使用了这个字符，计数减 1
            count[idx]--;
            // 如果此字符已经在栈中，则不需要处理
            if (inStack[idx])
                continue;

            while (top > -1 && stack[top] > c) {
                // 如果后面已经没有 stack[top] 字符了，那么就不应该把它弹出来
                if (count[stack[top] - 'a'] == 0)
                    break;
                // 否则弹出字符
                inStack[stack[top] - 'a'] = false;
                top--;
            }
            // 将字符入栈
            stack[++top] = c;
            inStack[idx] = true;
        }

        StringBuilder result = new StringBuilder(top + 1);
        for (int i = 0; i <= top; i++)
            result.append(stack[i]);

        return result.toString();
    }

    @Test
    public void testRemoveDuplicateLetters() {
        test(this::removeDuplicateLetters);
    }
}
