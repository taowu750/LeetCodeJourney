package acguide._0x00_basicalgorithm._0x07_greedy;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

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

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_input.txt",
                "acguide/_0x00_basicalgorithm/_0x07_greedy/data/G029_expect.txt");
    }

    public static class Union {
        private int count;
        private int[] roots;
        private double[] weights;
        private Map<Integer, List<Integer>> rid2path;

        public Union(int n, double[] weights) {
            count = n;
            roots = new int[n + 1];
            Arrays.setAll(roots, i -> i);

            this.weights = weights;
            // path: 从根到尾的路径
            rid2path = new HashMap<>((int) (n / 0.75) + 1);
            for (int i = 1; i <= n; i++) {
                rid2path.put(i, new ArrayList<>(Collections.singletonList(i)));
            }
        }

        public int connect(int child, int parent) {
            int p = rid(child), q = rid(parent);
            if (p == q) {
                return p;
            }

            roots[p] = q;
            int sizeP = rid2path.get(p).size(), sizeQ = rid2path.get(q).size();
            rid2path.get(q).addAll(rid2path.remove(p));

            double weight = (weights[p] * sizeP + weights[q] * sizeQ) / (sizeP + sizeQ);
            for (int id : rid2path.get(q)) {
                weights[id] = weight;
            }

            count--;

            return q;
        }

        public int rid(int id) {
            if (roots[id] != id) {
                roots[id] = rid(roots[id]);
            }

            return roots[id];
        }

        public double weight(int id) {
            return weights[id];
        }

        public List<Integer> path() {
            for (List<Integer> p : rid2path.values()) {
                return p;
            }
            return Collections.emptyList();
        }

        public int count() {
            return count;
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
        // 更好的方法是实现一个关联键的优先队列
        sortMap<weight, set<nodeId>> map
        for max(weight, set) in map:
            id = set.removeOne()
            if pid = parent[id]; pid == 0:
                goto ②

            pw = weights[pid]   ①
            map.remove(pw, pid)

            // rid 是合并路径的根节点
            rid, uw = union(id, pid)
            // path(id) 的根，接到 path(rid) 的尾后
            path(id).connect(path(rid))
            // 路径上每个节点的权值都要更新，否则 ① 处会出错
            weights[path(rid)] = uw

            map.add(uw, rid)
            if set.isEmpty:  ②
                map.remove(weight)

         return path
         */

        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), r = in.nextInt();
        int[] ws = new int[n + 1];
        double[] weights = new double[n + 1];
        // 记录权值-路径根节点的映射
        TreeMap<Double, Set<Integer>> w2i = new TreeMap<>();
        for (int i = 1; i <= n; i++) {
            ws[i] = in.nextInt();
            weights[i] = ws[i];
            w2i.computeIfAbsent(weights[i], k -> new HashSet<>(2)).add(i);
        }
        int[] parents = new int[n + 1];
        for (int i = 0; i < n - 1; i++) {
            int parent = in.nextInt(), child = in.nextInt();
            parents[child] = parent;
        }

        Union union = new Union(n, weights);
        while (union.count() > 1) {
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
                int pid = parents[id];
                if (pid == 0) {
                    continue;
                }

                w2i.computeIfPresent(union.weight(pid), (w, s) -> {
                    s.remove(pid);
                    return s.isEmpty() ? null : s;
                });

                int rid = union.connect(id, pid);
                w2i.computeIfAbsent(union.weight(rid), w -> new HashSet<>(2)).add(rid);
            } finally {
                if (ids.isEmpty()) {
                    w2i.remove(weight);
                }
            }
        }

        int ans = 0, i = 1;
        for (int id : union.path()) {
            ans += i++ * ws[id];
        }

        System.out.println(ans);
    }

    @Test
    public void testColor() {
        test(this::color);
    }
}
