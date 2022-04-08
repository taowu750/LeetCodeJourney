package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 递归实现组合型枚举: https://ac.nowcoder.com/acm/contest/998/B
 *
 * 从 1~n 这 n 个整数中随机选出 m 个，输出所有可能的选择方案。n>0, 0≤m≤n, n+(n−m)≤25。
 *
 * 例 1：
 * 输入：
 * 5 3
 * 输出：
 * 1 2 3
 * 1 2 4
 * 1 2 5
 * 1 3 4
 * 1 3 5
 * 1 4 5
 * 2 3 4
 * 2 3 5
 * 2 4 5
 * 3 4 5
 *
 * 说明：
 * - 同一行内的数升序排列
 * - 按照从小到大的顺序输出所有方案。对于两个不同的行，对应下标的数一一比较，字典序较小的排在前面
 */
public class G006_CombinatorialEnumeration {

    public static void test(BiFunction<Integer, Integer, List<List<Integer>>> method) {
        assertEquals(asList(asList(1, 2, 3), asList(1, 2, 4), asList(1, 2, 5), asList(1, 3, 4), asList(1, 3, 5),
                asList(1, 4, 5), asList(2, 3, 4), asList(2, 3, 5), asList(2, 4, 5), asList(3, 4, 5)),
                method.apply(5, 3));
    }

    public List<List<Integer>> combination(int n, int m) {
        this.n = n;
        this.m = m;
        ans = new ArrayList<>();
        path = new ArrayList<>(m);
        dfs(1);

        return ans;
    }

    private int n, m;
    private List<List<Integer>> ans;
    private ArrayList<Integer> path;

    private void dfs(int i) {
        if (path.size() == m) {
            ans.add((List<Integer>) path.clone());
            return;
        }
        // 先添加
        path.add(i);
        dfs(i + 1);
        path.remove(path.size() - 1);
        // 后面数字足够的情况下不添加
        if (n - i >= m - path.size()) {
            dfs(i + 1);
        }
    }

    @Test
    public void testCombination() {
        test(this::combination);
    }
}
