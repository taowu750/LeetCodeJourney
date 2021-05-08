package training.binarysearchtree;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.BinaryTreeNode.preorder;

/**
 * 538. 把二叉搜索树转换为累加树: https://leetcode-cn.com/problems/convert-bst-to-greater-tree/
 *
 * 给出二叉搜索树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），
 * 使每个节点 node 的新值等于原树中大于或等于 node.val 的值之和。
 *
 * 例 1：
 * 输入：[4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
 * 输出：[30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]
 * 解释：图片见链接
 *
 * 例 2：
 * 输入：root = [0,null,1]
 * 输出：[1,null,1]
 *
 * 例 3：
 * 输入：root = [1,0,2]
 * 输出：[3,3,2]
 *
 * 例 4：
 * 输入：root = [3,2,4,1]
 * 输出：[7,9,4,10]
 *
 * 约束：
 * - 树中的节点数介于 0 和 10**4 之间。
 * - 每个节点的值介于 -10**4 和 10**4 之间。
 * - 树中的所有值互不相同。
 * - 给定的树为二叉搜索树。
 */
public class E538_Medium_ConvertBSTToGreaterTree {

    static void test(UnaryOperator<TreeNode> method) {
        TreeNode result = method.apply(newTree(4,1,6,0,2,5,7,null,null,null,3,null,null,null,8));
        System.out.println(preorder(result));
        assertTrue(TreeNode.equals(newTree(30,36,21,36,35,26,15,null,null,null,33,null,null,null,8),
                result));

        assertTrue(TreeNode.equals(newTree(1,null,1),
                method.apply(newTree(0,null,1))));

        assertTrue(TreeNode.equals(newTree(3,3,2),
                method.apply(newTree(1,0,2))));

        assertTrue(TreeNode.equals(newTree(7,9,4,10),
                method.apply(newTree(3,2,4,1))));

        assertNull(method.apply(null));
    }

    private TreeNode next;

    /**
     * LeetCode 耗时：1 ms - 85.65%
     *          内存消耗：38.6 MB - 82.75%
     */
    public TreeNode convertBST(TreeNode root) {
        next = null;
        return recur(root);
    }

    /**
     * 定义每个节点的 next 节点为它的右子树的最小子节点。通过 next 节点，可以将树排成自然顺序，
     * 这样就可以计算累加和。
     */
    private TreeNode recur(TreeNode root) {
        if (root != null) {
            // 右->中->左，递归返回时，next 链接被建立
            recur(root.right);
            if (next != null)
                root.val += next.val;
            next = root;
            recur(root.left);
        }

        return root;
    }

    @Test
    void testConvertBST() {
        test(this::convertBST);
    }
}
