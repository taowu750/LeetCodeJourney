package training.linkedlist;

import org.junit.jupiter.api.Test;

import static training.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 从链表中删除给定的元素 val。
 * <p>
 * 例 1：
 * Input:  1->2->6->3->4->5->6, val = 6
 * Output: 1->2->3->4->5
 */
public class E203_Easy_RemoveLinkedListElements {

    interface RemoveElementsMethod {
        ListNode removeElements(ListNode head, int val);
    }

    static void test(RemoveElementsMethod method) {
        ListNode head = newList(-1, 1, 2, 6, 3, 4, 5, 6);
        head = method.removeElements(head, 6);
        printList(head);
        assertTrue(listEqual(head, 1, 2, 3, 4, 5));

        head = new ListNode(1);
        assertNull(method.removeElements(head, 1));
    }

    /**
     * {@link Review_E19_Medium_RemoveNthNodeFromEndOfList} 中的小技巧一样，
     * 我们也可以在开头添加一个结点避免对头结点进行特殊处理。
     */
    public ListNode removeElements(ListNode head, int val) {
        ListNode start = new ListNode(0), p = start;
        start.next = head;

        while (p.next != null) {
            if (p.next.val == val) {
                p.next = p.next.next;
            } else {
                p = p.next;
            }
        }

        return start.next;
    }

    @Test
    public void testRemoveElements() {
        test(this::removeElements);
    }
}
