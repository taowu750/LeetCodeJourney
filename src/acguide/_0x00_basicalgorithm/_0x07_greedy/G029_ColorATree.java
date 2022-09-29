package acguide._0x00_basicalgorithm._0x07_greedy;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;
import util.datastructure.KeyPriorityQueue;

import java.util.*;

/**
 * Color a Tree: https://www.acwing.com/problem/content/117/
 *
 * 一颗树有 n 个节点，这些节点被标号为：1,2,3…n，每个节点 i 都有一个权值 A[i]。
 *
 * 现在要把这棵树的节点全部染色，染色的规则是：
 * - 根节点 R 可以随时被染色；对于其他节点，在被染色之前它的父亲节点必须已经染上了色。
 * - 每次染色的代价为 T×A[i]，其中 T 代表当前是第几次染色。
 *
 * 求把这棵树染色的最小总代价。
 *
 * 输入格式：
 * 第一行包含两个整数 n 和 R，分别代表树的节点数以及根节点的序号。
 * 第二行包含 n 个整数，代表所有节点的权值，第 i 个数即为第 i 个节点的权值 A[i]。
 * 接下来 n−1 行，每行包含两个整数 a 和 b，代表两个节点的序号，两节点满足关系：a 节点是 b 节点的父节点。
 * 除根节点外的其他 n−1 个节点的父节点和它们本身会在这 n−1 行中表示出来。
 * 同一行内的数用空格隔开。
 *
 * 输出格式：
 * 输出一个整数，代表把这棵树染色的最小总代价。
 *
 *
 * 例 1：
 * 输入：
 * 5 1
 * 1 2 1 2 4
 * 1 2
 * 1 3
 * 2 4
 * 3 5
 * 输出：
 * 33
 *
 *
 * 说明：
 * - 1 ≤ n ≤ 1000,
 * - 1 ≤ A[i] ≤ 1000
 */
public class G029_ColorATree {

