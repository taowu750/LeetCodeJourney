package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一棵二叉树，检查它是否是其自身的镜像（即，围绕其中心对称）。
 *
 * 例如，此二叉树 [1,2,2,3,4,4,3] 是对称的：
 *      1
 *    /  \
 *   2    2
 *  / \  / \
 * 3  4 4   3
 *
 * 但是，这个二叉树 [1,2,2,null,3,null,3] 不是：
 *    1
 *   / \
 *  2   2
 *   \   \
 *   3    3
 *
 * 使用递归和迭代的方式解决这个问题
 */
public class E101_Easy_SymmetricTree {

    static void test(Predicate<TreeNode> method) {
        TreeNode root = newTree(1, 2, 2, 3, 4, 4, 3);
        assertTrue(method.test(root));

        root = newTree(1, 2, 2, null, 3, null, 3);
        assertFalse(method.test(root));

        root = newTree(1, 2, 2, null, 4, 4, null);
        assertTrue(method.test(root));

        root = newTree(1, 2, 2, 3, null, null, 3);
        assertTrue(method.test(root));

        root = newTree(1, 2, 2, 3, 4, 4, 5);
        assertFalse(method.test(root));
    }

    public boolean isSymmetric(TreeNode root) {
        if (root == null)
            return true;

        return isSymmetric(root.left, root.right);
    }

    private boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left == null && right == null)
            return true;
        if (left == null || right == null)
            return false;

        return left.val == right.val
                && isSymmetric(left.left, right.right)
                && isSymmetric(left.right, right.left);
    }

    @Test
    public void testIsSymmetric() {
        test(this::isSymmetric);
    }


    @SuppressWarnings("ConstantConditions")
    public boolean iterateMethod(TreeNode root) {
        if (root == null)
            return true;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root.left);
        queue.offer(root.right);
        while (!queue.isEmpty()) {
            TreeNode left = queue.poll(), right = queue.poll();
            if ((left == null && right != null)
                || (left != null && right == null))
                return false;
            if (left != null && right != null) {
                if (left.val != right.val)
                    return false;
                queue.offer(left.left);
                queue.offer(right.right);
                queue.offer(left.right);
                queue.offer(right.left);
            }
        }

        return true;
    }

    @Test
    public void testIterateMethod() {
        test(this::iterateMethod);
    }
}
