package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 113. 路径总和 II: https://leetcode-cn.com/problems/path-sum-ii/
 *
 * 给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有「从根节点到叶子节点」路径总和等于给定目标和的路径。
 *
 * 例 1：
 * 输入：root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * 输出：[[5,4,11,2],[5,8,4,5]]
 * 解释：
 *         (5)
 *        /  \
 *      (4)  (8)
 *      /   /  \
 *   (11)  13  (4)
 *   /  \     /  \
 *  7  (2)  (5)   1
 *
 * 例 2：
 * 输入：root = [1,2,3], targetSum = 5
 * 输出：[]
 * 解释：
 *     1
 *   /  \
 *  2    3
 *
 * 例 3：
 * 输入：root = [1,2], targetSum = 0
 * 输出：[]
 * 解释：
 *     1
 *   /
 *  2
 *
 * 约束：
 * - 树中节点总数在范围 [0, 5000] 内
 * - -1000 <= Node.val <= 1000
 * - -1000 <= targetSum <= 1000
 */
public class E113_Medium_PathSumII {

    static void test(BiFunction<TreeNode, Integer, List<List<Integer>>> method) {
        assertEquals(Arrays.asList(Arrays.asList(5,4,11,2), Arrays.asList(5,8,4,5)),
                method.apply(newTree(5,4,8,11,null,13,4,7,2,null,null,5,1), 22));

        assertTrue(method.apply(newTree(1,2,3), 5).isEmpty());
        assertTrue(method.apply(newTree(1,2), 0).isEmpty());
        assertTrue(method.apply(null, 0).isEmpty());
    }

    /**
     * 回溯算法。
     *
     * LeetCode 耗时：1 ms - 100.00%
     *          内存消耗：38.8 MB - 66.43%
     */
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        dfs(root, targetSum, path, result);

        return result;
    }

    //  path 类似前缀数组，这样就不用计算总和了
    private void dfs(TreeNode root, int targetSum, List<Integer> path, List<List<Integer>> allPath) {
        if (root == null)
            return;
        int pathSum = root.val + (path.size() > 0 ? path.get(path.size() - 1) : 0);
        if (root.left == null && root.right == null) {
            if (pathSum == targetSum) {
                List<Integer> result = new ArrayList<>(path.size() + 1);
                if (path.size() > 0) {
                    int val = path.get(0);
                    result.add(val);
                    for (int i = 1; i < path.size(); i++) {
                        val = path.get(i) - path.get(i - 1);
                        result.add(val);
                    }
                }
                result.add(root.val);
                allPath.add(result);
            }
            return;
        }

        path.add(pathSum);
        dfs(root.left, targetSum, path, allPath);
        path.remove(path.size() - 1);

        path.add(pathSum);
        dfs(root.right, targetSum, path, allPath);
        path.remove(path.size() - 1);
    }

    @Test
    public void testPathSum() {
        test(this::pathSum);
    }
}
