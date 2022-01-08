package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 513. 找树左下角的值: https://leetcode-cn.com/problems/find-bottom-left-tree-value/
 *
 * 给定一个二叉树的根节点 root，请找出该二叉树的最底层最左边节点的值。
 *
 * 假设二叉树中至少有一个节点。
 *
 * 例 1
 * 输入: root = [2,1,3]
 * 输出: 1
 *     2
 *    / \
 *   1  3
 *
 * 例 2：
 * 输入: [1,2,3,4,null,5,6,null,null,7]
 * 输出: 7
 *        1
 *      /  \
 *    2     3
 *   /     / \
 *  4     5   6
 *       /
 *      7
 *
 * 说明：
 * - 二叉树的节点个数的范围是 [1,10^4]
 * - -2^31 <= Node.val <= 2^31 - 1
 */
public class E512_Medium_FindBottomLeftTreeValue {

    public static void test(ToIntFunction<TreeNode> method) {
        assertEquals(1, method.applyAsInt(newTree(2, 1, 3)));
        assertEquals(7, method.applyAsInt(newTree(1,2,3,4,null,5,6,null,null,7)));
    }

    /**
     * 类似 {@link E199_Medium_BinaryTreeRightSideView}。
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：38 MB - 69.21%
     */
    public int findBottomLeftValue(TreeNode root) {
        leftViewCnt = 0;
        find(root, 0);

        return result;
    }

    private int leftViewCnt, result;

    private void find(TreeNode root, int depth) {
        if (root == null) {
            return;
        }
        if (depth == leftViewCnt) {
            result = root.val;
            leftViewCnt++;
        }
        find(root.left, depth + 1);
        find(root.right, depth + 1);
    }

    @Test
    public void testFindBottomLeftValue() {
        test(this::findBottomLeftValue);
    }
}
