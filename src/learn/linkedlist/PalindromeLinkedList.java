package learn.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static learn.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个单链表，确定它是否是回文。
 * 要求时间复杂度 O(n)，空间复杂度 O(1)。
 * <p>
 * 例 1：
 * Input: 1->2
 * Output: false
 * <p>
 * 例 2：
 * Input: 1->2->2->1
 * Output: true
 */
public class PalindromeLinkedList {

    static void test(Predicate<ListNode> method) {
        ListNode head = newList(-1, 1, 2);
        assertFalse(method.test(head));

        head = newList(-1, 1, 2, 2, 1);
        assertTrue(method.test(head));

        head = new ListNode(1);
        assertTrue(method.test(head));

        assertTrue(method.test(null));
    }

    public boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        // 让 slow 走到链表一半位置
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 如果 fast 不等于 null，链表包含奇数个元素，slow 需要再移动一次
        if (fast != null)
            slow = slow.next;

        // 反转后半段
        ListNode secondHalfHead = null;
        while (slow != null) {
            ListNode next = slow.next;
            slow.next = secondHalfHead;
            secondHalfHead = slow;
            slow = next;
        }

        // 比较前半段和反转的后半段是否相同
        for (; secondHalfHead != null; head = head.next, secondHalfHead = secondHalfHead.next) {
            if (head.val != secondHalfHead.val)
                return false;
        }

        return true;
    }

    @Test
    public void testIsPalindrome() {
        test(this::isPalindrome);
    }
}
