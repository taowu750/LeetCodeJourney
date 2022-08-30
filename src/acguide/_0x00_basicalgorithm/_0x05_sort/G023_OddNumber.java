package acguide._0x00_basicalgorithm._0x05_sort;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 奇数码问题：https://www.acwing.com/problem/content/110/
 *
 * 你一定玩过八数码游戏，它实际上是在一个 3×3 的网格中进行的，1 个空格和 1∼8 这 8 个数字恰好不重不漏地分布在这 3×3 的网格中。
 * 例如：
 * 5 2 8
 * 1 3 _
 * 4 6 7
 *
 * 在游戏过程中，可以把空格与其上、下、左、右四个方向之一的数字交换（如果存在）。
 * 例如在上例中，空格可与左、上、下面的数字交换，分别变成：
 * 5 2 8       5 2 _      5 2 8
 * 1 _ 3       1 3 8      1 3 7
 * 4 6 7       4 6 7      4 6 _
 *
 * 奇数码游戏是它的一个扩展，在一个 n×n 的网格中进行，其中 n 为奇数，1 个空格和 1∼n^2−1 这 n^2−1 个数恰好不重不漏地分布在 n×n 的网格中。
 * 空格移动的规则与八数码游戏相同，实际上，八数码就是一个 n=3 的奇数码游戏。
 *
 * 现在给定两个奇数码游戏的局面，请判断是否存在一种移动空格的方式，使得其中一个局面可以变化到另一个局面。
 * 对于每组数据，若两个局面可达，输出 TAK，否则输出 NIE。
 *
 * 例 1：
 * 输入：
 * 1 2 3
 * 0 4 6
 * 7 5 8
 *
 * 1 2 3
 * 4 5 6
 * 7 8 0
 * 输出：
 * TAK
 *
 * 例 2：
 * 输入：
 * 0
 *
 * 0
 * 输出：
 * TAK
 *
 * 说明：
 * - 0 代表空格
 * - 1 ≤ n < 500
 */
public class G023_OddNumber {

    public static void test(BiFunction<int[][], int[][], String> method) {
        assertEquals("TAK", method.apply(new int[][]{
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}}, new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}}));
        assertEquals("TAK", method.apply(new int[][]{{0}}, new int[][]{{0}}));
    }

    public String canConvert(int[][] a, int[][] b) {
        final int n = a.length;
        int[] tmp = new int[n * n - 1];
        for (int i = 0, k = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] != 0) {
                    tmp[k++] = a[i][j];
                }
            }
        }
        int[] aux = tmp.clone();
        long aCnt = sort(tmp, aux, 0, tmp.length - 1);

        for (int i = 0, k = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b[i][j] != 0) {
                    tmp[k] = b[i][j];
                    aux[k++] = b[i][j];
                }
            }
        }
        long bCnt = sort(tmp, aux, 0, tmp.length - 1);

        return aCnt % 2 == bCnt % 2 ? "TAK" : "NIE";
    }

    long sort(int[] a, int[] aux, int l, int r) {
        long ans = 0;
        if (l < r) {
            int m = (l + r) >>> 1;
            ans += sort(aux, a, l, m);
            ans += sort(aux, a, m + 1, r);
            ans += merge(a, aux, l, m, r);
        }
        return ans;
    }

    long merge(int[] a, int[] aux, int l, int m, int r) {
        long ans = 0;
        for (int i = l, j = m + 1, k = l; i <= m || j <= r; k++) {
            if (i > m) {
                a[k] = aux[j++];
            } else if (j > r) {
                a[k] = aux[i++];
            } else if (aux[i] <= aux[j]) {
                a[k] = aux[i++];
            } else {
                a[k] = aux[j++];
                ans += m - i + 1;
            }
        }

        return ans;
    }

    @Test
    public void testCanConvert() {
        test(this::canConvert);
    }
}
