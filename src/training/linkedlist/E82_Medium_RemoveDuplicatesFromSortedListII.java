package training.linkedlist;

import learn.linkedlist.ListNode;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static learn.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 82. 删除排序链表中的重复元素 II: https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/
 *
 * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除链表中所有存在数字重复情况的节点，
 * 只保留原始链表中「没有重复出现」的数字。返回同样按升序排列的结果链表。
 *
 * 例 1：
 * 输入：head = [1,2,3,3,4,4,5]
 * 输出：[1,2,5]
 *
 * 例 2：
 * 输入：head = [1,1,1,2,3]
 * 输出：[2,3]
 *
 * 约束：
 * - 链表中节点数目在范围 [0, 300] 内
 * - -100 <= Node.val <= 100
 * - 题目数据保证链表已经按升序排列
 */
public class E82_Medium_RemoveDuplicatesFromSortedListII {

    static void test(UnaryOperator<ListNode> method) {
        assertTrue(listEqual(method.apply(newList(-1, 1,2,3,3,4,4,5)), 1,2,5));
        assertTrue(listEqual(method.apply(newList(-1, 1,1,1,2,3)), 2,3));
        assertNull(method.apply(newList(-1, 3,3,3)));
        assertTrue(listEqual(method.apply(newList(-1, 1,2,2,3,4,4)), 1,3));
    }

    /**
     * LeetCode 耗时：1 ms - 71.62%
     *          内存消耗：37.6 MB - 96.60%
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null)
            return null;

        ListNode newHead = new ListNode();
        newHead.next = head;
        for (ListNode par = newHead, p = head; p != null && p.next != null;) {
            int nextVal = p.next.val;
            if (p.val == nextVal) {
                while (p != null && p.val == nextVal) {
                    p = p.next;
                    par.next = p;
                }
            } else {
                par = p;
                p = p.next;
            }
        }

        return newHead.next;
    }

    @Test
    public void testDeleteDuplicates() {
        test(this::deleteDuplicates);
    }
}
