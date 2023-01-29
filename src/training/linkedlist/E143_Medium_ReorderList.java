package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static training.linkedlist.ListNode.newList;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 143. 重排链表: https://leetcode-cn.com/problems/reorder-list/submissions/
 *
 * 给定一个单链表 L：L0 → L1 → … → Ln-1 → Ln ，
 * 将其重新排列后变为： L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → …
 *
 * 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 *
 * 例 1：
 * 给定链表 1->2->3->4, 重新排列为 1->4->2->3.
 *
 * 例 2：
 * 给定链表 1->2->3->4->5, 重新排列为 1->5->2->4->3.
 */
public class E143_Medium_ReorderList {

    public static void test(Consumer<ListNode> method) {
        ListNode head = newList(-1, 1,2,3,4);
        method.accept(head);
        listEqual(head, 1,4,2,3);

        head = newList(-1, 1,2,3,4,5);
        method.accept(head);
        listEqual(head, 1,5,2,4,3);

        head = newList(-1, 1,2);
        method.accept(head);
        listEqual(head, 1,2);

        head = newList(-1, 1,2,3);
        method.accept(head);
        listEqual(head, 1,3,2);
    }

    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：41.1 MB - 54.20%
     */
    public void reorderList(ListNode head) {
        if (head == null || head.next == null)
            return;

        ListNode slow = head, fast = head, parent = head;
        while (fast != null && fast.next != null) {
            parent = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        // 链表长度为奇数，让 slow 再往前一步
        if (fast != null) {
            parent = slow;
            slow = slow.next;
        }
        // 断掉前半部分和后半部分的链接
        parent.next = null;

        // 翻转后半部分的链表
        ListNode rightHalfHead = null;
        while (slow != null) {
            ListNode next = slow.next;
            slow.next = rightHalfHead;
            rightHalfHead = slow;
            slow = next;
        }

        // 交叉节点
        ListNode p = head;
        while (rightHalfHead != null) {
            ListNode pNext = p.next, rNext = rightHalfHead.next;
            p.next = rightHalfHead;
            rightHalfHead.next = pNext;
            p = pNext;
            rightHalfHead = rNext;
        }
    }

    @Test
    public void testReorderList() {
        test(this::reorderList);
    }
}
