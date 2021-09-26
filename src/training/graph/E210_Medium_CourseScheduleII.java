package training.graph;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 210. 课程表 II: https://leetcode-cn.com/problems/course-schedule-ii/
 *
 * 现在你总共有 numCourses 门课需要选，记为 0 到 numCourses - 1。给你一个数组 prerequisites，
 * 其中 prerequisites[i] = [ai, bi] ，表示在选修课程 ai 前「必须」先选修 bi 。
 *
 * 例如，想要学习课程 0，你需要先完成课程 1，我们用一个匹配来表示：[0,1] 。
 *
 * 返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回「任意一种」就可以了。
 * 如果不可能完成所有课程，返回「一个空数组」。
 *
 * 例 1：
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：[0,1]
 * 解释：总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1] 。
 *
 * 例 2：
 * 输入：numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * 输出：[0,2,1,3]
 * 解释：总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
 * 因此，一个正确的课程顺序是 [0,1,2,3] 。另一个正确的排序是 [0,2,1,3] 。
 *
 * 例 3：
 * 输入：numCourses = 1, prerequisites = []
 * 输出：[0]
 *
 * 约束：
 * - 1 <= numCourses <= 2000
 * - 0 <= prerequisites.length <= numCourses * (numCourses - 1)
 * - prerequisites[i].length == 2
 * - 0 <= ai, bi < numCourses
 * - ai != bi
 * - 所有 [ai, bi] 匹配「互不相同」
 */
public class E210_Medium_CourseScheduleII {

    static void test(BiFunction<Integer, int[][], int[]> method) {
        assertArrayEquals(new int[]{0, 1}, method.apply(2, new int[][]{{1,0}}));
        assertArrayEquals(new int[]{0,2,1,3}, method.apply(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}}));
        assertArrayEquals(new int[]{0}, method.apply(1, new int[][]{}));
    }

    /**
     * 有向图中是否有环算法 + 拓扑排序。
     *
     * LeetCode 耗时：5 ms - 69.77%
     *          内存消耗：39.6 MB - 49.14%
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        //noinspection unchecked
        List<Integer>[] graph = new List[numCourses];
        for (int[] edge : prerequisites) {
            if (graph[edge[1]] == null) {
                graph[edge[1]] = new ArrayList<>();
            }
            graph[edge[1]].add(edge[0]);
        }
        for (int i = 0; i < numCourses; i++) {
            if (graph[i] == null) {
                graph[i] = Collections.emptyList();
            }
        }

        if (hasCycle(graph)) {
            return new int[0];
        }

        return topicSort(graph);
    }

    private boolean hasCycle(List<Integer>[] graph) {
        boolean[] visited = new boolean[graph.length], onStack = new boolean[graph.length];

        for (int v = 0; v < graph.length; v++) {
            if (!visited[v] && hasCycle(graph, visited, onStack, v)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycle(List<Integer>[] graph, boolean[] visited, boolean[] onStack, int v) {
        visited[v] = true;
        onStack[v] = true;
        for (int w: graph[v]) {
            if (!visited[w]) {
                if (hasCycle(graph, visited, onStack, w)) {
                    return true;
                }
            } else if (onStack[w]) {
                return true;
            }
        }
        onStack[v] = false;

        return false;
    }

    private int[] topicSort(List<Integer>[] graph) {
        boolean[] visited = new boolean[graph.length];
        Deque<Integer> deque = new ArrayDeque<>(graph.length);

        for (int v = 0; v < graph.length; v++) {
            if (!visited[v]) {
                topicSort(graph, visited, deque, v);
            }
        }

        int[] result = new int[graph.length];
        int i = 0;
        for (int v : deque) {
            result[i++] = v;
        }

        return result;
    }

    private void topicSort(List<Integer>[] graph, boolean[] visited, Deque<Integer> result, int v) {
        visited[v] = true;
        for (int w : graph[v]) {
            if (!visited[w]) {
                topicSort(graph, visited, result, w);
            }
        }
        result.addFirst(v);
    }

    @Test
    public void testFindOrder() {
        test(this::findOrder);
    }
}
