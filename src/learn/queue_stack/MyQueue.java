package learn.queue_stack;

import java.util.Deque;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 仅使用两个堆栈来实现先进先出（FIFO）队列。已实现的队列应支持普通队列的所有方法
 * （push(x)，peek，pop 和 empty）。
 * <p>
 * 注意：
 * - 只能使用栈的标准操作：push、top、size、isEmpty。
 * - 根据您的语言，可能不支持该堆栈。只要只使用堆栈的标准操作，例如使用列表或双端队列
 * （双端队列）模拟堆栈。
 * <p>
 * 您是否可以实现队列以使每个操作均摊 O(1) 时间复杂度？换句话说，
 * 即使执行其中一项操作可能需要更长的时间，执行 n 次操作也将花费 O(n) 时间。
 * <p>
 * 例 1：
 * Input：
 * ["MyQueue", "push", "push", "peek", "pop", "empty"]
 * [[], [1], [2], [], [], []]
 * Output：
 * [null, null, null, 1, 1, false]
 * Explanation：
 * MyQueue myQueue = new MyQueue();
 * myQueue.push(1); // queue is: [1]
 * myQueue.push(2); // queue is: [1, 2] (leftmost is front of the queue)
 * myQueue.peek(); // return 1
 * myQueue.pop(); // return 1, queue is [2]
 * myQueue.empty(); // return false
 * <p>
 * 约束：
 * - 1 <= x <= 9
 * - 方法调用不超过 100 次
 * - 保证每次方法调用都有效
 */
public class MyQueue {

    /**
     * LeetCode 耗时：0ms
     */
    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue();
        myQueue.push(1); // queue is: [1]
        myQueue.push(2); // queue is: [1, 2]
        myQueue.push(3); // queue is: [1, 2, 3]
        assertEquals(myQueue.peek(), 1);
        assertEquals(myQueue.pop(), 1); // queue is [2, 3]
        assertFalse(myQueue.empty());
        assertEquals(myQueue.peek(), 2);
        myQueue.push(4); // queue is: [2, 3, 4]
        myQueue.push(5); // queue is: [2, 3, 4, 5]
        myQueue.push(6); // queue is: [2, 3, 4, 5, 6]
        assertEquals(myQueue.pop(), 2); // queue is [3, 4, 5, 6]
        assertEquals(myQueue.pop(), 3); // queue is [4, 5, 6]
        assertEquals(myQueue.pop(), 4); // queue is [5, 6]
        assertEquals(myQueue.pop(), 5); // queue is [6]
        assertEquals(myQueue.pop(), 6); // queue is []
        assertTrue(myQueue.empty());
    }


    private Deque<Integer> stack1, stack2;

    public MyQueue() {
        stack1 = new LinkedList<>();
        stack2 = new LinkedList<>();
    }

    public void push(int x) {
        stack1.push(x);
    }

    public int pop() {
        if (stack2.isEmpty())
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        return stack2.pop();
    }

    public int peek() {
        if (stack2.isEmpty())
            while (!stack1.isEmpty())
                stack2.push(stack1.pop());
        //noinspection ConstantConditions
        return stack2.peek();
    }

    public boolean empty() {
        return stack1.isEmpty() && stack2.isEmpty();
    }
}
