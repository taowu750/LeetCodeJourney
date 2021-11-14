package training.dynamicprogramming;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToIntFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 120. 三角形最小路径和: https://leetcode-cn.com/problems/triangle/
 *
 * 给定一个三角形 triangle ，找出自顶向下的最小路径和。
 *
 * 每一步只能移动到下一行中相邻的结点上。「相邻的结点」在这里指的是「下标」与「上一层结点下标」相同，
 * 或者等于「上一层结点下标 + 1」的两个结点。也就是说，如果正位于当前行的下标 i ，
 * 那么下一步可以移动到下一行的下标 i 或 i + 1 。
 *
 * 例 1：
 * 输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
 * 输出：11
 * 解释：如下面简图所示：
 *    2
 *   3 4
 *  6 5 7
 * 4 1 8 3
 * 自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
 *
 * 例 2：
 * 输入：triangle = [[-10]]
 * 输出：-10
 *
 * 说明：
 * - 1 <= triangle.length <= 200
 * - triangle[0].length == 1
 * - triangle[i].length == triangle[i - 1].length + 1
 * - -10^4 <= triangle[i][j] <= 10^4
 */
public class E120_Medium_Triangle {

    public static void test(ToIntFunction<List<List<Integer>>> method) {
        assertEquals(11, method.applyAsInt(asList(singletonList(2), asList(3,4), asList(6,5,7), asList(4,1,8,3))));
        assertEquals(-10, method.applyAsInt(singletonList(singletonList(-10))));
    }

    /**
     * 记忆化搜索。
     *
     * LeetCode 耗时：7 ms - 12.02%
     *          内存消耗：40.6 MB - 5.02%
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        result = Integer.MAX_VALUE;
        // 记住某个节点下的最小路径和
        Map<Tuple, Integer> cache = new HashMap<>();
        dfs(triangle, cache, 0, 0, 0);

        return result;
    }

    private static class Tuple {
        int r, c;

        public Tuple(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tuple)) return false;
            Tuple tuple = (Tuple) o;
            return r == tuple.r && c == tuple.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }
    }

    private int result;

    private void dfs(List<List<Integer>> triangle, Map<Tuple, Integer> cache,
                     int r, int c, int sum) {
        if (r == triangle.size()) {
            if (sum < result) {
                result = sum;
            }
            return;
        }
        // “回”的部分执行检查
        Tuple cur = new Tuple(r, c);
        if (cache.containsKey(cur)) {
            int min = cache.get(cur);
            if (sum + min < result) {
                result = sum + min;
            }
            return;
        }

        sum += triangle.get(r).get(c);
        dfs(triangle, cache, r + 1, c, sum);
        dfs(triangle, cache, r + 1, c + 1, sum);

        // “溯”的部分保存最短路径和
        Integer left = cache.get(new Tuple(r + 1, c));
        if (left == null) {
            cache.put(cur, triangle.get(r).get(c));
        } else {
            cache.put(cur, triangle.get(r).get(c) + Math.min(left, cache.get(new Tuple(r + 1, c + 1))));
        }
    }

    @Test
    public void testMinimumTotal() {
        test(this::minimumTotal);
    }


    /**
     * LeetCode 耗时：3 ms - 73.14%
     *          内存消耗：38 MB - 80.07%
     */
    public int dpMethod(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] dp = new int[n][];
        for (int i = 0; i < n; i++) {
            dp[i] = new int[i + 1];
        }
        // 初始情况
        for (int i = 0; i < n; i++) {
            dp[n - 1][i] = triangle.get(n - 1).get(i);
        }

        for (int r = n - 2; r >= 0; r--) {
            for (int c = 0; c <= r; c++) {
                dp[r][c] = triangle.get(r).get(c) + Math.min(dp[r + 1][c], dp[r + 1][c + 1]);
            }
        }

        return dp[0][0];
    }

    @Test
    public void testDpMethod() {
        test(this::dpMethod);
    }


    /**
     * LeetCode 耗时：3 ms - 73.14%
     *          内存消耗：38 MB - 80.07%
     */
    public int compressMethod(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[] dp = new int[n];
        // 初始情况
        for (int i = 0; i < n; i++) {
            dp[i] = triangle.get(n - 1).get(i);
        }

        for (int r = n - 2; r >= 0; r--) {
            for (int c = 0; c <= r; c++) {
                dp[c] = triangle.get(r).get(c) + Math.min(dp[c], dp[c + 1]);
            }
        }

        return dp[0];
    }

    @Test
    public void testCompressMethod() {
        test(this::compressMethod);
    }
}
