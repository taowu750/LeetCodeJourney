package training.linkedlist;


import learn.linkedlist.ListNode;
import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import static learn.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.SingleLinkedListNode.listEqual;
import static util.datastructure.SingleLinkedListNode.printList;

/**
 * 给定一个单链表的头和两个整数 left、right，其中 left <= right，将 left 到 right 的结点反转，
 * 然后返回反转后的链表。
 *
 * 你能遍历一次就完成操作吗？
 *
 * 例 1：
 * Input: head = [1,2,3,4,5], left = 2, right = 4
 * Output: [1,4,3,2,5]
 * Explanation:
 * 1->2->3->4->5
 *      ↓
 * 1->4->3->2->5
 *
 * 例 2：
 * Input: head = [5], left = 1, right = 1
 * Output: [5]
 *
 * 约束：
 * - 链表长度为 n
 * - 1 <= n <= 500
 * - -500 <= Node.val <= 500
 * - 1 <= left <= right <= n
 */
public class E92_Medium_ReverseLinkedListII {

    static void test(TriFunction<ListNode, Integer, Integer, ListNode> method) {
        ListNode reversed = method.apply(newList(-1, 1, 2, 3, 4, 5), 2, 4);
        printList(reversed);
        assertTrue(listEqual(reversed,1, 4, 3, 2, 5));

        assertTrue(listEqual(method.apply(newList(-1, 5), 1, 1),
                5));

        reversed = method.apply(newList(-1, 1, 2, 3, 4, 5), 1, 4);
        printList(reversed);
        assertTrue(listEqual(reversed,4, 3, 2, 1, 5));

        reversed = method.apply(newList(-1, 1, 2, 3, 4, 5), 3, 5);
        printList(reversed);
        assertTrue(listEqual(reversed,1, 2, 5, 4, 3));

        reversed = method.apply(newList(-1, 1, 2, 3, 4, 5), 1, 5);
        printList(reversed);
        assertTrue(listEqual(reversed,5, 4, 3, 2, 1));
    }

    /**
     * 参见 {@link learn.linkedlist.ReverseLinkedList#betterIterateMethod(ListNode)}。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode pred = head, start = head;
        // 找到反转区域开始结点和它的前驱
        for (int i = 1; i < left; i++) {
            pred = start;
            start = start.next;
        }
        if (pred == start)
            pred = null;

        // 反转指定区域
        ListNode  newStart = null, reverseEnd = start;
        int num = left != right ? right - left + 1 : 0;
        while (num-- > 0) {
            ListNode next = start.next;
            start.next = newStart;
            newStart = start;
            start = next;
        }

        // 如果进行了反转操作
        if (newStart != null) {
            // 将反转区域和前后连接起来
            if (pred != null)
                pred.next = newStart;
            else
                head = newStart;
            reverseEnd.next = start;
        }

        return head;
    }

    @Test
    public void testReverseBetween() {
        test(this::reverseBetween);
    }


    private ListNode successor;

    /**
     * 算法思想参见 https://labuladong.gitee.io/algo/数据结构系列/递归反转链表的一部分.html#二、反转链表前-n-个节点
     */
    public ListNode recursiveMethod(ListNode head, int left, int right) {
        if (left > 1) {
            head.next = recursiveMethod(head.next, left - 1, right - 1);
            return head;
        } else if (left >= right) {
            successor = head.next;
            return head;
        } else {
            // 注意，不要让 left + 1，否则下一次递归会进入 left > 1 条件中
            ListNode last = recursiveMethod(head.next, left, right - 1);
            head.next.next = head;
            head.next = successor;
            return last;
        }
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
