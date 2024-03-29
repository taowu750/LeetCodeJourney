package training.linkedlist;

import org.junit.jupiter.api.Test;
import util.datastructure.RandomLinkedListNode;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNull;
import static util.datastructure.RandomLinkedListNode.deepEqualRandomList;
import static util.datastructure.RandomLinkedListNode.printRandomList;

/**
 * 138. 复制带随机指针的链表: https://leetcode.cn/problems/copy-list-with-random-pointer/
 *
 * 给定一个链表，其中每个节点都包含一个附加的随机指针，该指针可以指向列表中的任何节点或为空。
 * 返回这个链表的“深拷贝”，即结点值相同，当结点对象不同。
 *
 * 链接列表在输入/输出中表示为n个节点的列表。每个节点表示为一对 [val，random_index]，其中：
 * - val：表示 Node.val 的整数
 * - random_index：随机指针指向的节点索引（范围从0到n-1）；如果不指向任何节点，则为 null。
 *
 * 例 1：
 * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Explanation: 图片参见
 * https://leetcode.com/explore/learn/card/linked-list/213/conclusion/1229/
 *
 * 例 2：
 * Input: head = [[1,1],[2,1]]
 * Output: [[1,1],[2,1]]
 *
 * 例 3：
 * Input: head = [[3,null],[3,0],[3,null]]
 * Output: [[3,null],[3,0],[3,null]]
 *
 * 例 4：
 * Input: head = []
 * Output: []
 *
 * 约束：
 * - -10000 <= Node.val <= 10000
 * - Node.random 为 null 或指向链表中的节点。
 * - 结点数不超过 1000
 */
public class E138_Medium_CopyListWithRandomPointer {

    public static class Node extends RandomLinkedListNode<Node> {

        public Node(int val) {
            super(val);
        }
    }

    public static Node newRandomList(Integer... valAndRandomIndexes) {
        return RandomLinkedListNode.newRandomList(Node.class, valAndRandomIndexes);
    }

    @Test
    public void testRandomList() {
        Node head = newRandomList(7, null, 13, 0, 11, 4, 10, 2, 1, 0);
        printRandomList(head);
        System.out.println();

        head = newRandomList(1, 1, 2, 1);
        printRandomList(head);
        System.out.println();

        head = newRandomList(3, null, 3, 0, 3, null);
        printRandomList(head);
    }

    public static void test(Function<Node, Node> method) {
        Node head = newRandomList(7, null, 13, 0, 11, 4, 10, 2, 1, 0);
        Node newHead = method.apply(head);
        printRandomList(newHead);
        System.out.println();
        deepEqualRandomList(head, newHead);

        head = newRandomList(1, 1, 2, 1);
        newHead = method.apply(head);
        printRandomList(newHead);
        System.out.println();
        deepEqualRandomList(head, newHead);

        head = newRandomList(3, null, 3, 0, 3, null);
        newHead = method.apply(head);
        printRandomList(newHead);
        System.out.println();
        deepEqualRandomList(head, newHead);

        assertNull(method.apply(null));
    }

    public Node copyRandomList(Node head) {
        if (head == null)
            return null;

        // 使用旧结点到新结点的映射
        IdentityHashMap<Node, Node> oldToNew = new IdentityHashMap<>();
        Node newHead = new Node(0);
        for (Node p = head, pn = newHead; p != null; p = p.next, pn = pn.next) {
            pn.next = new Node(p.val);
            oldToNew.put(p, pn.next);
        }
        for (Node p = head, pn = newHead; p != null; p = p.next, pn = pn.next) {
            if (p.random == null)
                pn.next.random = null;
            else
                pn.next.random = oldToNew.get(p.random);
        }

        return newHead.next;
    }

    @Test
    public void testCopyRandomList() {
        test(this::copyRandomList);
    }



    /**
     * 学会利用原来的数据结构去解决问题
     */
    public Node betterMethod(Node head) {
        if (head == null)
            return null;

        // 穿插新结点和旧结点
        for (Node p = head; p != null;) {
            Node next = p.next;
            p.next = new Node(p.val);
            p.next.next = next;
            p = next;
        }
        // 将新结点的 random 指针指向旧结点 random 的 next，
        // 也就是穿插的新结点
        Node newHead = head.next;
        for (Node p = head, pn = newHead;;) {
            if (p.random == null) {
                pn.random = null;
            } else {
                pn.random = p.random.next;
            }
            Node next = pn.next;
            p = next;
            if (next != null) {
                pn = next.next;
            } else
                break;
        }
        // 将新结点提取出来组成新链表，并恢复旧链表的结构。
        for (Node p = head, pn = newHead;;) {
            Node next = pn.next;
            p.next = next;
            if (next != null) {
                pn.next = next.next;
                p = next;
                pn = pn.next;
            } else
                break;
        }

        return newHead;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }


    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：40.9 MB - 76.54%
     */
    public Node hashMethod(Node head) {
        Node newHead = new Node(0), q = newHead;
        // o2n 存储老链表和新链表节点的联系
        Map<Node, Node> o2n = new HashMap<>();
        for (Node p = head; p != null; p = p.next) {
            Node pp;
            if (o2n.containsKey(p)) {
                pp = o2n.get(p);
            } else {
                pp = new Node(p.val);
            }
            // 如果原节点有 random，并且新节点 random 没有被设置
            if (p.random != null && pp.random == null) {
                // 如果原节点 random 指向自己，则新节点 random 也指向自己
                if (p.random == p) {
                    pp.random = pp;
                } else if (o2n.containsKey(p.random)) {  // 如果已经有老-新联系了
                    // 这种是 random 在 p 之前的情况
                    pp.random = o2n.get(p.random);
                } else { // 没有则创建一个新的节点，并建立老-新练习
                    // 这种是 random 在 p 之后的情况
                    pp.random = new Node(p.random.val);
                    o2n.put(p.random, pp.random);
                }
            }
            o2n.put(p, pp);
            q.next = pp;
            q = pp;
        }

        return newHead.next;
    }

    @Test
    public void testHashMethod() {
        test(this::hashMethod);
    }
}
