package company.baidu;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 一个花坛中有很多花和两个喷泉。喷泉可以浇到以自己为中心，半径为 r 的圆内的所有范围的花。
 *
 * 现在给出这些花的坐标和两个喷泉的坐标，要求你安排两个喷泉浇花的半径 r1 和 r2，
 * 使得所有的花都能被浇到的同时, r1**2 + r2**2 的值最小。
 *
 * 输入描述：
 * - 第一行 5 个整数 n，x1，y1，x2，y2 表示花的数量和两个喷泉的坐标。
 * - 接下来 n 行，每行两个整数 xi, yi 表示第 i 朵花的坐标。
 * - 满足 1 <= n <= 2000 ，花和喷泉的坐标满足 -10**7<= x, y <= 10**7。
 *
 * 输出描述：
 * - 一个整数，r1**2 + r2**2 的最小值。
 *
 * 例 1：
 * 输入：
 * 2 -1 0 5 3
 * 0 2
 * 5 2
 * 输出：
 * 6
 */
public class BD16_Hard_WateringFlowers {

    static void test(Runnable method) {
        StdIOTestUtil.test(method, "company/baidu/data/bd16_input.txt",
                "company/baidu/data/bd16_expect.txt");
    }

    /**
     * 牛客网耗时：209ms - 41.61%
     *      内存消耗：22924KB - 7.45%
     */
    public void wateringFlowers() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt(), x1 = in.nextInt(), y1 = in.nextInt(),
                    x2 = in.nextInt(), y2 = in.nextInt();

            long[][] dist = new long[n][2];
            for (int i = 0; i < n; i++) {
                int x = in.nextInt(), y = in.nextInt();
                // 记录每朵花到两个喷泉的距离
                dist[i][0] = (long) (x - x1) * (x - x1) + (long) (y - y1) * (y - y1);
                dist[i][1] = (long) (x - x2) * (x - x2) + (long) (y - y2) * (y - y2);
            }
            // 根据到喷泉 1 的距离降序排序
            Arrays.sort(dist, (d1, d2) -> -Long.compare(d1[0], d2[0]));

            // 计算最小的总和距离和到 d2 的最大距离，最后从它们两个中选最小的
            long total = Long.MAX_VALUE, d2 = 0;
            for (int i = 0; i < n; i++) {
                total = Math.min(total, dist[i][0] + d2);
                d2 = Math.max(d2, dist[i][1]);
            }

            System.out.println(Math.min(total, d2));
        }
    }

    @Test
    public void testWateringFlowers() {
        test(this::wateringFlowers);
    }
}
