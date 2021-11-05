package training.graph;

import org.junit.jupiter.api.Test;
import util.datastructure.function.TriFunction;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 815. 公交路线: https://leetcode-cn.com/problems/bus-routes/
 *
 * 给你一个数组 routes ，表示一系列公交线路，其中每个 routes[i] 表示一条公交线路，第 i 辆公交车将会在上面循环行驶。
 *
 * 例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ...
 * 这样的车站路线行驶。
 *
 * 现在从 source 车站出发（初始时不在公交车上），要前往 target 车站。 期间仅可乘坐公交车。
 *
 * 求出「最少乘坐的公交车数量」。如果不可能到达终点车站，返回 -1 。
 *
 * 例 1：
 * 输入：routes = [[1,2,7],[3,6,7]], source = 1, target = 6
 * 输出：2
 * 解释：最优策略是先乘坐第一辆公交车到达车站 7 , 然后换乘第二辆公交车到车站 6 。
 *
 * 例 2：
 * 输入：routes = [[7,12],[4,5,15],[6],[15,19],[9,12,13]], source = 15, target = 12
 * 输出：-1
 *
 * 说明：
 * - 1 <= routes.length <= 500.
 * - 1 <= routes[i].length <= 10^5
 * - routes[i] 中的所有值 互不相同
 * - sum(routes[i].length) <= 10^5
 * - 0 <= routes[i][j] < 10^6
 * - 0 <= source, target < 10^6
 */
public class E815_Hard_BusRoutes {

    public static void test(TriFunction<int[][], Integer, Integer, Integer> method) {
        assertEquals(2, method.apply(new int[][]{{1,2,7}, {3,6,7}}, 1, 6));
        assertEquals(-1, method.apply(new int[][]{{7,12}, {4,5,15}, {6}, {15,19}, {9,12,13}}, 1, 6));
    }

    private int result = Integer.MAX_VALUE;

    /**
     * 超出时间限制。
     */
    public int numBusesToDestination(int[][] routes, int source, int target) {
        // 站点的有向图。两个站点之间可能有多个公交车经过
        Map<Integer, Map<Integer, List<Integer>>> graph = new HashMap<>();
        for (int bus = 0; bus < routes.length; bus++) {
            int start = routes[bus][0];
            for (int i = 1; i < routes[bus].length; i++) {
                int end = routes[bus][i];
                graph.computeIfAbsent(start, k -> new HashMap<>())
                        .computeIfAbsent(end, k -> new ArrayList<>())
                        .add(bus);
                start = end;
            }
            // 因为是循环路线，首尾相连
            if (routes[bus].length > 1) {
                graph.computeIfAbsent(start, k -> new HashMap<>())
                        .computeIfAbsent(routes[bus][0], k -> new ArrayList<>())
                        .add(bus);
            }
        }

        result = Integer.MAX_VALUE;
        dfs(graph, new HashSet<>(), source, target, -1, 0);

        return result == Integer.MAX_VALUE ? -1 : result;
    }

    private void dfs(Map<Integer, Map<Integer, List<Integer>>> graph, Set<Integer> visited,
                     int src, int dst, int lastBus, int busCnt) {
        if (src == dst) {
            if (busCnt < result) {
                result = busCnt;
            }
            return;
        }

        visited.add(src);
        for (Map.Entry<Integer, List<Integer>> endWithBus : graph.getOrDefault(src, Collections.emptyMap()).entrySet()) {
            if (visited.contains(endWithBus.getKey())) {
                continue;
            }
            for (int bus : endWithBus.getValue()) {
                int nextBusCnt = busCnt + (lastBus == bus ? 0 : 1);
                dfs(graph, visited, endWithBus.getKey(), dst, bus, nextBusCnt);
            }
        }
        visited.remove(src);
    }

    @Test
    public void testNumBusesToDestination() {
        test(this::numBusesToDestination);
    }


