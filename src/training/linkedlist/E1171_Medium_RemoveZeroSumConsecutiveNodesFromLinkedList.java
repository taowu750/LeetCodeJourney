package training.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static training.linkedlist.ListNode.newList;
import static util.datastructure.SingleLinkedListNode.listEqual;

/**
 * 1171. 从链表中删去总和值为零的连续节点: https://leetcode-cn.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/
 *
 * 给你一个链表的头节点 head，请你编写代码，反复删去链表中由「总和值为 0」的连续节点组成的序列，直到不存在这样的序列为止。
 * 删除完毕后，请你返回最终结果链表的头节点。
 *
 * 你可以返回任何满足题目要求的答案。
 *
 * 例 1：
 * 输入：head = [1,2,-3,3,1]
 * 输出：[3,1]
 * 提示：答案 [1,2,1] 也是正确的。
 *
 * 例 2：
 * 输入：head = [1,2,3,-3,4]
 * 输出：[1,2,4]
 *
 * 例 3：
 * 输入：head = [1,2,3,-3,-2]
 * 输出：[1]
 *
 * 说明：
 * - 给你的链表中可能有 1 到 1000 个节点。
 * - 对于链表中的每个节点，节点的值：-1000 <= node.val <= 1000.
 */
public class E1171_Medium_RemoveZeroSumConsecutiveNodesFromLinkedList {

    public static void test(UnaryOperator<ListNode> method) {
        listEqual(method.apply(newList(-1, 1,2,-3,3,1)), 3, 1);
        listEqual(method.apply(newList(-1, 1,2,3,-3,4)), 1, 2, 4);
        listEqual(method.apply(newList(-1, 1,2,3,-3,-2)), 1);
        assertNull(method.apply(newList(-1, 2, 3, -2, -3)));
    }

    /**
     * 前缀和算法。
     *
     * LeetCode 耗时：4 ms - 14.35%
     *          内存消耗：38 MB - 70.37%
     */
    public ListNode removeZeroSumSublists(ListNode head) {
        ListNode newHead = new ListNode(), p = head;
        newHead.next = head;
        Map<Integer, ListNode> prefix2node = new HashMap<>();
        Map<ListNode, Integer> node2prefix = new HashMap<>();
        prefix2node.put(0, newHead);
        node2prefix.put(newHead, 0);

        int sum = 0;
        while (p != null) {
            sum += p.val;
            if (prefix2node.containsKey(sum)) {
                ListNode start = prefix2node.get(sum), s = start.next;
                while (s != p) {
                    prefix2node.remove(node2prefix.remove(s));
                    s = s.next;
                }
                start.next = p.next;
            } else {
                prefix2node.put(sum, p);
                node2prefix.put(p, sum);
            }
            p = p.next;
        }

        return newHead.next;
    }

    @Test
    public void testRemoveZeroSumSublists() {
        test(this::removeZeroSumSublists);
    }


    /**
     * 优化了上面的算法，只需要两次遍历即可。
     *
     * LeetCode 耗时：2 ms - 90.51%
     *          内存消耗：38.1 MB - 57.64%
     */
    public ListNode betterHashMethod(ListNode head) {
        ListNode newHead = new ListNode(0);
        newHead.next = head;

        // 首次遍历建立前缀和和节点的对应关系
        // 若同一和出现多次会覆盖，即记录该sum出现的最后一次节点
        Map<Integer, ListNode> prefix2node = new HashMap<>();
        int sum = 0;
        for (ListNode p = newHead; p != null; p = p.next) {
            sum += p.val;
            prefix2node.put(sum, p);
        }

        // 第二遍遍历 若当前节点处sum在下一处出现了则表明两结点之间所有节点和为0 直接删除区间所有节点
        sum = 0;
        for (ListNode p = newHead; p != null; p = p.next) {
            sum += p.val;
            p.next = prefix2node.get(sum).next;
        }

        return newHead.next;
    }

    @Test
    public void testBetterHashMethod() {
        test(this::betterHashMethod);
    }
}
