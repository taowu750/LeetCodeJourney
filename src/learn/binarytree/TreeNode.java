package learn.binarytree;

import java.util.LinkedList;
import java.util.Queue;

public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {
    }

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }


    @SuppressWarnings("ConstantConditions")
    public static TreeNode newTree(Integer... vals) {
        if (vals.length == 0)
            return null;

        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode root = new TreeNode(vals[0]);
        queue.offer(root);
        for (int i = 1, childCnt = 0; i < vals.length;) {
            if (childCnt == 0) {
                queue.peek().left = vals[i] != null ? new TreeNode(vals[i]) : null;
                childCnt++;
                i++;
            } else if (childCnt == 1) {
                queue.peek().right = vals[i] != null ? new TreeNode(vals[i]) : null;
                childCnt++;
                i++;
            } else {
                childCnt = 0;
                TreeNode lastParent = queue.poll();
                if (lastParent.left != null)
                    queue.offer(lastParent.left);
                if (lastParent.right != null)
                    queue.offer(lastParent.right);
            }
        }

        return root;
    }

    public static int height(TreeNode root) {
        if (root == null || (root.left == null && root.right == null))
            return 0;
        else {
            return Math.max(height(root.left), height(root.right)) + 1;
        }
    }

    public static boolean equals(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null)
            return true;
        if (t1 == null || t2 == null)
            return false;

        return t1.val == t2.val
                && equals(t1.left, t2.left)
                && equals(t1.right, t2.right);
    }
}
