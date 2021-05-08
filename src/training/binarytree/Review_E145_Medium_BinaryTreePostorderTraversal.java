package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static training.binarytree.TreeNode.newTree;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 给定二叉树的根，返回其节点值的后序遍历。
 *
 * 例 1：
 * Input: root = [1,null,2,3]
 * Output: [3,2,1]
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
 * Output: [2,1]
 * Explanation:
 * 1
 *  \
 *   2
 *
 * 约束：
 * - 结点数的范围为 [0, 100]
 * - -100 <= Node.val <= 100
 */
public class Review_E145_Medium_BinaryTreePostorderTraversal {

    static void test(Function<TreeNode, List<Integer>> method) {
        TreeNode tree = newTree(1, null, 2, 3);
        assertEquals(method.apply(tree), Arrays.asList(3, 2, 1));

        assertEquals(method.apply(null), Collections.emptyList());

        assertEquals(method.apply(new TreeNode(1)), Collections.singletonList(1));

        tree = newTree(1, 2);
        assertEquals(method.apply(tree), Arrays.asList(2, 1));

        tree = newTree(1, null, 2);
        assertEquals(method.apply(tree), Arrays.asList(2, 1));
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> orderTravers = new ArrayList<>();
        postorderTraversal(orderTravers, root);

        return orderTravers;
    }

    private void postorderTraversal(List<Integer> orderTraversal, TreeNode node) {
        if (node != null) {
            postorderTraversal(orderTraversal, node.left);
            postorderTraversal(orderTraversal, node.right);
            orderTraversal.add(node.val);
        }
    }

    @Test
    public void testInorderTraversal() {
        test(this::postorderTraversal);
    }


    /**
     * 使用迭代的方法后序遍历。
     */
    public List<Integer> iterateTraversal(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        Deque<TreeNode> stack = new LinkedList<>();
        List<Integer> orderTraversal = new ArrayList<>();
        LABEL_OUTER:
        for(;;) {
            if (root.left != null) {
                stack.push(root);
                root = root.left;
            } else if (root.right != null) {
                stack.push(root);
                root = root.right;
            } else {
                for(;;) {
                    orderTraversal.add(root.val);
                    if (stack.isEmpty())
                        break LABEL_OUTER;
                    TreeNode parent = stack.peek();
                    if (parent.right == null || parent.right == root) {
                        root = stack.pop();
                    } else {
                        root = parent;
                        break;
                    }
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
     * 用两个栈的做法。
     */
    public List<Integer> conciseIterate(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        Deque<TreeNode> stack = new LinkedList<>();
        LinkedList<Integer> orderTraversal = new LinkedList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            root = stack.pop();
            // 把 orderTraversal 也当成一个栈
            orderTraversal.push(root.val);
            if (root.left != null)
                stack.push(root.left);
            if (root.right != null)
                stack.push(root.right);
        }

        return orderTraversal;
    }

    @Test
    public void testConciseIterate() {
        test(this::conciseIterate);
    }
}
