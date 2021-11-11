package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static training.linkedlist.ListNode.newList;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 86. 分隔链表: https://leetcode-cn.com/problems/partition-list/
 *
 * 给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有「小于 x 的节点都出现在」大于或等于 x 的节点之前。
 *
 * 你应当「保留」两个分区中每个节点的初始相对位置。
 *
 * 例 1：
 * 输入：head = [1,4,3,2,5,2], x = 3
 * 输出：[1,2,2,4,3,5]
 *
 * 例 2：
 * 输入：head = [2,1], x = 2
 * 输出：[1,2]
 *
 * 说明：
 * - 链表中节点的数目在范围 [0, 200] 内
 * - -100 <= Node.val <= 100
 * - -200 <= x <= 200
 */
public class E86_Medium_PartitionList {

    public static void test(BiFunction<ListNode, Integer, ListNode> method) {
        listEqual(method.apply(newList(-1, 1,4,3,2,5,2), 3),
                1,2,2,4,3,5);
        listEqual(method.apply(newList(-1, 2,1), 2),
                1,2);
        listEqual(method.apply(newList(-1, 1,3,4,2,6), 7),
                1,3,4,2,6);
        listEqual(method.apply(newList(-1, 1,3,4,2,6), -1),
                1,3,4,2,6);
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.8 MB - 30.27%
     */
    public ListNode partition(ListNode head, int x) {
        // 分成两部分链表，一部分小于 x 的，其余大于等于 x 的
        ListNode lower = new ListNode(), other = new ListNode();
        ListNode lowerHead = lower, otherHead = other;
        while (head != null) {
            if (head.val < x) {
                lower.next = head;
                lower = lower.next;
            } else {
                other.next = head;
                other = other.next;
            }
            ListNode next = head.next;
            head.next = null;
            head = next;
        }
        lower.next = otherHead.next;

        return lowerHead.next;
    }

    @Test
    public void testPartition() {
        test(this::partition);
    }
}
