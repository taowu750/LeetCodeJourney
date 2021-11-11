package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.BinaryOperator;

import static training.linkedlist.ListNode.newList;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 445. 两数相加 II: https://leetcode-cn.com/problems/add-two-numbers-ii/
 *
 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。
 * 将这两数相加会返回一个新的链表。
 *
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
 *
 * 例 1：
 * 输入：l1 = [7,2,4,3], l2 = [5,6,4]
 * 输出：[7,8,0,7]
 *
 * 例 2：
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[8,0,7]
 *
 * 例 3：
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 *
 * 说明：
 * - 链表的长度范围为 [1, 100]
 * - 0 <= node.val <= 9
 * - 输入数据保证链表代表的数字无前导 0
 */
public class E445_Medium_AddTwoNumbersII {

    public static void test(BinaryOperator<ListNode> method) {
        listEqual(method.apply(newList(-1, 7,2,4,3), newList(-1, 5,6,4)),
                7,8,0,7);
        listEqual(method.apply(newList(-1, 2,4,3), newList(-1, 5,6,4)),
                8,0,7);
        listEqual(method.apply(newList(-1, 0), newList(-1, 0)),
                0);
        listEqual(method.apply(newList(-1, 5), newList(-1, 5)),
                1, 0);
    }

    /**
     * LeetCode 耗时：2 ms - 99.12%
     *          内存消耗：38.9 MB - 8.07%
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null || l1.val == 0) {
            return l2;
        } else if (l2 == null || l2.val == 0) {
            return l1;
        }

        l1 = reverse(l1);
        l2 = reverse(l2);

        ListNode newHead = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            if (l1 == null) {
                ListNode next = l2.next;
                int tmp = carry;
                carry = (l2.val + tmp) / 10;
                l2.val = (l2.val + tmp) % 10;
                l2.next = newHead;
                newHead = l2;
                l2 = next;
            } else if (l2 == null) {
                ListNode next = l1.next;
                int tmp = carry;
                carry = (l1.val + tmp) / 10;
                l1.val = (l1.val + tmp) % 10;
                l1.next = newHead;
                newHead = l1;
                l1 = next;
            } else {
                ListNode next = l1.next;
                int tmp = carry;
                carry = (l1.val + l2.val + tmp) / 10;
                l1.val = (l1.val + l2.val + tmp) % 10;
                l1.next = newHead;
                newHead = l1;
                l1 = next;
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            ListNode node = new ListNode(carry);
            node.next = newHead;
            newHead = node;
        }

        return newHead;
    }

    private ListNode reverse(ListNode list) {
        ListNode newHead = null;
        while (list != null) {
            ListNode next = list.next;
            list.next = newHead;
            newHead = list;
            list = next;
        }

        return newHead;
    }

    @Test
    public void testAddTwoNumbers() {
        test(this::addTwoNumbers);
    }
}
