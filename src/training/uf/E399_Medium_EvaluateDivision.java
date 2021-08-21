package training.uf;

import org.junit.jupiter.api.Test;
import training.linkedlist.E92_Medium_ReverseLinkedListII;
import training.linkedlist.Review_E206_Easy_ReverseLinkedList;
import util.datastructure.function.TriFunction;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 399. 除法求值: https://leetcode-cn.com/problems/evaluate-division/
 *
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi]
 * 和 values[i] 共同表示等式 Ai / Bi = values[i] 。每个 Ai 或 Bi 是一个表示单个变量的字符串。
 *
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，
 * 请你根据已知条件找出 Cj / Dj = ? 的结果作为答案。
 *
 * 返回 所有问题的答案 。如果存在某个无法确定的答案，则用 -1.0 替代这个答案。
 * 如果问题中出现了给定的已知条件中没有出现的字符串，也需要用 -1.0 替代这个答案。
 *
 * 注意：输入总是有效的。你可以假设除法运算中不会出现除数为 0 的情况，且不存在任何矛盾的结果。
 *
 * 例 1：
 * 输入：equations = [["a","b"],["b","c"]], values = [2.0,3.0],
 *      queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * 输出：[6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * 解释：
 * 条件：a / b = 2.0, b / c = 3.0
 * 问题：a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * 结果：[6.0, 0.5, -1.0, 1.0, -1.0 ]
 *
 * 例 2：
 * 输入：equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0],
 *      queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
 * 输出：[3.75000,0.40000,5.00000,0.20000]
 *
 * 例 3：
 * 输入：equations = [["a","b"]], values = [0.5],
 *      queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
 * 输出：[0.50000,2.00000,-1.00000,-1.00000]
 *
 * 约束：
 * - 1 <= equations.length <= 20
 * - equations[i].length == 2
 * - 1 <= Ai.length, Bi.length <= 5
 * - values.length == equations.length
 * - 0.0 < values[i] <= 20.0
 * - 1 <= queries.length <= 20
 * - queries[i].length == 2
 * - 1 <= Cj.length, Dj.length <= 5
 * - Ai, Bi, Cj, Dj 由小写英文字母与数字组成
 */
public class E399_Medium_EvaluateDivision {

    static void test(TriFunction<List<List<String>>, double[], List<List<String>>, double[]> method) {
        assertArrayEquals(new double[]{6.0,0.5,-1.0,1.0,-1.0}, method.apply(
                asList(asList("a","b"), asList("b","c")), new double[]{2.0,3.0},
                asList(asList("a","c"),asList("b","a"),asList("a","e"),asList("a","a"),asList("x","x"))),
                0.00001);

        assertArrayEquals(new double[]{3.75,0.40,5.0,0.2}, method.apply(
                asList(asList("a","b"), asList("b","c"), asList("bc","cd")), new double[]{1.5,2.5,5.0},
                asList(asList("a","c"),asList("c","b"),asList("bc","cd"),asList("cd","bc"))),
                0.00001);

        assertArrayEquals(new double[]{0.5,2.0,-1.0,-1.0}, method.apply(
                singletonList(asList("a", "b")), new double[]{0.5},
                asList(asList("a","b"),asList("b","a"),asList("a","c"),asList("x","y"))),
                0.00001);
    }

    private static class Node {
        String var;
        double quotient;

        public Node(String var, double quotient) {
            this.var = var;
            this.quotient = quotient;
        }
    }

