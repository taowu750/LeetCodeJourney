package training.stack;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 使用两个队列实现栈。
 *
 * 您是否可以实现栈以使每个操作均摊 O(1) 时间复杂度？换句话说，
 * 即使执行其中一项操作可能需要更长的时间，执行 n 次操作也将花费 O(n) 时间。
 * 你可以使用两个以上的队列。
 * <p>
 * 例 1：
 * Input：
 * ["MyStack", "push", "push", "top", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 * Output：
 * [null, null, null, 2, 2, false]
 * Explanation：
 * MyStack myStack = new MyStack();
 * myStack.push(1);
 * myStack.push(2);
 * myStack.top(); // return 2
 * myStack.pop(); // return 2
 * myStack.empty(); // return False
 * <p>
 * 约束：
 * - 1 <= x <= 9
 * - 方法调用不超过 100 次
 * - 保证每次方法调用都有效
 */
public class E225_Easy_MyStack {

    public static void main(String[] args) {
        E225_Easy_MyStack myStack = new E225_Easy_MyStack();
        myStack.push(1);
        myStack.push(2);
        assertEquals(myStack.top(), 2);
        assertEquals(myStack.pop(), 2);
        assertEquals(myStack.top(), 1);
        assertFalse(myStack.empty());
        assertEquals(myStack.pop(), 1);
        assertTrue(myStack.empty());
        myStack.push(3);
        myStack.push(4);
        assertEquals(myStack.top(), 4);
    }


    private Queue<Integer> queue;

    /**
     * 使用一个队列实现栈。
     */
    public E225_Easy_MyStack() {
        queue = new LinkedList<>();
    }

    /**
     * 也可以在 push 中弹出前面的元素
     */
    public void push(int x) {
        queue.add(x);
    }

    public int pop() {
        int size = queue.size();
        for (int i = 0; i < size - 1; i++) {
            queue.add(queue.remove());
        }

        return queue.remove();
    }

    public int top() {
        int result = pop();
        queue.add(result);

        return result;
    }

    public boolean empty() {
        return queue.isEmpty();
    }
}