    public static int whichCase = 1;

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_input.txt",
                "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_expect.txt");
        whichCase++;

        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_input2.txt",
                "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_expect2.txt");
        whichCase++;

        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_input3.txt",
                "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_expect3.txt");
        whichCase++;
    }

    public static class Union {
        private final int[] roots;
        private final double[] weights;
        private final int[] size;

        public Union(int n, double[] weights) {
            roots = new int[n + 1];
            Arrays.setAll(roots, i -> i);
            this.weights = weights;
            size = new int[n + 1];
            Arrays.fill(size, 1);
        }

        public int connect(int child, int parent) {
            int p = rid(child), q = rid(parent);
            if (p == q) {
                return p;
            }

            roots[p] = q;
            int sizeP = size[p], sizeQ = size[q];
            weights[q] = (weights[p] * sizeP + weights[q] * sizeQ) / (sizeP + sizeQ);
            size[q] += size[p];

            return q;
        }

        public int rid(int id) {
            if (roots[id] != id) {
                roots[id] = rid(roots[id]);
            }

            return roots[id];
        }

        public double weight(int rid) {
            return weights[rid];
        }

        public int size(int rid) {
            return size[rid];
        }
    }

    public void color() {
        /*
        要能不断找到最大权值的节点，并且不同节点权值可能相等

        然后要能和它的父节点合并，所以需要移除父节点
        它的父节点可能也是个等效权值节点，所以必须通过id移除父节点

        注意节点 n 的父节点可能包含在某个等效权值节点 en 中，en 也就是 n 的父节点
        所以需要能够在合并过程中，改变被合并节点子节点的父节点为 en

        最大权值节点可能包含了根节点，此时需要选取第二大的节点进行合并，否则会死循环


        伪代码如下：
        // ws 是每个等价权值节点对应的权值和，weights 是每个等价权值节点的平均权值
        ans = sum(ws)
        sortMap<weight, set<nodeId>> map
        for max(weight, set) in map:
            id = set.removeOne()
            if pid = rid(parent[id]); pid == 0:
                goto ①

            ans += ws[pid] * size(id)
            pw = weights[pid]
            map.remove(pw, pid)

            // pid 是合并路径的根节点
            uw = union(id, pid)
            map.add(uw, pid)
            ws[pid] += ws[id]

            if set.isEmpty:  ①
                map.remove(weight)

         return ans
         */

        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), r = in.nextInt(), ans = 0;
        int[] ws = new int[n + 1];
        double[] weights = new double[n + 1];
        // 记录权值-路径根节点的映射
        TreeMap<Double, Set<Integer>> w2i = new TreeMap<>();
        for (int i = 1; i <= n; i++) {
            ws[i] = in.nextInt();
            ans += ws[i];
            weights[i] = ws[i];
            w2i.computeIfAbsent(weights[i], k -> new HashSet<>(2)).add(i);
        }
        int[] parents = new int[n + 1];
        for (int i = 0; i < n - 1; i++) {
            int parent = in.nextInt(), child = in.nextInt();
            parents[child] = parent;
        }

        Union union = new Union(n, weights);
        while (!w2i.isEmpty()) {
            final Map.Entry<Double, Set<Integer>> entry = w2i.lastEntry();
            double weight = entry.getKey();
            final Set<Integer> ids = entry.getValue();
            int id = 0;
            for (int i : ids) {
                id = i;
                break;
            }
            ids.remove(id);

            try {
                int pid = union.rid(parents[id]);
                if (pid == 0) {
                    continue;
                }

                ans += union.size(pid) * ws[id];
                w2i.computeIfPresent(union.weight(pid), (w, s) -> {
                    s.remove(pid);
                    return s.isEmpty() ? null : s;
                });

                union.connect(id, pid);
                w2i.computeIfAbsent(union.weight(pid), w -> new HashSet<>(2)).add(pid);
                ws[pid] += ws[id];
            } finally {
                if (ids.isEmpty()) {
                    w2i.remove(weight);
                }
            }
        }

        System.out.println(ans);
    }

    @Test
    public void testColor() {
        test(this::color);
    }


    public static class UF {
        private final int[] roots;
        private final int[] size;

        public UF(int n) {
            roots = new int[n + 1];
            Arrays.setAll(roots, i -> i);
            size = new int[n + 1];
            Arrays.fill(size, 1);
        }

        public int connect(int child, int parent) {
            int p = rid(child), q = rid(parent);
            if (p == q) {
                return p;
            }

            roots[p] = q;
            size[q] += size[p];

            return q;
        }

        public int rid(int id) {
            if (roots[id] != id) {
                roots[id] = rid(roots[id]);
            }

            return roots[id];
        }

        public int size(int rid) {
            return size[rid];
        }
    }

    /**
     * 使用 {@link KeyPriorityQueue} 的方法
     */
    public void keyPriorityQueueMethod() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), r = in.nextInt(), ans = 0;
        int[] weights = new int[n + 1];
        KeyPriorityQueue<Integer, Double> pq = new KeyPriorityQueue<>(n, (a, b) -> -Double.compare(a, b), n);
        for (int i = 1; i <= n; i++) {
            weights[i] = in.nextInt();
            ans += weights[i];
            pq.push(i, (double) weights[i]);
        }
        int[] parents = new int[n + 1];
        for (int i = 0; i < n - 1; i++) {
            int parent = in.nextInt(), child = in.nextInt();
            parents[child] = parent;
        }

        double rootWeight = 0;
        UF uf = new UF(n);
        while (!pq.isEmpty()) {
            // 取最大权值的节点
            final KeyPriorityQueue.Entry<Integer, Double> e = pq.pollEntry();
            // 如果取的是根节点
            if (e.key == r) {
                rootWeight = e.value;
                continue;
            }
            // 取它的父节点
            int id = e.key, pid = uf.rid(parents[id]);
            double weight = e.value, parentWeight = pq.peekOrDefault(pid, rootWeight);
            int size = uf.size(id), parentSize = uf.size(pid);
            // 更新权值，计算结果
            ans += parentSize * weights[id];
            // 计算等价权值
            double mergeWeight = (weight * size + parentWeight * parentSize) / (size + parentSize);
            // 进行合并
            pq.push(uf.connect(id, pid), mergeWeight);
            if (pid == r) {
                rootWeight = mergeWeight;
            }
            weights[pid] += weights[id];
        }

        System.out.println(ans);
    }

    @Test
    public void testKeyPriorityQueueMethod() {
        test(this::keyPriorityQueueMethod);
    }
}
