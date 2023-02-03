package training.linkedlist;

import org.junit.jupiter.api.Test;
import util.datastructure.SingleLinkedListNode;

import java.util.function.BiFunction;

import static util.datastructure.SingleLinkedListNode.circularSortedListEqual;
import static util.datastructure.SingleLinkedListNode.printList;

/**
 * 708. 循环有序列表的插入: https://leetcode.cn/problems/insert-into-a-sorted-circular-linked-list/
 *
 * 给定循环单调非递减列表中的一个点，写一个函数向这个列表中插入一个新元素 insertVal ，使这个列表仍然是循环非降序的。
 *
 * 给定的可以是这个列表中任意一个顶点的指针，并不一定是这个列表中最小元素的指针。
 *
 * 如果有多个满足条件的插入位置，你可以选择任意一个位置插入新的值，插入后整个列表仍然保持有序。
 *
 * 如果列表为空（给定的节点是 null），你需要创建一个循环有序列表并返回这个节点。否则，请返回原先给定的节点。
 *
 * 例 1：
 * 输入:
 * 3->5->1
 * 4
 * 输出:
 * 5->1->3->4
 * 解释：
 * 3->5->1 是一个循环链表，所以 3 是 1 的下一个节点。3->5->1 与 1->3->5 相同。
 *
 * 例 2：
 * 输入:
 * 2->2->2
 * 3
 * 输出:
 * 3->2->2->2
 *
 * 说明：
 * - 0 <= Number of Nodes <= 5 * 10^4
 * - -10^6 <= Node.val, insertVal <= 10^6
 */
public class E708_Medium_InsertingNodeIntoCircularOrderedLinkedList {

    public static class Node extends SingleLinkedListNode<Node> {

        public Node() {
            super();
        }

        public Node(int val) {
            super(val);
        }

        public Node(int val, Node next) {
            super(val, next);
        }

        public static Node newList(int pos, int... vals) {
            return newList(Node.class, pos, vals);
        }
    }

    public static void test(BiFunction<Node, Integer, Node> method) {
        Node head = method.apply(Node.newList(0, 3, 5, 1), 4);
        printList(head, 4);
        circularSortedListEqual(head,3, 4, 5, 1);

        head = method.apply(Node.newList(0, 2, 2, 2), 3);
        printList(head, 4);
        circularSortedListEqual(head,2, 2, 2, 3);

        circularSortedListEqual(method.apply(null, 4),4);

        circularSortedListEqual(method.apply(Node.newList(0, 1), 0),
                1, 0);

        circularSortedListEqual(method.apply(Node.newList(0, 3, 5, 1), 0),
                3, 5, 0, 1);

        circularSortedListEqual(method.apply(Node.newList(0, 1, 3, 5), 6),
                1, 3, 5, 6);

        circularSortedListEqual(method.apply(Node.newList(0, 3, 4, 1), 2),
                3, 4, 1, 2);
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：41.3 MB - 14.08%
     */
    public Node insert(Node head, int insertVal) {
        if (head == null) {
            head = new Node(insertVal);
            head.next = head;
            return head;
        }

        Node p = head;
        do {
            /*
            插入节点有三种情况：
            1. 在链表之间
            2. 是最小值
            3. 是最大值
             */
            if ((insertVal >= p.val && insertVal <= p.next.val) ||
                    (p.val > p.next.val && (insertVal < p.next.val || insertVal > p.val))) {
                break;
            }
            p = p.next;
        } while (p != head);
        p.next = new Node(insertVal, p.next);

        return head;
    }

    @Test
    public void testInsert() {
        test(this::insert);
    }
}
