package training.narytree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * N 叉树节点
 */
public class Node {
    public int val;
    public List<Node> children;

    public Node() {
        this.children = new ArrayList<>();
    }

    public Node(int val) {
        this.val = val;
        this.children = new ArrayList<>();
    }

    public Node(int val, List<Node> children) {
        this.val = val;
        this.children = children;
    }

    public static Node newTree(Integer... vals) {
        if (vals.length == 0) {
            return null;
        }

        Node root = new Node(vals[0]), parent = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        for (int i = 1; i < vals.length; i++) {
            Integer val = vals[i];
            if (val == null) {
                parent = queue.remove();
            } else {
                Node node = new Node(val);
                parent.children.add(node);
                queue.add(node);
            }
        }

        return root;
    }

    public static boolean equals(Node t1, Node t2) {
        if (t1 == null && t2 == null) {
            return true;
        } else if (t1 == null || t2 == null || t1.val != t2.val || t1.children.size() != t2.children.size()) {
            return false;
        }

        for (int i = 0; i < t1.children.size(); i++) {
            if (!equals(t1.children.get(i), t2.children.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }

        return equals(this, (Node) obj);
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
