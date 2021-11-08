package training.stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BiFunction;

/**
 * 402. 移掉 K 位数字: https://leetcode-cn.com/problems/remove-k-digits/
 *
 * 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。
 * 请你以字符串形式返回这个最小的数字。
 *
 * 例 1：
 * 输入：num = "1432219", k = 3
 * 输出："1219"
 * 解释：移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219 。
 *
 * 例 2：
 * 输入：num = "10200", k = 1
 * 输出："200"
 * 解释：移掉首位的 1 剩下的数字为 200. 注意输出不能有任何前导零。
 *
 * 例 3：
 * 输入：num = "10", k = 2
 * 输出："0"
 * 解释：从原数字移除所有的数字，剩余为空就是 0 。
 *
 * 说明：
 * - 1 <= k <= num.length <= 10^5
 * - num 仅由若干位数字（0 - 9）组成
 * - 除了 0 本身之外，num 不含任何前导零
 */
public class E402_Medium_RemoveKDigits {

    public static void test(BiFunction<String, Integer, String> method) {
        Assertions.assertEquals("1219", method.apply("1432219", 3));
        Assertions.assertEquals("200", method.apply("10200", 1));
        Assertions.assertEquals("0", method.apply("10", 2));
        Assertions.assertEquals("0", method.apply("1023", 3));
        Assertions.assertEquals("11", method.apply("112", 1));
    }

    /**
     * LeetCode 耗时：12 ms - 22.82%
     *          内存消耗：38.6 MB - 59.95%
     */
    public String removeKdigits(String num, int k) {
        if (k == num.length()) {
            return "0";
        } else if (k == num.length() - 1) {
            char min = num.charAt(0);
            for (int i = 1; i < num.length(); i++) {
                if (num.charAt(i) < min) {
                    min = num.charAt(i);
                }
            }

            return String.valueOf(min);
        }

        // 递增栈，让列表尾做栈顶，列表头做栈底，方便后面组成结果
        Deque<Character> stack = new LinkedList<>();
        for (int i = 0, remain = num.length() - k; i < num.length(); i++) {
            // 注意，要保证剩余元素满足 remain 才能弹出
            while (!stack.isEmpty() && stack.getLast() > num.charAt(i)
                    && (stack.size() + num.length() - i) > remain) {
                stack.removeLast();
            }
            // 当还可以添加时再添加
            if (stack.size() < remain) {
                stack.addLast(num.charAt(i));
            }
        }

        // 去除开头的 0
        int size = stack.size();
        for (int i = 0; i < size - 1; i++) {
            if (stack.getFirst() == '0') {
                stack.removeFirst();
            } else {
                break;
            }
        }

        StringBuilder result = new StringBuilder();
        for (char ch : stack) {
            result.append(ch);
        }

        return result.toString();
    }

    @Test
    public void testRemoveKdigits() {
        test(this::removeKdigits);
    }
}
