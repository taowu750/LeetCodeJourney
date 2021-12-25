package training.graph;

import org.junit.jupiter.api.Test;
import util.datastructure.function.ToIntTriFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 743. 网络延迟时间: https://leetcode-cn.com/problems/network-delay-time/
 *
 * 有 n 个网络节点，标记为 1 到 n。
 *
 * 给你一个列表 times，表示信号经过有向边的传递时间。times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，
 * wi 是一个信号从源节点传递到目标节点的时间。
 *
 * 现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1 。
 *
 * 例 1：
 * 输入：times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 * 输出：2
 *
 * 例 2：
 * 输入：times = [[1,2,1]], n = 2, k = 1
 * 输出：1
 *
 * 例 3：
 * 输入：times = [[1,2,1]], n = 2, k = 2
 * 输出：-1
 *
 * 说明：
 * - 1 <= k <= n <= 100
 * - 1 <= times.length <= 6000
 * - times[i].length == 3
 * - 1 <= ui, vi <= n
 * - ui != vi
 * - 0 <= wi <= 100
 * - 所有 (ui, vi) 对都互不相同（即，不含重复边）
 */
public class E743_Medium_NetworkDelayTime {

    public static void test(ToIntTriFunction<int[][], Integer, Integer> method) {
        assertEquals(2, method.applyAsInt(new int[][]{
                {2,1,1},
                {2,3,1},
                {3,4,1}}, 4, 2));
        assertEquals(1, method.applyAsInt(new int[][]{{1,2,1}}, 2, 1));
        assertEquals(-1, method.applyAsInt(new int[][]{{1,2,1}}, 2, 2));
    }

    /**
     * LeetCode 耗时：336 ms - 5.30%
     *          内存消耗：42.1 MB - 65.67%
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] graph = new List[n + 1];
        for (int[] edge : times) {
            if (graph[edge[0]] == null) {
                graph[edge[0]] = new ArrayList<>(8);
            }
            graph[edge[0]].add(edge);
        }

        int[] dist = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        dist[k] = 0;
        dfs(graph, dist, k);

        int result = 0;
        for (int c : dist) {
            result = Math.max(result, c);
        }

        return result != Integer.MAX_VALUE ? result : -1;
    }

    private void dfs(List<int[]>[] graph, int[] cost, int src) {
        if (graph[src] == null) {
            return;
        }
        for (int[] edge: graph[src]) {
            int dst = edge[1], weight = edge[2];
            if (cost[dst] > cost[src] + weight) {
                cost[dst] = cost[src] + weight;
                dfs(graph, cost, dst);
            }
        }
    }

    @Test
    public void testNetworkDelayTime() {
        test(this::networkDelayTime);
    }


    /**
     * Dijkstra 最短路径方法，参见：
     * https://leetcode-cn.com/problems/network-delay-time/solution/wang-luo-yan-chi-shi-jian-by-leetcode-so-6phc/
     *
     * 将所有节点分成两类：已确定从起点到当前点的最短路长度的节点，以及未确定从起点到当前点的最短路长度的节点
     * （下面简称「未确定节点」和「已确定节点」）。
     *
     * 每次从「未确定节点」中取一个与起点距离最短的点，将它归类为「已确定节点」，并用它「更新」从起点到其他所有
     * 「未确定节点」的距离。直到所有点都被归类为「已确定节点」。
     *
     * 用节点 A「更新」节点 B 的意思是，用起点到节点 A 的最短路长度加上从节点 A 到节点 B 的边的长度，
     * 去比较起点到节点 BB 的最短路长度，如果前者小于后者，就用前者更新后者。这种操作也被叫做「松弛」。
     *
     * 这里暗含的信息是：每次选择「未确定节点」时，起点到它的最短路径的长度可以被确定。
     *
     * 可以这样理解，因为我们已经用了每一个「已确定节点」更新过了当前节点，无需再次更新（因为一个点不能多次到达）。
     * 而当前节点已经是所有「未确定节点」中与起点距离最短的点，不可能被其它「未确定节点」更新。
     * 所以当前节点可以被归类为「已确定节点」。
     *
     *
     * 使用枚举的 Dijkstra 方法。
     *
     * 关于无穷大取 0x3f3f3f3f 的解释参见：https://blog.csdn.net/jiange_zh/article/details/50198097
     *
     * LeetCode 耗时：2 ms - 100.00%
     *          内存消耗：43.2 MB - 6.46%
     */
    public int dijkstraEnumMethod(int[][] times, int n, int k) {
        final int INF = 0x3f3f3f3f;
        // 因为是稠密图，所有用邻接表更好
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; ++i) {
            Arrays.fill(graph[i], INF);
        }
        for (int[] t : times) {
            int x = t[0] - 1, y = t[1] - 1;
            graph[x][y] = t[2];
        }

        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; ++i) {
            int x = -1;
            for (int y = 0; y < n; ++y) {
                if (!used[y] && (x == -1 || dist[y] < dist[x])) {
                    x = y;
                }
            }
            used[x] = true;
            for (int y = 0; y < n; ++y) {
                dist[y] = Math.min(dist[y], dist[x] + graph[x][y]);
            }
        }

        int result = 0;
        for (int c : dist) {
            result = Math.max(result, c);
        }

        return result != INF ? result : -1;
    }

    @Test
    public void testDijkstraEnumMethod() {
        test(this::dijkstraEnumMethod);
    }

    /**
     * 使用堆来代替枚举最小的「未确定节点」。
     *
     * LeetCode 耗时：7 ms - 66.01%
     *          内存消耗：42.1 MB - 63.64%
     */
    public int dijkstraHeapMethod(int[][] times, int n, int k) {
        final int INF = 0x3f3f3f3f;
        List<int[]>[] graph = new List[n];
        for (int[] edge : times) {
            if (graph[edge[0] - 1] == null) {
                graph[edge[0] - 1] = new ArrayList<>(8);
            }
            graph[edge[0] - 1].add(edge);
        }

        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        // 元素 a 是数组，其中 a[0] 是 dist，a[1] 是标号
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        pq.offer(new int[]{0, k - 1});
        while (!pq.isEmpty()) {
            int[] p = pq.poll();
            int time = p[0], x = p[1];
            // 如果后续对已经在队列中的节点的time进行了更新，那么之前队列中该节点存储的time值就失效了，需要跳过
            if (dist[x] < time || graph[x] == null) {
                continue;
            }
            // 使用邻接表后不再需要遍历所有节点
            for (int[] e : graph[x]) {
                int y = e[1] - 1, d = dist[x] + e[2];
                if (d < dist[y]) {
                    dist[y] = d;
                    // 可能节点 y 已存在于队列中，会有重复入队的可能
                    pq.offer(new int[]{d, y});
                }
            }
        }

        int result = 0;
        for (int c : dist) {
            result = Math.max(result, c);
        }

        return result != INF ? result : -1;
    }

    @Test
    public void testDijkstraHeapMethod() {
        test(this::dijkstraHeapMethod);
    }
}
