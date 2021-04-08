package training.backtracking;

import org.junit.jupiter.api.Test;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
 *
 * 例 1：
 * 输入: n = 4, k = 2
 * 输出:
 * [
 *   [2,4],
 *   [3,4],
 *   [2,3],
 *   [1,2],
 *   [1,3],
 *   [1,4],
 * ]
 */
public class E77_Medium_Combinations {

    static void test(BiFunction<Integer, Integer, List<List<Integer>>> method) {
        assertTrue(CollectionUtil.deepEqualsIgnoreOrder(method.apply(4, 2),
                Arrays.asList(Arrays.asList(2, 4), Arrays.asList(3, 4), Arrays.asList(2, 3),
                        Arrays.asList(1, 2), Arrays.asList(1, 3), Arrays.asList(1, 4))));

        assertEquals(method.apply(1, 1), singletonList(singletonList(1)));
    }

    /**
     * LeetCode 耗时：2 ms - 94.25%
     *          内存消耗：40.1 MB - 16.48%
     */
    public List<List<Integer>> combine(int n, int k) {
        int cap = 1;
        for (int i = n; i > n - k; i--)
            cap *= i;
        int factor = 1;
        for (int i = 2; i <= k; i++)
            factor *= i;
        List<List<Integer>> result = new ArrayList<>(cap / factor + 1);

        dfs(result, 1, k, n, new ArrayList<>(k));

        return result;
    }

    private void dfs(List<List<Integer>> result, int i, int k, int n, ArrayList<Integer> subset) {
        if (k == 0) {
            result.add((List<Integer>) subset.clone());
            return;
        }

        int limit = n - k + 1;
        for (int j = i; j <= limit; j++) {
            subset.add(j);
            dfs(result, j + 1, k - 1, n, subset);
            subset.remove(subset.size() - 1);
        }
    }

    @Test
    public void testCombine() {
        test(this::combine);
    }
}
