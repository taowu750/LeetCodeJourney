package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 士兵：https://www.acwing.com/problem/content/125/
 *
 * 格格兰郡的 N 名士兵随机散落在全郡各地。格格兰郡中的位置由一对 (x,y) 整数坐标表示。
 *
 * 士兵可以进行移动，每次移动，一名士兵可以向上，向下，向左或向右移动一个单位（因此，他的 x 或 y 坐标也将加 1 或减 1）。
 * 现在希望通过移动士兵，使得所有士兵彼此相邻的处于同一条水平线内，即所有士兵的 y 坐标相同并且 x 坐标相邻。
 *
 * 请你计算满足要求的情况下，所有士兵的总移动次数最少是多少。
 * 需注意，两个或多个士兵不能占据同一个位置。
 *
 * 输入格式：
 * - 第一行输入整数 N，代表士兵的数量。
 * - 接下来的 N 行，每行输入两个整数 x 和 y，分别代表一个士兵所在位置的 x 坐标和 y 坐标，
 *   第 i 行即为第 i 个士兵的坐标 (x[i],y[i])。
 *
 * 输出格式：
 * 输出一个整数，代表所有士兵的总移动次数的最小值。
 *
 * 数据范围：
 * - 1 ≤ N ≤ 10000,
 * - −10000 ≤ x[i],y[i] ≤ 10000
 *
 *
 * 例 1：
 * 输入：
 * 5
 * 1 2
 * 2 2
 * 1 3
 * 3 -2
 * 3 3
 * 输出：
 * 8
 */
public class G036_Soldiers {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G036_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G036_output.txt");
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G036_input1.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G036_output1.txt");
    }

    public void move() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] x = new int[n], y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.nextInt();
            y[i] = in.nextInt();
        }
        Arrays.sort(x);
        Arrays.sort(y);

        /*
        对x进行排序后,要求使得士兵全部相邻的最小移动次数.那么在移动前和移动后，士兵的相对位置是不变的.
        举例来说,记add为移动后的最左端的士兵的位置

        x[ 1 ] -> add + 1;
        x[ 2 ] -> add + 2;
        …
        x[ n ] -> add + n;

        转换一下:
        x[ 1 ] - 1 -> add;
        x[ 2 ] - 2 -> add;
        …
        x[ n ] - n -> add;

        于是就成了求把转换后的士兵位置移动到 add 的最小数，这就转化为了跟y轴一样的问题了
         */
        for (int i = 0; i < n; i++) {
            x[i] -= i;
        }
        Arrays.sort(x);
        // 中位数算法
        int xMid = x[n/2], yMid = y[n/2];
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.abs(x[i] - xMid);
            ans += Math.abs(y[i] - yMid);
        }
        System.out.println(ans);
    }

    @Test
    public void testMove() {
        test(this::move);
    }
}
