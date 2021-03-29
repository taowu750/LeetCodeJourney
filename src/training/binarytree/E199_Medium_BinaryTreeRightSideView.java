package training.binarytree;

import learn.binarytree.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一棵二叉树，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 *
 * 例 1：
 * Input: [1,2,3,null,5,null,4]
 * Output: [1, 3, 4]
 * Explanation:
 *      1       <---
 *    /  \
 *  2     3     <---
 *   \     \
 *    5     4   <---
 *
 * 例 2：
 * Input: [1,2,3,null,5]
 * Output: [1, 3, 5]
 * Explanation:
 *      1       <---
 *    /  \
 *  2     3     <---
 *   \
 *    5         <---
 */
public class E199_Medium_BinaryTreeRightSideView {

    static void test(Function<TreeNode, List<Integer>> method) {
        assertEquals(method.apply(newTree(1,2,3,null,5,null,4)), asList(1,3,4));
        assertEquals(method.apply(newTree(1,2,3,null,5)), asList(1,3,5));
        assertTrue(method.apply(null).isEmpty());
    }

    /**
     * LeetCode 耗时：2 ms - 20.29%
     *          内存消耗：37 MB - 81.18%
     */
    public List<Integer> rightSideView(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.remove();
                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
                if (i == size - 1)
                    result.add(node.val);
            }
        }

        return result;
    }

    @Test
    public void testRightSideView() {
        test(this::rightSideView);
    }


    /**
     * DFS 方法。
     *
     * LeetCode 耗时：1 ms - 99.24%
     *          内存消耗：37.1 MB - 62.21%
     */
    public List<Integer> dfsMethod(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, 0, result);

        return result;
    }

    private void dfs(TreeNode root, int depth, List<Integer> result) {
        if (root == null)
            return;

        // 中->右->左。每层添加第一个看到的节点
        if (depth == result.size())
            result.add(root.val);
        dfs(root.right, depth + 1, result);
        dfs(root.left, depth + 1, result);
    }

    @Test
    public void testDfsMethod() {
        test(this::dfsMethod);
    }
}
