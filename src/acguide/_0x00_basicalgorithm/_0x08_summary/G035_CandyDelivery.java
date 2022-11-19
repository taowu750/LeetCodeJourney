package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 糖果传递: https://www.acwing.com/problem/content/124/
 *
 * 有 n 个小朋友坐成「一圈」，每人有 a[i] 个糖果。每人只能给左右两人传递糖果。每人每次传递一个糖果代价为 1。
 *
 * 求使所有人获得均等糖果的最小代价。
 *
 *
 * 输入格式：
 * - 第一行输入一个正整数 n，表示小朋友的个数。
 * - 接下来 n 行，每行一个整数 a[i]，表示第 i 个小朋友初始得到的糖果的颗数。
 *
 * 输出格式：
 * - 输出一个整数，表示最小代价。
 *
 *
 * 数据范围：
 * - 1 ≤ n ≤ 1000000,
 * - 0 ≤ a[i] ≤ 2×10^9,
 * - 数据保证「一定有解」。
 *
 *
 * 例 1：
 * 输入：
 * 4
 * 1
 * 2
 * 5
 * 4
 * 输出：
 * 4
 */
public class G035_CandyDelivery {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G035_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G035_output.txt");
    }

    /**
     * 参见：https://www.acwing.com/solution/content/955/
     *
     * 七夕祭先导题
     */
    public void delivery() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] a = new int[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            sum += a[i];
        }
        int ave = (int) (sum / n);

        int[] c = new int[n];
        for (int i = 1; i < n; i++) {
            c[i] = c[i - 1] + a[i] - ave;
        }
        Arrays.sort(c);

        long ans = 0;
        int mid = c[n / 2];
        for (int i = 0; i < n; i++) {
            ans += Math.abs(c[i] - mid);
        }
        System.out.println(ans);
    }

    @Test
    public void testDelivery() {
        test(this::delivery);
    }
}
