package training.linkedlist;

import learn.linkedlist.ListNode;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static learn.linkedlist.ListNode.newList;
import static org.junit.jupiter.api.Assertions.assertNull;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 148. 排序链表: https://leetcode-cn.com/problems/sort-list/
 *
 * 给你链表的头结点 head ，请将其按「升序」排列并返回排序后的链表。
 * 你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？
 *
 * 例 1：
 * 输入：head = [4,2,1,3]
 * 输出：[1,2,3,4]
 *
 * 例 2：
 * 输入：head = [-1,5,3,4,0]
 * 输出：[-1,0,3,4,5]
 *
 * 例 3：
 * 输入：head = []
 * 输出：[]
 *
 * 约束：
 * - 链表中节点的数目在范围 [0, 5 * 10**4] 内
 * - -10**5 <= Node.val <= 10**5
 */
public class E148_Medium_SortList {

    static void test(UnaryOperator<ListNode> method) {
        listEqual(method.apply(newList(-1, 4,2,1,3)), 1,2,3,4);
        listEqual(method.apply(newList(-1, -1,5,3,4,0)), -1,0,3,4,5);
        assertNull(method.apply(null));
        listEqual(method.apply(newList(-1, 100,-1,3,4,6,5,3,2,18,1,15)),
                -1,1,2,3,3,4,5,6,15,18,100);
    }

    /**
     * LeetCode 耗时：9 ms - 40.90%
     *          内存消耗：42.9 MB - 97.55%
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        // 计算链表长度
        int len = 0;
        ListNode n = head;
        while (n != null) {
            n = n.next;
            len++;
        }

        /*
        自底向上的归并算法。

        每一轮依次归并 left、right 两块 mergeSize 大小的归并块（最后一块 right 可能不满 mergeSize）。
        为了避免移动指针，让 nextLeft 和 nextRight 记录下一次归并块的左右起始位置，nextEnd 记录下一次归并块的结束位置。
        next* 指针在 left、right 归并过程中移动，通过这个巧妙的办法就可以达到 NlogN 的时间复杂度。

        mergeSize 每一轮完全归并结束后就倍增，直到超过链表长度。
         */
        ListNode left = head, right = head.next,
                nextLeft = head.next.next, nextRight = nextLeft, nextEnd = nextLeft;
        ListNode newList = new ListNode(), p = newList;
        for (int mergeSize = 1; mergeSize < len;) {
            // nextEpochRight 和 nextEpochNextLeft 分别记录下一轮归并的 right 和 nextLeft。
            // 它们可以在下面的第一次归并过程中取得。
//            ListNode nextEpochRight = null, nextEpochNextLeft = null;
            do {
                for (int i = 0, j = 0, k = 0; i < mergeSize || (j < mergeSize && right != null);) {
                    if (i < mergeSize && (j == mergeSize || right == null || left.val <= right.val)) {
                        p.next = left;
                        left = left.next;
                        i++;
                    } else {
                        p.next = right;
                        right = right.next;
                        j++;
                    }
                    p = p.next;
                    if (k < mergeSize && nextRight != null)
                        nextRight = nextRight.next;
                    k++;
                    if (nextEnd != null)
                        nextEnd = nextEnd.next;
                }
                p.next = nextLeft;
                left = nextLeft;
                right = nextRight;
                nextLeft = nextRight = nextEnd;
            } while (right != null);

            p = newList;
            left = right = nextLeft = newList.next;
            mergeSize *= 2;
            // 查找下一轮的 right、nextLeft
            for (int i = 0; i < mergeSize && right != null; i++) {
                right = right.next;
                if (nextLeft != null) {
                    nextLeft = nextLeft.next;
                    if (nextLeft != null)
                        nextLeft = nextLeft.next;
                }
            }
            nextRight = nextEnd = nextLeft;
        }

        return newList.next;
    }

    @Test
    public void testSortList() {
        test(this::sortList);
    }
}
