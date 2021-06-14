package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import util.datastructure.BinaryTreeNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 426. 将二叉搜索树转化为排序的双向链表: https://leetcode-cn.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
 *
 * 将一个「二叉搜索树」就地转化为一个「已排序的双向循环链表」。
 *
 * 对于双向循环列表，你可以将左右孩子指针作为双向循环链表的前驱和后继指针，第一个节点的前驱是最后一个节点，
 * 最后一个节点的后继是第一个节点。
 *
 * 特别地，我们希望可以「就地」完成转换操作。当转化完成以后，树中节点的左指针需要指向前驱，树中节点的右指针需要指向后继。
 * 还需要返回链表中最小元素的指针。
 *
 * 例 1：
 * 输入：root = [4,2,5,1,3]
 * 输出：[1,2,3,4,5]
 *
 * 例 2：
 * 输入：root = [2,1,3]
 * 输出：[1,2,3]
 *
 * 例 3：
 * 输入：root = []
 * 输出：[]
 * 解释：输入是空树，所以输出也是空链表。
 *
 * 例 4：
 * 输入：root = [1]
 * 输出：[1]
 *
 * 约束：
 * - -1000 <= Node.val <= 1000
 * - Node.left.val < Node.val < Node.right.val
 * - Node.val 的所有值都是独一无二的
 * - 0 <= Number of Nodes <= 2000
 */
public class E426_Medium_ConvertBinarySearchTreeToSortedDoublyLinkedList {

    static void assertList(Node head) {
        if (head == null) {
            return;
        }

        Node p = head;
        while (p.right != head) {
            if (p.right == null) {
                throw new AssertionError(p.val + " right is null");
            }
            if (p.right.left != p) {
                throw new AssertionError(p.val + " link error");
            }
            if (p.val >= p.right.val) {
                throw new AssertionError(p.val + " >= " + p.right.val);
            }
            p = p.right;
        }
        assertSame(head.left, p);
    }

    static void test(UnaryOperator<Node> method) {
        assertList(method.apply(Node.newTree(4,2,5,1,3)));
        assertList(method.apply(Node.newTree(2,1,3)));
        assertList(method.apply(null));
        assertList(method.apply(Node.newTree(1)));
    }

    static class Node extends BinaryTreeNode<Node> {

        public Node(int val) {
            super(val);
        }

        public static Node newTree(Integer...vals) {
            return newTree(Node.class, vals);
        }
    }

    /**
     * 此题方法类似于 {@link Review_E98_Medium_ValidateBinarySearchTree}。
     *
     * LeetCode 耗时：1 ms - 19.16%
     *          内存消耗：37.8 MB - 41.92%
     */
    public Node treeToDoublyList(Node root) {
        Deque<Node> stack = new LinkedList<>();
        Node pre = null, head = null;
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (pre != null) {
                pre.right = root;
                root.left = pre;
            } else {
                head = root;
            }
            pre = root;
            root = root.right;
        }
        if (head != null) {
            head.left = pre;
            pre.right = head;
        }

        return head;
    }

    @Test
    public void testTreeToDoublyList() {
        test(this::treeToDoublyList);
    }


    private Node pre, head;

    /**
     * 递归方法。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.6 MB - 87.43%
     */
    public Node recurMethod(Node root) {
        pre = head = null;
        recur(root);
        if (head != null) {
            head.left = pre;
            pre.right = head;
        }

        return head;
    }

    private void recur(Node root) {
        if (root != null) {
            recur(root.left);
            if (pre != null) {
                pre.right = root;
                root.left = pre;
            } else {
                head = root;
            }
            pre = root;
            recur(root.right);
        }
    }

    @Test
    public void testRecurMethod() {
        test(this::recurMethod);
    }
}
