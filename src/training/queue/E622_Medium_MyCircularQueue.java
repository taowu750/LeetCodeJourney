package training.queue;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 622. 设计循环队列：https://www.acwing.com/problem/content/description/137/
 *
 * 设计循环队列的实现。循环队列是一种线性数据结构，其中的操作是基于FIFO（先进先出）原理执行的，
 * 最后一个位置又连接回第一个位置以构成一个圆。也称为“环形缓冲区”。
 * <p>
 * 循环队列的好处之一是我们可以利用队列前面的空间。在普通队列中，一旦队列已满，即使队列前面有空位，
 * 我们也不能插入下一个元素。但是使用循环队列，我们可以使用空间来存储新值。
 * <p>
 * 实现 MyCircularQueue 类：
 * - MyCircularQueue(k): 初始化一个大小为 k 的队列。
 * - int Front(): 返回队列头的元素。如果队列为空，返回 -1。
 * - int Rear(): 返回队列尾的元素。如果队列为空，返回 -1。
 * - boolean enQueue(int value): 插入一个元素到队尾，插入成功返回 true。
 * - boolean deQueue(): 从队头删除一个元素，删除成功返回 true。
 * - boolean isEmpty(): 查看队列是否为空。
 * - boolean isFull(): 查看队列是否为满。
 * <p>
 * 例 1：
 * Input:
 * ["MyCircularQueue", "enQueue", "enQueue", "enQueue", "enQueue", "Rear", "isFull", "deQueue", "enQueue", "Rear"]
 * [[3], [1], [2], [3], [4], [], [], [], [4], []]
 * Output:
 * [null, true, true, true, false, 3, true, true, true, 4]
 * Explanation:
 * MyCircularQueue myCircularQueue = new MyCircularQueue(3);
 * myCircularQueue.enQueue(1); // return True
 * myCircularQueue.enQueue(2); // return True
 * myCircularQueue.enQueue(3); // return True
 * myCircularQueue.enQueue(4); // return False
 * myCircularQueue.Rear();     // return 3
 * myCircularQueue.isFull();   // return True
 * myCircularQueue.deQueue();  // return True
 * myCircularQueue.enQueue(4); // return True
 * myCircularQueue.Rear();     // return 4
 * <p>
 * 约束：
 * - 1 <= k <= 1000
 * - 0 <= value <= 1000
 * - 最多调用 3000 次方法
 */
public class E622_Medium_MyCircularQueue {

    public interface ICircularQueue {
        int Front();

        int Rear();

        boolean enQueue(int value);

        boolean deQueue();

        boolean isEmpty();

        boolean isFull();
    }

    public static void test(Function<Integer, ICircularQueue> factory) {
        ICircularQueue myCircularQueue = factory.apply(3);
        assertTrue(myCircularQueue.enQueue(1));
        assertTrue(myCircularQueue.enQueue(2));
        assertTrue(myCircularQueue.enQueue(3));
        assertFalse(myCircularQueue.enQueue(4));
        assertEquals(myCircularQueue.Rear(), 3);
        assertEquals(myCircularQueue.Front(), 1);
        assertTrue(myCircularQueue.isFull());
        assertTrue(myCircularQueue.deQueue());
        assertTrue(myCircularQueue.enQueue(4));
        assertEquals(myCircularQueue.Rear(), 4);
        assertTrue(myCircularQueue.deQueue());
        assertTrue(myCircularQueue.deQueue());
        assertTrue(myCircularQueue.deQueue());
        assertFalse(myCircularQueue.deQueue());
        assertTrue(myCircularQueue.isEmpty());
        assertTrue(myCircularQueue.enQueue(1));
        assertTrue(myCircularQueue.enQueue(2));
        assertTrue(myCircularQueue.enQueue(3));
        assertFalse(myCircularQueue.enQueue(4));
        assertTrue(myCircularQueue.deQueue());
        assertTrue(myCircularQueue.deQueue());
        assertTrue(myCircularQueue.deQueue());
        assertFalse(myCircularQueue.deQueue());
    }

    public static class MyQueue implements ICircularQueue {

        private final int[] queue;
        // 只靠 head 和 tail 指针很难同时处理队列为空、为满、只有一个元素的情况
        private int head, tail, count;

        public MyQueue(int n) {
            queue = new int[n];
            tail = -1;
        }

        public int Front() {
            if (isEmpty()) {
                return -1;
            }
            return queue[head];
        }

        public int Rear() {
            if (isEmpty()) {
                return -1;
            }
            return queue[tail];
        }

        public boolean enQueue(int value) {
            if (isFull()) {
                return false;
            }
            count++;
            queue[tail = (tail + 1) % queue.length] = value;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) {
                return false;
            }
            count--;
            head = (head + 1) % queue.length;
            return true;
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public boolean isFull() {
            return count == queue.length;
        }
    }

    @Test
    public void testMyQueue() {
        test(MyQueue::new);
    }


    public class OtherQueue implements ICircularQueue {

        private final int[] queue;
        private final int cap;
        private int head, tail;

        public OtherQueue(int n) {
            // 如果设计 tail 指向尾元素的下一个元素，这样判断 isEmpty() 容易判断，只要看 tail==head 即可。
            // 但是看 isFull() 就麻烦了，判断条件应该是 tail%len==head，但是这一情况有两种可能，一个是前面说的isFull()，
            // 另一方面，刚开始时我们初始化了 tail = head = 0，也是满足这一条件的。
            // 所以为了区分这两种情况，我们就不要让循环队列装满，于是我们判断 (tail+1)%len==head(即空出来一个元素)，
            // 但是这样就会导致少一个元素，所以我们把整个循环队列的长度在实现时隐式+1。
            //
            // 类似地，如果我们设计 tail 指向尾元素，也是会有对应多种情况的问题。
            cap = n + 1;
            queue = new int[cap];
        }

        public int Front() {
            if (isEmpty()) {
                return -1;
            }
            return queue[head];
        }

        public int Rear() {
            if (isEmpty()) {
                return -1;
            }
            return queue[(tail - 1 + cap) % cap];
        }

        public boolean enQueue(int value) {
            if (isFull()) {
                return false;
            }
            queue[tail] = value;
            tail = (tail + 1) % cap;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) {
                return false;
            }
            head = (head + 1) % cap;
            return true;
        }

        public boolean isEmpty() {
            return head == tail;
        }

        public boolean isFull() {
            return (tail + 1) % cap == head;
        }
    }

    @Test
    public void testOtherQueue() {
        test(OtherQueue::new);
    }
}
