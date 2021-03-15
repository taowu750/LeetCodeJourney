package learn.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static learn.linkedlist.ListNode.*;
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
        assertTrue(listEqual(head, 5, 4, 3, 2, 1));

        head = new ListNode(3);
        assertTrue(listEqual(method.apply(head), 3));
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


    public ListNode betterRecursiveMethod(ListNode head) {
        if (head == null)
            return null;
        return recursive(head);
    }

    /**
     * 解析来自于：https://labuladong.gitee.io/algo/数据结构系列/递归反转链表的一部分.html
     *
     * 在理解这个递归方法的时候，不要试图去用脑子模拟压栈，这样没有效率且容易出错。
     *
     * 对于递归算法，最重要的就是<strong>明确递归函数的定义</strong>。具体来说，我们的 reverse 函数定义是这样的：
     *      输入一个节点 head，将「以 head 为起点」的链表反转，并返回反转之后的头结点。
     *
     * 比如说我们想反转这个链表：
     *      head
     *        ↓
     *        1->2->3->4->5->NULL
     *
     * 那么输入 reverse(head) 后，会在这里进行递归：
     *      ListNode last = reverse(head.next);
     *
     * 不要跳进递归（你的脑袋能压几个栈呀？），而是要根据刚才的<strong>函数定义</strong>，
     * 来弄清楚这段代码会产生什么结果：
     *      head
     *        ↓
     *        1->reverse(2->3->4->5->NULL)
     *
     * 这个 reverse(head.next) 执行完成后，整个链表就成了这样（由定义推得）：
     *      head         last
     *        ↓           ↓
     *        1->2<-3<-4<-5
     *           ↓
     *          NULL
     * 并且根据函数定义，reverse 函数会返回反转之后的头结点，我们用变量 last 接收了。
     *
     * 现在再来看下面的代码：
     *      head.next.next = head;
     * 就有：
     *      head         last
     *        ↓           ↓
     *        1<->2<-3<-4<-5
     *            ×
     *           NULL
     *
     * 接下来：
     *      head.next = null;
     *      return last;
     * 就有：
     *        head         last
     *          ↓           ↓
     *    NULL<-1<-2<-3<-4<-5
     */
    private ListNode recursive(ListNode head) {
        // 基准条件
        if (head.next == null)
            return head;
        ListNode last = recursive(head.next);
        head.next.next = head;
        head.next = null;

        return last;
    }

    @Test
    public void testBetterRecursiveMethod() {
        test(this::betterRecursiveMethod);
    }
}
