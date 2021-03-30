package training.design;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制 。
 *
 * 实现 LRUCache 类：
 * - LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
 * - int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
 * - void put(int key, int value) 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字-值」。
 *   当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。
 *
 * 你是否可以在 O(1) 时间复杂度内完成这两种操作？
 *
 * 约束：
 * - 1 <= capacity <= 3000
 * - 0 <= key <= 3000
 * - 0 <= value <= 10**4
 * - 最多调用 3 * 10**4 次 get 和 put
 */
public class E126_Medium_LRUCache {

    static void test(IntFunction<ILRUCache> factory) {
        ILRUCache lRUCache = factory.apply(2);
        lRUCache.put(2, 1); // 缓存是 {2=1}
        lRUCache.put(1, 1); // 缓存是 {2=1,1=1}
        lRUCache.put(2, 3); // 缓存是 {1=1,2=3}
        lRUCache.put(4, 1); // 缓存是 {2=3,4=1}
        assertEquals(lRUCache.get(1), -1);
        assertEquals(lRUCache.get(4), 1); // 缓存是 {2=3,4=1}

        lRUCache = factory.apply(2);
        lRUCache.put(1, 0); // 缓存是 {1=0}
        lRUCache.put(2, 2); // 缓存是 {1=0,2=2}
        assertEquals(lRUCache.get(1), 0); // 缓存是 {2=2,1=0}
        lRUCache.put(3, 3); // 缓存是 {1=0,3=3}
        assertEquals(lRUCache.get(2), -1);
        lRUCache.put(4, 4); // 缓存是 {3=3,4=4}
        assertEquals(lRUCache.get(1), -1);
        assertEquals(lRUCache.get(3), 3); // 缓存是 {4=4,3=3}
        assertEquals(lRUCache.get(4), 4); // 缓存是 {3=3,4=4}
    }

    @Test
    public void testLRUCache() {
        test(LRUCache::new);
    }

    @Test
    public void testBetterLRUCache() {
        test(LinkedListLRUCache::new);
    }
}

interface ILRUCache {

    int get(int key);

    void put(int key, int value);
}

/**
 * LeetCode 耗时：21 ms - 43.92%
 *          内存消耗：46.4 MB - 65.58%
 */
class LRUCache extends LinkedHashMap<Integer, Integer> implements ILRUCache {

    private final int capacity;

    LRUCache(int capacity) {
        // 注意使用下面的这个构造器
        super((int) (capacity / 0.75) + 2, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    @Override
    public void put(int key, int value) {
        super.put(key, value);
    }

    // 直接继承 LinkedHashMap，实现下面的方法就可以了
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}


/**
 * 使用双向链表和哈希表实现 LRUCache。
 *
 * LeetCode 耗时：24 ms - 22.23%
 *          内存消耗：46.7 MB - 26.81%
 */
class LinkedListLRUCache implements ILRUCache {

    private static class Node {
        int key;
        int val;
        Node prev;
        Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }

        public Node(int key, int val, Node prev) {
            this.key = key;
            this.val = val;
            this.prev = prev;
        }
    }

    private final int capacity;
    private Map<Integer, Node> cache;
    private Node head, tail;

    LinkedListLRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>((int) (capacity / 0.75) + 1);
    }

    @Override
    public int get(int key) {
        Node node = cache.getOrDefault(key, null);
        if (node == null)
            return -1;
        moveToTail(node);
        return node.val;
    }

    @Override
    public void put(int key, int value) {
        // 队列为空，创建节点
        if (head == null) {
            head = tail = new Node(key, value);
            cache.put(key, head);
            return;
        }

        Node node = cache.getOrDefault(key, null);
        if (node != null) {
            // node 存在，则将其移到队尾，更新值
            moveToTail(node);
            node.val = value;
        } else {
            // 添加新的节点到队尾
            node = new Node(key, value, tail);
            cache.put(key, node);
            tail.next = node;
            tail = node;
            // 容量溢出，则移除队头节点
            if (cache.size() > capacity) {
                cache.remove(head.key);
                head = head.next;
                head.prev = null;
            }
        }
    }

    private void moveToTail(Node node) {
        // 如果 node 不是 tail，将其移到队尾
        if (node != tail) {
            // 如果 node 是 head，将下一个节点作为新的 head
            if (node == head)
                head = head.next;
            // 将 node 的前后节点连接起来
            if (node.prev != null)
                node.prev.next = node.next;
            node.next.prev = node.prev;
            // 将 node 作为新的 tail
            node.prev = tail;
            tail.next = node;
            node.next = null;
            tail = node;
        }
    }
}
