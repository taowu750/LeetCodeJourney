package learn.binarytree;

import learn.binarytree.PopulatingNextRightPointersInEachNode.Node;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import static learn.binarytree.PopulatingNextRightPointersInEachNode.newTree;
import static learn.binarytree.PopulatingNextRightPointersInEachNode.nextFilled;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 类似于{@link PopulatingNextRightPointersInEachNode}，当这次是任意二叉树。
 *
 * 例 1：
 * Input: root = [1,2,3,4,5,null,7]
 * Output: [1,#,2,3,#,4,5,7,#]
 * Explanation:
 *      1            1-->NULL
 *    /  \         /   \
 *   2   3        2---->3-->NULL
 *  / \   \     /  \     \
 * 4  5    7   4-->5---->7-->NULL
 *
 * 约束：
 * - 结点数小于 6000
 * - -100 <= node.val <= 100
 */
public class PopulatingNextRightPointersInEachNodeII {

    public static void printNextRightTree(Node root) {
        if (root == null) {
            System.out.println("null");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.remove();
                System.out.print(node.val + ":" + (node.next != null ? node.next.val : "") + " ");
                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
            }
            System.out.println();
        }
    }

    static void test(Function<Node, Node> method) {
        Node root = newTree(1, 2, 3, 4, 5, null, 7);
        root = method.apply(root);
        printNextRightTree(root);
        assertTrue(TreeNode.equals(root, newTree(1, 2, 3, 4, 5, null, 7)));
        assertTrue(nextFilled(root));
        System.out.println();

        root = newTree(1, null, 2, 3, 4, 5, null, null, 6);
        root = method.apply(root);
        printNextRightTree(root);
        assertTrue(TreeNode.equals(root, newTree(1, null, 2, 3, 4, 5, null, null, 6)));
        assertTrue(nextFilled(root));
        System.out.println();

        /*
                 1
                / \
               2   3
              / \   \
             4   5   6
            /         \
           7           8
         */
        root = newTree(1, 2, 3, 4, 5, null, 6, 7, null, null, null, null, 8);
        root = method.apply(root);
        printNextRightTree(root);
        assertTrue(TreeNode.equals(root, newTree(1, 2, 3, 4, 5, null, 6, 7, null, null, null, null, 8)));
        assertTrue(nextFilled(root));
        System.out.println();

        /*
                    0
                   / \
                  2   4
                 /   / \
                1   3   4
               / \   \   \
              5   1   6   8
         */
        root = newTree(0, 2, 4, 1, null, 3, -1, 5, 1, null, 6, null, 8);
        root = method.apply(root);
        printNextRightTree(root);
        assertTrue(TreeNode.equals(root, newTree(0, 2, 4, 1, null, 3, -1, 5, 1, null, 6, null, 8)));
        assertTrue(nextFilled(root));
        System.out.println();

        /*
                   -1
                  /  \
                 -7    9
                     /   \
                    -1    -7
                      \   /
                       8 -9
         */
        root = newTree(-1, -7, 9, null, null, -1, -7, null, 8, -9);
        root = method.apply(root);
        printNextRightTree(root);
        assertTrue(TreeNode.equals(root, newTree(-1, -7, 9, null, null, -1, -7, null, 8, -9)));
        assertTrue(nextFilled(root));
        System.out.println();
    }


    public Node levelIterMethod(Node root) {
        if (root == null)
            return null;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            Node head = null;
            for (int i = 0; i < size; i++) {
                Node next = queue.remove();
                if (head == null)
                    head = next;
                else {
                    head.next = next;
                    head = next;
                }
                if (next.left != null)
                    queue.add(next.left);
                if (next.right != null)
                    queue.add(next.right);
            }
        }

        return root;
    }

    @Test
    public void testLevelIterMethod() {
        test(this::levelIterMethod);
    }


    public Node connect(Node root) {
        if (root == null || (root.left == null && root.right == null))
            return root;

        Node pRoot = root;
        while (pRoot != null) {
            Node p = pRoot, last = null;
            pRoot = null;
            do {
                if (p.left != null && p.right != null) {
                    p.left.next = p.right;
                    if (last != null)
                        last.next = p.left;
                    last = p.right;
                    if (pRoot == null)
                        pRoot = p;
                } else {
                    Node child = p.left != null ? p.left : p.right;
                    if (last != null)
                        last.next = child;
                    if (child != null) {
                        last = child;
                        if (pRoot == null)
                            pRoot = p;
                    }
                }
                p = p.next;
            } while (p != null);
            if (pRoot != null)
                pRoot = pRoot.left != null ? pRoot.left : pRoot.right;
        }

        return root;
    }

    @Test
    public void testConnect() {
        test(this::connect);
    }


    public Node betterIterMethod(Node root) {
        if (root == null)
            return null;

        for (Node p = root, next, last; p != null; p = next) {
            next = null;
            last = null;
            do {
                Node left = p.left != null ? p.left : p.right;
                Node right = p.right != null ? p.right : p.left;
                if (p.left != null)
                    p.left.next = p.right;
                if (last != null)
                    last.next = left;
                if (right != null)
                    last = right;
                if (next == null)
                    next = left;
                p = p.next;
            } while (p != null);
        }

        return root;
    }

    @Test
    public void testBetterIterMethod() {
        test(this::betterIterMethod);
    }


    public Node recursiveMethod(Node root) {
        if (root == null)
            return null;
        Node head = new Node(), p = head, pRoot = root;
        while (pRoot != null) {
            if (pRoot.left != null) {
                p.next = pRoot.left;
                p = p.next;
            }
            if (pRoot.right != null) {
                p.next = pRoot.right;
                p = p.next;
            }
            pRoot = pRoot.next;
        }
        recursiveMethod(head.next);

        return root;
    }

    @Test
    public void testRecursiveMethod() {
        test(this::recursiveMethod);
    }
}
