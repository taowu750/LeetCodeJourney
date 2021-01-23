package learn.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static learn.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 合并两个排序的链表，并将其作为排序表返回。该列表应通过将前两个列表的节点拼接在一起制成。
 * <p>
 * 例 1：
 * Input: l1 = [1,2,4], l2 = [1,3,4]
 * Output: [1,1,2,3,4,4]
 * <p>
 * 例 2：
 * Input: l1 = [], l2 = []
 * Output: []
 * <p>
 * 例 3：
 * Input: l1 = [], l2 = [0]
 * Output: [0]
 *
 * 约束：
 * - 链表的长度范围为 [0, 50]
 * - -100 <= Node.val <= 100
 * - 两个链表都是非递减链表
 */
public class MergeTwoSortedLists {

    static void test(BiFunction<ListNode, ListNode, ListNode> method) {
        ListNode l1 = newList(-1, 1, 2, 4),
                l2 = newList(-1, 1, 3, 4);
        ListNode merged = method.apply(l1, l2);
        printList(merged);
        assertTrue(listEquals(merged, 1, 1, 2, 3, 4, 4));

        assertNull(method.apply(null, null));

        assertTrue(listEquals(method.apply(null, new ListNode(0)),
                0));
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode start = new ListNode(0), p = start;

        for (;l1 != null && l2 != null; p = p.next) {
            if (l1.val < l2.val) {
                p.next = l1;
                l1 = l1.next;
            } else if (l1.val > l2.val) {
                p.next = l2;
                l2 = l2.next;
            } else {
                p.next = l1;
                l1 = l1.next;
                p = p.next;
                p.next = l2;
                l2 = l2.next;
            }
        }
        if (l1 != null)
            p.next = l1;
        else if (l2 != null)
            p.next = l2;

        return start.next;
    }

    @Test
    public void testMergeTwoLists() {
        test(this::mergeTwoLists);
    }
}
