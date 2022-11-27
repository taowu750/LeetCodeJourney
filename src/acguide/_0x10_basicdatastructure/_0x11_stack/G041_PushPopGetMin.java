package acguide._0x10_basicdatastructure._0x11_stack;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 包含min函数的栈: https://www.acwing.com/problem/content/90/
 *
 * 设计一个支持push，pop，top等操作并且可以在O(1)时间内检索出最小元素的堆栈。
 * - push(x)–将元素x插入栈中
 * - pop()–移除栈顶元素
 * - top()–得到栈顶元素
 * - getMin()–得到栈中最小元素
 *
 * 数据范围：
 * - 操作命令总数 [0,100]。
 *
 *
 * 例 1：
 * MinStack minStack = new MinStack();
 * minStack.push(-1);
 * minStack.push(3);
 * minStack.push(-4);
 * minStack.getMin();   --> Returns -4.
 * minStack.pop();
 * minStack.top();      --> Returns 3.
 * minStack.getMin();   --> Returns -1.
 */
public class G041_PushPopGetMin {

    public interface IMinStack {

        void push(int x);

        void pop();

        int top();

        int getMin();
    }

    public static void test(Supplier<IMinStack> factory) {
        IMinStack minStack = factory.get();
        minStack.push(-1);
        minStack.push(3);
        minStack.push(-4);
        assertEquals(-4, minStack.getMin());
        minStack.pop();
        assertEquals(3, minStack.top());
        assertEquals(-1, minStack.getMin());
    }

    public static class MinStack implements IMinStack {

        private int[][] stack;
        private int top = -1;

        public MinStack() {
            stack = new int[16][2];
        }

        @Override
        public void push(int x) {
            if (top == stack.length - 1) {
                int[][] newStack = new int[stack.length * 2][2];
                System.arraycopy(stack, 0, newStack, 0, stack.length);
                stack = newStack;
            }
            stack[++top][0] = x;
            stack[top][1] = Math.min(x, top > 0 ? stack[top-1][1] : x);
        }

        @Override
        public void pop() {
            top--;
        }

        @Override
        public int top() {
            return stack[top][0];
        }

        @Override
        public int getMin() {
            return stack[top][1];
        }
    }

    @Test
    public void testMinStack() {
        test(MinStack::new);
    }
}
