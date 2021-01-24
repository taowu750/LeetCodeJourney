package learn.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static learn.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树的根，返回其节点值的中序遍历。
 *
 * 例 1：
 * Input: root = [1,null,2,3]
 * Output: [1,3,2]
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
 * Output: [2,1]
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
public class BinaryTreeInorderTraversal {

    static void test(Function<TreeNode, List<Integer>> method) {
        TreeNode tree = newTree(1, null, 2, 3);
        assertEquals(method.apply(tree), Arrays.asList(1, 3, 2));

        assertEquals(method.apply(null), Collections.emptyList());

        assertEquals(method.apply(new TreeNode(1)), Collections.singletonList(1));

        tree = newTree(1, 2);
        assertEquals(method.apply(tree), Arrays.asList(2, 1));

        tree = newTree(1, null, 2);
        assertEquals(method.apply(tree), Arrays.asList(1, 2));
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> orderTravers = new ArrayList<>();
        inorderTraversal(orderTravers, root);

        return orderTravers;
    }

    private void inorderTraversal(List<Integer> orderTraversal, TreeNode node) {
        if (node != null) {
            inorderTraversal(orderTraversal, node.left);
            orderTraversal.add(node.val);
            inorderTraversal(orderTraversal, node.right);
        }
    }

    @Test
    public void testInorderTraversal() {
        test(this::inorderTraversal);
    }



    /**
     * 使用迭代的方法中序遍历。
     */
    public List<Integer> iterateTraversal(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        Deque<TreeNode> stack = new LinkedList<>();
        List<Integer> orderTraversal = new ArrayList<>();
        LABEL_OUTER:
        for (;;) {
            if (root.left != null) {
                stack.push(root);
                root = root.left;
            } else {
                orderTraversal.add(root.val);
                if (root.right != null)
                    root = root.right;
                else {
                    do {
                        if (stack.isEmpty())
                            break LABEL_OUTER;
                        root = stack.pop();
                        orderTraversal.add(root.val);
                        root = root.right;
                    } while (root == null);
                }
            }
        }

        return orderTraversal;
    }

    @Test
    public void testIterateTraversal() {
        test(this::iterateTraversal);
    }


    /**
     * 更简洁的迭代方法。
     */
    public List<Integer> conciseIterate(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        Deque<TreeNode> stack = new LinkedList<>();
        List<Integer> orderTraversal = new ArrayList<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            orderTraversal.add(root.val);
            root = root.right;
        }

        return orderTraversal;
    }

    @Test
    public void testConciseIterate() {
        test(this::conciseIterate);
    }
}
