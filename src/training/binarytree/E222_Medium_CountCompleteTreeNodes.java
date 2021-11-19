package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 222. 完全二叉树的节点个数: https://leetcode-cn.com/problems/count-complete-tree-nodes/
 *
 * 给你一棵完全二叉树的根节点 root ，求出该树的节点个数。
 *
 * 完全二叉树的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，
 * 并且最下面一层的节点都集中在该层最左边的若干位置。若最底层为第 h 层，则该层包含 1~2^h个节点。
 *
 * 进阶：遍历树来统计节点是一种时间复杂度为 O(n) 的简单解决方案。你可以设计一个更快的算法吗？
 *
 * 例 1：
 * 输入：root = [1,2,3,4,5,6]
 * 输出：6
 *
 * 例 2：
 * 输入：root = []
 * 输出：0
 *
 * 例 3：
 * 输入：root = [1]
 * 输出：1
 *
 * 说明：
 * - 树中节点的数目范围是[0, 5 * 10^4]
 * - 0 <= Node.val <= 5 * 10^4
 * - 题目数据保证输入的树是完全二叉树
 */
public class E222_Medium_CountCompleteTreeNodes {

    public static void test(ToIntFunction<TreeNode> method) {
        assertEquals(6, method.applyAsInt(newTree(1, 2, 3, 4, 5, 6)));
        assertEquals(0, method.applyAsInt(null));
        assertEquals(1, method.applyAsInt(newTree(1)));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：41 MB - 21.69%
     */
    public int countNodes(TreeNode root) {
        result = 0;
        find(root, 1);

        return result;
    }

    private int result;

    /**
     * 给节点分配序号，最大序号就是节点数量。
     */
    private void find(TreeNode root, int order) {
        if (root == null) {
            return;
        }
        int lh = height(root.left), rh = height(root.right);
        result = order;
        // 当左子树高于右子树，则完全数最后一个节点（具有最大序号的节点）肯定在左子树
        if (lh > rh) {
            find(root.left, order * 2);
        } else {  // 当左子树高度等于右子树，则完全数最后一个节点肯定在右子树
            find(root.right, order * 2 + 1);
        }
    }

    /**
     * 计算高度
     */
    private int height(TreeNode root) {
        int result = 0;
        while (root != null) {
            root = root.left;
            result++;
        }

        return result;
    }

    @Test
    public void testCountNodes() {
        test(this::countNodes);
    }
}
