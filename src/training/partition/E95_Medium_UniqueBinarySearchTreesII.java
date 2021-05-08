package training.partition;

import training.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntFunction;

import static java.util.Arrays.asList;
import static training.binarytree.TreeNode.newTree;
import static util.CollectionUtil.equalsIgnoreOrder;

/**
 * 95. 不同的二叉搜索树 II: https://leetcode-cn.com/problems/unique-binary-search-trees-ii/
 *
 * 给定一个整数 n，生成所有由 1 ... n 为节点所组成的二叉搜索树。
 *
 * 例 1：
 * 输入：3
 * 输出：
 * [
 *  [1,null,3,2],
 *  [3,2,null,1],
 *  [3,1,null,null,2],
 *  [2,1,3],
 *  [1,null,2,null,3]
 * ]
 * 解释：
 * 以上的输出对应以下 5 种不同结构的二叉搜索树：
 *
 *    1         3     3      2      1
 *     \       /     /      / \      \
 *      3     2     1      1   3      2
 *     /     /       \                 \
 *    2     1         2                 3
 *
 * 约束：
 * - 0 <= n <= 8
 */
public class E95_Medium_UniqueBinarySearchTreesII {

    static void test(IntFunction<List<TreeNode>> method) {
        equalsIgnoreOrder(asList(newTree(1,null,3,2), newTree(3,2,null,1),
                newTree(3,1,null,null,2), newTree(2,1,3), newTree(1,null,2,null,3)),
                method.apply(3));
    }

    /**
     * 分治算法。类似于 {@link E241_Medium_DifferentWaysToAddParentheses}。
     *
     * LeetCode 耗时：1 ms - 99.59%
     *          内存消耗：39.1 MB - 59.81%
     */
    public List<TreeNode> generateTrees(int n) {
        if (n == 0)
            return Collections.emptyList();
        return partition(1, n);
    }

    private List<TreeNode> partition(int lo, int hi) {
        if (lo > hi)
            return Collections.singletonList(null);
        if (lo == hi)
            return Collections.singletonList(new TreeNode(lo));

        List<TreeNode> result = new LinkedList<>();
        for (int i = lo; i <= hi; i++) {
            List<TreeNode> leftResult = partition(lo, i - 1);
            List<TreeNode> rightResult = partition(i + 1, hi);
            for (TreeNode leftChild : leftResult) {
                for (TreeNode rightChild : rightResult) {
                    TreeNode root = new TreeNode(i);
                    root.left = leftChild;
                    root.right = rightChild;
                    result.add(root);
                }
            }
        }

        return result;
    }

    @Test
    public void testGenerateTrees() {
        test(this::generateTrees);
    }
}
