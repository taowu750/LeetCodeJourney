package learn.linkedlist;

import org.junit.jupiter.api.Test;

import static learn.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定链表的开头，请从链表末尾删除第 n 个节点并返回其开头。
 * 你可以遍历一次完成此操作吗？
 *
 * 例 1：
 * Input: head = [1,2,3,4,5], n = 2
 * Output: [1,2,3,5]
 * Explanation:
 * 1->2->3->(4)->5
 *       |
 *       V
 * 1->2->3------>5
 *
 * 例 2：
 * Input: head = [1], n = 1
 * Output: []
 *
 * 例 3：
 * Input: head = [1,2], n = 1
 * Output: [1]
 *
 * 约束：
 * - 结点数量为 sz.
 * - 1 <= sz <= 30
 * - 0 <= Node.val <= 100
 * - 1 <= n <= sz
 */
public class RemoveNthNodeFromEndOfList {

    interface RemoveNthFromEndMethod {

        ListNode removeNthFromEnd(ListNode head, int n);
    }

    static void test(RemoveNthFromEndMethod method) {
        ListNode head = newList(-1, 1, 2, 3, 4, 5);
        head = method.removeNthFromEnd(head, 2);
        printList(head);
        assertTrue(listEqual(head,1, 2, 3, 5));

        head = newList(-1, 1);
        head = method.removeNthFromEnd(head, 1);
        assertNull(head);

        head = newList(-1, 1, 2);
        head = method.removeNthFromEnd(head, 1);
        printList(head);
        assertTrue(listEqual(head,1));

        head = newList(-1, 1, 2, 3, 4);
        head = method.removeNthFromEnd(head, 4);
        printList(head);
        assertTrue(listEqual(head, 2, 3, 4));
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode front = head, behind = head, par = head;
        // 将 behind 移动 n 位
        for(int i = 0; i < n; behind = behind.next, i++);

        // 删除一一个元素
        if (behind == null)
            return head.next;

        // 让 front 移动到待删除元素位置
        while (behind != null) {
            par = front;
            front = front.next;
            behind = behind.next;
        }
        par.next = front.next;

        return head;
    }

    @Test
    public void testRemoveNthFromEnd() {
        test(this::removeNthFromEnd);
    }



    public ListNode trick(ListNode head, int n) {
        // 小技巧：在开头加一个结点，消除处理头结点的特殊情况
        ListNode start = new ListNode(0);
        ListNode slow = start, fast = start;
        slow.next = head;
        // 移动 fast 指针 n + 1 次
        for(int i = 0; i <= n; fast = fast.next, i++);

        // 将 slow 移动到待删除元素之前
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }
        slow.next = slow.next.next;

        return start.next;
    }

    @Test
    public void testTrick() {
        test(this::trick);
    }
}
