package acguide._0x00_basicalgorithm._0x08_summary;

import org.junit.jupiter.api.Test;
import util.StdIOTestUtil;

import java.util.Scanner;

/**
 * 最大的和: https://www.acwing.com/problem/content/128/
 *
 * 给定一个包含整数的二维矩阵，子矩形是位于整个阵列内的任何大小为 1×1 或更大的连续子阵列。矩形的总和是该矩形中所有元素的总和。
 * 在这个问题中，具有最大和的子矩形被称为最大子矩形。
 *
 * 输入格式：
 * - 输入中将包含一个 N×N 的整数数组。
 * - 第一行只输入一个整数 N，表示方形二维数组的大小。
 * - 从第二行开始，输入由空格和换行符隔开的 N^2 个整数，它们即为二维数组中的 N^2 个元素，
 *   输入顺序从二维数组的第一行开始向下逐行输入，同一行数据从左向右逐个输入。
 *
 * 输出格式：
 * - 输出一个整数，代表最大子矩形的总和。
 *
 * 数据范围：
 * - 1 ≤ N ≤ 100
 * - 数组中的数字会保持在 [−127,127] 的范围内。
 *
 *
 * 例 1：
 * 输入：
 * 4
 * 0 -2 -7 0
 * 9 2 -6 2
 * -4 1 -4 1
 * -1 8 0 -2
 * 输出：
 * 15
 */
public class G039_MaxSubSum {

    public static void test(Runnable method) {
        StdIOTestUtil.test(method, "acguide/_0x00_basicalgorithm/_0x08_summary/data/G039_input.txt",
                "acguide/_0x00_basicalgorithm/_0x08_summary/data/G039_output.txt");
    }

    /**
     * 转化法，把问题转化为一维数组求最大子数组和。
     *
     * 和 {@link training.greedy.Mianshi17_24_Hard_MaxSubmatrix} 一样
     */
    public void maxSum() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        // 每行都是一个前缀数组
        int[][] arr = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                arr[i][j] = arr[i][j-1] + in.nextInt();
            }
        }

        int ans = Integer.MIN_VALUE;
        // 求出 [colLo, colHi] 的所有列合并的一维数组，并使用一维数组求最大子数组和的算法求解
        for (int colLo = 1; colLo <= n; colLo++) {
            for (int colHi = colLo; colHi <= n; colHi++) {
                int sum = 0;
                for (int i = 1; i <= n; i++) {
                    int val = arr[i][colHi] - arr[i][colLo-1];
                    sum += val;
                    if (sum > ans) {
                        ans = sum;
                    }
                    if (sum < 0) {
                        sum = 0;
                    }
                }
            }
        }

        System.out.println(ans);
    }

    @Test
    public void testMaxSum() {
        test(this::maxSum);
    }
}
