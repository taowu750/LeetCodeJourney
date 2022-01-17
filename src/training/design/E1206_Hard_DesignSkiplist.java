package training.design;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 1206. 设计跳表: https://leetcode-cn.com/problems/design-skiplist/
 *
 * 不使用任何库函数，设计一个跳表。
 * 跳表是在 O(log(n)) 时间内完成增加、删除、搜索操作的数据结构。跳表相比于树堆与红黑树，其功能与性能相当，
 * 并且跳表的代码长度相较下更短，其设计思想与链表相似。
 *
 * 跳表中有很多层，每一层是一个短的链表。在第一层的作用下，增加、删除和搜索操作的时间复杂度不超过 O(n)。
 * 跳表的每一个操作的平均时间复杂度是 O(log(n))，空间复杂度是 O(n)。
 *
 * 在本题中，你的设计应该要包含这些函数：
 * - bool search(int target) : 返回target是否存在于跳表中。
 * - void add(int num): 插入一个元素到跳表。
 * - bool erase(int num): 在跳表中删除一个值，如果num不存在，直接返回false. 如果存在多个num，删除其中任意一个即可。
 *
 * 注意，跳表中可能存在多个相同的值，你的代码需要处理这种情况。
 *
 * 跳表原理可以参见这篇博客：https://www.jianshu.com/p/9d8296562806
 *
 * 例 1：
 * Skiplist skiplist = new Skiplist();
 *
 * skiplist.add(1);
 * skiplist.add(2);
 * skiplist.add(3);
 * skiplist.search(0);   // 返回 false
 * skiplist.add(4);
 * skiplist.search(1);   // 返回 true
 * skiplist.erase(0);    // 返回 false，0 不在跳表中
 * skiplist.erase(1);    // 返回 true
 * skiplist.search(1);   // 返回 false，1 已被擦除
 *
 * 说明：
 * - 0 <= num, target <= 20000
 * - 最多调用 50000 次 search, add, 以及 erase操作。
 */
public class E1206_Hard_DesignSkiplist {

    public static void test(Supplier<ISkiplist> factory) {
        ISkiplist skiplist = factory.get();
        skiplist.add(1);
        skiplist.add(2);
        skiplist.add(3);
        assertFalse(skiplist.search(0));
        skiplist.add(4);
        assertTrue(skiplist.search(1));
        assertFalse(skiplist.erase(0));
        assertTrue(skiplist.erase(1));
        assertFalse(skiplist.search(1));
    }

    @Test
    public void testSkiplist() {
        test(Skiplist::new);
    }
}

interface ISkiplist {

    boolean search(int target);

    void add(int num);

    boolean erase(int num);
}

/**
 * 参见：
 * https://leetcode-cn.com/problems/design-skiplist/solution/javashou-xie-shi-xian-tiao-biao-by-feng-omdm0/
 *
 * LeetCode 耗时：18 ms - 84.06%
 *          内存消耗：43.9 MB - 88.12%
 */
class Skiplist implements ISkiplist {

    private static final double SKIPLIST_P = 0.5f;
    private static final int MAX_LEVEL = 32;

    private static class Node {
        int val;
        // 节点在不同层的下一个节点
        Node[] next;

        public Node(int val, int level) {
            this.val = val;
            next = new Node[level];
        }

        public Node (int level) {
            next = new Node[level];
        }
    }

    private int currentLevel;
    private Node head;

    public Skiplist() {
        currentLevel = 1;
        head = new Node(MAX_LEVEL);
    }

    public boolean search(int target) {
        Node p = head;
        for (int i = currentLevel - 1; i >= 0; i--) {
            p = findClosest(p, i, target);
            if (p.next[i] != null && p.next[i].val == target) {
                return true;
            }
        }

        return false;
    }

    /**
     * 找到 level 层 next 节点值刚好大于等于 val 的节点。
     */
    private Node findClosest(Node node, int level, int val) {
        while (node.next[level] != null && node.next[level].val < val) {
            node = node.next[level];
        }
        return node;
    }

    public void add(int num) {
        int level = randomLevel();
        Node newNode = new Node(num, level);
        Node p = head;
        // 计算出当前 num 索引的实际层数，从该层开始添加索引
        for (int i = currentLevel - 1; i >= 0; i--) {
            p = findClosest(p, i, num);
            if (i < level) {
                // 插到 p 的后面
                if (p.next[i] == null) {
                    p.next[i] = newNode;
                } else {
                    Node next = p.next[i];
                    p.next[i] = newNode;
                    newNode.next[i] = next;
                }
            }
        }
        // 如果随机出来的层数比当前的层数还大，那么超过 currentLevel 的 head 直接指向 newNode
        if (currentLevel < level) {
            for (int i = currentLevel; i < level; i++) {
                head.next[i] = newNode;
            }
            currentLevel = level;
        }
    }

    public boolean erase(int num) {
        boolean flag = false;
        Node p = head;
        for (int i = currentLevel - 1; i >= 0; i--) {
            p = findClosest(p, i, num);
            if (p.next[i] != null && p.next[i].val == num) {
                p.next[i] = p.next[i].next[i];
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 该 randomLevel 方法会随机生成 1~MAX_LEVEL 之间的数，且 ：
     *      1/2 的概率返回 1
     *      1/4 的概率返回 2
     *      1/8 的概率返回 3 以此类推
     */
    private int randomLevel() {
        int level = 1;
        // 当 level < MAX_LEVEL，且随机数小于设定的晋升概率时，level + 1
        while (Math.random() < SKIPLIST_P && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }
}

/*
    /**
     * 根据当前元素数量更新最大级数。索引级数由一下公式确立：
     *   level 1:  2/SKIPLIST_P < size
     *   level 2:  2/SKIPLIST_P^2 < size
     *   ...
     *
     * 因此 maxLevel = ⌈log_p^(2/size)⌉
     *\/
    private void updateMaxLevel() {
        maxLevel = calcMaxLevel(size);
    }

    private static int calcMaxLevel(int size) {
        return (int) (Math.log(SKIPLIST_P) / Math.log(2. / size)) + 1;
    }
 */
