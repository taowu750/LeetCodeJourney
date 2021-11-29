package util.datastructure;

import training.binarytree.TreeNode;

import java.lang.reflect.Constructor;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinaryTreeNode<T extends BinaryTreeNode<T>> {

    public int val;
    public T left;
    public T right;

    public BinaryTreeNode() {
    }

    public BinaryTreeNode(int val) {
        this.val = val;
    }

    public BinaryTreeNode(int val, T left, T right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }


    @SuppressWarnings("ConstantConditions")
    public static <T extends BinaryTreeNode<T>> T newTree(Class<T> clazz, Integer... vals) {
        if (vals.length == 0)
            return null;

        try {
            Constructor<T> constructor = clazz.getConstructor(int.class);
            constructor.setAccessible(true);
            Queue<T> queue = new LinkedList<>();
            T root = constructor.newInstance(vals[0]);
            queue.offer(root);
            for (int i = 1, childCnt = 0; i < vals.length;) {
                if (childCnt == 0) {
                    queue.peek().left = vals[i] != null ? constructor.newInstance(vals[i]) : null;
                    childCnt++;
                    i++;
                } else if (childCnt == 1) {
                    queue.peek().right = vals[i] != null ? constructor.newInstance(vals[i]) : null;
                    childCnt++;
                    i++;
                } else {
                    childCnt = 0;
                    T lastParent = queue.poll();
                    if (lastParent.left != null)
                        queue.offer(lastParent.left);
                    if (lastParent.right != null)
                        queue.offer(lastParent.right);
                }
            }
            constructor.setAccessible(false);

            return root;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends BinaryTreeNode<T>> int height(T root) {
        if (root == null || (root.left == null && root.right == null))
            return 0;
        else {
            return Math.max(height(root.left), height(root.right)) + 1;
        }
    }

    public static <T extends BinaryTreeNode<T>> T find(T root, int val) {
        if (root == null || root.val == val)
            return root;
        T result = find(root.left, val);
        if (result != null)
            return result;
        return find(root.right, val);
    }

    public static <T extends BinaryTreeNode<T>> boolean equals(T t1, T t2) {
        if (t1 == null && t2 == null)
            return true;
        if (t1 == null || t2 == null)
            return false;

        return t1.val == t2.val
                && equals(t1.left, t2.left)
                && equals(t1.right, t2.right);
    }

    public static <T extends BinaryTreeNode<T>> List<Integer> preorder(T root) {
        List<Integer> order = new ArrayList<>();
        preorder(order, root);
        return order;
    }

    private static <T extends BinaryTreeNode<T>> void preorder(List<Integer> order, T root) {
        if (root != null) {
            order.add(root.val);
            preorder(order, root.left);
            preorder(order, root.right);
        }
    }

    public static <T extends BinaryTreeNode<T>> boolean isValid(T root) {
        Deque<T> stack = new LinkedList<>();
        T pre = null;
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (pre != null && root.val <= pre.val)
                return false;
            pre = root;
            root = root.right;
        }

        return true;
    }

    public static <T extends BinaryTreeNode<T>> boolean contentEquals(T t1, T t2) {
        return new HashSet<>(preorder(t1)).equals(new HashSet<>(preorder(t2)));
    }

    private static boolean balanced;

    public static <T extends BinaryTreeNode<T>> boolean isBalanced(T root) {
        balanced = true;
        heightCheck(root);
        return balanced;
    }

    private static <T extends BinaryTreeNode<T>> int heightCheck(T root) {
        if (root == null)
            return 0;
        if (balanced) {
            int leftHeight = heightCheck(root.left);
            int rightHeight = heightCheck(root.right);
            if (Math.abs(leftHeight - rightHeight) > 1)
                balanced = false;
            return Math.max(leftHeight, rightHeight) + 1;
        }

        return 0;
    }

    public static void assertBST(TreeNode root) {
        assertTrue(isBalanced(root), "the tree is not balanced");
        assertTrue(isValid(root), "the tree is invalid search tree");
    }
}
