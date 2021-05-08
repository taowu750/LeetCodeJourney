package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static training.linkedlist.ListNode.*;
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
public class Review_E234_Easy_PalindromeLinkedList {

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


    private ListNode left;

    /**
     * 使用后序遍历，模仿回文字符串的双指针算法。
     *
     * 这么做的核心逻辑就是把链表节点放入一个栈，然后再拿出来，这时候元素顺序就是反的，
     * 只不过我们利用的是递归函数的堆栈而已。
     *
     * 参考：https://labuladong.gitee.io/algo/高频面试系列/判断回文链表.html
     */
    public boolean recursiveMethod(ListNode head) {
        left = head;
        return recur(head);
    }

    private boolean recur(ListNode right) {
        if (right == null)
            return true;
        boolean res = recur(right.next);
        if (res) {
            res = right.val == left.val;
            left = left.next;
            return res;
        }
        return false;
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
