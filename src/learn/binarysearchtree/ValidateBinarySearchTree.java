package learn.binarysearchtree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Predicate;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定二叉树的根，确定它是否是合法的二叉搜索树（BST）。
 * 有效的 BST 定义如下：
 * - 节点的左子树仅包含键小于节点键的节点。
 * - 节点的右子树仅包含键大于节点键的节点。
 * - 左右子树也必须是二叉搜索树。
 *
 * 例 1：
 * Input: root = [2,1,3]
 * Output: true
 * Explanation:
 *    2
 *  /  \
 * 1   3
 *
 * 例 2：
 * Input: root = [5,1,4,null,null,3,6]
 * Output: false
 * Explanation:
 *    5
 *  /  \
 * 1   4
 *   /  \
 *  3   6
 *
 * 约束：
 * - 树中结点数量范围为 [1, 10**4]
 * - -2**31 <= Node.val <= 2**31 - 1
 */
public class ValidateBinarySearchTree {

    static void test(Predicate<TreeNode> method) {
        assertTrue(method.test(newTree(2, 1, 3)));

        assertFalse(method.test(newTree(5, 1, 4, null, null, 3, 6)));

        /*
             5
           /  \
          4    6
             /  \
            3    7
         */
        assertFalse(method.test(newTree(5, 4, 6, null, null, 3, 7)));

        assertFalse(method.test(newTree(1, 2)));
    }

    /**
     * LeetCode 耗时：1ms - 33.49%
     */
    public boolean isValidBST(TreeNode root) {
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode pre = null;
        // 迭代式的中序遍历。遍历左子树时，pre 是左子节点；遍历右子树时，pre 是根结点。
        // 使用这种模式可以解决很多问题，例如 BST 中第 k 个最小值。
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (pre != null && root.val <= pre.val)
                return false;
            pre = root;
            root = root.right;
        }
        return true;
    }

    @Test
    public void testIsValidBST() {
        test(this::isValidBST);
    }


    TreeNode prev;

    /**
     * LeetCode 耗时：0ms - 100%
     */
    public boolean recursiveMethod(TreeNode root) {
        prev = null;
        return recursive(root);
    }

    private boolean recursive(TreeNode root) {
        if (root == null)
            return true;
        if (!recursive(root.left) || (prev != null && root.val <= prev.val))
            return false;
        // 写递归方法需要注意，prev 是一个需要追踪的变量
        prev = root;
        return recursive(root.right);
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }


    /**
     * LeetCode 耗时：0ms - 100%
     */
    public boolean otherRecursiveMethod(TreeNode root) {
        return help(root, null, null);
    }

    private boolean help(TreeNode p, Integer low, Integer high) {
        if (p == null) return true;
        return (low == null || p.val > low)
                && (high == null || p.val < high)
                && help(p.left, low, p.val)
                && help(p.right, p.val, high);
    }

    @Test
    public void testOtherRecursiveMethod() {
        test(this::otherRecursiveMethod);
    }
}
