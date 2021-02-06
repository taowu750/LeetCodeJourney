package learn.queue_stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 设计一个堆栈，该堆栈支持在常数时间内 push，pop，top 和检索最小元素。
 * <p>
 * 例 1：
 * Input:
 * ["MinStack","push","push","push","getMin","pop","top","getMin"]
 * [[],[-2],[0],[-3],[],[],[],[]]
 * Output:
 * [null,null,null,null,-3,null,0,-2]
 * Explanation:
 * MinStack minStack = new MinStack();
 * minStack.push(-2);
 * minStack.push(0);
 * minStack.push(-3);
 * minStack.getMin(); // return -3
 * minStack.pop();
 * minStack.top();    // return 0
 * minStack.getMin(); // return -2
 */
public class MinStack {

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        assertEquals(minStack.getMin(), -3);
        minStack.pop();
        assertEquals(minStack.top(), 0);
        assertEquals(minStack.getMin(), -2);
    }

    private static class StackElement {
        int val;
        StackElement next;

        public StackElement(int val) {
            this.val = val;
        }

        public StackElement(int val, StackElement next) {
            this.val = val;
            this.next = next;
        }
    }

    private StackElement top;
    private int minVal;

    public MinStack() {
        minVal = Integer.MAX_VALUE;
    }

    public void push(int x) {
        if (top == null)
            top = new StackElement(x);
        else
            top = new StackElement(x, top);
        if (x < minVal)
            minVal = x;
    }

    /**
     * 这种方式实现的 getMin 不是常数时间操作。我么可以让 StackElement
     * 新增一个 minVal 字段，让它保存当前元素为栈顶的最小值。
     */
    public void pop() {
        if (top != null) {
            int popped = top.val;
            top = top.next;
            if (popped == minVal) {
                minVal = Integer.MAX_VALUE;
                for (StackElement p = top; p != null; p = p.next) {
                    if (p.val < minVal)
                        minVal = p.val;
                }
            }
        }
    }

    public int top() {
        if (top == null)
            return Integer.MIN_VALUE;
        else
            return top.val;
    }

    public int getMin() {
        return minVal;
    }
}
