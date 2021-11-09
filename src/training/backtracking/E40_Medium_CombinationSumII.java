package training.backtracking;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static util.CollectionUtil.deepEqualsIgnoreOrder;

/**
 * 40. 组合总和 II: https://leetcode-cn.com/problems/combination-sum-ii/
 *
 * 给定一个数组 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合。
 *
 * candidates 中的每个数字在每个组合中只能使用一次。注意：解集不能包含重复的组合。
 *
 * 例 1：
 * 输入: candidates = [10,1,2,7,6,1,5], target = 8,
 * 输出:
 * [
 * [1,1,6],
 * [1,2,5],
 * [1,7],
 * [2,6]
 * ]
 *
 * 例 2：
 * 输入: candidates = [2,5,2,1,2], target = 5,
 * 输出:
 * [
 * [1,2,2],
 * [5]
 * ]
 *
 * 说明：
 * - 1 <= candidates.length <= 100
 * - 1 <= candidates[i] <= 50
 * - 1 <= target <= 30
 */
public class E40_Medium_CombinationSumII {

    public static void test(BiFunction<int[], Integer, List<List<Integer>>> method) {
        deepEqualsIgnoreOrder(asList(asList(1,1,6),asList(1,2,5),asList(1,7),asList(2,6)),
                method.apply(new int[]{10,1,2,7,6,1,5}, 8));
        deepEqualsIgnoreOrder(asList(asList(1,2,2), singletonList(5)),
                method.apply(new int[]{2,5,2,1,2}, 5));
        deepEqualsIgnoreOrder(Collections.emptyList(),
                method.apply(new int[]{1}, 2));
    }

    /**
     * 类似于字符串 Trie，这里用于添加 List<Integer> 以防止出现重复
     */
    public static class IntTrie {

        private static class Node {
            // 这个 Trie 不需要 boolean 变量标识节点是不是一个终点，
            // 因为当一个较长的序列等于 target，那么小于或大于它的序列一定不等于 target
            Map<Integer, Node> children = new HashMap<>();
        }

        private Node root;

        public IntTrie() {
            root = new Node();
        }

        public void insert(List<Integer> list) {
            Node p = root;
            for (Integer num : list) {
                Node child = p.children.get(num);
                if (child == null) {
                    child = new Node();
                    p.children.put(num, child);
                }
                p = child;
            }
        }

        public List<List<Integer>> getAll() {
            List<List<Integer>> result = new ArrayList<>();
            dfs(root, result, new ArrayList<>());

            return result;
        }

        private void dfs(Node root, List<List<Integer>> result, ArrayList<Integer> path) {
            if (root.children.isEmpty()) {
                // 防止添加空数组
                if (path.size() > 0) {
                    result.add((List<Integer>) path.clone());
                }
            } else {
                root.children.forEach((num, child) -> {
                    path.add(num);
                    dfs(child, result, path);
                    path.remove(path.size() - 1);
                });
            }
        }
    }

    /**
     * 超时
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        int idx = Arrays.binarySearch(candidates, target);
        if (idx < 0) {
            idx = -idx - 1;
        }
        if (idx == candidates.length || candidates[idx] > target) {
            idx--;
        }
        if (idx < 0) {
            return Collections.emptyList();
        }

        // 计算前缀数组
        int[] prefix = new int[candidates.length];
        prefix[0] = candidates[0];
        for (int i = 1; i < candidates.length; i++) {
            prefix[i] = prefix[i - 1] + candidates[i];
        }

        IntTrie result = new IntTrie();
        dfs(candidates, idx, target, prefix, result, new ArrayList<>(candidates.length));

        return result.getAll();
    }

    private void dfs(int[] distinct, int i, int target, int[] prefix,
                     IntTrie result, ArrayList<Integer> path) {
        if (target == 0) {
            result.insert(path);
            return;
        }
        if (i < 0 || prefix[i] < target) {
            return;
        }
        if (prefix[i] == target) {
            List<Integer> res = new ArrayList<>(path.size() + i + 1);
            res.addAll(path);
            for (int j = i; j >= 0; j--) {
                res.add(distinct[j]);
            }
            result.insert(res);
            return;
        }

        if (distinct[i] <= target) {
            path.add(distinct[i]);
            dfs(distinct, i - 1, target - distinct[i], prefix, result, path);
            path.remove(path.size() - 1);
        }
        dfs(distinct, i - 1, target, prefix, result, path);
    }

    @Test
    public void testCombinationSum2() {
        test(this::combinationSum2);
    }


    /**
     * 为了去重，可以使用 Map 记录每个数和它们的出现次数，这样就可以将它们一起处理。参见：
     * https://leetcode-cn.com/problems/combination-sum-ii/solution/zu-he-zong-he-ii-by-leetcode-solution/
     *
     * 更好的方法是不用 map，直接排序数组，根据相邻元素相等判断。
     *
     * LeetCode 耗时：5 ms - 14.23%
     *          内存消耗：38.5 MB - 80.03%
     */
    public List<List<Integer>> betterDfs(int[] candidates, int target) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int candidate : candidates) {
            map.merge(candidate, 1, Integer::sum);
        }
        int[][] num2cnt = new int[map.size()][2];
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            num2cnt[i][0] = entry.getKey();
            num2cnt[i][1] = entry.getValue();
            i++;
        }

        List<List<Integer>> result = new ArrayList<>();
        dfs(result, new ArrayList<>(candidates.length), target, num2cnt, 0);

        return result;
    }

    private void dfs(List<List<Integer>> result, ArrayList<Integer> path, int target,
                     int[][] num2cnt, int idx) {
        if (target == 0) {
            result.add((List<Integer>) path.clone());
            return;
        }
        if (idx == num2cnt.length || num2cnt[idx][0] > target) {
            return;
        }

        dfs(result, path, target, num2cnt, idx + 1);
        int most = Math.min(target / num2cnt[idx][0], num2cnt[idx][1]);
        for (int i = 1; i <= most; ++i) {
            path.add(num2cnt[idx][0]);
            dfs(result, path, target - i * num2cnt[idx][0], num2cnt, idx + 1);
        }
        for (int i = 1; i <= most; ++i) {
            path.remove(path.size() - 1);
        }
    }

    @Test
    public void testBetterDfs() {
        test(this::betterDfs);
    }
}
