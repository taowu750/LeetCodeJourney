package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static training.linkedlist.ListNode.newList;
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

    public static void test(UnaryOperator<ListNode> method) {
        listEqual(method.apply(newList(-1, 4, 2, 1, 3)), 1, 2, 3, 4);
        listEqual(method.apply(newList(-1, -1, 5, 3, 4, 0)), -1, 0, 3, 4, 5);
        assertNull(method.apply(null));
        listEqual(method.apply(newList(-1, 100, -1, 3, 4, 6, 5, 3, 2, 18, 1, 15)),
                -1, 1, 2, 3, 3, 4, 5, 6, 15, 18, 100);
    }

    /**
     * LeetCode 耗时：17 ms - 18.02%
     *          内存消耗：49.2 MB - 77.95%
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
        for (int mergeSize = 1; mergeSize < len; mergeSize *= 2) {
            // nextEpochRight 和 nextEpochNextLeft 分别记录下一轮归并的 right 和 nextLeft。
            ListNode nextEpochRight = null, nextEpochNextLeft = null;
            int mergeCount = 0;
            do {
                mergeCount++;
                // 归并相邻两块。每次将节点放到 p 的末尾
                for (int i = 0, j = 0, k = 0; i < mergeSize || (j < mergeSize && right != null); ) {
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
                    if (k == 0) {
                        // 在第二、三次归并开始时更新 nextEpoch 指针
                        if (mergeCount == 2) {
                            nextEpochRight = p;
                        } else if (mergeCount == 3) {
                            nextEpochNextLeft = p;
                        }
                    }

                    // 移动 nextRight，找到下次归并中右边块的开始位置
                    if (k < mergeSize && nextRight != null)
                        nextRight = nextRight.next;
                    // 这里的 k++ 不要写在上面的 if 判断里面，会导致上面的 if(k==0) 出错
                    k++;
                    // 移动 nextEnd，找到下次归并的结束位置+1，也就是下下次归并的开始位置
                    if (nextEnd != null)
                        nextEnd = nextEnd.next;
                }
                p.next = nextLeft;
                left = nextLeft;
                right = nextRight;
                // 第一次归并结束后记录下一轮归并的 right 和 nextLeft。
                // 但需要注意的是，这里的记录只是防止下一轮归并块数仅有 2 的情况，
                // 因为第二次归并过程中会改变节点顺序，所以还需要在第二、三次归并到第一个节点的时候更新这两个指针的值。
                if (mergeCount == 1) {
                    nextEpochRight = nextLeft;
                    nextEpochNextLeft = nextEnd;
                }
                nextLeft = nextRight = nextEnd;

                // 当下次归并的右边一块开头是 null 时，本轮归并结束
            } while (right != null);

            p = newList;
            left = newList.next;
            right = nextEpochRight;
            nextLeft = nextRight = nextEnd = nextEpochNextLeft;
        }

        return newList.next;
    }

    @Test
    public void testSortList() {
        test(this::sortList);
    }


    /**
     * 递归式的归并排序。
     *
     * LeetCode 耗时：12 ms - 45.69%
     *          内存消耗：49.6 MB - 55.70%
     */
    public ListNode recursiveMethod(ListNode head) {
        return recursiveMethod(head, null);
    }

    private ListNode recursiveMethod(ListNode head, ListNode tail) {
        if (head == null) {
            return null;
        }
        /*
        这一步很关键，当只有一个节点时，会将节点的 next 指针置为 null。
        这样一来，每个节点的 next 指针最后都会变成 null，从而将链表所有节点拆分，简化了归并步骤
         */
        if (head.next == tail) {
            head.next = null;
            return head;
        }

        // 用快慢针将链表分为两半
        ListNode slow = head, fast = head;
        while (fast != tail && fast.next != tail) {
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode left = recursiveMethod(head, slow);
        ListNode right = recursiveMethod(slow, tail);

        return merge(left, right);
    }

    private ListNode empty = new ListNode();

    private ListNode merge(ListNode left, ListNode right) {
        ListNode p = empty, l = left, r = right;
        // 因为之前每个节点都被分割了，所以这里直接用 null
        while (l != null && r != null) {
            if (l.val <= r.val) {
                p.next = l;
                l = l.next;
            } else {
                p.next = r;
                r = r.next;
            }
            p = p.next;
        }
        if (l != null) {
            p.next = l;
        }
        if (r != null) {
            p.next = r;
        }

        return empty.next;
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }


    /**
     * 更好的自底向上：https://leetcode.cn/problems/sort-list/solution/pai-xu-lian-biao-by-leetcode-solution/
     *
     * LeetCode 耗时：15 ms - 30.99%
     *          内存消耗：49.2 MB - 79%
     */
    public ListNode betterDownToUp(ListNode head) {
        if (head == null) {
            return null;
        }

        // 1. 首先从头向后遍历,统计链表长度
        int length = 0; // 用于统计链表长度
        ListNode node = head;
        while (node != null) {
            length++;
            node = node.next;
        }

        // 2. 初始化 引入dummyNode
        ListNode dummyHead = new ListNode(0);
        dummyHead.next = head;

        // 3. 每次将链表拆分成若干个长度为subLen的子链表 , 并按照每两个子链表一组进行合并
        for (int subLen = 1; subLen < length; subLen <<= 1) {
            ListNode prev = dummyHead;
            ListNode curr = dummyHead.next;     // curr用于记录拆分链表的位置

            while (curr != null) {               // 如果链表没有被拆完
                // 3.1 拆分subLen长度的链表1
                ListNode head_1 = curr;        // 第一个链表的头 即 curr初始的位置
                for (int i = 1; i < subLen && curr.next != null; i++) {     // 拆分出长度为subLen的链表1
                    curr = curr.next;
                }

                // 3.2 拆分subLen长度的链表2
                ListNode head_2 = curr.next;  // 第二个链表的头  即 链表1尾部的下一个位置
                curr.next = null;             // 断开第一个链表和第二个链表的链接
                curr = head_2;                // 第二个链表头 重新赋值给curr
                for (int i = 1; i < subLen && curr != null && curr.next != null; i++) {      // 再拆分出长度为subLen的链表2
                    curr = curr.next;
                }

                // 3.3 再次断开 第二个链表最后的next的链接
                ListNode next = null;
                if (curr != null) {
                    next = curr.next;   // next用于记录 拆分完两个链表的结束位置
                    curr.next = null;   // 断开连接
                }

                // 3.4 合并两个subLen长度的有序链表
                prev.next = merge(head_1, head_2);        // prev.next 指向排好序链表的头
                while (prev.next != null) {  // while循环 将prev移动到 subLen*2 的位置后去
                    prev = prev.next;
                }
                curr = next;              // next用于记录 拆分完两个链表的结束位置
            }
        }
        // 返回新排好序的链表
        return dummyHead.next;
    }

    @Test
    public void testBetterDownToUp() {
        test(this::betterDownToUp);
    }
}
