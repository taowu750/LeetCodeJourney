package acguide._0x00_basicalgorithm._0x04_binarysearch;

import org.junit.jupiter.api.Test;

import java.util.function.ToIntBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Best Cow Fences: https://ac.nowcoder.com/acm/contest/1000/A
 *
 * 给定正整数数列 A，求一个平均数最大的、长度不小于 L 的（连续的）子段。
 *
 * 例 1：
 * 输入：
 * A = [6,4,2,10,3,8,5,9,4,1], L = 6
 * 输出：
 * 6500
 * 解释：
 * 不进行四舍五入，输出最大平均值的 1000 倍的整数部分
 *
 * 约束：
 * - A 的长度是 n
 * - 1 ≤ n ≤ 100000
 * - 1 ≤ A[i] ≤ 2000
 * - 1 ≤ L ≤ n
 */
public class G016_BestCowFences {

    public static void test(ToIntBiFunction<int[], Integer> method) {
        assertEquals(6500, method.applyAsInt(new int[]{6,4,2,10,3,8,5,9,4,1}, 6));
    }

    public int maxAverage(int[] a, int L) {
        final int n = a.length;
        // 平均值的上下界分别是最小值和最大值
        double l = a[0], r = a[0];
        for (int j : a) {
            l = Math.min(l, j);
            r = Math.max(r, j);
        }

        double[] b = new double[n], sum = new double[n + 1];
        while (r - l > 1e-5) {
            double mid = (l + r) / 2;
            for (int i = 0; i < n; i++) {
                b[i] = a[i] - mid;
            }
            for (int i = 0; i < n; i++) {
                sum[i + 1] = b[i] + sum[i];
            }
            double max = Double.NEGATIVE_INFINITY, minSumJ = Double.POSITIVE_INFINITY;
            for (int i = L; i <= n; i++) {
                minSumJ = Math.min(minSumJ, sum[i - L]);
                max = Math.max(max, sum[i] - minSumJ);
            }
            if (max >= 0) {
                l = mid;
            } else {
                r = mid;
            }
        }

        return (int) (r * 1000);
    }

    @Test
    public void testMaxAverage() {
        test(this::maxAverage);
    }
}
