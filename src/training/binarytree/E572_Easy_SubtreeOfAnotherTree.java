package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.binarytree.TreeNode.newTree;

/**
 * 572. 另一棵树的子树: https://leetcode-cn.com/problems/subtree-of-another-tree/
 *
 * 给你两棵二叉树 root 和 subRoot 。检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。如果存在，
 * 返回 true ；否则，返回 false 。
 *
 * 二叉树 tree 的一棵子树包括 tree 的某个节点和这个节点的「所有后代节点」。tree 也可以看做它自身的一棵子树。
 *
 * 例 1：
 * 输入：root = [3,4,5,1,2], subRoot = [4,1,2]
 * 输出：true
 * 解释：
 *      3
 *     / \
 *    4  5      4
 *   / \       / \
 *  1  2      1  2
 *
 * 例 2：
 * 输入：root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
 * 输出：false
 * 解释：
 *      3
 *     / \
 *    4  5      4
 *   / \       / \
 *  1  2      1  2
 *    /
 *   0
 *
 * 说明：
 * - root 树上的节点数量范围是 [1, 2000]
 * - subRoot 树上的节点数量范围是 [1, 1000]
 * - -10^4 <= root.val <= 10^4
 * - -10^4 <= subRoot.val <= 10^4
 */
public class E572_Easy_SubtreeOfAnotherTree {

    public static void test(BiPredicate<TreeNode, TreeNode> method) {
        assertTrue(method.test(newTree(3,4,5,1,2), newTree(4,1,2)));
        assertFalse(method.test(newTree(3,4,5,1,2,null,null,null,null,0), newTree(4,1,2)));
        /*
               3
              / \
             4   5    3
            /   /    / \
           1   2    1   2
         */
        assertFalse(method.test(newTree(3,4,5,1,null,2), newTree(3,1,2)));
    }

    /**
     * 暴力 dfs 法。除了这种方法还可以使用前序遍历把树变成字符串，再使用 KMP、Rabin-Karp 之类的字符串搜索算法；
     * 或者还可以使用树哈希算法，参见：
     * https://leetcode-cn.com/problems/subtree-of-another-tree/solution/ling-yi-ge-shu-de-zi-shu-by-leetcode-solution/
     *
     * LeetCode 耗时：3 ms - 85.65%
     *          内存消耗：38.6 MB - 41.70%
     */
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null) {
            return subRoot == null;
        }

        if (eq(root, subRoot)) {
            return true;
        }

        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    private boolean eq(TreeNode root, TreeNode subRoot) {
        if (root == null) {
            return subRoot == null;
        } else if (subRoot == null) {
            return false;
        }

        return root.val == subRoot.val && eq(root.left, subRoot.left) && eq(root.right, subRoot.right);
    }

    @Test
    public void testIsSubtree() {
        test(this::isSubtree);
    }
}
