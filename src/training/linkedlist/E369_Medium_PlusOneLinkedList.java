package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static training.linkedlist.ListNode.newList;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 369. 给单链表加一: https://leetcode-cn.com/problems/plus-one-linked-list/
 *
 * 用一个「非空」单链表来表示一个非负整数，然后将这个整数加一。
 *
 * 你可以假设这个整数除了 0 本身，没有任何前导的 0。
 *
 * 这个整数的各个数位按照「高位在链表头部、低位在链表尾部」的顺序排列。
 *
 * 例 1：
 * 输入: [1,2,3]
 * 输出: [1,2,4]
 */
public class E369_Medium_PlusOneLinkedList {

    public static void test(UnaryOperator<ListNode> method) {
        listEqual(method.apply(newList(-1, 1,2,3)), 1, 2, 4);
        listEqual(method.apply(newList(-1, 9,9,9)), 1, 0, 0, 0);
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36 MB - 74.19%
     */
    public ListNode plusOne(ListNode head) {
        head = reverse(head);
        ListNode p = head, par = p;
        int carry = 1;
        while (p != null) {
            int sum = p.val + carry;
            p.val = sum % 10;
            carry = sum / 10;
            par = p;
            p = p.next;
        }
        if (carry != 0) {
            par.next = new ListNode(carry);
        }

        return reverse(head);
    }

    private ListNode reverse(ListNode head) {
        ListNode newHead = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = newHead;
            newHead = head;
            head = next;
        }

        return newHead;
    }

    @Test
    public void testPlusOne() {
        test(this::plusOne);
    }
}
