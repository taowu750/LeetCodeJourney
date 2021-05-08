package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static training.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 83. 删除排序链表中的重复元素: https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/
 *
 * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除所有重复的元素，使每个元素「只出现一次」。
 * 返回同样按升序排列的结果链表。
 *
 * 例 1：
 * 输入：head = [1,1,2]
 * 输出：[1,2]
 *
 * 例 2：
 * 输入：head = [1,1,2,3,3]
 * 输出：[1,2,3]
 *
 * 约束：
 * - 链表中节点数目在范围 [0, 300] 内
 * - -100 <= Node.val <= 100
 * - 题目数据保证链表已经按升序排列
 */
public class E83_Easy_RemoveDuplicatesFromSortedList {

    static void test(UnaryOperator<ListNode> method) {
        assertTrue(listEqual(method.apply(newList(-1, 1,1,2)),
                1,2));

        assertTrue(listEqual(method.apply(newList(-1, 1,1,2,3,3)),
                1,2,3));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.9 MB - 52.67%
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null)
            return null;

        for (ListNode p = head; p.next != null;) {
            if (p.val == p.next.val) {
                p.next = p.next.next;
            } else {
                p = p.next;
            }
        }

        return head;
    }

    @Test
    public void testDeleteDuplicates() {
        test(this::deleteDuplicates);
    }
}
