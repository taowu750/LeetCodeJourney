package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static training.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 142. 环形链表 II: https://leetcode-cn.com/problems/linked-list-cycle-ii/
 *
 * 如果链表中有环，返回这个环的开始结点；没有返回 null。
 * 要求空间复杂度为 O(1)。
 * <p>
 * 例 1：
 * Input: head = [3,2,0,-4], pos = 1
 * Output: tail connects to node index 1
 * <p>
 * 例 2：
 * Input: head = [1,2], pos = 0
 * Output: tail connects to node index 0
 * <p>
 * 例 3：
 * Input: head = [1], pos = -1
 * Output: no cycle
 * <p>
 * 约束：
 * - 结点数满足 [0, 10**4]
 * - -10**5 <= Node.val <= 10**5
 * - pos 是 -1 或合法下标
 */
public class E142_Medium_LinkedListCycleHead {

    static void test(Function<ListNode, ListNode> method) {
        ListNode head = newList(1, 3, 2, 0, -4);
        assertEquals(method.apply(head), getNodeAt(head, 1));

        head = newList(0, 1, 2);
        assertEquals(method.apply(head), getNodeAt(head, 0));

        head = newList(-1, 1);
        assertNull(method.apply(head));
    }

    public ListNode detectCycle(ListNode head) {
        // 使用 HashSet 存储每个顶点
        Set<ListNode> listNodes = new HashSet<>();
        for (; head != null && !listNodes.contains(head); head = head.next)
            listNodes.add(head);
        return head;
    }

    @Test
    public void testDetectCycle() {
        test(this::detectCycle);
    }



    /**
     * 找出快指针和慢指针之间的数学关系
     *
     * 设从链表头到环的开头的长度为 x1；
     * 设从环的开头到快慢指针第一次相遇的位置的长度为 x2；
     * 设快慢指针第一次相遇的位置到环的开头的长度为 x3。
     *
     * 我们得出以下公式：
     * - 快指针移动的距离：x1 + x2 + x3 + x2 + 2nr（n = 0, 1, ...）
     * - 慢指针移动的距离：x1 + x2 + nr
     * - 因为快指针速度是慢指针的两倍，所以有：x1 + x2 + x3 + x2 + 2nr = 2(x1 + x2 + nr)
     * 最后可以得出 x1 = x3。
     *
     * 因此，我们在快慢指针相遇之后，在让两个指针分别从链表头和相遇位置开始移动，
     * 一次一步，最后它们会在环开头相遇。
     */
    public ListNode useTwoPoint(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return fast;
            }
        }

        return null;
    }

    @Test
    public void testUseTwoPoint() throws Exception {
        test(this::useTwoPoint);
    }
}
