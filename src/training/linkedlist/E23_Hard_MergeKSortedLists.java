package training.linkedlist;

import learn.linkedlist.ListNode;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;

import static learn.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 23. 合并K个升序链表: https://leetcode-cn.com/problems/merge-k-sorted-lists/
 *
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 *
 * 例 1：
 * 输入：lists = [[1,4,5],[1,3,4],[2,6]]
 * 输出：[1,1,2,3,4,4,5,6]
 * 解释：链表数组如下：
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * 将它们合并到一个有序链表中得到。
 * 1->1->2->3->4->4->5->6
 *
 * 例 2：
 * 输入：lists = []
 * 输出：[]
 *
 * 例 3：
 * 输入：lists = [[]]
 * 输出：[]
 *
 * 约束：
 * - k == lists.length
 * - 0 <= k <= 10^4
 * - 0 <= lists[i].length <= 500
 * - -10^4 <= lists[i][j] <= 10^4
 * - lists[i] 按升序排列
 * - lists[i].length 的总和不超过 10^4
 */
public class E23_Hard_MergeKSortedLists {

    static void test(Function<ListNode[], ListNode> method) {
        assertTrue(listEqual(method.apply(new ListNode[]{
                        newList(-1, 1,4,5),
                        newList(-1, 1,3,4),
                        newList(-1, 2,6),}),
                1,1,2,3,4,4,5,6));

        assertNull(method.apply(new ListNode[0]));
        assertNull(method.apply(new ListNode[]{null, null}));

        assertTrue(listEqual(method.apply(new ListNode[]{
                        newList(-1, 1,2),
                        null,
                        newList(-1, 1,3,4,9,100),
                        null,
                        newList(-1, 5),}),
                1,1,2,3,4,5,9,100));
    }

    /**
     * 时间复杂度 O(NK)
     *
     * LeetCode 耗时：126 ms - 18.88%
     *          内存消耗：40.1 MB - 69.02%
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;

        ListNode last = lists[0], head = new ListNode();
        for (int i = 1; i < lists.length; i++) {
            ListNode current = lists[i], p = head;
            while (last != null && current != null) {
                int cmp = Integer.compare(last.val, current.val);
                if (cmp <= 0) {
                    p.next = last;
                    last = last.next;
                } else {
                    p.next = current;
                    current = current.next;
                }
                p = p.next;
            }
            if (last != null)
                p.next = last;
            if (current != null)
                p.next = current;
            last = head.next;
            head.next = null;
        }

        return last;
    }

    @Test
    void testMergeKLists() {
        test(this::mergeKLists);
    }


    /**
     * 时间复杂度 O(N log(K))
     *
     * LeetCode 耗时：5 ms - 66.20%
     *          内存消耗：40.3 MB - 41.16%
     */
    public ListNode pqMethod(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;

        PriorityQueue<ListNode> pq = new PriorityQueue<>(lists.length, new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return Integer.compare(o1.val, o2.val);
            }
        });

        for (ListNode list : lists) {
            if (list != null)
                pq.add(list);
        }
        ListNode head = new ListNode(), p = head;
        while (!pq.isEmpty()) {
            p.next = pq.remove();
            if (p.next.next != null)
                pq.add(p.next.next);
            p = p.next;
        }

        return head.next;
    }

    @Test
    void testPqMethod() {
        test(this::pqMethod);
    }
}
