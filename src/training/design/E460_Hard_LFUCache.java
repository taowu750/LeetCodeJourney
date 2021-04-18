package training.design;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 请你为「最不经常使用（LFU）」缓存算法设计并实现数据结构。
 *
 * 实现 LFUCache 类：
 * - LFUCache(int capacity)： 用数据结构的容量 capacity 初始化对象
 * - int get(int key)： 如果键存在于缓存中，则获取键的值，否则返回 -1。
 * - void put(int key, int value)： 如果键已存在，则变更其值；如果键不存在，请插入键值对。当缓存达到其容量时，
 *   则应该在插入新项之前，使最不经常使用的项无效。在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，
 *   应该去除「最久未使用」的键。
 *
 * 注意「项的使用次数」就是自插入该项以来对其调用「get 和 put 函数的次数之和」。使用次数会在对应项被移除后置为 0。
 *
 * 为了确定最不常使用的键，可以为缓存中的每个键维护一个「使用计数器」。使用计数最小的键是最久未使用的键。
 *
 * 当一个键首次插入到缓存中时，它的使用计数器被设置为 1 (由于 put 操作)。对缓存中的键执行 get 或 put 操作，
 * 使用计数器的值将会递增。
 *
 * 例 1：
 * 输入：
 * ["LFUCache", "put", "put", "get", "put", "get", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [3], [4, 4], [1], [3], [4]]
 * 输出：
 * [null, null, null, 1, null, -1, 3, null, -1, 3, 4]
 * 解释：
 * // cnt(x) = 键 x 的使用计数
 * // cache=[] 将显示最后一次使用的顺序（最左边的元素是最近的）
 * LFUCache lFUCache = new LFUCache(2);
 * lFUCache.put(1, 1);   // cache=[1,_], cnt(1)=1
 * lFUCache.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
 * lFUCache.get(1);      // 返回 1
 *                       // cache=[1,2], cnt(2)=1, cnt(1)=2
 * lFUCache.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
 *                       // cache=[3,1], cnt(3)=1, cnt(1)=2
 * lFUCache.get(2);      // 返回 -1（未找到）
 * lFUCache.get(3);      // 返回 3
 *                       // cache=[3,1], cnt(3)=2, cnt(1)=2
 * lFUCache.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
 *                       // cache=[4,3], cnt(4)=1, cnt(3)=2
 * lFUCache.get(1);      // 返回 -1（未找到）
 * lFUCache.get(3);      // 返回 3
 *                       // cache=[3,4], cnt(4)=1, cnt(3)=3
 * lFUCache.get(4);      // 返回 4
 *                       // cache=[3,4], cnt(4)=2, cnt(3)=3
 *
 * 约束：
 * - 0 <= capacity, key, value <= 10**4
 * - 最多调用 10**5 次 get 和 put 方法
 */
public class E460_Hard_LFUCache {

    static void test(IntFunction<ILFUCache> factory) {
        ILFUCache lFUCache = factory.apply(2);
        lFUCache.put(1, 1);   // cache=[1,_], cnt(1)=1
        lFUCache.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
        assertEquals(1, lFUCache.get(1));  // 返回 1
                                                    // cache=[1,2], cnt(2)=1, cnt(1)=2
        lFUCache.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
                                // cache=[3,1], cnt(3)=1, cnt(1)=2
        assertEquals(-1, lFUCache.get(2));      // 返回 -1（未找到）
        assertEquals(3, lFUCache.get(3));      // 返回 3
                                                        // cache=[3,1], cnt(3)=2, cnt(1)=2
        lFUCache.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
                                // cache=[4,3], cnt(4)=1, cnt(3)=2
        assertEquals(-1, lFUCache.get(1));      // 返回 -1（未找到）
        assertEquals(3, lFUCache.get(3));      // 返回 3
                                                        // cache=[3,4], cnt(4)=1, cnt(3)=3
        assertEquals(4, lFUCache.get(4));      // 返回 4
                                                        // cache=[3,4], cnt(4)=2, cnt(3)=3
    }

    @Test
    void testLFUCache() {
        test(LFUCache::new);
    }
}

interface ILFUCache {

    int get(int key);

    void put(int key, int value);
}

class LFUCache implements ILFUCache {

    private static class Node {
        int key, val, cnt;
        Node prev, next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
            cnt = 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            Node node = (Node) o;
            return key == node.key;
        }

        @Override
        public int hashCode() {
            return key;
        }
    }

    private static class NodeBin {
        Node head, tail;

        void push(Node node) {
            if (head == null)
                head = tail = node;
            else {
                node.next = head;
                head.prev = node;
                head = node;
            }
        }

        Node poll() {
            if (tail == null)
                return null;

            Node result;
            if (head == tail) {
                result = head;
                head = tail = null;
            } else {
                result = tail;
                tail = tail.prev;
                tail.next = null;
            }
            return result;
        }

        void delete(Node node) {
            Node prev = node.prev, next = node.next;
            if (prev == next)
                head = tail = null;
            else if (prev == null) {
                head = head.next;
                head.prev = null;
            } else if (next == null) {
                tail = tail.prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }

        boolean isEmpty() {
            return head == null;
        }
    }

    private int capacity;
    // 使用 TreeMap 按照次数进行排序，每种次数对应一条链表
    private TreeMap<Integer, NodeBin> cntMap;
    private Map<Integer, Node> searchMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        cntMap = new TreeMap<>();
        searchMap = new HashMap<>((int) (capacity / 0.75) + 1);
    }

    @Override
    public int get(int key) {
        Node node = searchMap.get(key);
        if (node == null)
            return -1;
        transfer(node);
        return node.val;
    }

    @Override
    public void put(int key, int value) {
        Node node = searchMap.get(key);
        if (node == null) {
            // 容量已满，删除计数最小的队列中最久未使用的节点
            if (searchMap.size() == capacity) {
                Entry<Integer, NodeBin> entry = cntMap.firstEntry();
                //noinspection ConstantConditions
                searchMap.remove(entry.getValue().poll().key);
            }

            node = new Node(key, value);
            searchMap.put(key, node);
            NodeBin bin = cntMap.get(1);
            if (bin == null)
                cntMap.put(key, bin = new NodeBin());
            bin.push(node);
        } else {
            node.val = value;
            transfer(node);
        }
    }

    // 增加 node 的计数，并将其转到新的链表中
    private void transfer(Node node) {
        node.cnt++;
        NodeBin srcBin = cntMap.get(node.cnt - 1);
        srcBin.delete(node);
        if (srcBin.isEmpty())
            cntMap.remove(node.cnt - 1);

        NodeBin dstBin = cntMap.get(node.cnt);
        if (dstBin == null)
            cntMap.put(node.cnt, dstBin = new NodeBin());
        dstBin.push(node);
    }
}
