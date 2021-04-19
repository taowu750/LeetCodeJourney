package training.design;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
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

        lFUCache = factory.apply(0);
        lFUCache.put(1, 1);
        assertEquals(-1, lFUCache.get(1));

        lFUCache = factory.apply(10);
        lFUCache.put(10,13);
        lFUCache.put(3,17);
        lFUCache.put(6,11);
        lFUCache.put(10,5);
        lFUCache.put(9,10);
        assertEquals(-1, lFUCache.get(13));
        lFUCache.put(2,19);
        // [2:(10,5), 1:(2,19)(9,10)(6,11)(3,17)]
        assertEquals(19, lFUCache.get(2));
        assertEquals(17, lFUCache.get(3));
        // [2:(3,17)(2,19)(10,5), 1:(9,10)(6,11)]
        lFUCache.put(5,25);
        assertEquals(-1, lFUCache.get(8));
        lFUCache.put(9,22);
        lFUCache.put(5,5);
        lFUCache.put(1,30);
        assertEquals(-1, lFUCache.get(11));
        lFUCache.put(9,12);
        assertEquals(-1, lFUCache.get(7));
        assertEquals(5, lFUCache.get(5));
        assertEquals(-1, lFUCache.get(8));
        assertEquals(12, lFUCache.get(9));
        lFUCache.put(4,30);
        lFUCache.put(9,3);
        assertEquals(3, lFUCache.get(9));
        assertEquals(5, lFUCache.get(10));
        assertEquals(5, lFUCache.get(10));
        lFUCache.put(6,14);
        lFUCache.put(3,1);
        assertEquals(1, lFUCache.get(3));
        lFUCache.put(10,11);
        assertEquals(-1, lFUCache.get(8));
        lFUCache.put(2,14);
        assertEquals(30, lFUCache.get(1));
        assertEquals(5, lFUCache.get(5));
        assertEquals(30, lFUCache.get(4));
        lFUCache.put(11,4);
        lFUCache.put(12,24);
        lFUCache.put(5,18);
        assertEquals(-1, lFUCache.get(13));
        lFUCache.put(7,23);
        assertEquals(-1, lFUCache.get(8));
        assertEquals(24, lFUCache.get(12));
        lFUCache.put(3,27);
        lFUCache.put(2,12);
        assertEquals(18, lFUCache.get(5));
        lFUCache.put(2,9);
        lFUCache.put(13,4);
        lFUCache.put(8,18);
        lFUCache.put(1,7);
        assertEquals(14, lFUCache.get(6));
        lFUCache.put(9,29);
        lFUCache.put(8,21);
        assertEquals(18, lFUCache.get(5));
        lFUCache.put(6,30);
        lFUCache.put(1,12);
        assertEquals(11, lFUCache.get(10));
        lFUCache.put(4,15);
        lFUCache.put(7,22);
        lFUCache.put(11,26);
        lFUCache.put(8,17);
        lFUCache.put(9,29);
        assertEquals(18, lFUCache.get(5));
        lFUCache.put(3,4);
        lFUCache.put(11,30);
        assertEquals(-1, lFUCache.get(12));
        lFUCache.put(4,29);
        assertEquals(4, lFUCache.get(3));
        assertEquals(29, lFUCache.get(9));
        assertEquals(30, lFUCache.get(6));
        lFUCache.put(3,4);
        assertEquals(12, lFUCache.get(1));
        assertEquals(11, lFUCache.get(10));
        lFUCache.put(3,29);
        lFUCache.put(10,28);
        lFUCache.put(1,20);
        lFUCache.put(11,13);
        assertEquals(29, lFUCache.get(3));
        lFUCache.put(3,12);
        lFUCache.put(3,8);
        lFUCache.put(10,9);
        lFUCache.put(3,26);
        assertEquals(17, lFUCache.get(8));
        assertEquals(-1, lFUCache.get(7));
        assertEquals(18, lFUCache.get(5));
        lFUCache.put(13,17);
        lFUCache.put(2,27);
        lFUCache.put(11,15);
        assertEquals(-1, lFUCache.get(12));
        lFUCache.put(9,19);
        lFUCache.put(2,15);
        lFUCache.put(3,16);
        assertEquals(20, lFUCache.get(1));
        lFUCache.put(12,17);
        lFUCache.put(9,1);
        lFUCache.put(6,19);
        assertEquals(29, lFUCache.get(4));
        assertEquals(18, lFUCache.get(5));
        assertEquals(18, lFUCache.get(5));
        lFUCache.put(8,1);
        lFUCache.put(11,7);
        lFUCache.put(5,2);
        lFUCache.put(9,28);
        assertEquals(20, lFUCache.get(1));
        lFUCache.put(2,2);
        lFUCache.put(7,4);
        lFUCache.put(4,22);
        lFUCache.put(7,24);
        lFUCache.put(9,26);
        lFUCache.put(13,28);
        lFUCache.put(11,2);
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

/**
 * 更好的做法是使用彼此相连的频次双向链表。结构如下：
 * head                                                     tail
 *  ↓                                                        ↓
 * [freq 3: (1,3)⇆(2,4)⇆...] ⇆ [freq 2: (4,7)⇆(3,3)⇆...] ⇆ [freq 1: (11,3)⇆(29,0)⇆...]
 *
 * LeetCode 耗时：24 ms - 79.35%
 *          内存消耗：46.3 MB - 90.69%
 */
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
                result.prev = null;
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
                node.next = null;
            } else if (next == null) {
                tail = tail.prev;
                tail.next = null;
                node.prev = null;
            } else {
                prev.next = next;
                next.prev = prev;
                node.prev = node.next = null;
            }
        }

        boolean isEmpty() {
            return head == null;
        }

        @Override
        public String toString() {
            if (head == null)
                return "null";
            else {
                StringBuilder sb = new StringBuilder();
                for (Node p = head; p != null; p = p.next)
                    sb.append('(').append(p.key).append(',').append(p.val).append(')');
                return sb.toString();
            }
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
        if (capacity == 0)
            return;

        Node node = searchMap.get(key);
        if (node == null) {
            // 容量已满，删除计数最小的队列中最久未使用的节点
            if (searchMap.size() == capacity) {
                Map.Entry<Integer, NodeBin> entry = cntMap.firstEntry();
                //noinspection ConstantConditions
                searchMap.remove(entry.getValue().poll().key);
                if (entry.getValue().isEmpty())
                    cntMap.remove(entry.getKey());
            }

            node = new Node(key, value);
            searchMap.put(key, node);
            NodeBin bin = cntMap.get(1);
            if (bin == null)
                cntMap.put(1, bin = new NodeBin());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Integer cnt : cntMap.descendingKeySet())
            sb.append(cnt).append(':').append(cntMap.get(cnt)).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        sb.append(']');

        return sb.toString();
    }
}