    /**
     * LeetCode 耗时：1 ms - 73.82%
     *          内存消耗：37.1 MB - 69.67%
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, List<Node>> graph = new HashMap<>();
        int i = 0;
        for (List<String> equation : equations) {
            graph.computeIfAbsent(equation.get(0), s -> new ArrayList<>(4))
                    .add(new Node(equation.get(1), values[i]));
            graph.computeIfAbsent(equation.get(1), s -> new ArrayList<>(4))
                    .add(new Node(equation.get(0), 1 / values[i]));
            i++;
        }

        double[] result = new double[queries.size()];
        Set<String> visited = new HashSet<>((int) (graph.size() / 0.75) + 1);
        for (i = 0; i < result.length; i++) {
            String src = queries.get(i).get(0), dst = queries.get(i).get(1);
            if (!graph.containsKey(src) || !graph.containsKey(dst)) {
                result[i] = -1;
            } else if (src.equals(dst)) {
                result[i] = 1;
            } else {
                result[i] = dfs(graph, visited, src, dst, 1);
                visited.clear();
            }
        }

        return result;
    }

    private double dfs(Map<String, List<Node>> graph, Set<String> visited,
                       String src, String dst, double multi) {
        visited.add(src);
        for (Node neighbor : graph.get(src)) {
            if (visited.contains(neighbor.var)) {
                continue;
            }
            if (neighbor.var.equals(dst)) {
                return multi * neighbor.quotient;
            } else {
                double result = dfs(graph, visited, neighbor.var, dst, multi * neighbor.quotient);
                if (result != -1.0) {
                    return result;
                }
            }
        }

        return -1;
    }

    @Test
    public void testCalcEquation() {
        test(this::calcEquation);
    }


    /**
     * 并查集方法，参见：
     * https://leetcode-cn.com/problems/evaluate-division/solution/399-chu-fa-qiu-zhi-nan-du-zhong-deng-286-w45d/
     *
     * LeetCode 耗时：1 ms - 73.82%
     *          内存消耗：37 MB - 88.27%
     */
    public double[] ufMethod(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Set<String> set = new HashSet<>(equations.size());
        for (List<String> equation : equations) {
            set.add(equation.get(0));
            set.add(equation.get(1));
        }

        UF uf = new UF(set.size());
        Map<String, Integer> var2id = new HashMap<>((int) (set.size() / 0.75) + 1);
        int id = 0;
        for (String var : set) {
            var2id.put(var, id++);
        }

        int i = 0;
        for (List<String> equation : equations) {
            uf.union(var2id.get(equation.get(0)), var2id.get(equation.get(1)), values[i++]);
        }

        double[] result = new double[queries.size()];
        for (i = 0; i < result.length; i++) {
            int srcId = var2id.getOrDefault(queries.get(i).get(0), -1);
            int dstId = var2id.getOrDefault(queries.get(i).get(1), -1);
            if (srcId != -1 && dstId != -1) {
                result[i] = uf.isConnected(srcId, dstId);
            } else {
                result[i] = -1;
            }
        }

        return result;
    }

    private static class UF {
        private int[] parent;
        // weight[i] = i / parent[i]
        private double[] weight;

        UF(int count) {
            parent = new int[count];
            weight = new double[count];

            for (int i = 0; i < count; i++) {
                parent[i] = i;
                weight[i] = 1;
            }
        }

        /**
         * p / q = value
         */
        void union(int p, int q, double value) {
            int rootP = find(p), rootQ = find(q);
            if (rootP == rootQ) {
                return;
            }

            // 将 p 合并到 rootQ 上，同时更新权重
            // 不必担心这一步会导致树增高，查找过程中会进行路径压缩
            parent[rootP] = rootQ;
            /*
            基本条件：
            p / q = value
            weight[p] = p / rootP
            weight[q] = q / rootQ

            则有：
            rootP = p / weight[p]
            rootQ = q / weight[q]

            weight[rootP] 需要更新为 rootP 和 rootQ 的商，则有：
            weight[rootP]
            = rootP / rootQ
            = (p / weight[p]) / (q / weight[q])
            = weight[q] * value / weight[p]
             */
            weight[rootP] = weight[q] * value / weight[p];
        }

        /**
         * 找到 p 的根，并执行路径压缩，同时更新 weight[p] = p / parent[p]。
         *
         * 递归算法，想出定义、每层需要做的事情即可，参见 {@link Review_E206_Easy_ReverseLinkedList}
         * 和 {@link E92_Medium_ReverseLinkedListII}。
         */
        int find(int p) {
            if (p != parent[p]) {
                int origin = parent[p];
                // 查找 parent[p] 的根，同时更新了原 parent[p]（origin） 的权重
                parent[p] = find(parent[p]);
                weight[p] *= weight[origin];
            }
            return parent[p];
        }

        double isConnected(int p, int q) {
            int rootP = find(p), rootQ = find(q);
            if (rootP == rootQ) {
                return weight[p] / weight[q];
            }

            return -1;
        }
    }

    @Test
    public void testUfMethod() {
        test(this::ufMethod);
    }
}
