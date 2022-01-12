package training.graph;

import org.junit.jupiter.api.Test;
import util.datastructure.UF;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 785. 判断二分图: https://leetcode-cn.com/problems/is-graph-bipartite/
 *
 * 存在一个「无向图」，图中有 n 个节点。其中每个节点都有一个介于 0 到 n - 1 之间的唯一编号。
 * 给你一个二维数组 graph ，其中 graph[u] 是一个节点数组，由节点 u 的邻接节点组成。
 * 形式上，对于 graph[u] 中的每个 v，都存在一条位于节点 u 和节点 v 之间的无向边。该无向图同时具有以下属性：
 * - 不存在自环（graph[u] 不包含 u）。
 * - 不存在平行边（graph[u] 不包含重复值）。
 * - 如果 v 在 graph[u] 内，那么 u 也应该在 graph[v] 内（该图是无向图）
 * - 这个图可能不是连通图，也就是说两个节点 u 和 v 之间可能不存在一条连通彼此的路径。
 *
 * 二分图定义：如果能将一个图的节点集合分割成两个独立的子集 A 和 B，并使图中的每一条边的两个节点一个来自 A 集合，
 * 一个来自 B 集合，就将这个图称为二分图。其中，集合 A、B 内部节点之间没有边。
 *
 * 如果图是二分图，返回 true ；否则，返回 false 。
 *
 * 例 1：
 * 输入：graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
 * 输出：false
 * 解释：不能将节点分割成两个独立的子集，以使每条边都连通一个子集中的一个节点与另一个子集中的一个节点。
 *
 * 例 2：
 * 输入：graph = [[1,3],[0,2],[1,3],[0,2]]
 * 输出：true
 * 解释：可以将节点分成两组: {0, 2} 和 {1, 3} 。
 *
 * 说明：
 * - graph.length == n
 * - 1 <= n <= 100
 * - 0 <= graph[u].length < n
 * - 0 <= graph[u][i] <= n - 1
 * - graph[u] 不会包含 u
 * - graph[u] 的所有值「互不相同」
 * - 如果 graph[u] 包含 v，那么 graph[v] 也会包含 u
 */
public class E785_Medium_IsGraphBipartite {

    public static void test(Predicate<int[][]> method) {
        assertFalse(method.test(new int[][]{{1,2,3}, {0,2}, {0,1,3}, {0,2}}));
        assertTrue(method.test(new int[][]{{1,3}, {0,2}, {1,3}, {0,2}}));
    }

    /**
     * 染色法判定二分图，参见：https://blog.csdn.net/li13168690086/article/details/81506044
     *
     * 如果某个图为二分图，那么它至少有两个顶点，且其所有环的长度均为偶数，任何无环的的图均是二分图。
     * - 无环的情况下，那么肯定可以给每个相邻节点染上不同的颜色
     * - 有环的情况下，环的长度均为偶数，从起点到终点也可以给每个相邻节点染上不同的颜色
     *
     * LeetCode 耗时：0 ms - 100.00%
     *          内存消耗：39.1 MB - 15.06%
     */
    public boolean isBipartite(int[][] graph) {
        isBipartite = true;
        boolean[] visited = new boolean[graph.length];
        boolean[] color = new boolean[graph.length];

        for (int v = 0; v < graph.length; v++) {
            if (!visited[v]) {
                dfs(graph, visited, color, v);
                if (!isBipartite) {
                    break;
                }
            }
        }

        return isBipartite;
    }

    private boolean isBipartite;

    private void dfs(int[][] graph, boolean[] visited, boolean[] color, int v) {
        if (!isBipartite) {
            return;
        }
        visited[v] = true;
        for (int w : graph[v]) {
            if (!visited[w]) {
                color[w] = !color[v];
                dfs(graph, visited, color, w);
            } else if (color[w] == color[v]) {
                isBipartite = false;
                return;
            }
        }
    }

    @Test
    public void testIsBipartite() {
        test(this::isBipartite);
    }


    /**
     * UF 方法，参见：
     * https://leetcode-cn.com/problems/is-graph-bipartite/solution/bfs-dfs-bing-cha-ji-san-chong-fang-fa-pan-duan-er-/
     *
     * LeetCode 耗时：2 ms - 24.50%
     *          内存消耗：39 MB - 20.63%
     */
    public boolean ufMethod(int[][] graph) {
        UF uf = new UF(graph.length);
        // 遍历每个顶点，将当前顶点的所有邻接点合并
        for (int v = 0; v < graph.length; v++) {
            for (int w : graph[v]) {
                // 若某个邻接点与当前顶点已经在一个集合中了，说明不是二分图，返回 false
                if (uf.connected(v, w)) {
                    return false;
                }
                // 注意，是合并所有临界点
                uf.union(graph[v][0], w);
            }
        }

        return true;
    }

    @Test
    public void testUfMethod() {
        test(this::ufMethod);
    }
}
