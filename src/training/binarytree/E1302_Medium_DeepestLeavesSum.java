package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 1302. 层数最深叶子节点的和: https://leetcode-cn.com/problems/deepest-leaves-sum/
 *
 * 给你一棵二叉树的根节点 root ，请你返回层数最深的叶子节点的和 。
 *
 * 例 1：
 * 输入：root = [1,2,3,4,5,null,6,7,null,null,null,null,8]
 * 输出：15
 *
 * 例 2：
 * 输入：root = [6,7,8,2,7,1,3,9,null,1,4,null,null,null,5]
 * 输出：19
 *
 * 说明：
 * - 树中节点数目在范围 [1, 10^4] 之间。
 * - 1 <= Node.val <= 100
 */
public class E1302_Medium_DeepestLeavesSum {

    public static void test(ToIntFunction<TreeNode> method) {
        assertEquals(15, method.applyAsInt(newTree(1,2,3,4,5,null,6,7,null,null,null,null,8)));
        assertEquals(19, method.applyAsInt(newTree(6,7,8,2,7,1,3,9,null,1,4,null,null,null,5)));
    }

    /**
     * LeetCode 耗时：5 ms - 56.87%
     *          内存消耗：43.7 MB - 17.53%
     */
    public int deepestLeavesSum(TreeNode root) {
        Map<Integer, Integer> deep2sum = new HashMap<>();
        sum(root, deep2sum, 1);

        return deep2sum.get(deep2sum.size());
    }

    private void sum(TreeNode root, Map<Integer, Integer> deep2sum, int deep) {
        if (root == null) {
            return;
        }
        deep2sum.merge(deep, root.val, Integer::sum);
        sum(root.left, deep2sum, deep + 1);
        sum(root.right, deep2sum, deep + 1);
    }

    @Test
    public void testDeepestLeavesSum() {
        test(this::deepestLeavesSum);
    }


    /**
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：43.8 MB - 14.78%
     */
    public int betterMethod(TreeNode root) {
        maxDeep = leavesSum = 0;
        sum(root, 0);

        return leavesSum;
    }

    private int maxDeep, leavesSum;

    /**
     * 只需要记录最后一层的就好了。
     */
    private void sum(TreeNode root, int deep) {
        if (root == null) {
            return;
        }
        if (deep == maxDeep) {
            leavesSum += root.val;
        } else if (deep > maxDeep) {
            maxDeep = deep;
            leavesSum = root.val;
        }
        sum(root.left, deep + 1);
        sum(root.right, deep + 1);
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
