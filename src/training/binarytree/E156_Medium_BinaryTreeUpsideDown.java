package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.binarytree.TreeNode.newTree;

/**
 * 156. 上下翻转二叉树：https://leetcode-cn.com/problems/binary-tree-upside-down/
 *
 * 给你一个二叉树的根节点 root ，请你将此二叉树上下翻转，并返回新的根节点。
 *
 * 你可以按下面的步骤翻转一棵二叉树：
 * 1. 原来的左子节点变成新的根节点
 * 2. 原来的根节点变成新的右子节点
 * 3. 原来的右子节点变成新的左子节点
 *
 * 上面的步骤逐层进行。题目数据保证每个右节点都有一个同级节点（即共享同一父节点的左节点）且不存在子节点。
 *
 * 例 1：
 * 输入：root = [1,2,3,4,5]
 * 输出：[4,5,2,null,null,3,1]
 *
 * 例 2：
 * 输入：root = []
 * 输出：[]
 *
 * 例 3：
 * 输入：root = [1]
 * 输出：[1]
 *
 * 说明：
 * - 树中节点数目在范围 [0, 10] 内
 * - 1 <= Node.val <= 10
 * - 树中的每个右节点都有一个同级节点（即共享同一父节点的左节点）
 * - 树中的每个右节点都没有子节点
 */
public class E156_Medium_BinaryTreeUpsideDown {

    public static void test(UnaryOperator<TreeNode> method) {
        assertTrue(TreeNode.equals(newTree(4, 5, 2, null, null, 3, 1), method.apply(newTree(1, 2, 3, 4, 5))));
        assertNull(method.apply(null));
        assertTrue(TreeNode.equals(newTree(1), method.apply(newTree(1))));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：36.3 MB - 11.23%
     */
    public TreeNode upsideDownBinaryTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode left = root.left, right = root.right;
        if (left != null) {
            root.left = root.right = null;
        }
        while (left != null) {
            TreeNode nextLeft = left.left, nextRight = left.right;
            left.left = right;
            left.right = root;

            root = left;
            left = nextLeft;
            right = nextRight;
        }

        return root;
    }

    @Test
    public void testUpsideDownBinaryTree() {
        test(this::upsideDownBinaryTree);
    }
}
