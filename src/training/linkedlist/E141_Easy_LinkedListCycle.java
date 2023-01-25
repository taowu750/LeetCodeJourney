package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static training.linkedlist.ListNode.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 141. 环形链表：https://leetcode.cn/problems/linked-list-cycle/
 *
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
public class E141_Easy_LinkedListCycle {

    public static void test(Predicate<ListNode> method) {
        ListNode head = newList(1, 3, 2, 0, -4);
        assertTrue(method.test(head));

        head = newList(0, 1, 2);
        assertTrue(method.test(head));

        head = newList(-1, 1);
        assertFalse(method.test(head));
    }

    /**
     * 慢指针第一圈走不完一定会和快指针相遇：
     * 1. 首先，第一步，快指针先进入环
     * 2. 第二步：当慢指针刚到达环的入口时，快指针此时在环中的某个位置(也可能此时相遇)
     * 3. 第三步：设此时快指针和慢指针距离为x，若在第二步相遇，则x = 0；
     * 4. 第四步：设环的周长为n，那么看成快指针追赶慢指针，需要追赶n-x；
     * 5. 第五步：快指针每次都追赶慢指针1个单位，设慢指针速度 1，快指针 2，那么追赶需要 n-x 秒：(2(n-x)+x) % n == n-x
     * 6. 第六步：在n-x秒内，慢指针走了n-x单位，因为x>=0，则慢指针走的路程小于等于n，即走不完一圈就和快指针相遇
     */
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
