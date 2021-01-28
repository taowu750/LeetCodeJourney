package learn.binarytree;

import org.junit.jupiter.api.Test;
import util.datastructure.BinaryTreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一颗完全二叉树。其中每个结点定义如下：
 * class Node {
 *     public int val;
 *     public Node left;
 *     public Node right;
 *     public Node next;
 * };
 * 你需要将每个结点 next 指针指向其下一个右节点。如果没有下一个右节点，则为 NULL。
 *
 * 最初，所有 next 指针都设置为 NULL。
 * 你应该仅使用常数空间。不过递归方法的隐式堆栈空间不算作此问题的额外空间。
 *
 * 例 1：
 * Input: root = [1,2,3,4,5,6,7]
 * Output: [1,#,2,3,#,4,5,6,7,#]
 * Explanation:
 *       1              1-->NULL
 *    /    \        /      \
 *   2     3       2------>3-->NULL
 *  / \   / \     / \     / \
 * 4   5 6  7    4-->5-->6-->7-->NULL
 * 给定上面完美的二叉树（左图），您的函数应该填充每个 next 指针，以指向其下一个右节点，如右图所示。
 * 序列化的输出按层次顺序排列， * 与下一个指针相连，并带有'＃'表示每个级别的结束。
 *
 * 约束：
 * - 结点数少于 4096
 * - -1000 <= node.val <= 1000
 */
public class PopulatingNextRightPointersInEachNode {

    public static class Node extends BinaryTreeNode<Node> {
        public Node next;

        public Node() {}

        public Node(int val) {
            super(val);
        }

        public Node(int val, Node left, Node right, Node next) {
            super(val, left, right);
            this.next = next;
        }
    }

    public static Node newTree(Integer... vals) {
        return TreeNode.newTree(Node.class, vals);
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean nextFilled(Node root) {
        if (root == null || (root.left == null && root.right == null))
            return true;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                if (i != size - 1 && node.next != queue.peek())
                    return false;
                else if (i == size - 1 && node.next != null)
                    return false;

                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
        }

        return true;
    }

    static void test(Function<Node, Node> method) {
        Node root = newTree(1, 2, 3, 4, 5, 6, 7);
        assertTrue(nextFilled(method.apply(root)));

        root = newTree(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertTrue(nextFilled(method.apply(root)));
    }

    public Node connect(Node root) {
        if (root == null || root.left == null)
            return root;
        connect(root.left, root.right);

        return root;
    }

    private void connect(Node left, Node right) {
        if (left != null) {
            left.next = right;
            connect(left.left, left.right);
            connect(left.right, right.left);
            connect(right.left, right.right);
        }
    }

    @Test
    public void testConnect() {
        test(this::connect);
    }


    /**
     * 迭代方法，利用 next 指针
     */
    public Node iterateMethod(Node root) {
        if (root == null || root.left == null)
            return root;

        Node pRoot = root;
        while (pRoot.left != null) {
            Node p = pRoot;
            for (;;) {
                p.left.next = p.right;
                if (p.next != null) {
                    p.right.next = p.next.left;
                    p = p.next;
                } else
                    break;
            }
            pRoot = pRoot.left;
        }

        return root;
    }

    @Test
    public void testIterateMethod() {
        test(this::iterateMethod);
    }
}
