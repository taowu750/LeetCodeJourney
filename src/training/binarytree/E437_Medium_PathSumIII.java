package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static training.binarytree.TreeNode.newTree;

/**
 * 437. 路径总和 III: https://leetcode-cn.com/problems/path-sum-iii/
 *
 * 给定一个二叉树的根节点 root，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的「路径」的数目。
 *
 * 「路径」不需要从根节点开始，也不需要在叶子节点结束，但是路径」方向必须是向下的（只能从父节点到子节点）。
 *
 * 例 1：
 * 输入：root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
 * 输出：3
 * 解释：和等于 8 的路径有 3 条，如图所示。
 *
 * 例 2：
 * 输入：root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * 输出：3
 *
 * 约束：
 * - 二叉树的节点个数的范围是 [0,1000]
 * - -10^9 <= Node.val <= 10^9
 * - -1000 <= targetSum <= 1000
 */
public class E437_Medium_PathSumIII {

    static void test(ToIntBiFunction<TreeNode, Integer> method) {
        assertEquals(3, method.applyAsInt(newTree(10,5,-3,3,2,null,11,3,-2,null,1), 8));
        assertEquals(3, method.applyAsInt(newTree(5,4,8,11,null,13,4,7,2,null,null,5,1), 22));
        assertEquals(0, method.applyAsInt(null, 0));
        /*
           1
            \
             2
              \
               3
                \
                 4
                  \
                   5
         */
        assertEquals(2, method.applyAsInt(newTree(1,null,2,null,3,null,4,null,5), 3));
    }

    private int result;

    /**
     * LeetCode 耗时：22 ms - 53.47%
     *          内存消耗：50.3 MB - 5.01%
     */
    public int pathSum(TreeNode root, int targetSum) {
        result = 0;
        dfs(root, targetSum, new HashMap<>());

        return result;
    }

    /**
     * 自底向上，计算所有可能性。
     */
    private void dfs(TreeNode root, int targetSum, Map<TreeNode, List<Integer>> cache) {
        if (root == null) {
            return;
        }

        dfs(root.left, targetSum, cache);
        dfs(root.right, targetSum, cache);

        int size = 1;
        if (root.left != null) {
            size += cache.get(root.left).size();
        }
        if (root.right != null) {
            size += cache.get(root.right).size();
        }

        List<Integer> pathSums = new ArrayList<>(size);
        pathSums.add(root.val);
        if (root.val == targetSum) {
            result++;
        }
        if (root.left != null) {
            for (int subPathSum : cache.get(root.left)) {
                pathSums.add(root.val + subPathSum);
                if (root.val + subPathSum == targetSum) {
                    result++;
                }
            }
        }
        if (root.right != null) {
            for (int subPathSum : cache.get(root.right)) {
                pathSums.add(root.val + subPathSum);
                if (root.val + subPathSum == targetSum) {
                    result++;
                }
            }
        }

        cache.put(root, pathSums);
    }

    @Test
    public void testPathSum() {
        test(this::pathSum);
    }
}
