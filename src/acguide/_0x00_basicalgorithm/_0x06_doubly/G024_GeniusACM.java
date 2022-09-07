package acguide._0x00_basicalgorithm._0x06_doubly;

import org.junit.jupiter.api.Test;
import util.datastructure.function.ToIntTriFunction;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Genius ACM：https://www.acwing.com/problem/content/111/
 *
 * 给定一个整数 M，对于任意一个整数集合 S，定义“校验值”如下:
 * 从集合 S 中取出 M 对数(即 2×M 个数，「不能重复使用集合中的数」，如果 S 中的整数不够 M 对，则取到不能取为止)，
 * 使得“每对数的差的平方”之和最大，这个最大值就称为集合 S 的“校验值”。
 *
 * 现在给定一个长度为 N 的数列 A 以及一个整数 T。我们要把 A 分成若干段，使得每一段的“校验值”都不超过 T。
 * 求最少需要分成几段。
 *
 * 例 1：
 * 输入：
 * M=1, T=49, A=[8,2,1,7,9]
 * 输出：
 * 2
 *
 * 例 2：
 * 输入：
 * M=1, T=64, A=[8,2,1,7,9]
 * 输出：
 * 1
 *
 * 说明：
 * - 1 ≤ N,M ≤ 500000
 * - 0 ≤ T ≤ 10^18
 * - 0 ≤ A_i ≤ 2^20
 */
public class G024_GeniusACM {

    public static void test(ToIntTriFunction<int[], Integer, Long> method) {
        assertEquals(2, method.applyAsInt(new int[]{8,2,1,7,9}, 1, 49L));
        assertEquals(1, method.applyAsInt(new int[]{8,2,1,7,9}, 1, 64L));
        assertEquals(8, method.applyAsInt(new int[]{6, 0, 16, 16, 5, 19, 16, 18, 19, 0, 10, 19, 12, 19, 0, 9, 3, 7, 19, 3, 6, 17, 17, 4, 7, 10, 14, 11, 14, 9, 6, 6, 0, 12, 16, 15, 15, 16, 3, 0, 19, 7, 16, 18, 1, 19, 9, 12, 3, 16, 8, 17, 15, 2, 11, 15, 11, 2, 6, 18, 15, 5, 17, 8, 8, 19, 16, 2, 6, 14, 5, 19, 17, 12, 9, 11, 17, 4, 1, 11, 13, 16, 10, 3, 16, 19, 19, 16, 3, 13, 2, 11, 3, 7, 1, 13, 19, 18, 14, 17},
                88, 1039L));
    }

    public int split(int[] A, int M, long T) {
        int n = A.length, ans = 0;
        int[] sort = A.clone(), merge = A.clone();
        for (int l = 0, r = 0; l < n; l = r, ans++) {
            for (int p = 1; p != 0;) {
                Arrays.sort(sort, r, r + p);
                // 归并
                for (int i = l, j = r, k = l; i < r || j < r + p; k++) {
                    if (i >= r) {
                        merge[k] = sort[j++];
                    } else if (j >= r + p) {
                        merge[k] = sort[i++];
                    } else if (sort[i] <= sort[j]) {
                        merge[k] = sort[i++];
                    } else {
                        merge[k] = sort[j++];
                    }
                }
                // 计算校验和
                int pair = Math.min(M, (r + p - l) / 2);
                long validate = 0;
                for (int i = l, j = r + p - 1; pair > 0; i++, j--, pair--) {
                    validate += (long) (merge[j] - merge[i]) * (merge[j] - merge[i]);
                    if (validate > T) {
                        break;
                    }
                }
                if (validate <= T) {
                    System.arraycopy(merge, l, sort, l, r + p - l);
                    r += p;
                    p *= 2;
                    // p 不能超过数组长度
                    p = Math.min(p, n - r);
                } else {
                    // 排序结果作废
                    System.arraycopy(A, r, sort, r, p);
                    p /= 2;
                }
            }
        }

        return ans;
    }

    @Test
    public void testSplit() {
        test(this::split);
    }
}
