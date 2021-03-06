package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树的根，返回其节点值的先序遍历。
 *
 * 例 1：
 * Input: root = [1,null,2,3]
 * Output: [1,2,3]
 * Explanation:
 * 1
 *  \
 *   3
 *  /
 * 2
 *
 * 例 2：
 * Input: root = []
 * Output: []
 *
 * 例 3：
 * Input: root = [1]
 * Output: [1]
 *
 * 例 4：
 * Input: root = [1,2]
 * Output: [1,2]
 * Explanation:
 *   1
 *  /
 * 2
 *
 * 例 5：
 * Input: root = [1,null,2]
 * Output: [1,2]
 * Explanation:
 * 1
 *  \
 *   2
 *
 * 约束：
 * - 结点数的范围为 [0, 100]
 * - -100 <= Node.val <= 100
 */
public class Review_E144_Medium_BinaryTreePreorderTraversal {

    static void test(Function<TreeNode, List<Integer>> method) {
        TreeNode tree = newTree(1, null, 2, 3);
        assertEquals(method.apply(tree), Arrays.asList(1, 2, 3));

        assertEquals(method.apply(null), Collections.emptyList());

        assertEquals(method.apply(new TreeNode(1)), Collections.singletonList(1));

        tree = newTree(1, 2);
        assertEquals(method.apply(tree), Arrays.asList(1, 2));

        tree = newTree(1, null, 2);
        assertEquals(method.apply(tree), Arrays.asList(1, 2));
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> orderTravers = new ArrayList<>();
        preorderTraversal(orderTravers, root);

        return orderTravers;
    }

    private void preorderTraversal(List<Integer> orderTraversal, TreeNode node) {
        if (node != null) {
            orderTraversal.add(node.val);
            preorderTraversal(orderTraversal, node.left);
            preorderTraversal(orderTraversal, node.right);
        }
    }

    @Test
    public void testPreorderTraversal() {
        test(this::preorderTraversal);
    }


    /**
     * 使用迭代的方式先序遍历
     */
    public List<Integer> iterateTraversal(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        Deque<TreeNode> stack = new LinkedList<>();
        List<Integer> orderTraversal = new ArrayList<>();
        for(;;) {
            orderTraversal.add(root.val);
            if (root.left != null) {
                if (root.right != null)
                    stack.push(root.right);
                root = root.left;
            } else {
                if (root.right != null)
                    root = root.right;
                else if (!stack.isEmpty()) {
                    root = stack.pop();
                } else
                    break;
            }
        }

        return orderTraversal;
    }

    @Test
    public void testIterateTraversal() {
        test(this::iterateTraversal);
    }


    public List<Integer> conciseIterate(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        Deque<TreeNode> stack = new LinkedList<>();

        while (root != null) {
            while (root != null) {
                order.add(root.val);
                if (root.right != null)
                    stack.push(root.right);
                root = root.left;
            }
            if (stack.isEmpty())
                break;
            root = stack.pop();
        }

        return order;
    }

    @Test
    public void testConciseIterate() {
        test(this::conciseIterate);
    }
}
