package company.other;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToLongBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 趋势科技笔试题。
 *
 * 给定一个长度为 m（m >= 1）的字符串，这个字符串表示一个非负整数 N。其中 N 没有前导 0。
 *
 * 再给定一个整数 x（x <= m），要你从整数 N 中删除 x 个数字，使得余下的数字最小。最后输出这个最小的数字。
 *
 * 例 1：
 * 输入：N=2020，x = 1
 * 输出：20
 * 解释：删除第一个 2，剩下 020=20
 *
 * 例 2：
 * 输入：N=1432319，x = 3
 * 输出：1219
 */
public class O1_Medium_SmallestRemainingAfterDeletingXNumbers {

    static void test(ToLongBiFunction<String, Integer> method) {
        assertEquals(20, method.applyAsLong("2020", 1));
        assertEquals(1219, method.applyAsLong("1432319", 3));
    }

    public long remainSmallest(String num, int x) {
        int m = num.length();
        if (x == m)
            return 0;

        char[] digits = num.toCharArray();
        /*
        单调栈，保存单调顺序，例如 1432 压入栈中后变为 12。
        参见 LeetCode 739 题
         */
        Deque<Character> stack = new LinkedList<>();

        /*
        要删 x 个数，最后剩下 m - x 个数，并且这些数的相对顺序是不能变的。
        那么我们先从前面 x + 1 个数里面选最小的，因为后面至少要有 m - x - 1 个数。
        选到之后，再不断压单调栈，不停选最小的。
         */

        // 初始时先压 x + 1 个数到栈中
        for (int i = 0; i < x + 1; i++) {
            // 从单调栈中弹出比 digits[i] 大的数，维持单调顺序。而且这样元素的原本相对顺序也不会变。
            while (!stack.isEmpty() && stack.peekLast() > digits[i]) {
                stack.removeLast();
            }
            stack.addLast(digits[i]);
        }

        long result = stack.removeFirst() - '0';
        // 先从单调栈中弹出最小的元素
        for (int i = x + 1; i < m; i++) {
            // 还是和上面一样，将下一个数压到栈中，保持一个递增顺序。
            while (!stack.isEmpty() && stack.peekLast() > digits[i]) {
                stack.removeLast();
            }
            stack.addLast(digits[i]);
            result = result * 10 + stack.removeFirst() - '0';
        }
        // 使用单调栈，每个元素最多被压栈、出栈一次，时间复杂度 O(m)

        return result;
    }

    @Test
    public void testRemainSmallest() {
        test(this::remainSmallest);
    }
}
