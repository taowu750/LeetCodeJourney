package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 107. 二叉树的层序遍历 II: https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii/
 *
 * 给定一个二叉树，返回其节点值自底向上的层序遍历。（即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
 *
 * 例 1：
 * 给定二叉树 [3,9,20,null,null,15,7],
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回其自底向上的层序遍历为：
 * [
 *   [15,7],
 *   [9,20],
 *   [3]
 * ]
 *
 */
public class E107_Medium_BinaryTreeLevelOrderTraversalII {

    public static void test(Function<TreeNode, List<List<Integer>>> method) {
        assertEquals(asList(asList(15,7), asList(9, 20), asList(3)),
                method.apply(newTree(3,9,20,null,null,15,7)));
    }

    /**
     * LeetCode 耗时：1 ms - 94.51%
     *          内存消耗：38.7 MB - 36.66%
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        if (root == null) {
            return Collections.emptyList();
        }

        LinkedList<List<Integer>> result = new LinkedList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.remove();
                list.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            result.addFirst(list);
        }

        return result;
    }

    @Test
    public void testLevelOrderBottom() {
        test(this::levelOrderBottom);
    }
}
