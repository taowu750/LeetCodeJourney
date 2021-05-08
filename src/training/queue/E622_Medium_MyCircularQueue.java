package training.queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 设计循环队列的实现。循环队列是一种线性数据结构，其中的操作是基于FIFO（先进先出）原理执行的，
 * 最后一个位置又连接回第一个位置以构成一个圆。也称为“环形缓冲区”。
 * <p>
 * 循环队列的好处之一是我们可以利用队列前面的空间。在普通队列中，一旦队列已满，即使队列前面有空位，
 * 我们也不能插入下一个元素。但是使用循环队列，我们​​可以使用空间来存储新值。
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

    public static void main(String[] args) {
        E622_Medium_MyCircularQueue myCircularQueue = new E622_Medium_MyCircularQueue(3);
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

    private int[] buffer;
    private int size;
    private int headIndex;
    private int tailIndex;

    public E622_Medium_MyCircularQueue(int k) {
        buffer = new int[k];
        headIndex = 0;
        tailIndex = -1;
    }

    public boolean enQueue(int value) {
        if (size == buffer.length)
            return false;
        tailIndex = ++tailIndex % buffer.length;
        buffer[tailIndex] = value;
        size++;

        return true;
    }

    public boolean deQueue() {
        if (size == 0)
            return false;

        headIndex = ++headIndex % buffer.length;
        size--;

        return true;
    }

    public int Front() {
        if (size == 0)
            return -1;

        return buffer[headIndex];
    }

    public int Rear() {
        if (size == 0)
            return -1;

        return buffer[tailIndex];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == buffer.length;
    }
}
