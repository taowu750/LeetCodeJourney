package training.design;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 895. 最大频率栈: https://leetcode-cn.com/problems/maximum-frequency-stack/
 *
 * 实现 FreqStack，模拟类似栈的数据结构的操作的一个类。
 *
 * FreqStack 有两个函数：
 * - push(int x)，将整数 x 推入栈中。
 * - pop()，它移除并返回栈中出现最频繁的元素。如果最频繁的元素不只一个，则移除并返回最接近栈顶的元素。
 *
 * 例 1：
 * 输入：
 * ["FreqStack","push","push","push","push","push","push","pop","pop","pop","pop"],
 * [[],[5],[7],[5],[7],[4],[5],[],[],[],[]]
 * 输出：[null,null,null,null,null,null,null,5,7,5,4]
 * 解释：
 * 执行六次 .push 操作后，栈自底向上为 [5,7,5,7,4,5]。然后：
 *
 * pop() -> 返回 5，因为 5 是出现频率最高的。
 * 栈变成 [5,7,5,7,4]。
 *
 * pop() -> 返回 7，因为 5 和 7 都是频率最高的，但 7 最接近栈顶。
 * 栈变成 [5,7,5,4]。
 *
 * pop() -> 返回 5 。
 * 栈变成 [5,7,4]。
 *
 * pop() -> 返回 4 。
 * 栈变成 [5,7]。
 *
 * 说明：
 * - 对 FreqStack.push(int x) 的调用中 0 <= x <= 10^9。
 * - 如果栈的元素数目为零，则保证不会调用 FreqStack.pop()。
 * - 单个测试样例中，对 FreqStack.push 的总调用次数不会超过 10000。
 * - 单个测试样例中，对 FreqStack.pop 的总调用次数不会超过 10000。
 * - 所有测试样例中，对 FreqStack.push 和 FreqStack.pop 的总调用次数不会超过 150000。
 */
public class E895_Hard_MaximumFrequencyStack {

    public static void test(Supplier<IFreqStack> factory) {
        IFreqStack stack = factory.get();
        stack.push(5);
        stack.push(7);
        stack.push(5);
        stack.push(7);
        stack.push(4);
        stack.push(5);
        assertEquals(5, stack.pop());
        assertEquals(7, stack.pop());
        assertEquals(5, stack.pop());
        assertEquals(4, stack.pop());
        assertEquals(7, stack.pop());
        assertEquals(5, stack.pop());

        stack = factory.get();
        stack.push(4);
        stack.push(0);
        stack.push(9);
        stack.push(3);
        stack.push(4);
        stack.push(2);
        assertEquals(4, stack.pop());
        stack.push(6);
        assertEquals(6, stack.pop());
        stack.push(1);
        assertEquals(1, stack.pop());
        stack.push(1);
        assertEquals(1, stack.pop());
        stack.push(4);
        assertEquals(4, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(3, stack.pop());
        assertEquals(9, stack.pop());
        assertEquals(0, stack.pop());
        assertEquals(4, stack.pop());
    }

    @Test
    public void testFreqStack() {
        test(FreqStack::new);
    }
}

interface IFreqStack {

    void push(int val);

    int pop();
}

/**
 * 参见：
 * https://leetcode-cn.com/problems/maximum-frequency-stack/solution/zui-da-pin-lu-zhan-by-leetcode/
 *
 * LeetCode 耗时：27 ms - 85.74%
 *          内存消耗：48 MB - 36.36%
 */
class FreqStack implements IFreqStack {

    // 保存数字及其对应的频率
    private Map<Integer, Integer> freq;
    // 保存频率及其对应的所有数字。其中的要点是使用栈保存数字之间的顺序、并且每个频率都保存数字的副本
    private Map<Integer, Deque<Integer>> group;
    // 当前的最大频率
    private int maxFreq;

    public FreqStack() {
        freq = new HashMap<>();
        group = new HashMap<>();
    }

    public void push(int val) {
        // 更新 val 的频率
        int cnt = freq.merge(val, 1, Integer::sum);
        // 更新当前的最大频率
        maxFreq = Math.max(cnt, maxFreq);
        // 将 val 添加到对应的频率栈中
        group.computeIfAbsent(cnt, c -> new LinkedList<>()).push(val);
    }

    public int pop() {
        // 获取最大频率的最接近栈顶的数字
        Deque<Integer> stack = group.get(maxFreq);
        int num = stack.pop();
        // num 的频率减 1
        freq.merge(num, -1, (old, delta) -> old + delta > 0 ? old + delta : null);
        // 如果栈空了，表示当前最大频率下没有数字了
        if (stack.isEmpty()) {
            group.remove(maxFreq);
            maxFreq--;
        }

        return num;
    }
}
