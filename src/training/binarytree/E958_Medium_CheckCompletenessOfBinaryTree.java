package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.binarytree.TreeNode.newTree;

/**
 * 958. 二叉树的完全性检验: https://leetcode-cn.com/problems/check-completeness-of-a-binary-tree/
 *
 * 给定一个二叉树，确定它是否是一个完全二叉树。
 *
 * 例 1：
 * 输入：[1,2,3,4,5,6]
 * 输出：true
 * 解释：最后一层前的每一层都是满的（即，结点值为 {1} 和 {2,3} 的两层），且最后一层中的所有结点（{4,5,6}）都尽可能地向左。
 *        1
 *      /  \
 *     2    3
 *    / \  /
 *   4  5 6
 *
 * 例 2：
 * 输入：[1,2,3,4,5,null,7]
 * 输出：false
 * 解释：值为 7 的结点没有尽可能靠向左侧。
 *        1
 *      /  \
 *     2    3
 *    / \    \
 *   4  5    7
 *
 * 说明：
 * - 树中将会有 1 到 100 个结点。
 */
public class E958_Medium_CheckCompletenessOfBinaryTree {

    public static void test(Predicate<TreeNode> method) {
        assertTrue(method.test(newTree(1,2,3,4,5,6)));
        assertFalse(method.test(newTree(1,2,3,4,5,null,7)));
        /*
                1
               / \
              2   3
             /   / \
            5   7   8
         */
        assertFalse(method.test(newTree(1,2,3,5,null,7,8)));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.9 MB - 12.09%
     */
    public boolean isCompleteTree(TreeNode root) {
        if (root == null) {
            return true;
        }

        int ld = depth(root.left), rd = depth(root.right);
        if (ld - rd < 0 || ld - rd > 1) {
            return false;
        }

        if (ld == rd) {
            return checkAll(root.left) && isCompleteTree(root.right);
        } else {
            return isCompleteTree(root.left) && checkAll(root.right);
        }
    }

    /**
     * 测试 root 是否是满二叉树
     */
    private boolean checkAll(TreeNode root) {
        if (root == null) {
            return true;
        }

        int ld = depth(root.left), rd = depth(root.right);
        if (ld != rd) {
            return false;
        }

        return checkAll(root.left) && checkAll(root.right);
    }

    private int depth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        return 1 + depth(root.left);
    }

    @Test
    public void testIsCompleteTree() {
        test(this::isCompleteTree);
    }


    /**
     * 对于任意一个节点 v，它的左孩子为 2*v 右孩子为 2*v + 1。一颗二叉树是完全二叉树当且仅当节点编号依次为
     * 1, 2, 3, ... 且没有间隙。
     *
     * 完全二叉树最后一个节点编号应该和它的节点数量相同。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.9 MB - 12.09%
     */
    public boolean serialNumberMethod(TreeNode root) {
        size = maxNumber = 0;
        serialNumberMethod(root, 1);

        return size == maxNumber;
    }

    private long size, maxNumber;

    private void serialNumberMethod(TreeNode root, int number) {
        if (root == null) {
            return;
        }

        size++;
        maxNumber = Math.max(maxNumber, number);
        serialNumberMethod(root.left, number * 2);
        serialNumberMethod(root.right, number * 2 + 1);
    }

    @Test
    public void testSerialNumberMethod() {
        test(this::serialNumberMethod);
    }
}
