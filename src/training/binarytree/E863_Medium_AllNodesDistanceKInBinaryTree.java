package training.binarytree;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static training.binarytree.TreeNode.newTree;
import static util.CollectionUtil.setEquals;
import static util.datastructure.BinaryTreeNode.find;

/**
 * 863. 二叉树中所有距离为 K 的结点: https://leetcode-cn.com/problems/all-nodes-distance-k-in-binary-tree/
 *
 * 给定一个二叉树（具有根结点 root），一个目标结点 target，和一个整数值 K 。
 *
 * 返回到目标结点 target 距离为 K 的所有结点的值的列表。答案可以以任何顺序返回。
 *
 * 例 1：
 * 输入：root = [3,5,1,6,2,0,8,null,null,7,4], target = 5, K = 2
 * 输出：[7,4,1]
 * 解释：
 * 所求结点为与目标结点（值为 5）距离为 2 的结点，值分别为 7，4，以及 1
 *           3
 *         /  \
 *        5    1
 *       / \  / \
 *      6  2 0  8
 *        / \
 *       7  4
 *
 * 说明：
 * - 给定的树是非空的。
 * - 树上的每个结点都具有唯一的值 0 <= node.val <= 500。
 * - 目标结点 target 是树上的结点。
 * - 0 <= K <= 1000.
 */
public class E863_Medium_AllNodesDistanceKInBinaryTree {

    public static void test(TriFunction<TreeNode, TreeNode, Integer, List<Integer>> method) {
        TreeNode root = newTree(3,5,1,6,2,0,8,null,null,7,4), target = find(root, 5);
        setEquals(asList(7, 4, 1), method.apply(root, target, 2));
    }

    /**
     * LeetCode 耗时：13 ms - 99.58%
     *          内存消耗：38.6 MB - 26.90%
     */
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        if (k == 0) {
            return Collections.singletonList(target.val);
        }

        List<Integer> result = new ArrayList<>();
        distance(root, target, k, result);

        return result;
    }

    /**
     * distance 返回 root 到 target 的距离：
     * - root == target: 返回 0
     * - target 不是 root 的子节点：返回 -1
     * - target 是 root 的子节点：返回路径长度
     *
     * 大体思路就是先找到 target，找到后先找它符合条件的子节点。父节点使用回溯进行查找。
     */
    private int distance(TreeNode root, TreeNode target, int k, List<Integer> result) {
        if (root == null) {
            return -1;
        }

        if (root == target) {
            findChild(root.left, k - 1, result);
            findChild(root.right, k - 1, result);
            return 0;
        } else {
            int ld = distance(root.left, target, k, result);
            int rd = distance(root.right, target, k, result);
            int dist = Math.max(ld, rd);
            // target 是 root 的子节点
            if (dist >= 0) {
                // root 到 target 的距离
                int res = dist + 1;
                // root 到 target 的距离刚好是 k
                if (res == k) {
                    result.add(root.val);
                } else if (res < k) {
                    // root 到 target 的距离小于 k，此时可以查找 root 的另一颗子树，并且继续向上回溯
                    if (ld == dist) {
                        findChild(root.right, k - res - 1, result);
                    } else {
                        findChild(root.left, k - res - 1, result);
                    }
                }

                return res;
            } else {
                return -1;
            }
        }
    }

    /**
     * 已找到 target，搜索 target 符合条件的子节点
     */
    private void findChild(TreeNode child, int dist, List<Integer> result) {
        if (child == null) {
            return;
        }
        if (dist > 0) {
            findChild(child.left, dist - 1, result);
            findChild(child.right, dist - 1, result);
        } else {
            result.add(child.val);
        }
    }

    @Test
    public void testDistanceK() {
        test(this::distanceK);
    }
}
