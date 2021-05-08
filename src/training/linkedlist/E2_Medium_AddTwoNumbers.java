package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static training.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定两个非空链表，它们代表两个非负整数。每个节点都包含一个十进制位数，
 * 整数的十进制位数以相反的顺序存储。将两个数字相加并返回和的位数相反顺序链表。
 * 除了数字0本身以外，其他数字都不包含任何前导零。
 * <p>
 * 例 1：
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [7,0,8]
 * Explanation: 342 + 465 = 807.
 * <p>
 * 例 2：
 * Input: l1 = [0], l2 = [0]
 * Output: [0]
 * <p>
 * 例 3：
 * Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * Output: [8,9,9,9,0,0,0,1]
 * <p>
 * 约束：
 * - 链表结点数范围为 [1, 100]。
 * - 0 <= Node.val <= 9
 * - 除了数字 0 以外，确保列表表示的数字没有前导零。
 */
public class E2_Medium_AddTwoNumbers {

    static void test(BiFunction<ListNode, ListNode, ListNode> method) {
        ListNode l1 = newList(-1, 2, 4, 3),
                l2 = newList(-1, 5, 6, 4);
        ListNode sum = method.apply(l1, l2);
        printList(sum);
        assertTrue(listEqual(sum, 7, 0, 8));

        l1 = newList(-1, 0);
        l2 = newList(-1, 0);
        sum = method.apply(l1, l2);
        printList(sum);
        assertTrue(listEqual(sum, 0));

        l1 = newList(-1, 9, 9, 9, 9, 9, 9, 9);
        l2 = newList(-1, 9, 9, 9, 9);
        sum = method.apply(l1, l2);
        printList(sum);
        assertTrue(listEqual(sum, 8, 9, 9, 9, 0, 0, 0, 1));
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode sumHead = new ListNode(-1), p = sumHead;
        int carry = 0;

        for (;l1 != null && l2 != null; l1 = l1.next, l2 = l2.next, p = p.next) {
            int sum = l1.val + l2.val + carry;
            p.next = new ListNode(sum % 10);
            carry = sum / 10;
        }
        for (; l1 != null; l1 = l1.next, p = p.next) {
            int sum = l1.val + carry;
            p.next = new ListNode(sum % 10);
            carry = sum / 10;
        }
        for (; l2 != null; l2 = l2.next, p = p.next) {
            int sum = l2.val + carry;
            p.next = new ListNode(sum % 10);
            carry = sum / 10;
        }
        if (carry != 0) {
            p.next = new ListNode(carry);
        }

        return sumHead.next;
    }

    @Test
    public void testAddTwoNumbers() {
        test(this::addTwoNumbers);
    }
}
