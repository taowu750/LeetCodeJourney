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

    public static void test(Function<ListNode, ListNode> method) {
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
     * 设从链表头到环的开头的长度为 a；
     * 设从环的开头到快慢指针第一次相遇的位置的长度为 b；
     * 设快慢指针第一次相遇的位置到环的开头的长度为 c。
     *
     * slow 指针进入环后，又走了 b 的距离与 fast 相遇。此时，fast 指针已经走完了环的 n 圈，
     * 因此它走过的总距离为 a+n(b+c)+b=a+(n+1)b+nc。
     *
     * 根据题意，任意时刻，fast 指针走过的距离都为 slow 指针的 2 倍。因此，我们有
     *          a+(n+1)b+nc=2(a+b) → a=c+(n−1)(b+c)
     *
     * 有了 a=c+(n−1)(b+c) 的等量关系，我们会发现：从相遇点到入环点的距离加上 n−1 圈的环长，
     * 恰好等于从链表头部到入环点的距离。
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
