package acguide._0x00_basicalgorithm._0x05_sort;

import org.junit.jupiter.api.Test;

import java.util.function.ToLongFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 超快速排序：https://www.acwing.com/problem/content/109/
 *
 * 给定整数序列 a，如果只允许进行比较和交换相邻两个数的操作，求至少需要多少次交换才能把 a 从小到大排序。
 *
 * 例 1：
 * 输入：
 * a=[9,1,0,5,4]
 * 输出：
 * 6
 *
 * 例 2：
 * 输入：
 * a=[1,2,3]
 * 输出：
 * 0
 *
 * 例 3：
 * 输入：
 * 空数组
 * 输出：
 * 0
 *
 * 说明：
 * - 0 ≤ n < 500000
 * - 0 ≤ ai ≤ 999999999
 */
public class G022_UltraQuickSort {

    public static void test(ToLongFunction<int[]> method) {
        assertEquals(6, method.applyAsLong(new int[]{9,1,0,5,4}));
        assertEquals(0, method.applyAsLong(new int[]{1,2,3}));
        assertEquals(0, method.applyAsLong(new int[]{}));
    }

    public long ultraQuickSort(int[] a) {
        ans = 0;
        sort(a, a.clone(), 0, a.length - 1);
        return ans;
    }

    long ans;

    void sort(int[] a, int[] aux, int l, int r) {
        if (l < r) {
            int m = (l + r) >>> 1;
            sort(aux, a, l, m);
            sort(aux, a, m + 1, r);
            merge(a, aux, l, m, r);
        }
    }

    void merge(int[] a, int[] aux, int l, int m, int r) {
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
    }

    @Test
    public void testUltraQuickSort() {
        test(this::ultraQuickSort);
    }
}
