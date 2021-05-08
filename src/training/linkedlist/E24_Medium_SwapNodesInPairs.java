package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.linkedlist.ListNode.newList;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 给定一个链表，每隔两个相邻节点交换一次，并返回其头部。
 *
 * 例 1：
 * Input: head = [1,2,3,4]
 * Output: [2,1,4,3]
 * Explanation:
 * 1->2->3->4
 *     ↓
 * 2->1->4->3
 *
 * 例 2：
 * Input: head = []
 * Output: []
 *
 * 例 3：
 * Input: head = [1]
 * Output: [1]
 *
 * 约束：
 * - 结点数量范围 [0, 100]
 * - 0 <= Node.val <= 100
 */
public class E24_Medium_SwapNodesInPairs {

    static void test(Function<ListNode, ListNode> method) {
        assertTrue(listEqual(method.apply(newList(-1, 1,2,3,4)),
                2, 1, 4, 3));

        assertNull(method.apply(null));

        assertTrue(listEqual(method.apply(newList(-1, 1)),
                1));

        assertTrue(listEqual(method.apply(newList(-1, 1,2,3,4,5)),
                2, 1, 4, 3, 5));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public ListNode swapPairs(ListNode head) {
        ListNode p = head;
        while (p != null && p.next != null) {
            int tmp = p.val;
            p.val = p.next.val;
            p.next.val = tmp;
            p = p.next.next;
        }

        return head;
    }

    @Test
    public void testSwapPairs() {
        test(this::swapPairs);
    }
}
