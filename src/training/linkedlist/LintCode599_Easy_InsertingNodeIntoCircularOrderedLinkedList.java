package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static training.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.SingleLinkedListNode.*;

/**
 * 给一个来自已经排过序的循环链表的节点，写一个函数来将一个值插入到循环链表中，并且保持还是有序循环链表。
 * 给出的节点可以是链表中的任意一个单节点。返回插入后的新链表。
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
 */
public class LintCode599_Easy_InsertingNodeIntoCircularOrderedLinkedList {

    static void test(BiFunction<ListNode, Integer, ListNode> method) {
        ListNode head = method.apply(newList(0, 3, 5, 1), 4);
        printList(head, 4);
        assertTrue(circularListEqual(head,3, 4, 5, 1));

        head = method.apply(newList(0, 2, 2, 2), 3);
        printList(head, 4);
        assertTrue(circularListEqual(head,2, 2, 2, 3));

        assertTrue(circularListEqual(method.apply(null, 4),4));
    }

    public ListNode insert(ListNode node, int x) {
        ListNode inserted = new ListNode(x), p = node;
        // 注意是循环链表
        if (node == null) {
            inserted.next = inserted;
            return inserted;
        }
        while (p.next != node && (x < p.val || x >  p.next.val))
            p = p.next;
        ListNode next = p.next;
        p.next = inserted;
        inserted.next = next;

        return node;
    }

    @Test
    public void testInsert() {
        test(this::insert);
    }
}
