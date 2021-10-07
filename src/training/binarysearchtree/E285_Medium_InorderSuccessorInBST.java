package training.binarysearchtree;

import org.junit.jupiter.api.Test;
import training.binarytree.TreeNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static training.binarytree.TreeNode.newTree;

/**
 * 285. 二叉搜索树中的中序后继: https://leetcode-cn.com/problems/inorder-successor-in-bst/
 *
 * 给定一棵二叉搜索树和其中的一个节点 p ，找到该节点在树中的中序后继。如果节点没有中序后继，请返回 null 。
 * 节点 p 的后继是值比 p.val 大的节点中键值最小的节点。
 *
 * 例 1：
 * 输入：root = [2,1,3], p = 1
 * 输出：2
 * 解释：这里 1 的中序后继是 2。请注意 p 和返回值都应是 TreeNode 类型。
 *     2
 *   /  \
 *  1   3
 *
 * 例 2：
 * 输入：root = [5,3,6,2,4,null,null,1], p = 6
 * 输出：null
 * 解释：因为给出的节点没有中序后继，所以答案就返回 null 了。
 *       5
 *      / \
 *     3  6
 *    / \
 *   2  4
 *  /
 * 1
 *
 * 约束：
 * - 树中节点的数目在范围 [1, 10^4] 内。
 * - -10^5 <= Node.val <= 10^5
 * - 树中各节点的值均保证唯一。
 */
public class E285_Medium_InorderSuccessorInBST {

    public static void test(BinaryOperator<TreeNode> method) {
        assertEquals(2, method.apply(newTree(2, 1, 3), new TreeNode(1)).val);
        assertNull(method.apply(newTree(5, 3, 6, 2, 4, null, null, 1), new TreeNode(6)));
        assertEquals(2, method.apply(newTree(5, 3, 6, 2, 4, null, null, 1), new TreeNode(1)).val);
    }

    /**
     * LeetCode 耗时：4 ms - 24.80%
     *          内存消耗：39.9 MB - 5.04%
     */
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        boolean find = false;
        TreeNode result = null;
        Deque<TreeNode> stack = new LinkedList<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (root.val == p.val) {
                find = true;
            } else if (find) {
                result = root;
                break;
            }
            root = root.right;
        }

        return result;
    }

    @Test
    public void testInorderSuccessor() {
        test(this::inorderSuccessor);
    }


    private boolean find;
    private TreeNode result;

    /**
     * LeetCode 耗时：3 ms - 69.15%
     *          内存消耗：39.3 MB - 23.79%
     */
    public TreeNode recurMethod(TreeNode root, TreeNode p) {
        find = false;
        result = null;
        find(root, p);

        return result;
    }

    private void find(TreeNode root, TreeNode p) {
        if (root == null || result != null) {
            return;
        }

        find(root.left, p);
        if (root.val == p.val) {
            find = true;
        } else if (find && result == null) {
            result = root;
            return;
        }
        find(root.right, p);
    }

    @Test
    public void testRecurMethod() {
        test(this::recurMethod);
    }


    /**
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：39.2 MB - 42.14%
     */
    public TreeNode betterMethod(TreeNode root, TreeNode p) {
        // pre 表示父节点
        TreeNode pre = null;
        while (root.val != p.val) {
            if (p.val < root.val) {
                pre = root;
                root = root.left;
            } else {
                root = root.right;
            }
        }

        // 右节点不存在，则后继节点就是父节点
        if (root.right == null) {
            return pre;
        }

        // 否则是右子树的最小节点
        root = root.right;
        while (root.left != null) {
            root = root.left;
        }

        return root;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
