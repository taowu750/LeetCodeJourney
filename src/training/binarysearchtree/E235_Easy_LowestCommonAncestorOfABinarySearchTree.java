package training.binarysearchtree;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.datastructure.BinaryTreeNode.find;

/**
 * 给定一个二叉搜索树（BST），在 BST 中找到两个给定节点的最低共同祖先（LCA）。
 *
 * 例 1：
 * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
 * Output: 6
 * Explanation:
 *          6
 *       /   \
 *     2      8
 *   /  \   /  \
 *  0   4  7   9
 *    /  \
 *   3    5
 *
 * 例 2:
 * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
 * Output: 2
 *
 * 例 3：
 * Input: root = [2,1], p = 2, q = 1
 * Output: 2
 *
 * 约束：
 * - 树中结点数量范围为 [2, 10**5]
 * - -10**9 <= Node.val <= 10**9
 * - 所有结点值是唯一的
 * - p != q
 * - p 和 q 都存在于 BST 中
 */
public class E235_Easy_LowestCommonAncestorOfABinarySearchTree {

    public static void test(TriFunction<TreeNode, TreeNode, TreeNode, TreeNode> method) {
        TreeNode root = newTree(6,2,8,0,4,7,9,null,null,3,5);
        assertEquals(method.apply(root, find(root, 2), find(root, 8)).val, 6);

        assertEquals(method.apply(root, find(root, 2), find(root, 4)).val, 2);

        root = newTree(2, 1);
        assertEquals(method.apply(root, find(root, 2), find(root, 1)).val, 2);
    }

    /**
     * LeetCode 耗时：4ms - 56.60%
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root.val == p.val || root.val == q.val)
            return root;

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left == null)
            return right;
        else if (right == null)
            return left;
        else
            return root;
    }

    @Test
    public void testLowestCommonAncestor() {
        test(this::lowestCommonAncestor);
    }


    /**
     * 注意利用二叉搜索树的排序特性。
     *
     * LeetCode 耗时：3ms - 100%
     */
    public TreeNode betterMethod(TreeNode root, TreeNode p, TreeNode q) {
        if (p.val < root.val && q.val < root.val)
            return betterMethod(root.left, p, q);
        if (p.val > root.val && q.val > root.val)
            return betterMethod(root.right, p, q);

        return root;
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
