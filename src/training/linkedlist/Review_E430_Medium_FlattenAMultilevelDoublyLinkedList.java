package training.linkedlist;

import org.junit.jupiter.api.Test;
import util.datastructure.MultilevelDoubleLinkedListNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.MultilevelDoubleLinkedListNode.listEqual;
import static util.datastructure.MultilevelDoubleLinkedListNode.printList;

/**
 * 给定一个双向链接列表，该列表除了下一个和上一个指针外，还可以具有一个子指针，
 * 该子指针可能指向也可能不指向单独的双向链接列表。这些子列表可能有一个或多个自己的子列表，
 * 依此类推，以产生一个多级数据结构，如下例所示。
 *
 * 例 1：
 * Input: head = [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 * Output: [1,2,3,7,8,11,12,9,10,4,5,6]
 * Explanation:
 * 输入的多级链表如下所示：
 * 1---2---3---4---5---6
 *         |
 *         7---8---9---10
 *             |
 *             11---12
 * 展开后变成这样：
 * 1---2---3---7---8---11---12---9---10---4---5---6
 *
 * 例 2：
 * Input: head = [1,2,null,3]
 * Output: [1,3,2]
 * Explanation:
 * 输入的多级链表如下所示：
 * 1--2---NULL
 * |
 * 3---NULL
 *
 * 例 3：
 * Input: head = []
 * Output: []
 *
 * 例 4：
 * Input: [37099,10580,33214,85010,91986,30187,null,
 * 85793,98560,46360,50155,37244,null,
 * null,null,27598,81763,null,
 * 40623]
 * Output: [37099,85793,98560,46360,27598,40623,81763,50155,37244,10580,33214,85010,91986,30187]
 * Explanation:
 * 37099---10580---33214---85010---91986---30187
 * |
 * 85793---98560---46360---50155---37244
 *                 |
 *                 27598---81763
 *                 |
 *                 40623
 *
 * 约束：
 * - 结点数不超过 1000
 * - 1 <= Node.val <= 10**5
 */
public class Review_E430_Medium_FlattenAMultilevelDoublyLinkedList {

    public static class Node extends MultilevelDoubleLinkedListNode<Node> {

        public Node() {
        }

        public Node(int val) {
            super(val);
        }

        public Node(int val, Node prev) {
            super(val, prev);
        }
    }

    public static Node newList(Integer... vals) {
        return MultilevelDoubleLinkedListNode.newList(Node.class, vals);
    }

    static void test(Function<Node, Node> method) {
        Node head = newList(1, 2, 3, 4, 5, 6, null,
                null, null, 7, 8, 9, 10, null,
                null, 11, 12);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 1, 2, 3, 7, 8, 11, 12, 9, 10, 4, 5, 6));

        head = newList(1, 2, null,
                3);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 1, 3, 2));

        assertNull(method.apply(null));

        head = newList(1, 2, 3, 4);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 1, 2, 3, 4));

        head = newList(37099, 10580, 33214, 85010, 91986, 30187, null,
                85793, 98560, 46360, 50155, 37244, null,
                null, null, 27598, 81763, null,
                40623);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 37099, 85793, 98560, 46360, 27598, 40623,
                81763, 50155, 37244, 10580, 33214, 85010, 91986, 30187));
    }

    public Node flatten(Node head) {
        Node start = new Node(), p = start;
        start.next = head;
        Deque<Node> nextStack = new LinkedList<>();

        while (p.next != null) {
            p = p.next;
            while (p.next != null && p.child == null)
                p = p.next;
            if (p.child != null) {
                if (p.next != null)
                    nextStack.push(p.next);
                p.next = p.child;
                p.child.prev = p;
                p.child = null;
            } else {
                while (p.next == null && !nextStack.isEmpty()) {
                    p.next = nextStack.pop();
                    p.next.prev = p;
                    p = p.next;
                }
            }
        }

        return start.next;
    }

    @Test
    public void testFlatten() {
        test(this::flatten);
    }


    /**
     * 不使用栈的方法
     */
    public Node noStackMethod(Node head) {
        if (head == null)
            return null;

        Node p = head;
        while (p != null) {
            if (p.child == null) {
                p = p.next;
                continue;
            }
            Node pChild = p.child;
            // 移动到下级链表的末尾
            while (pChild.next != null)
                pChild = pChild.next;
            pChild.next = p.next;
            if (p.next != null)
                p.next.prev = pChild;

            p.next = p.child;
            p.child.prev = p.next;
            p.child = null;
        }

        return head;
    }

    @Test
    public void testNoStackMethod() {
        test(this::noStackMethod);
    }


    public Node recursiveMethod(Node head) {
        if (head == null)
            return null;
        return recur(head)[0];
    }

    private Node[] recur(Node head) {
        Node p = head;
        while (p.next != null) {
            if (p.child != null) {
                Node next = p.next;
                Node[] ht = recur(p.child);
                p.child = null;
                p.next = ht[0];
                p = ht[1];
                p.next = next;
            }
            p = p.next;
        }
        if (p.child != null) {
            Node[] ht = recur(p.child);
            p.child = null;
            p.next = ht[0];
            p = ht[1];
        }
        return new Node[]{head, p};
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
