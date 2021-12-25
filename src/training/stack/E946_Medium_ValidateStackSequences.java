package training.stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 946. 验证栈序列: https://leetcode-cn.com/problems/validate-stack-sequences/
 *
 * 给定 pushed 和 popped 两个序列，每个序列中的「值都不重复」，只有当它们可能是在最初空栈上进行的推入
 * push 和弹出 pop 操作序列的结果时，返回 true；否则，返回 false。
 *
 * 例 1：
 * 输入：pushed = [1,2,3,4,5], popped = [4,5,3,2,1]
 * 输出：true
 * 解释：我们可以按以下顺序执行：
 * push(1), push(2), push(3), push(4), pop() -> 4,
 * push(5), pop() -> 5, pop() -> 3, pop() -> 2, pop() -> 1
 *
 * 例 2：
 * 输入：pushed = [1,2,3,4,5], popped = [4,3,5,1,2]
 * 输出：false
 * 解释：1 不能在 2 之前弹出。
 *
 * 约束：
 * - 1 <= pushed.length == popped.length <= 1000
 * - 0 <= pushed[i], popped[i] < 1000
 * - pushed 是 popped 的排列。
 */
public class E946_Medium_ValidateStackSequences {

    public static void test(BiPredicate<int[], int[]> method) {
        assertTrue(method.test(new int[]{1,2,3,4,5}, new int[]{4,5,3,2,1}));
        assertFalse(method.test(new int[]{1,2,3,4,5}, new int[]{4,3,5,1,2}));
    }

    /**
     * LeetCode 耗时：2 ms - 83.70%
     *          内存消耗：38.1 MB - 56.49%
     */
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Deque<Integer> stack = new ArrayDeque<>(pushed.length);
        for (int i = 0, j = 0; j < popped.length;) {
            if (stack.isEmpty() || stack.peek() != popped[j]) {
                if (i == pushed.length) {
                    return false;
                }
                stack.push(pushed[i++]);
            } else {
                stack.pop();
                j++;
            }
        }

        return true;
    }

    @Test
    public void testValidateStackSequences() {
        test(this::validateStackSequences);
    }
}
