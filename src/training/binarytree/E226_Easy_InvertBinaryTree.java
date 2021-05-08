package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定二叉树的根，翻转树，并返回其根。
 *
 * 例 1：
 * Input: root = [4,2,7,1,3,6,9]
 * Output: [4,7,2,9,6,3,1]
 * Explanation:
 *         4                  4
 *      /    \             /    \
 *     2      7    =>    7      2
 *   /  \   /  \       /  \   /  \
 *  1    3 6    9     9    6 3    1
 *
 * 例 2：
 * Input: root = [2,1,3]
 * Output: [2,3,1]
 *     2         2
 *   /  \  =>  /  \
 *  1    3    3    1
 *
 * 例 3：
 * Input: root = []
 * Output: []
 *
 * 约束：
 * - 树的结点数量范围为 [0, 100]
 * - -100 <= Node.val <= 100
 */
public class E226_Easy_InvertBinaryTree {

    static void test(Function<TreeNode, TreeNode> method) {
        assertTrue(TreeNode.equals(method.apply(newTree(4,2,7,1,3,6,9)),
                newTree(4,7,2,9,6,3,1)));

        assertTrue(TreeNode.equals(method.apply(newTree(2,1,3)),
                newTree(2,3,1)));

        assertNull(method.apply(null));
    }

    /**
     * 写递归算法的关键是要明确函数的「定义」是什么，然后相信这个定义，利用这个定义推导最终结果，
     * 绝不要跳入递归的细节。
     *
     * 写树相关的算法，简单说就是，先搞清楚当前 root 节点该做什么，然后根据函数定义递归调用子节点，
     * 递归调用会让孩子节点做相同的事情。
     *
     * 二叉树题目的一个难点就是，如何把题目的要求细化成每个节点需要做的事情。
     *
     * LeetCode 耗时：0ms - 100%
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null)
            return null;
        // 以下代码放在后序也是可以的，但在中序不行。
        // 因为先处理了左子树，再处理根结点，这样左子树被换到了右子树，
        // 于是左子树会被处理两次，而右子树不会被处理。
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        root.left = invertTree(root.left);
        root.right = invertTree(root.right);
        return root;
    }

    @Test
    public void testInvertTree() {
        test(this::invertTree);
    }
}
