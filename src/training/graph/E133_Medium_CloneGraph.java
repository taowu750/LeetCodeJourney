package training.graph;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定一个连通无向图的节点的引用，返回图的深层拷贝。
 * 图中的每个节点都包含一个 val（int）和其邻居的列表（List [Node]）。
 * <p>
 * 为了简单起见，每个节点的值都与节点的索引（从 1 开始）相同。例如，第一个节点的 val = 1，
 * 第二个节点的 val = 2，依此类推。该图在测试用例中使用邻接表表示。
 * <p>
 * 邻接表是用于表示有限图的无序列表的集合。每个列表都描述了图中节点的邻居集。
 * 给定的节点将始终是 val = 1 的第一个节点。您必须返回给定节点的副本作为对克隆图的引用。
 * <p>
 * 例 1：
 * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 * Output: [[2,4],[1,3],[2,4],[1,3]]
 * Explanation:
 * 1----2
 * |    |
 * |    |
 * 4----3
 * 结点 1 的邻接表是 [2,4]
 * 结点 2 的邻接表是 [1,3]
 * 结点 3 的邻接表是 [2,4]
 * 结点 4 的邻接表是 [1,3]
 * <p>
 * 例 2：
 * Input: adjList = [[]]
 * Output: [[]]
 * <p>
 * 例 3：
 * Input: adjList = []
 * Output: []
 * Explanation: 空的图，没有结点
 * <p>
 * Example 4:
 * Input: adjList = [[2],[1]]
 * Output: [[2],[1]]
 * Explanation:
 * 1----2
 * <p>
 * 约束：
 * - 1 <= Node.val <= 100
 * - 每个结点的值不相同
 * - 结点个数不超过 100
 * - 图中没有重复边或自环
 * - 图是连通图
 */
public class E133_Medium_CloneGraph {

    public static class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }

        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<>();
        }

        public Node(int _val, List<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    public static Node newGraph(List<List<Integer>> adjList) {
        Map<Integer, Node> nodes = new HashMap<>();
        int i = 1;
        for (List<Integer> adjs : adjList) {
            if (!nodes.containsKey(i))
                nodes.put(i, new Node(i));
            for (Integer val : adjs) {
                if (!nodes.containsKey(val))
                    nodes.put(val, new Node(val));
            }
            i++;
        }
        i = 1;
        for (List<Integer> adjs : adjList) {
            Node node = nodes.get(i);
            for (Integer val : adjs) {
                node.neighbors.add(nodes.get(val));
            }
            i++;
        }

        return nodes.get(1);
    }

    public static void printGraph(Node node) {
        if (node == null)
            System.out.println("null");
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        queue.add(node);
        visited.add(node);
        while (!queue.isEmpty()) {
            node = queue.remove();
            System.out.printf("(%d): ", node.val);
            for (Node neighbor : node.neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
                System.out.printf("%d ", neighbor.val);
            }
            System.out.println();
        }
    }

    public static boolean cloneEquals(Node node, Node other) {
        return cloneEquals(node, other, new HashSet<>());
    }

    private static boolean cloneEquals(Node node, Node other, Set<Integer> visited) {
        if (node == null && other == null)
            return true;
        if (node == null || other == null || node == other)
            return false;
        if (visited.contains(node.val))
            return true;
        if (node.val != other.val)
            return false;
        visited.add(node.val);
        if (node.neighbors.size() != other.neighbors.size())
            return false;

        for (int i = 0; i < node.neighbors.size(); i++) {
            if (!cloneEquals(node.neighbors.get(i),
                    other.neighbors.get(i), visited))
                return false;
        }

        return true;
    }

    @Test
    public void testGraph() {
        Node graph = newGraph(asList(asList(2, 4), asList(1, 3),
                asList(2, 4), asList(1, 3)));
        printGraph(graph);
    }


    static void test(Function<Node, Node> method) {
        Node graph = newGraph(asList(asList(2, 4), asList(1, 3),
                asList(2, 4), asList(1, 3)));
        assertTrue(cloneEquals(method.apply(graph), graph));

        graph = newGraph(emptyList());
        assertTrue(cloneEquals(method.apply(graph), graph));

        assertTrue(cloneEquals(method.apply(null), null));

        graph = newGraph(asList(singletonList(2), singletonList(1)));
        assertTrue(cloneEquals(method.apply(graph), graph));
    }

    /**
     * DFS 方法。
     *
     * LeetCode 耗时：24ms - 99.13%
     */
    public Node cloneGraph(Node node) {
        if (node == null)
            return null;

        return cloneGraph(node, new HashMap<>());
    }

    private Node cloneGraph(Node node, Map<Integer, Node> visited) {
        Node n = visited.get(node.val);
        if (n == null) {
            Node newNode = new Node(node.val);
            visited.put(node.val, newNode);
            for (Node neighbor : node.neighbors)
                newNode.neighbors.add(cloneGraph(neighbor, visited));

            return newNode;
        } else {
            return n;
        }
    }

    @Test
    public void testCloneGraph() {
        test(this::cloneGraph);
    }
}
