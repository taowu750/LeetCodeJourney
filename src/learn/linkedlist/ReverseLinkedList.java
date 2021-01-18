package learn.linkedlist;

import learn.linkedlist.LinkedListCycle.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static learn.linkedlist.LinkedListCycle.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 反转单链表。
 * 可以迭代或递归地反向链接列表。你能同时实现吗？
 * <p>
 * 例 1：
 * Input: 1->2->3->4->5->NULL
 * Output: 5->4->3->2->1->NULL
 */
public class ReverseLinkedList {

    static void test(Function<ListNode, ListNode> method) {
        ListNode head = newList(-1, 1, 2, 3, 4, 5);
        head = method.apply(head);
        printList(head);
        assertTrue(listEquals(head, 5, 4, 3, 2, 1));

        head = new ListNode(3);
        assertTrue(listEquals(method.apply(head), 3));
    }

    public ListNode reverseList(ListNode head) {
        if (head == null)
            return null;

        ListNode tail = head;
        while (tail.next != null) {
            ListNode next = tail.next;
            tail.next = next.next;
            next.next = head;
            head = next;
        }

        return head;
    }

    @Test
    public void testReverseList() {
        test(this::reverseList);
    }


    /**
     * 更好的迭代方法。
     */
    public ListNode betterIterateMethod(ListNode head) {
        // head 是原链表的开头
        // newHead 是新的链表（反转）的开头
        ListNode newHead = null;
        while (head != null) {
            ListNode next = head.next;
            // 这类似于一个压栈操作。从 head 链表去除头元素压到 newHead “栈”中
            head.next = newHead;
            newHead = head;
            head = next;
        }

        return newHead;
    }

    @Test
    public void testBetterIterateMethod() {
        test(this::betterIterateMethod);
    }

    /**
     * 递归解法
     */
    public ListNode recursiveMethod(ListNode head) {
        return recursiveMethod(head, null);
    }

    private ListNode recursiveMethod(ListNode head, ListNode newHead) {
        if (head == null)
            return newHead;
        ListNode next = head.next;
        head.next = newHead;
        return recursiveMethod(next, head);
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
