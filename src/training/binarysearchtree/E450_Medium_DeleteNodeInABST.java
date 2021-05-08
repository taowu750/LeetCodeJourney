package training.binarysearchtree;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.datastructure.BinaryTreeNode.contentEquals;
import static util.datastructure.BinaryTreeNode.isValid;

/**
 * 给定 BST 的根节点引用和 key，删除 BST 中具有给定 key 的节点。返回 BST 的根节点引用（可能已更新）。
 * 你能给出时间复杂度为 O(height of tree) 的实现吗？
 *
 * 例 1：
 * Input: root = [5,3,6,2,4,null,7], key = 3
 * Output: [5,4,6,2,null,null,7]
 * Explanation:
 *        5              5
 *      /  \           /  \
 *    3     6   =>    4    6
 *  /  \     \      /       \
 * 2   4     7     2        7
 *
 * 例 2：
 * Input: root = [5,3,6,2,4,null,7], key = 0
 * Output: [5,3,6,2,4,null,7]
 * Explanation:
 * 这棵树不包含值为 0 的结点
 *
 * 例 3：
 * Input: root = [], key = 0
 * Output: []
 *
 * 约束：
 * - 结点数量范围为 [1, 10**4]
 * - -10**5 <= Node.val <= 10**5
 * - 所有结点值是唯一的
 * - -10**5 <= key <= 10**5
 */
public class E450_Medium_DeleteNodeInABST {

    static void test(BiFunction<TreeNode, Integer, TreeNode> method) {
        TreeNode root = method.apply(newTree(5,3,6,2,4,null,7), 3);
        assertTrue(isValid(root));
        assertTrue(contentEquals(root, newTree(5,4,6,2,null,null,7)));

        root = method.apply(newTree(5,3,6,2,4,null,7), 0);
        assertTrue(isValid(root));
        assertTrue(contentEquals(root, newTree(5,3,6,2,4,null,7)));

        assertNull(method.apply(null, 0));

        assertNull(method.apply(newTree(1), 1));
    }

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        // 查找结点
        TreeNode p = root, parent = root;
        while (p != null) {
            if (p.val == key)
                break;
            else if (p.val < key) {
                parent = p;
                p = p.right;
            } else {
                parent = p;
                p = p.left;
            }
        }

        // 如果找到了
        if (p != null) {
            TreeNode replacement;
            // 查找用来替换的结点
            if (p.left == null)
                replacement = p.right;
            else if (p.right == null)
                replacement = p.left;
            else {
                TreeNode rightMin = p.right, rightMinParent = p.right;
                while (rightMin.left != null) {
                    rightMinParent = rightMin;
                    rightMin = rightMin.left;
                }
                rightMin.left = p.left;
                if (rightMin != rightMinParent) {
                    rightMinParent.left = rightMin.right;
                    rightMin.right = p.right;
                }
                replacement = rightMin;
            }

            // 进行替换
            if (p == parent)
                return replacement;
            else if (p == parent.left)
                parent.left = replacement;
            else
                parent.right = replacement;
        }

        return root;
    }

    @Test
    public void testDeleteNode() {
        test(this::deleteNode);
    }
}
