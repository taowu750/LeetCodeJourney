package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树的根，返回其最大深度。
 * 一棵二叉树的最大深度是沿着从根节点到最远的叶节点的最长路径的节点数。
 *
 * 例 1：
 * Input: root = [3,9,20,null,null,15,7]
 * Output: 3
 * Explanation:
 *   3
 *  / \
 * 9  20
 *   /  \
 *  15  7
 *
 * 例 2：
 * Input: root = [1,null,2]
 * Output: 2
 *
 * 例 3：
 * Input: root = []
 * Output: 0
 *
 * 例 4：
 * Input: root = [0]
 * Output: 1
 *
 * 约束：
 * - 树的结点数量范围为 [0, 10**4]
 * - -100 <= Node.val <= 100
 */
public class E104_Easy_MaximumDepthOfBinaryTree {

    static void test(ToIntFunction<TreeNode> method) {
        TreeNode root = newTree(3, 9, 20, null, null, 15, 7);
        assertEquals(method.applyAsInt(root), 3);

        root = newTree(1, null, 2);
        assertEquals(method.applyAsInt(root), 2);

        assertEquals(method.applyAsInt(null), 0);

        root = new TreeNode(0);
        assertEquals(method.applyAsInt(root), 1);
    }

    public int maxDepth(TreeNode root) {
        if (root == null)
            return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    @Test
    public void testMaxDepth() {
        test(this::maxDepth);
    }
}
