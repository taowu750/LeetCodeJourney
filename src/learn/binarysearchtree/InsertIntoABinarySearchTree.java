package learn.binarysearchtree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.BinaryTreeNode.isValid;

/**
 * 给定二叉搜索树（BST）的根节点和要插入树中的值。插入后返回 BST 的根节点。
 * 保证新值在原始 BST 中不存在。
 *
 * 请注意，可能存在多种有效的插入方法，只要插入后树仍然是 BST，你可以返回任何值。
 *
 * 例 1：
 * Input: root = [4,2,7,1,3], val = 5
 * Output: [4,2,7,1,3,5]
 * Explanation:
 *        4            4
 *      /  \         /   \
 *     2   7  =>    2    7
 *   /  \         /  \  /
 *  1   3        1   3 5
 *  或者：
 *       5
 *     /  \
 *    2   7
 *  /  \
 * 1   3
 *      \
 *      4
 *
 * 例 2：
 * Input: root = [40,20,60,10,30,50,70], val = 25
 * Output: [40,20,60,10,30,50,70,null,null,25]
 *
 * 例 3：
 * Input: root = [4,2,7,1,3,null,null,null,null,null,null], val = 5
 * Output: [4,2,7,1,3,5]
 *
 * 约束：
 * - 结点数量范围为 [1, 10**4]
 * - -10**8 <= Node.val <= 10**8
 * - 所有结点值是唯一的
 */
public class InsertIntoABinarySearchTree {

    static void test(BiFunction<TreeNode, Integer, TreeNode> method) {
        TreeNode tree = method.apply(newTree(4, 2, 7, 1, 3), 5);
        assertTrue(isValid(tree));
        assertTrue(TreeNode.contentEquals(tree, newTree(4, 2, 7, 1, 3, 5)));

        tree = method.apply(newTree(40,20,60,10,30,50,70), 25);
        assertTrue(isValid(tree));
        assertTrue(TreeNode.contentEquals(tree, newTree(40,20,60,10,30,50,70,null,null,25)));

        tree = method.apply(newTree(4,2,7,1,3,null,null,null,null,null,null), 5);
        assertTrue(isValid(tree));
        assertTrue(TreeNode.contentEquals(tree, newTree(4,2,7,1,3,5)));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null)
            return new TreeNode(val);

        TreeNode p = root, parent = root;
        while (p != null) {
            parent = p;
            if (p.val < val)
                p = p.right;
            else
                p = p.left;
        }
        if (parent.val < val)
            parent.right = new TreeNode(val);
        else
            parent.left = new TreeNode(val);

        return root;
    }

    @Test
    public void testInsertIntoBST() {
        test(this::insertIntoBST);
    }
}
