package learn.binarysearchtree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 给定一个二叉搜索树（BST）的根和一个整数 val。在 BST 中找到节点值等于 val 的节点，
 * 并返回以该节点为根的子树。如果这样的节点不存在，则返回 null。
 *
 * 例 1：
 * Input: root = [4,2,7,1,3], val = 2
 * Output: [2,1,3]
 * Explanation:
 *       4
 *     /  \
 *    2   7
 *  /  \
 * 1    3
 *
 * 例 2：
 * Input: root = [4,2,7,1,3], val = 5
 * Output: []
 *
 * 约束：
 * - 结点数量范围为 [1, 5000]
 * - 1 <= Node.val <= 10**7
 * - 1 <= val <= 10**7
 */
public class SearchInABinarySearchTree {

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public TreeNode searchBST(TreeNode root, int val) {
        while (root != null) {
            if (root.val == val)
                return root;
            else if (root.val < val)
                root = root.right;
            else
                root = root.left;
        }

        return null;
    }

    @Test
    public void testSearchBST() {
        TreeNode root = newTree(4,2,7,1,3);
        assertEquals(searchBST(root, 2).val, 2);

        assertNull(searchBST(root, 5));
    }
}
