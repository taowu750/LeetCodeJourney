package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import static java.util.Arrays.asList;
import static util.CollectionUtil.deepEqualsIgnoreOutOrder;

/**
 * 递归实现指数型枚举：https://ac.nowcoder.com/acm/contest/998/A
 *
 * 从 1~n 这 n(n<20) 个整数中随机选取任意多个，输出所有可能的选择方案。
 *
 * 例 1：
 * 输入：
 * 3
 * 输出：
 * 3
 *
 * 2
 * 2 3
 * 1
 * 1 3
 * 1 2
 * 1 2 3
 *
 * 说明：
 * - 输出中同一行内的数字需要升序排列，各行（不同方案）之间的顺序任意
 */
public class G005_ExponentialEnumeration {

    public static void test(IntFunction<List<List<Integer>>> method) {
        deepEqualsIgnoreOutOrder(asList(asList(3), asList(), asList(2), asList(2, 3), asList(1), asList(1, 3), asList(1, 2), asList(1, 2, 3)),
                method.apply(3));
    }

    public List<List<Integer>> combination(int n) {
        ans = new ArrayList<>();
        path = new ArrayList<>();
        dfs(n, 1);

        return ans;
    }

    private List<List<Integer>> ans;
    private ArrayList<Integer> path;

    private void dfs(int n, int i) {
        if (i > n) {
            ans.add((List<Integer>) path.clone());
            return;
        }
        dfs(n, i + 1);
        path.add(i);
        dfs(n, i + 1);
        path.remove(path.size() - 1);
    }

    @Test
    public void testCombination() {
        test(this::combination);
    }
}
