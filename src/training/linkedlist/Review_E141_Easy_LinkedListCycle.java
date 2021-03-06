package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static training.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定头，即链表的头，确定链表中是否有循环。
 * 如果列表中有某些节点可以通过连续跟随下一个指针再次到达，则链接列表中会有一个循环。
 * 我们使用 pos 用来表示最后一个结点的 next 结点的索引。请注意，pos 不是参数。
 *
 * 例 1：
 * Input: head = [3,2,0,-4], pos = 1
 * Output: true
 * Explanation:
 * (3)->(2)->(0)->(-4)-
 *       ^            |
 *       |            |
 *       --------------
 *
 * 例 2：
 * Input: head = [1,2], pos = 0
 * Output: true
 *
 * 例 3：
 * Input: head = [1], pos = -1
 * Output: false
 *
 * 约束：
 * - 结点数满足 [0, 10**4]
 * - -10**5 <= Node.val <= 10**5
 * - pos 是 -1 或合法下标
 */
public class Review_E141_Easy_LinkedListCycle {

    static void test(Predicate<ListNode> method) {
        ListNode head = newList(1, 3, 2, 0, -4);
        assertTrue(method.test(head));

        head = newList(0, 1, 2);
        assertTrue(method.test(head));

        head = newList(-1, 1);
        assertFalse(method.test(head));
    }

    public boolean hasCycle(ListNode head) {
        // 使用快慢指针
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast)
                return true;
        }

        return false;
    }

    @Test
    public void testHasCycle() {
        test(this::hasCycle);
    }
}
