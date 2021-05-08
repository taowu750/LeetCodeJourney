package training.binarysearchtree;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.ToIntBiFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 230. 二叉搜索树中第K小的元素: https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/
 *
 * 给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第 k 个最小元素（从 1 开始计数）。
 *
 * 例 1：
 * 输入：root = [3,1,4,null,2], k = 1
 * 输出：1
 * 解释：
 *      3
 *    /  \
 *   1   4
 *    \
 *    2
 *
 * 例 2：
 * 输入：root = [5,3,6,2,4,null,null,1], k = 3
 * 输出：3
 * 解释：
 *            5
 *          /  \
 *         3   6
 *       /  \
 *      2   4
 *    /
 *   1
 *
 * 约束：
 * - 树中的节点数为 n 。
 * - 1 <= k <= n <= 10**4
 * - 0 <= Node.val <= 10**4
 */
public class E230_Medium_KthSmallestElementInBST {

    static void test(ToIntBiFunction<TreeNode, Integer> method) {
        assertEquals(1, method.applyAsInt(newTree(3,1,4,null,2), 1));
        assertEquals(3, method.applyAsInt(newTree(5,3,6,2,4,null,null,1), 3));
    }

    /**
     * 这不是最高效的解法，时间复杂度为 O(N)。最好的方法是每个节点需要记录，以自己为根的这棵二叉树有多少个节点。
     * 对于每个节点 node 就可以通过 node.left 推导出 node 的排名，从而做到 O(logN) 时间复杂度。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38.2 MB - 78.98%
     */
    public int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new LinkedList<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (--k == 0)
                return root.val;
            root = root.right;
        }

        return -1;
    }

    @Test
    void testKthSmallest() {
        test(this::kthSmallest);
    }


    int k;
    TreeNode find;

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：37.8 MB - 99.17%
     */
    public int recurMethod(TreeNode root, int k) {
        this.k = k;
        find = null;
        recur(root);

        return find.val;
    }

    private void recur(TreeNode root) {
        if (root == null || find != null)
            return;
        recur(root.left);
        if (--k == 0) {
            find = root;
            return;
        }
        recur(root.right);
    }

    @Test
    void testRecurMethod() {
        test(this::recurMethod);
    }
}
