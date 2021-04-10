package company.baidu;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 度度熊有一张网格纸，但是纸上有一些点过的点，每个点都在网格点上，若把网格看成一个坐标轴平行于网格线的坐标系的话，
 * 每个点可以用一对整数 x，y 来表示。度度熊必须沿着网格线画一个「正方形」，使所有点在正方形的「内部或者边界」。
 * 然后把这个正方形剪下来。
 *
 * 问剪掉正方形的最小面积是多少。
 *
 * 输入描述：
 * - 第一行一个数 n(2≤n≤1000) 表示点数
 * - 接下来每行一对整数 xi,yi(－1e9<=xi,yi<=1e9) 表示网格上的点
 *
 * 输出描述：
 * - 一行输出最小面积
 *
 * 例 1：
 * 输入：
 * 2
 * 0 0
 * 0 3
 * 输出：
 * 9
 */
public class BD2_CuttingGridPaper {

    static void test(Runnable method) {
        StdIOTestUtil.test(method, "company/baidu/data/bd2_input.txt",
                "company/baidu/data/bd2_expect.txt");
    }

    /**
     * 牛客网耗时：85ms - 91.65%
     *      内存消耗：16164KB - 14.30%
     */
    public void cuttingGridPaper() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
            int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                if (x < xMin)
                    xMin = x;
                if (x > xMax)
                    xMax = x;
                if (y < yMin)
                    yMin = y;
                if (y > yMax)
                    yMax = y;
            }
            int maxSize = Math.max(xMax - xMin, yMax - yMin);
            System.out.println(maxSize * maxSize);
        }
    }

    @Test
    public void test() {
        test(this::cuttingGridPaper);
    }
}
