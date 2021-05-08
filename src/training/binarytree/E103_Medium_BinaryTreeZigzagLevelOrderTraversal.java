package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定一个二叉树，返回其节点值的锯齿形层序遍历。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 *
 * 例 1：
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 *
 * 约束：
 * [
 *   [3],
 *   [20,9],
 *   [15,7]
 * ]
 */
public class E103_Medium_BinaryTreeZigzagLevelOrderTraversal {

    static void test(Function<TreeNode, List<List<Integer>>> method) {
        assertEquals(Arrays.asList(singletonList(3), Arrays.asList(20, 9), Arrays.asList(15, 7)),
                method.apply(newTree(3, 9, 20, null, null, 15, 7)));
    }

    /**
     * LeetCode 耗时：1 ms - 98.72%
     *          内存消耗：38.7 MB - 38.07%
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        boolean leftToRight = true;
        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            LinkedList<Integer> floor = new LinkedList<>();
            if (leftToRight) {
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.remove();
                    floor.add(node.val);
                    if (node.left != null)
                        queue.add(node.left);
                    if (node.right != null)
                        queue.add(node.right);
                }
            } else {
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.remove();
                    floor.push(node.val);
                    if (node.left != null)
                        queue.add(node.left);
                    if (node.right != null)
                        queue.add(node.right);
                }
            }
            result.add(floor);
            leftToRight = !leftToRight;
        }

        return result;
    }

    @Test
    void testZigzagLevelOrder() {
        test(this::zigzagLevelOrder);
    }
}
