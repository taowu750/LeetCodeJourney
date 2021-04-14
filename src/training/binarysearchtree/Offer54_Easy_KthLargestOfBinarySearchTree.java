package training.binarysearchtree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToIntBiFunction;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一棵二叉搜索树，请找出其中第 k 大的节点。
 *
 * 例 1：
 * 输入: root = [3,1,4,null,2], k = 1
 *    3
 *   / \
 *  1   4
 *   \
 *    2
 * 输出: 4
 *
 * 例 2：
 * 输入: root = [5,3,6,2,4,null,null,1], k = 3
 *        5
 *       / \
 *      3   6
 *     / \
 *    2   4
 *   /
 *  1
 * 输出: 4
 *
 * 约束：
 * - 1 ≤ k ≤ 二叉搜索树元素个数
 */
public class Offer54_Easy_KthLargestOfBinarySearchTree {

    static void test(ToIntBiFunction<TreeNode, Integer> method) {
        assertEquals(4, method.applyAsInt(newTree(3,1,4,null,2), 1));
        assertEquals(4, method.applyAsInt(newTree(5,3,6,2,4,null,null,1), 3));
    }

    /**
     * LeetCode 耗时：1 ms - 40.94%
     *          内存消耗：38.4 MB - 43.45%
     */
    public int kthLargest(TreeNode root, int k) {
        Deque<TreeNode> stack = new LinkedList<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.right;
            }
            root = stack.pop();
            if (--k == 0)
                return root.val;
            root = root.left;
        }

        return 0;
    }

    @Test
    public void testKthLargest() {
        test(this::kthLargest);
    }


    int res, k;

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.5 MB - 30.36%
     */
    public int recurMethod(TreeNode root, int k) {
        // 为每个节点分配一个序号，k 是共享变量
        this.k = k;
        dfs(root);
        return res;
    }

    void dfs(TreeNode root) {
        if(root == null)
            return;
        dfs(root.right);
        if(k == 0)
            return;
        if(--k == 0)
            res = root.val;
        dfs(root.left);
    }

    @Test
    public void testRecurMethod() {
        test(this::recurMethod);
    }
}