    /**
     * 由于求解的目标是最少乘坐的公交车数量，对于同一辆公交车，乘客可以在其路线中的任意车站间无代价地移动，
     * 于是我们可以把公交路线当作点。如果两条公交路线有相同车站，则可以在这两条路线间换乘公交车，那么这两条
     * 公交路线之间可视作有一条长度为 1 的边。这样建出的图包含的点数即为公交路线的数量，记作 n。
     *
     * 完成了建图后，我们需要先明确新的图的起点和终点，然后使用广度优先搜索，计算出的起点和终点的最短路径，从而得到最少换乘次数。
     *
     * 注意到原本的起点车站和终点车站可能同时位于多条公交路线上，因此在新图上可能有多个起点和终点。对于这种情况，
     * 我们初始可以同时入队多个点，并在广度优先搜索结束后检查到各个终点的最短路径，取其最小值才是最少换乘次数。
     *
     * 实际建图时，我们遍历所有公交路线，记录每一个车站属于哪些公交路线。然后我们遍历每一个车站，
     * 如果有多条公交路线经过该点，则在这些公交路线之间连边。
     *
     * 参见：
     * https://leetcode-cn.com/problems/bus-routes/solution/gong-jiao-lu-xian-by-leetcode-solution-yifz/
     *
     * LeetCode 耗时：37 ms - 63.32%
     *          内存消耗：64.7 MB - 43.77%
     */
    public int bfsMethod(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }

        // 路线之间的图
        List<Integer>[] graph = new List[routes.length];
        for (int i = 0; i < routes.length; i++) {
            graph[i] = new ArrayList<>();
        }

        // 包含 source 的路线
        LinkedList<Integer> start = new LinkedList<>();
        // 包含 dst 的路线
        Set<Integer> end = new HashSet<>();
        // 每个站点和包含站点的路线
        Map<Integer, List<Integer>> station2bus = new HashMap<>();
        for (int bus = 0; bus < routes.length; bus++) {
            for (int station : routes[bus]) {
                station2bus.computeIfAbsent(station, k -> new ArrayList<>()).add(bus);
                if (station == source) {
                    start.add(bus);
                } else if (station == target) {
                    end.add(bus);
                }
            }
        }
        if (start.isEmpty() || end.isEmpty()) {
            return -1;
        }
        station2bus.forEach((station, buses) -> {
            for (Integer srcBus : buses) {
                for (Integer dstBus : buses) {
                    if (srcBus.equals(dstBus)) {
                        continue;
                    }
                    graph[srcBus].add(dstBus);
                }
            }
        });

        int level = 1;
        boolean[] visited = new boolean[routes.length];
        while (!start.isEmpty()) {
            int size = start.size();
            for (int i = 0; i < size; i++) {
                int bus = start.removeFirst();
                if (end.contains(bus)) {
                    return level;
                }
                visited[bus] = true;
                for (int next : graph[bus]) {
                    if (visited[next]) {
                        continue;
                    }
                    start.addLast(next);
                }
            }
            level++;
        }

        return -1;
    }

    @Test
    public void testBfsMethod() {
        test(this::bfsMethod);
    }

    /**
     * 双向 BFS。
     *
     * LeetCode 耗时：37 ms - 63.32%
     *          内存消耗：61 MB - 65.06%
     */
    public int bbfsMethod(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }

        // 路线之间的图
        List<Integer>[] graph = new List[routes.length];
        for (int i = 0; i < routes.length; i++) {
            graph[i] = new ArrayList<>();
        }

        // 包含 source 的路线
        Set<Integer> start = new HashSet<>();
        // 包含 dst 的路线
        Set<Integer> end = new HashSet<>();
        // 每个站点和包含站点的路线
        Map<Integer, List<Integer>> station2bus = new HashMap<>();
        for (int bus = 0; bus < routes.length; bus++) {
            for (int station : routes[bus]) {
                station2bus.computeIfAbsent(station, k -> new ArrayList<>()).add(bus);
                if (station == source) {
                    start.add(bus);
                } else if (station == target) {
                    end.add(bus);
                }
            }
        }
        if (start.isEmpty() || end.isEmpty()) {
            return -1;
        }

        station2bus.forEach((station, buses) -> {
            for (Integer srcBus : buses) {
                for (Integer dstBus : buses) {
                    if (srcBus.equals(dstBus)) {
                        continue;
                    }
                    graph[srcBus].add(dstBus);
                }
            }
        });

        int level = 1;
        boolean[] visited = new boolean[routes.length];
        while (!start.isEmpty()) {
            if (start.size() > end.size()) {
                Set<Integer> tmp = start;
                start = end;
                end = tmp;
            }
            Set<Integer> nextStart = new HashSet<>();
            for (int bus : start) {
                visited[bus] = true;
                if (end.contains(bus)) {
                    return level;
                }
                for (int next : graph[bus]) {
                    if (visited[next]) {
                        continue;
                    }
                    nextStart.add(next);
                }
            }
            start = nextStart;
            level++;
        }

        return -1;
    }

    @Test
    public void testBbfsMethod() {
        test(this::bbfsMethod);
    }
}
