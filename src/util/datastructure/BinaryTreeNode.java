package util.datastructure;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Queue;

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

    public static <T extends BinaryTreeNode<T>> boolean equals(T t1, T t2) {
        if (t1 == null && t2 == null)
            return true;
        if (t1 == null || t2 == null)
            return false;

        return t1.val == t2.val
                && equals(t1.left, t2.left)
                && equals(t1.right, t2.right);
    }
}
