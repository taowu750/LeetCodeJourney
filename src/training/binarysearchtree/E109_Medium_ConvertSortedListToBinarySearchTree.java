package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import training.binarytree.TreeNode;
import training.linkedlist.ListNode;

import java.util.function.Function;

import static training.linkedlist.ListNode.newList;
import static util.datastructure.BinaryTreeNode.assertBST;

/**
 * 109. 有序链表转换二叉搜索树: https://leetcode-cn.com/problems/convert-sorted-list-to-binary-search-tree/
 *
 * 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
 *
 * 本题中，一个高度平衡二叉树是指一个二叉树每个节点的左右两个子树的高度差的绝对值不超过 1。
 *
 * 例 1：
 * 给定的有序链表： [-10, -3, 0, 5, 9],
 * 一个可能的答案是：[0, -3, 9, -10, null, 5], 它可以表示下面这个高度平衡二叉搜索树：
 *       0
 *      / \
 *    -3   9
 *    /   /
 *  -10  5
 */
public class E109_Medium_ConvertSortedListToBinarySearchTree {

    public static void test(Function<ListNode, TreeNode> method) {
        assertBST(method.apply(newList(-1, -10, -3, 0, 5, 9)));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：39.9 MB - 7.54%
     */
    public TreeNode sortedListToBST(ListNode head) {
        return sort(head, null);
    }

    private TreeNode sort(ListNode head, ListNode tail) {
        if (head == tail) {
            return null;
        }

        ListNode mid = findMid(head, tail);
        return new TreeNode(mid.val, sort(head, mid), sort(mid.next, tail));
    }

    private ListNode findMid(ListNode head, ListNode tail) {
        // 找到链表中点
        ListNode slow = head, fast = head;
        while (fast != tail && fast.next != tail) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    @Test
    public void testSortedListToBST() {
        test(this::sortedListToBST);
    }
}
