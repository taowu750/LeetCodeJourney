package training.graph;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 207. 课程表: https://leetcode-cn.com/problems/course-schedule/
 *
 * 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1 。
 *
 * 在选修某些课程之前需要一些先修课程。 先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi] ，
 * 表示如果要学习课程 ai 则必须先学习课程 bi 。
 *
 * 例如，先修课程对 [0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
 * 请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。
 *
 * 例 1：
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：true
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。
 *
 * 例 2：
 * 输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
 * 输出：false
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要先完成 课程 0 ；并且学习课程 0 之前，你还应先完成课程 1 。这是不可能的。
 *
 * 约束：
 * - 1 <= numCourses <= 105
 * - 0 <= prerequisites.length <= 5000
 * - prerequisites[i].length == 2
 * - 0 <= ai, bi < numCourses
 * - prerequisites[i] 中的所有课程对互不相同
 */
public class E207_Medium_CourseSchedule {

    static void test(BiPredicate<Integer, int[][]> method) {
        assertTrue(method.test(2, new int[][]{{1,0}}));
        assertFalse(method.test(2, new int[][]{{1,0},{0,1}}));
        assertFalse(method.test(20, new int[][]{{0, 10}, {3, 18},{5,5},{6,11},{11,14},{13,1},{15,1},{17,4}}));
    }

    private boolean hasCircle;

    /**
     * 参见：https://blog.csdn.net/KID_LWC/article/details/82391702
     *
     * LeetCode 耗时：4 ms - 78%
     *          内存消耗：39.4MB - 17%
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites.length <= 1) {
            return true;
        }

        Map<Integer, List<Integer>> graph = new HashMap<>((int) (prerequisites.length / 0.75) + 1);
        for (int[] edge: prerequisites) {
            graph.computeIfAbsent(edge[1], to -> new ArrayList<>(8)).add(edge[0]);
        }
        boolean[] visited = new boolean[numCourses], onStack = new boolean[numCourses];

        hasCircle = false;
        for (int v: graph.keySet()) {
            if (!visited[v]) {
                dfs(graph, visited, onStack, v);
                if (hasCircle) {
                    break;
                }
            }
        }

        return !hasCircle;
    }

    private void dfs(Map<Integer, List<Integer>> graph, boolean[] visited, boolean[] onStack, int v) {
        if (hasCircle) {
            return;
        }

        visited[v] = true;
        onStack[v] = true;
        for (int w: graph.getOrDefault(v, Collections.emptyList())) {
            if (!visited[w]) {
                dfs(graph, visited, onStack, w);
            } else if (onStack[w]) {
                hasCircle = true;
                return;
            }
        }
        onStack[v] = false;
    }

    @Test
    public void testCanFinish() {
        test(this::canFinish);
    }
}
