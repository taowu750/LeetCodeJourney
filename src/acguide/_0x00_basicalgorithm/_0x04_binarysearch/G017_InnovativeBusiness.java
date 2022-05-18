package acguide._0x00_basicalgorithm._0x04_binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Innovative Business: https://www.acwing.com/problem/content/115/
 *
 * 有 N 个元素，每一对元素之间的大小关系是确定的，不存在两个元素大小相等的情况。
 * 关系不具有传递性，也就是说，元素的大小关系是 N 个点与 N*(N-1)/2 条有向边构成的任意有向图。
 *
 * 然而，这是一道交互式试题，这些关系不能一次性得知，你必须通过不超过 10000 次提问，每次提问只能了解某两个元素之间的关系。
 * 把这 N 个元素排成一行，使得每个元素都小于右边与它相邻的元素，N ≤ 1000。如果答案不唯一，则输出任意一个均可。
 *
 * 提问 API 如下所示：
 * - boolean compare(int a, int b)：返回 a 是否小于 b
 *
 * 例 1：
 * 输入：
 * [[0, 1, 0], [0, 0, 0], [1, 1, 0]]
 * 输出：
 * [3, 1, 2]
 * 解释：
 * 输入是一个邻接矩阵 relations，其中 relations[i][j] 等于 1 表示 i 小于 j，等于 0 表示 i 大于 j
 *
 * 说明：
 * - 1 ≤ N ≤ 1000
 * - 1 ≤ a,b ≤ N
 */
public class G017_InnovativeBusiness {

    private static int[][] relations;
    private static int cnt = 0;

    public static boolean compare(int a, int b) {
        if (++cnt > 10000) {
            throw new AssertionError("call cnt exceed limit");
        }
        return relations[a - 1][b - 1] == 1;
    }

    public static void test(IntFunction<int[]> method) {
        cnt = 0;
        relations = new int[][]{
                {0,1,0},
                {0,0,0},
                {1,1,0}
        };
        assertArrayEquals(new int[]{3,1,2}, method.apply(3));
    }

    public int[] specialSort(int N) {
        int[] ans = new int[N];
        ans[0] = 1;
        for (int i = 1; i < N; i++) {
            int l = 0, r = i, elem = i + 1;
            while (l < r) {
                int mid = (l + r) >>> 1;
                if (compare(ans[mid], elem)) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            if (l < i) {
                System.arraycopy(ans, l, ans, l + 1, i - l);
            }
            ans[l] = elem;
        }

        return ans;
    }

    @Test
    public void testSpecialSort() {
        test(this::specialSort);
    }
}
