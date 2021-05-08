package training.binarytree;

import org.junit.jupiter.api.Test;
import util.datastructure.BinaryTreeNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.BinaryTreeNode.preorder;

/**
 * 给定二叉树的根，将树展平为“链表”：
 * - “链表”应该使用相同的 TreeNode 类，其中右子指针指向列表中的下一个节点，左子指针总是空的。
 * - “链表”的顺序应与二叉树的先序遍历相同。
 *
 * 你能只使用 O(1) 的额外空间吗？
 *
 * 例 1：
 * Input: root = [1,2,5,3,4,null,6]
 * Output: [1,null,2,null,3,null,4,null,5,null,6]
 *        1          1
 *      /  \          \
 *     2    5   =>     2
 *   /  \    \          \
 * 3    4    6           3
 *                        \
 *                         4
 *                          \
 *                           5
 *                            \
 *                             6
 *
 * 例 2：
 * Input: root = []
 * Output: []
 *
 * 例 3：
 * Input: root = [0]
 * Output: [0]
 *
 * 约束：
 * - 树结点数量范围为 [0, 2000]
 * - -100 <= Node.val <= 100
 */
public class E114_Medium_FlattenBinaryTreeToLinkedList {

    static void test(Consumer<TreeNode> method) {
        TreeNode root = newTree(1,2,5,3,4,null,6);
        method.accept(root);
        System.out.println(preorder(root));
        assertTrue(BinaryTreeNode.equals(root, newTree(1,null,2,null,3,null,4,null,5,null,6)));

        root = new TreeNode(0);
        method.accept(root);
        assertTrue(BinaryTreeNode.equals(root, newTree(0)));

        root = newTree(1,null,2,3);
        method.accept(root);
        assertTrue(BinaryTreeNode.equals(root, newTree(1,null,2,null,3)));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     *          内存占用：38.8MB - 20.68%
     */
    public void flatten(TreeNode root) {
        if (root != null)
            recur(root);
    }

    private TreeNode recur(TreeNode root) {
        if (root.left == null && root.right == null)
            return root;
        TreeNode last = null, right = root.right;
        if (root.left != null) {
            last = recur(root.left);
            last.right = right;
            root.right = root.left;
            root.left = null;
        }
        if (right != null)
            last = recur(right);
        return last;
    }

    @Test
    public void testFlatten() {
        test(this::flatten);
    }


    public void iterateMethod(TreeNode root) {
        Deque<TreeNode> stack = new LinkedList<>();
        while (root != null) {
            while (root.left != null || root.right != null) {
                if (root.right != null)
                    stack.push(root.right);
                root.right = root.left;
                root.left = null;
                if (root.right == null)
                    break;
                root = root.right;
            }
            if (stack.isEmpty())
                break;
            root.right = stack.pop();
            root = root.right;
        }
    }

    @Test
    public void testIterateMethod() {
        test(this::iterateMethod);
    }
}
