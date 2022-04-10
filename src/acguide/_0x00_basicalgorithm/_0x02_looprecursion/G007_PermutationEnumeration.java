package acguide._0x00_basicalgorithm._0x02_looprecursion;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 递归实现排列型枚举: https://ac.nowcoder.com/acm/contest/998/C
 *
 * 从 1~n 这 n(n<10) 个整数排成一行后随机打乱顺序，输出所有可能的次序。
 *
 * 例 1：
 * 输入：
 * 3
 * 输出：
 * 1 2 3
 * 1 3 2
 * 2 1 3
 * 2 3 1
 * 3 1 2
 * 3 2 1
 *
 * 说明：
 * - 按照从小到大的顺序输出所有方案，每行1个；对于两个不同的行，对应下标的数一一比较，字典序较小的排在前面
 */
public class G007_PermutationEnumeration {

    public static void test(IntFunction<List<List<Integer>>> method) {
        assertEquals(asList(asList(1, 2, 3), asList(1, 3, 2), asList(2, 1, 3), asList(2, 3, 1), asList(3, 1, 2), asList(3, 2, 1)),
                method.apply(3));
        assertEquals(asList(asList(1)), method.apply(1));
    }

    /**
     * 注意，需要把 path 换成数组才能卡过牛客网的时间限制。
     */
    public List<List<Integer>> enumeration(int n) {
        ans = new ArrayList<>();
        path = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            path.add(i);
        }
        dfs(0);

        return ans;
    }

    private List<List<Integer>> ans;
    private ArrayList<Integer> path;

    private void dfs(int i) {
        if (i >= path.size() - 1) {
            ans.add((List<Integer>) path.clone());
            return;
        }

        for (int j = i; j < path.size(); j++) {
            /*
            因为输出要保持字典序，所以把 i 交换到后面时，要先把 i 放到后面第一位（也就是 i + 1）
             */
            set(i, j);
            dfs(i + 1);
            unset(i, j);
        }
    }

    /**
     * 将 path[j] 放到 i 处，path[i] 放到 i+1 处，(i,j) 中的元素后移一位
     */
    private void set(int i, int j) {
        if (i == j) {
            return;
        }
        Integer elemI = path.get(i);
        path.set(i, path.get(j));
        for (int k = j - 1; k > i; k--) {
            path.set(k + 1, path.get(k));
        }
        path.set(i + 1, elemI);
    }

    /**
     * 执行和 set 相反的操作
     */
    private void unset(int i, int j) {
        if (i == j) {
            return;
        }
        Integer elemJ = path.get(i);
        path.set(i, path.get(i + 1));
        for (int k = i + 1; k < j; k++) {
            path.set(k, path.get(k + 1));
        }
        path.set(j, elemJ);
    }

    @Test
    public void testEnumeration() {
        test(this::enumeration);
    }


    /**
     * 书上的方法。
     */
    public List<List<Integer>> betterMethod(int n) {
        this.n = n;
        ans = new ArrayList<>();
        visited = new boolean[n + 1];
        order = new int[n + 1];
        dfs2(1);

        return ans;
    }

    private int n;
    private boolean[] visited;
    private int[] order;

    private void dfs2(int i) {
        if (i > n) {
            List<Integer> path = new ArrayList<>(n);
            for (int j = 1; j <= n; j++) {
                path.add(order[j]);
            }
            ans.add(path);
            return;
        }
        for (int j = 1; j <= n; j++) {
            if (visited[j]) {
                continue;
            }
            visited[j] = true;
            order[i] = j;
            dfs2(i + 1);
            visited[j] = false;
        }
    }

    @Test
    public void testBetterMethod() {
        test(this::betterMethod);
    }
}
