package learn.linkedlist;

import org.junit.jupiter.api.Test;

import static learn.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定链表的头，将列表向右旋转k个位置。
 * <p>
 * 例 1：
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [4,5,1,2,3]
 * <p>
 * 例 2：
 * Input: head = [0,1,2], k = 4
 * Output: [2,0,1]
 * <p>
 * 约束：
 * - 结点数范围为 [0, 500]
 * - -100 <= Node.val <= 100
 * - 0 <= k <= 2 * 10**9
 */
public class RotateList {

    interface RotateRightMethod {

        ListNode rotateRight(ListNode head, int k);
    }

    static void test(RotateRightMethod method) {
        ListNode head = newList(-1, 1, 2, 3, 4, 5);
        head = method.rotateRight(head, 2);
        printList(head);
        assertTrue(listEqual(head, 4, 5, 1, 2, 3));

        head = newList(-1, 0, 1, 2);
        head = method.rotateRight(head, 4);
        printList(head);
        assertTrue(listEqual(head, 2, 0, 1));

        head = new ListNode(1);
        assertTrue(listEqual(method.rotateRight(head, 20000), 1));

        head = newList(-1, 1, 2);
        assertTrue(listEqual(method.rotateRight(head, 20001), 2, 1));
    }

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null)
            return head;

        int len = 1;
        // 让 tail 移到链表末尾，并记录链表长度
        ListNode tail = head;
        for(; tail.next != null; tail = tail.next, len += 1);
        // 将 p 移到新的头结点的前面
        ListNode p = head;
        for (int i = len - (k = k % len) - 1; i > 0; i--, p = p.next);
        // 如果 k 不是链表长度的倍数，则设置新的头结点
        if (k > 0) {
            tail.next = head;
            head = p.next;
            p.next = null;
        }

        return head;
    }

    @Test
    public void testRotateRight() {
        test(this::rotateRight);
    }
}
