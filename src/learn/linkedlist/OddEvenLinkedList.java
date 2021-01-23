package learn.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static learn.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个单链表，将所有奇数节点组合在一起，然后是偶数节点。请注意，
 * 这里我们谈论的是节点号，而不是节点中的值。
 * 要求时间复杂度 O(n)，空间复杂度 O(1)。
 * <p>
 * 例 1：
 * Input: 1->2->3->4->5->NULL
 * Output: 1->3->5->2->4->NULL
 * <p>
 * 例 2：
 * Input: 2->1->3->5->6->4->7->NULL
 * Output: 2->3->6->7->1->5->4->NULL
 * <p>
 * 约束：
 * - 偶数和奇数组中的相对顺序应保持输入中的原样。
 * - 第一个节点被认为是奇数，第二个节点被认为是偶数，依此类推...
 * - 链表的长度在 [0, 10**4] 之间。
 */
public class OddEvenLinkedList {

    static void test(Function<ListNode, ListNode> method) {
        ListNode head = newList(-1, 1, 2, 3, 4, 5);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 1, 3, 5, 2, 4));

        head = newList(-1, 2, 1, 3, 5, 6, 4, 7);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 2, 3, 6, 7, 1, 5, 4));

        head = new ListNode(1);
        head = method.apply(head);
        printList(head);
        assertTrue(listEqual(head, 1));

        head = method.apply(null);
        assertNull(head);
    }

    public ListNode oddEvenList(ListNode head) {
        ListNode oddHead = new ListNode(0),
                evenHead = new ListNode(0),
                oddTail = oddHead,
                evenTail = evenHead;

        for (int i = 1; head != null; i++, head = head.next) {
            if ((i & 1) == 1) {
                oddTail.next = head;
                oddTail = head;
            } else {
                evenTail.next = head;
                evenTail = head;
            }
        }
        evenTail.next = null;
        oddTail.next = evenHead.next;

        return oddHead.next;
    }

    @Test
    public void testOddEvenList() {
        test(this::oddEvenList);
    }
}
