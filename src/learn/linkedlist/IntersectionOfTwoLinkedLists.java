package learn.linkedlist;

import learn.linkedlist.LinkedListCycle.*;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static learn.linkedlist.LinkedListCycle.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 查找两个链表的交点。例如下面两个链表：
 * A：    a1->a2--
 *               |
 *               v
 *               c1->c2->c3
 *               ^
 *               |
 * B：b1->b2->b3--
 * A、B 两个链表的交点是 c1。
 *
 * 例 1：
 * Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5], skipA = 2, skipB = 3
 * Output: 交点值 = 8
 * Explanation: 交点值是 8，listA 交点前有两个结点，listB 交点前有三个结点。
 * A：   4->1--
 *            |
 *            v
 *            8->4->5
 *            ^
 *            |
 * B：5->6->1--
 *
 * 例 2：
 * Input: intersectVal = 2, listA = [1,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
 * Output: 交点值 = 2
 * Explanation:
 * A：1->9->1--
 *            |
 *            v
 *            2->4
 *            ^
 *            |
 * B：      3--
 *
 * 例 3：
 * Input: intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
 * Output: null
 * Explanation:
 * A：2->6->4
 *
 * B：1->5
 *
 * 注意：
 * - 如果两个链表根本没有交集，则返回null
 * - 函数返回后，链表必须保留其原始结构
 * - 整个链接结构中的任何地方都没有循环
 * - 每个链表上的每个值都在范围 [1, 10**9]
 * - 使用 O(n) 的时间和 O(1) 的空间
 */
public class IntersectionOfTwoLinkedLists {

    static void test(BiFunction<ListNode, ListNode, ListNode> method) {
        ListNode listA = newList(-1, 4, 1, 8, 4, 5);
        ListNode intersect = getNodeAt(listA, 2);
        ListNode listB = intersectList(listA, intersect, 5, 6, 1);
        assertEquals(method.apply(listA, listB), intersect);

        listA = newList(-1, 1, 9, 1, 2, 4);
        intersect = getNodeAt(listA, 3);
        listB = intersectList(listA, intersect, 3);
        assertEquals(method.apply(listA, listB), intersect);

        listA = newList(-1, 2, 6, 4);
        listB = newList(-1, 1, 5);
        assertNull(method.apply(listA, listB));
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null)
            return null;

        ListNode pA = headA, pB = headB;
        int lenA = 1, lenB = 1;
        // 将指针 pA 和 pB 分别移动到 listA 和 listB 的末尾，并记录它们的长度
        for(; pA.next != null; pA = pA.next, lenA++);
        for(; pB.next != null; pB = pB.next, lenB++);

        // 如果末尾结点不相等，则两个链表没有交点
        if (pA != pB)
            return null;

        pA = headA;
        pB = headB;
        // 将较长的链表指针移动合适位置，使得 pA 和 pB 从相等长度开始
        if (lenA > lenB)
            for(int i = 0; i < lenA - lenB; pA = pA.next, i++);
        else if (lenA < lenB)
            for(int i = 0; i < lenB - lenA; pB = pB.next, i++);
        // 移动找到交点
        for(; pA != pB; pA = pA.next, pB = pB.next);

        return pA;
    }

    @Test
    public void testGetIntersectionNode() {
        test(this::getIntersectionNode);
    }



    /**
     * 我们将两个链表“连”起来。就是让一个指针走到链表末尾时，再从另一个链表的开头走起。
     * 这就消除了两个链表的长度差异。如果它们有交点，最后会在交点相遇；
     * 否则会移动到链表末尾，都变成 null。
     */
    public ListNode connectList(ListNode headA, ListNode headB) {
        if (headA == null || headB == null)
            return null;

        ListNode pA = headA, pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }

        return pA;
    }

    @Test
    public void testConnectList() {
        test(this::connectList);
    }
}
