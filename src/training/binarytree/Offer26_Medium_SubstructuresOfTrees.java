package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static training.binarytree.TreeNode.newTree;

/**
 * 剑指 Offer 26. 树的子结构: https://leetcode-cn.com/problems/shu-de-zi-jie-gou-lcof/
 *
 * 输入两棵二叉树A和B，判断B是不是A的子结构。(约定空树不是任意一个树的子结构)
 * B是A的子结构， 即「A中有出现和B相同的结构和节点值」。
 *
 * 例如:
 * 给定的树 A:
 *
 *     3
 *    / \
 *   4  5
 *  / \
 * 1  2
 * 给定的树 B：
 *   4
 *  /
 * 1
 * 返回 true，因为 B 与 A 的一个子树拥有相同的结构和节点值。
 *
 * 例 1：
 * 输入：A = [1,2,3], B = [3,1]
 * 输出：false
 *
 * 例 2：
 * 输入：A = [3,4,5,1,2], B = [4,1]
 * 输出：true
 *
 * 约束：
 * - 0 <= 节点个数 <= 10000
 */
public class Offer26_Medium_SubstructuresOfTrees {

    static void test(BiPredicate<TreeNode, TreeNode> method) {
        assertFalse(method.test(newTree(1,2,3), newTree(3,1)));
        assertTrue(method.test(newTree(3,4,5,1,2), newTree(4,1)));
        assertTrue(method.test(newTree(1,2,3,4), newTree(3)));
        assertTrue(method.test(newTree(10,12,6,8,3,11), newTree(10,12,6,8)));
        assertFalse(method.test(newTree(10,12,6,8,3,11), null));
        assertFalse(method.test(null, newTree(10,12,6,8)));
        assertFalse(method.test(null, null));
    }

    /**
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：40 MB - 81.01%
     */
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (A == null || B == null) {
            return false;
        }

        if (contains(A, B)) {
            return true;
        } else {
            return isSubStructure(A.left, B) || isSubStructure(A.right, B);
        }
    }

    /**
     * 判断 A 是否包含 B
     */
    private boolean contains(TreeNode A, TreeNode B) {
        // B 的为 null，则返回 true
        if (B == null) {
            return true;
        }
        // B 存在，但 A 不存在或值和 B 不同，则 A 不包含 B
        if (A == null || A.val != B.val) {
            return false;
        }
        return contains(A.left, B.left) && contains(A.right, B.right);
    }

    @Test
    public void testIsSubStructure() {
        test(this::isSubStructure);
    }
}
