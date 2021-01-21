package learn.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个链表，其中每个节点都包含一个附加的随机指针，该指针可以指向列表中的任何节点或为空。
 * 返回这个链表的“深拷贝”，即结点值相同，当结点对象不同。
 * <p>
 * 链接列表在输入/输出中表示为n个节点的列表。每个节点表示为一对 [val，random_index]，其中：
 * - val：表示 Node.val 的整数
 * - random_index：随机指针指向的节点索引（范围从0到n-1）；如果不指向任何节点，则为 null。
 * <p>
 * 例 1：
 * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Explanation: 图片参见
 * https://leetcode.com/explore/learn/card/linked-list/213/conclusion/1229/
 * <p>
 * 例 2：
 * Input: head = [[1,1],[2,1]]
 * Output: [[1,1],[2,1]]
 * <p>
 * 例 3：
 * Input: head = [[3,null],[3,0],[3,null]]
 * Output: [[3,null],[3,0],[3,null]]
 * <p>
 * 例 4：
 * Input: head = []
 * Output: []
 * <p>
 * 约束：
 * - -10000 <= Node.val <= 10000
 * - Node.random 为 null 或指向链表中的节点。
 * -结点数不超过 1000
 */
public class CopyListWithRandomPointer {

    public static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    public static Node newRandomList(Integer... valAndRandomIndexes) {
        Node start = new Node(0), p = start;
        List<Node> nodes = new ArrayList<>(valAndRandomIndexes.length / 2);
        for (int i = 0; i < valAndRandomIndexes.length; i += 2) {
            p.next = new Node(valAndRandomIndexes[i]);
            p = p.next;
            nodes.add(p);
        }
        p = start.next;
        for (int i = 0; i < valAndRandomIndexes.length; i += 2, p = p.next) {
            if (valAndRandomIndexes[i + 1] == null)
                p.random = null;
            else
                p.random = nodes.get(valAndRandomIndexes[i + 1]);
        }

        return start.next;
    }

    public static void printRandomList(Node head) {
        LinkedList<Integer> valLens = new LinkedList<>();
        for (Node p = head; p != null; p = p.next) {
            int randomVal = p.random == null ? 1000 : p.random.val;
            valLens.add(Math.max(Integer.toString(p.val).length(),
                    Integer.toString(randomVal).length()));
            System.out.printf("%-" + valLens.peekLast() + "d", p.val);
            if (p.next != null)
                System.out.print("-->");
        }
        System.out.println();
        for (int valLen : valLens)
            System.out.printf("%-" + valLen + "s   ", "|");
        System.out.println();
        Node p = head;
        for (int valLen : valLens) {
            if (p.random == null)
                System.out.printf("%-" + valLen + "s", "null");
            else
                System.out.printf("%-" + valLen + "d", p.random.val);
            if (p.next != null)
                System.out.print("-->");
            p = p.next;
        }
        System.out.println();
    }

    public static boolean deepEqualRandomList(Node l1, Node l2) {
        while (l1 != null && l2 != null) {
            if (l1 == l2)
                return false;
            if (l1.val != l2.val)
                return false;
            if (l1.random == l2.random && l1.random != null)
                return false;
            if (l1.random != null && l2.random != null && l1.random.val != l2.random.val)
                return false;
            l1 = l1.next;
            l2 = l2.next;
        }
        return l1 == null && l2 == null;
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

    static void test(Function<Node, Node> method) {
        Node head = newRandomList(7, null, 13, 0, 11, 4, 10, 2, 1, 0);
        Node newHead = method.apply(head);
        printRandomList(newHead);
        System.out.println();
        assertTrue(deepEqualRandomList(head, newHead));

        head = newRandomList(1, 1, 2, 1);
        newHead = method.apply(head);
        printRandomList(newHead);
        System.out.println();
        assertTrue(deepEqualRandomList(head, newHead));

        head = newRandomList(3, null, 3, 0, 3, null);
        newHead = method.apply(head);
        printRandomList(newHead);
        System.out.println();
        assertTrue(deepEqualRandomList(head, newHead));

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
}
