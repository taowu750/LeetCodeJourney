package training.linkedlist;

import learn.linkedlist.ListNode;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static learn.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.SingleLinkedListNode.listEqual;
import static util.datastructure.SingleLinkedListNode.printList;

/**
 * 给定一个链表，每次反转链表 k 个节点，最后返回其修改后的列表。
 *
 * k 是正整数，小于或等于链表的长度。如果节点数不是 k 的倍数，那么剩下节点最终应该保持原样。
 *
 * 你能否只使用 O(1) 的额外空间？
 * 不能更改列表节点中的值，只能更改节点本身。
 *
 * 例 1：
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [2,1,4,3,5]
 * Explanation:
 * (1->2)->(3->4)->5
 *        ↓
 * (2->1)->(4->3)->5
 *
 * 例 2：
 * Input: head = [1,2,3,4,5], k = 3
 * Output: [3,2,1,4,5]
 * Explanation:
 * (1->2->3)->4->5
 *        ↓
 * (3->2->1)->4->5
 *
 * 例 3：
 * Input: head = [1,2,3,4,5], k = 1
 * Output: [1,2,3,4,5]
 *
 * 例 4：
 * Input: head = [1], k = 1
 * Output: [1]
 *
 * 约束：
 * - 链表结点数量 sz 的范围 为 [1, 5000]
 * - 0 <= Node.val <= 1000
 * - 1 <= k <= sz
 */
public class E25_Hard_ReverseNodesInKGroup {

    static void test(BiFunction<ListNode, Integer, ListNode> method) {
        ListNode head = newList(-1, 1, 2, 3, 4, 5);
        head = method.apply(head, 2);
        printList(head);
        assertTrue(listEqual(head, 2, 1, 4, 3, 5));

        head = newList(-1, 1, 2, 3, 4, 5);
        head = method.apply(head, 3);
        printList(head);
        assertTrue(listEqual(head, 3, 2, 1, 4, 5));

        head = newList(-1, 1, 2, 3, 4, 5);
        head = method.apply(head, 1);
        printList(head);
        assertTrue(listEqual(head, 1, 2, 3, 4, 5));

        head = newList(-1, 1, 2, 3, 4, 5);
        head = method.apply(head, 5);
        printList(head);
        assertTrue(listEqual(head, 5, 4, 3, 2, 1));

        head = new ListNode(1);
        head = method.apply(head, 1);
        printList(head);
        assertTrue(listEqual(head, 1));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (k == 1)
            return head;

        ListNode tmpHead = new ListNode(), lastTail = tmpHead, p = head;
        int reverseCnt;
        while (p != null) {
            ListNode start = null, end = p;
            for (reverseCnt = 0; reverseCnt < k && p != null; reverseCnt++) {
                ListNode next = p.next;
                p.next = start;
                start = p;
                p = next;
            }
            // 最后不足 k 个结点需要保持原样，因此要反转回来
            if (reverseCnt < k) {
                p = start;
                start = null;
                while (p != null) {
                    ListNode next = p.next;
                    p.next = start;
                    start = p;
                    p = next;
                }
            }
            // 进行下一次反转
            lastTail.next = start;
            lastTail = end;
        }

        return tmpHead.next;
    }

    @Test
    public void testReverseKGroup() {
        test(this::reverseKGroup);
    }


    /**
     * 每次反转前面 k 个结点，后面剩余的结点又可以看作一个链表，因此这是一个递归的结构。
     *
     * 我们可以实现一个反转一个区间之内的子函数，来模块化算法，简化我们的递归思考流程。
     *
     * 参考：https://labuladong.gitee.io/algo/高频面试系列/k个一组反转链表.html
     */
    ListNode recursiveMethod(ListNode head, int k) {
        if (head == null)
            return null;
        ListNode b = head;
        for (int i = 0; i < k; i++) {
            // 不足 k 个，不需要反转
            if (b == null)
                return head;
            b = b.next;
        }
        // 反转前 k 个元素
        ListNode newHead = reverse(head, b);
        // 递归反转后续链表并连接起来
        head.next = reverseKGroup(b, k);
        return newHead;
    }

    /**
     * 反转区间 [a, b) 的链表
     */
    public ListNode reverse(ListNode a, ListNode b) {
        ListNode head = b;
        while (a != b) {
            ListNode next = a.next;
            a.next = head;
            head = a;
            a = next;
        }
        return head;
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
