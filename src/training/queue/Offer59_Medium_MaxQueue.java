package training.queue;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 剑指 Offer 59 - II. 队列的最大值: https://leetcode-cn.com/problems/dui-lie-de-zui-da-zhi-lcof/
 *
 * 请定义一个队列并实现函数 max_value 得到队列里的最大值，要求函数max_value、push_back 和 pop_front
 * 的「均摊」时间复杂度都是O(1)。
 *
 * 若队列为空，pop_front 和 max_value 需要返回 -1。
 *
 * 例 1：
 * 输入:
 * ["MaxQueue","push_back","push_back","max_value","pop_front","max_value"]
 * [[],[1],[2],[],[],[]]
 * 输出: [null,null,null,2,1,2]
 *
 * 例 2：
 * 输入:
 * ["MaxQueue","pop_front","max_value"]
 * [[],[],[]]
 * 输出: [null,-1,-1]
 *
 * 约束：
 * - 1 <= push_back,pop_front,max_value的总操作数 <= 10000
 * - 1 <= value <= 10^5
 */
public class Offer59_Medium_MaxQueue {

    static void test(Supplier<IMaxQueue> factory) {
        IMaxQueue maxQueue = factory.get();
        maxQueue.push_back(1);
        maxQueue.push_back(2);
        assertEquals(2, maxQueue.max_value());
        assertEquals(1, maxQueue.pop_front());
        assertEquals(2, maxQueue.max_value());

        maxQueue = factory.get();
        assertEquals(-1, maxQueue.pop_front());
        assertEquals(-1, maxQueue.max_value());
    }

    @Test
    public void testMaxQueue() {
        test(MaxQueue::new);
    }
}

interface IMaxQueue {

    int max_value();

    void push_back(int value);

    int pop_front();
}

/**
 * 普通队列+递增队列。
 *
 * LeetCode 耗时：31 ms - 99.10%
 *          内存消耗：46.2 MB - 63.10%
 */
class MaxQueue implements IMaxQueue {

    private Queue<Integer> queue;
    private Deque<Integer> decrQueue;


    public MaxQueue() {
        queue = new LinkedList<>();
        decrQueue = new LinkedList<>();
    }

    @Override
    public int max_value() {
        if (queue.isEmpty()) {
            return -1;
        }

        return decrQueue.getFirst();
    }

    @Override
    public void push_back(int value) {
        while (!decrQueue.isEmpty() && decrQueue.getLast() < value) {
            decrQueue.removeLast();
        }

        queue.add(value);
        decrQueue.addLast(value);
    }

    @Override
    public int pop_front() {
        if (queue.isEmpty()) {
            return -1;
        }

        int value = queue.remove();
        if (value == decrQueue.getFirst()) {
            decrQueue.removeFirst();
        }

        return value;
    }
}