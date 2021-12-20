package training.binarytree;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 429. N 叉树的层序遍历: https://leetcode-cn.com/problems/n-ary-tree-level-order-traversal/
 *
 * 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）。
 * 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔（参见示例）。
 *
 * 例 1：
 * 输入：root = [1,null,3,2,4,null,5,6]
 * 输出：[[1],[3,2,4],[5,6]]
 *
 * 例 2：
 * 输入：root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 * 输出：[[1],[2,3,4,5],[6,7,8,9,10],[11,12,13],[14]]
 *
 * 说明：
 * - 树的高度不会超过 1000
 * - 树的节点总数在 [0, 10^4] 之间
 */
public class E429_Medium_NaryTreeLevelOrderTraversal {

    public static void test(Function<Node, List<List<Integer>>> method) {
        assertEquals(asList(singletonList(1), asList(3,2,4), asList(5,6)),
                method.apply(Node.newTree(asList(1,null,3,2,4,null,5,6))));
        assertEquals(asList(singletonList(1), asList(2,3,4,5), asList(6,7,8,9,10), asList(11,12,13), singletonList(14)),
                method.apply(Node.newTree(asList(1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14))));
    }

    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
            this.children = new ArrayList<>();
        }

        public Node(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }

        public static Node newTree(List<Integer> vals) {
            if (vals.isEmpty()) {
                return null;
            }

            Node root = new Node(vals.get(0)), parent = root;
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            for (int i = 1; i < vals.size(); i++) {
                Integer val = vals.get(i);
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
    }


    /**
     * LeetCode 耗时：2 ms - 90.65%
     *          内存消耗：39.2 MB - 68.74%
     */
    public List<List<Integer>> levelOrder(Node root) {
        if (root == null) {
            return Collections.emptyList();
        }

        Queue<Node> queue = new LinkedList<>();
        List<List<Integer>> result = new ArrayList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> floor = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Node node = queue.remove();
                floor.add(node.val);
                queue.addAll(node.children);
            }
            result.add(floor);
        }

        return result;
    }

    @Test
    public void testLevelOrder() {
        test(this::levelOrder);
    }
}
